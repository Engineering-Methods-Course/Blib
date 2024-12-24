package Server;

import common.ClientServerMessage;
import common.Subscriber;
import gui.ServerMonitorFrameController;
import logic.LibrarianController;
import logic.SubscriberController;
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
    private SubscriberController subscriberController = null;
    private LibrarianController librarianController = null;

    public ServerController(int port, ServerMonitorFrameController serverMonitorController) {
        super(port);
        this.serverMonitorController = serverMonitorController;
        subscriberController = SubscriberController.getInstance();
        librarianController = LibrarianController.getInstance();
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
                 * 103 - Get all subscribers list (return case 104)
                 * 201 - Get info of a specific subscriber (return case 202)
                 * 203 - Edit info of a specific subscriber (return case 204)
                 * 999 - Client disconnected
                 */
                ClientServerMessage message = (ClientServerMessage) msg;
                switch (message.getId()) {
                    // Get all subscribers list
                    case 103:
                        try {
                            ArrayList<Subscriber> subscribersList = LibrarianController.getSubscribersList(conn);
                            client.sendToClient(new ClientServerMessage(104, subscribersList));
                            System.out.println("Subscribers list was sent to client");
                            break;
                        } catch (Exception e) {
                            System.out.println("Error: with getting subscribers list (case103)" + e);
                            client.sendToClient(new ClientServerMessage(104, null));
                            break;
                        }
                        // Login subscriber
                    case 201:
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                Subscriber subscriberDetails = subscriberController.subscriberLogin((ArrayList<String>) message.getMessageContent(), conn);
                                client.sendToClient(new ClientServerMessage(202, subscriberDetails));
                                System.out.println("Subscriber details was sent to client");
                            } else {
                                System.out.println("Cannot logIn the account Message is not a ArrayList<String>");
                                client.sendToClient(new ClientServerMessage(202, null));
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Error: with subscriber login method (case201) " + e);
                            client.sendToClient(new ClientServerMessage(202, null));
                            break;
                        }
                        //  Edit subscriber details
                    case 203:
                        try {
                            if (message.getMessageContent() instanceof Subscriber) {
                                Subscriber editedDetails = subscriberController.editSubscriberDetails((Subscriber) message.getMessageContent(), conn);
                                client.sendToClient(new ClientServerMessage(204, editedDetails));
                                System.out.println("Updated Subscriber details was sent to client");
                            } else {
                                System.out.println("Cannot Edit account Message is not a subscriber");
                                client.sendToClient(new ClientServerMessage(204, null));

                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Error: with Edit method (case203)" + e);
                            client.sendToClient(new ClientServerMessage(204, null));
                            break;
                        }
                        // Client disconnected
                    case 999:
                        serverMonitorController.clientDisconnected(client);
                        // send null to client to make him stop waiting for a response
                        client.sendToClient(null);
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