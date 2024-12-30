package Server;


import common.*;
import gui.ServerMonitorFrameController;
import logic.DBController;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class extends the AbstractServer class and implements the SubscriberController and LibrarianController interfaces.
 * It is used to control the server.
 */
public class ServerController extends AbstractServer {

    private static Connection conn;
    private final ServerMonitorFrameController serverMonitorController;
    private final DBController dbController;


    public ServerController(int port, ServerMonitorFrameController serverMonitorController) {
        super(port);
        this.serverMonitorController = serverMonitorController;
        dbController = DBController.getInstance();

    }

    /**
     * This method overrides the one in the superclass. Called
     * when a client has connected to the server add it to the list of clients.
     *
     * @param client the connection connected to the client.
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Client connected" + client.toString());
        serverMonitorController.clientConnected(client);
    }

    /**
     * This method overrides the one in the superclass. Called
     * when a client has disconnected from the server remove it from the list of clients.
     *
     * @param client the connection with the client.
     */
    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected");
        serverMonitorController.clientDisconnected(client);
    }

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     * @param
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        try {
            if (msg instanceof ClientServerMessage) {

                /*
                 * 100 - user wants to log in to his account
                 * 102 - user logs out of his account
                 * 104 - user closed the app
                 * 200 - subscriber wants to search a book by its name
                 * 202 - subscriber wants to search a book by its genre
                 * 204 - subscriber wants to search a book by its description
                 * 206 - subscriber wants to see details of a specific book
                 * 208 - subscriber wants to order a book
                 * 210 - subscriber wants to see his borrowed book list
                 * 212 - subscriber wants to extend his borrow time
                 * 214 - subscriber wants to view his subscription history
                 * 216 - subscriber wants to edit his personal information
                 * 300 - librarian wants to register a new subscriber into the system
                 * 302 - librarian borrows a book to a subscriber
                 * 304 - librarian returns a book of a subscriber to the library
                 * 306 - librarian wants to view subscribers in the system
                 * 308 - librarian wants to view details of a specific subscriber
                 * 310 - librarian wants to extend the borrow time for a book subscriber that was borrowed by a subscriber
                 * 312 - librarian wants to view a list of all logs
                 */
                ClientServerMessage message = (ClientServerMessage) msg;
                switch (message.getId()) {
                    case(100):
                        /**
                         * do: user wants to log in to his account
                         * in: arraylist<username,password>
                         * return:(id 101) subscriber / librarian base of the username
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                ArrayList<String> userDetails = dbController.userLogin((ArrayList<String>) message.getMessageContent(), conn);
                                if(userDetails != null){
                                    if(userDetails.get(0).equals("subscriber")){
                                        client.sendToClient( new ClientServerMessage(101, new Subscriber(Integer.parseInt(userDetails.get(1)), userDetails.get(2),
                                                userDetails.get(3), userDetails.get(4), userDetails.get(5), Boolean.parseBoolean(userDetails.get(6)),
                                                Integer.parseInt(userDetails.get(7)))));
                                        System.out.println("Subscriber details were sent to client");
                                    }
                                    else if(userDetails.get(0).equals("librarian")){
                                        client.sendToClient(new ClientServerMessage(101, new Librarian(Integer.parseInt(userDetails.get(1)),
                                                userDetails.get(2), userDetails.get(3))));
                                        System.out.println("Librarian details were sent to client");
                                    }
                                }
                                else{
                                    client.sendToClient(new ClientServerMessage(101, null));
                                    System.out.println("Could not find the user in the database");
                                }
                            }
                            else {
                                client.sendToClient(new ClientServerMessage(101, null));
                                System.out.println("Cannot log in the account - message is not an ArrayList<String>");
                            }
                            break;
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(101, null));
                            System.out.println("Error: with subscriber login method (case 100) " + e);
                            break;
                        }
                    case(102):
                        break;
                    case(104):
                        /**
                         * Do: disconnect client from server
                         */
                        serverMonitorController.clientDisconnected(client);
                        client.sendToClient(null); // send null to client to make him stop waiting for a response
                        break;
                    case(200):
                        break;
                    case(202):
                        break;
                    case(204):
                        break;
                    case(206):
                        break;
                    case(208):
                        break;
                    case(210):
                        break;
                    case(212):
                        break;
                    case(214):
                        break;
                    case(216):
                        try {
                            if (message.getMessageContent() instanceof Subscriber) {
                               // Subscriber editedDetails = subscriberController.editSubscriberDetails((Subscriber) message.getMessageContent(), conn);
                                //.sendToClient(new ClientServerMessage(217, editedDetails));
                                System.out.println("Updated Subscriber details was sent to client");
                            } else {
                                System.out.println("Cannot Edit account Message is not a subscriber");
                                client.sendToClient(new ClientServerMessage(217, null));

                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Error: with Edit method (case203)" + e);
                            client.sendToClient(new ClientServerMessage(217, null));
                            break;
                        }
                    case(300):
                        break;
                    case(302):
                        break;
                    case(304):
                        break;
                    case(306):
                        try {
//<Subscriber> subscribersList = LibrarianController.getSubscribersList(conn);
                         //   client.sendToClient(new ClientServerMessage(306, subscribersList));
                            System.out.println("Subscribers list was sent to client");
                            break;
                        } catch (Exception e) {
                            System.out.println("Error: with getting subscribers list (case103)" + e);
                            client.sendToClient(new ClientServerMessage(306, null));
                            break;
                        }
                    case(308):
                        break;
                    case(310):
                        break;
                    default:
                        System.out.println("Invalid command id(handleMessageFromClient ServerController)");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: incorrect msg object type (handleMessageFromClient ServerController)" + e);
        }

    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
        connectToDb();
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    private void connectToDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
            /* handle the error*/
            System.out.println("Driver definition failed");
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=IST", "root", "Aa123456");
            System.out.println("SQL connection succeed");
        } catch (SQLException ex) {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    /**
     * This method sends a message to all clients connected to the server.
     *
     * @param message The message to be sent to all clients.
     */
    public void sendMessagesToAllClients(ClientServerMessage message) {
        try {
            sendToAllClients(message);
        } catch (Exception e) {
            System.out.println("Error: sending message to all clients" + e);
        }
    }
}