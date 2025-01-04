package Server;


import common.ClientServerMessage;
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
                 * 102 - user closed the app
                 * 200 - subscriber wants to search a book by its name
                 * 202 - subscriber wants to search a book by its genre
                 * 204 - subscriber wants to search a book by its description
                 * 206 - subscriber wants to see details of a specific book
                 * 208 - subscriber wants to order a book
                 * 210 - subscriber wants to see his borrowed book list
                 * 212 - subscriber wants to extend his borrow time
                 * 214 - subscriber wants to view his subscription history
                 * 216 - subscriber wants to edit his personal information
                 * 218 - subscriber wants to edit his login information
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
                    case (100):
                        /**
                         * do: user wants to log in to his account
                         * in: arraylist<username,password>
                         * return:(id 101) subscriber / librarian base of the username
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                // get the user details from the database
                                client.sendToClient(new ClientServerMessage(101, dbController.userLogin((ArrayList<String>) message.getMessageContent(), conn)));
                                System.out.println("user details were sent to client");
                            }
                            // message type isn't an arraylist
                            else {
                                client.sendToClient(new ClientServerMessage(101, null));
                                System.out.println("Cannot log in the account - message is not an ArrayList<String> (case 100)");
                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(101, null));
                            System.out.println("Error: with user login method (case 100) " + e);
                        }
                        break;
                    case (102):
                        /**
                         * Do: disconnect client from server
                         */
                        serverMonitorController.clientDisconnected(client);
                        client.sendToClient(null); // send null to client to make him stop waiting for a response
                        System.out.println("Client " + client + " disconnected");
                        break;
                    case (200):
                        /**
                         * do: subscriber wants to search a book by its name
                         * in: string
                         * return: (id 201) arraylist<book>
                         */
                        if (message.getMessageContent() instanceof String) {
                            try {
                                client.sendToClient(new ClientServerMessage(201, dbController.searchBookByName((String) message.getMessageContent(), conn)));
                                System.out.println("Books search by name was sent to client");
                            } catch (Exception e) {
                                System.out.println("Error: with getting book search by name (case 200)" + e);
                                client.sendToClient(new ClientServerMessage(201, null));
                            }
                        } else {
                            System.out.println("Cannot search book by name - message is not a string (case 200)");
                            client.sendToClient(new ClientServerMessage(201, null));

                        }
                        break;
                    case (202):
                        break;
                    case (204):
                        /**
                         * do: subscriber wants to search a book by its description
                         * in: string
                         * return: (id 201) arraylist<book>
                         */
                        try {
                            if (message.getMessageContent() instanceof String) {
                                client.sendToClient(new ClientServerMessage(201, dbController.searchBookByDescription((String) message.getMessageContent(), conn)));
                                System.out.println("Books search by description was sent to client");
                            } else {
                                System.out.println("Cannot search book by description - message is not a string (case 204)");
                                client.sendToClient(new ClientServerMessage(201, null));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with getting book search by description (case 204)" + e);
                            client.sendToClient(new ClientServerMessage(201, null));
                        }
                        break;
                    case (206):
                        break;
                    case (208):
                        break;
                    case (210):
                        break;
                    case (212):
                        break;
                    case (214):
                        break;
                    case (216):
                        /**
                         * do: subscriber wants to edit his personal information
                         * in: ArrayList<String>
                         * return: (id 217) arraylist<string> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(217, dbController.editSubscriberDetails((ArrayList<String>) message.getMessageContent(), conn)));
                                System.out.println("Subscriber details were edited");
                            } else {
                                client.sendToClient(new ClientServerMessage(217, new ArrayList<String>() {{
                                    add("False");
                                    add("Cannot Edit account Message is not ArrayList<String>");
                                }}));
                                System.out.println("Cannot Edit account Message is not a subscriber");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with Edit method (case 216)" + e);
                            client.sendToClient(new ClientServerMessage(217, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error");
                            }}));

                        }
                        break;
                    case (218):
                        /**
                         * do: subscriber wants to view his personal information
                         * in: int
                         * return: (id 217) arraylist<string> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(217, dbController.editSubscriberLoginDetails((ArrayList<String>) message.getMessageContent(), conn)));
                                System.out.println("Subscriber login details were edited");
                            } else {
                                client.sendToClient(new ClientServerMessage(217, new ArrayList<String>() {{
                                    add("False");
                                    add("Cannot view account Message is not an ArrayList<String>");
                                }}));
                                System.out.println("Cannot view account Message is not an ArrayList<String>");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with Edit method (case 218)" + e);
                            client.sendToClient(new ClientServerMessage(217, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error");
                            }}));
                        }
                        break;
                    case (300):
                        break;
                    case (302):
                        break;
                    case (304):
                        break;
                    case (306):
                        /**
                         * do: librarian wants to view subscribers in the system
                         * in: none
                         * return: (id 307) arraylist<subscriber>
                         */
                        try {
                            client.sendToClient(new ClientServerMessage(307, dbController.viewAllSubscribers(conn)));
                            System.out.println("Subscribers list was sent to client");
                        } catch (Exception e) {
                            System.out.println("Error: with getting subscribers list (case 306)" + e);
                            client.sendToClient(new ClientServerMessage(307, null));

                        }
                        break;
                    case (308):
                        break;
                    case (310):
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