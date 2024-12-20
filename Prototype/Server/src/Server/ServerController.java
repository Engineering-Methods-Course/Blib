package Server;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import common.*;
import gui.*;
import logic.*;
import ocsf.server.*;

/**
 * This class extends the AbstractServer class and implements the SubscriberController and LibrarianController interfaces.
 * It is used to control the server.
 */
public class ServerController extends AbstractServer {

    private final ServerMonitorFrameController serverMonitorController;
    private static Connection conn;
    private SubscriberController subscriberController = null;

    public ServerController(int port, ServerMonitorFrameController serverMonitorController) {
        super(port);
        this.serverMonitorController = serverMonitorController;
        subscriberController = SubscriberController.getInstance();
    }

    /**
     * This method overrides the one in the superclass. Called
     * when a client has connected to the server add it to the list of clients.
     *
     * @param client the connection connected to the client.
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Client connected");

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
                ClientServerMessage message = (ClientServerMessage) msg;

                /**
                 * 201 - Get info of a specific subscriber
                 * 203 - Edit info of a specific subscriber
                 * 205 - Get a list of all subscribers
                 */
                switch (message.getId()) {
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
                        } catch (Exception e) {
                            System.out.println("Error: with LogIn method " + e);
                            client.sendToClient(new ClientServerMessage(202, null));
                        }
                        break;
                    //  Edit subscriber details
                    case 203:
                        try {
                            if (message.getMessageContent() instanceof Subscriber) {
                                Subscriber editedDetails = subscriberController.editSubscriberDetails((Subscriber) message.getMessageContent(), conn);
                                client.sendToClient(new ClientServerMessage(204, editedDetails));
                                System.out.println("Updated Subscriber details was sent to client");
                            } else {
                                System.out.println("Cannot Edit account Message is not a subscriber");
                                client.sendToClient(null);
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with Edit method" + e);
                        }
                        break;
                    //! will be implemented in the future
                    /*case 205:
                        ArrayList<Subscriber> subscribersList = getSubscribersList(conn);
                        ClientServerMessage subscribersListCommandMessage = new ClientServerMessage(206, subscribersList);
                        client.sendToClient(subscribersListCommandMessage);
                        break;*/
                    default:
                        System.out.println("Invalid command id");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: incorrect msg object type" + e);
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
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=IST", "root", "Aa123456");
            System.out.println("SQL connection succeed");
        } catch (SQLException ex) {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}