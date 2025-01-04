package client;

import common.ClientServerMessage;
import common.Subscriber;
import gui.SubscriberEditProfileFrameController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import ocsf.client.*;
import common.ChatIF;


import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;


public class ClientController extends AbstractClient
{
    private Connection conn;

    ChatIF clientUI;
    public static boolean awaitResponse = false;

    /**
     * Constructs an instance of the chat client.
     *
     * @param host     The server to connect to.
     * @param port     The port number to connect on.
     * @param clientUI The interface type variable.
     */
    public ClientController(String host, int port, ChatIF clientUI)
            throws IOException
    {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
    }

    /**
     * This method handles all data that comes in from the server
     *
     * @param msg The message from the server.
     */
    public void handleMessageFromServer(Object msg)
    {
        awaitResponse = false;
        try {
            if (msg instanceof ClientServerMessage) {
                ClientServerMessage message = (ClientServerMessage) msg;

                switch (message.getId()) {
                    // get the details of a specific subscriber
                    case 202:
                        if (message.getMessageContent() == null) {
                            System.out.println("Wrong Username(id) or Password");
                            Platform.runLater(() -> showErrorAlert("Login error", "Wrong Username(id) or Password"));
                        }
                        else if (message.getMessageContent() instanceof Subscriber) {
                            Subscriber subscriberFromServer = (Subscriber) message.getMessageContent();
                            Subscriber.setLocalSubscriber(subscriberFromServer);
                        }
                        break;
                    //  Edit subscriber details
                    case 204:
                        if (message.getMessageContent() == null) {
                            System.out.println("Could not update subscriber details");
                            Platform.runLater(() -> showErrorAlert("Update error", "Could not update subscriber details"));

                        }
                        else if (message.getMessageContent() instanceof Subscriber) {
                            Subscriber subscriberFromServer = (Subscriber) message.getMessageContent();
                            Subscriber.setLocalSubscriber(subscriberFromServer);
                            Subscriber.setLocalSubscriber(subscriberFromServer);
                            Platform.runLater(() -> showInformationAlert("Update successful", "Subscriber details updated successfully"));
                        }
                        break;
                    // Get all subscribers list
                    case 104:
                        if (message.getMessageContent() == null) {
                            System.out.println("Unable to present Subscribers");
                            Platform.runLater(() -> showErrorAlert("No Subscribers", "Unable to present Subscribers"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList<?>) {
                            ArrayList<Subscriber> subscribersFromServer = (ArrayList<Subscriber>) message.getMessageContent();
                            //?PrototypeViewAllController.setSubscribers(subscribersFromServer);
                        }
                        break;
                    // Server closed connection
                    case 998:
                        System.out.println("Server has closed its connection");
                        Platform.runLater(() -> showErrorAlert("Server closed connection", "Server has closed its connection"));
                        break;
                    default:
                        System.out.println("Invalid command id");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error: incorrect msg object type" + e);
        }
    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromClientUI(ClientServerMessage message)
    {
        try {
            openConnection(); // Ensure the connection is open before sending
            awaitResponse = true; // Mark as waiting for a response
            sendToServer(message); // Send the ClientServerMessage object
            // Wait for the server response
            while (awaitResponse) {
                try {
                    Thread.sleep(100); // Poll for the response
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            clientUI.display("Could not send message to server: Terminating client. " + e);
            quit();
        }
    }

    /**
     * This method terminates the client.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void quit()
    {
        try {
            closeConnection();
        }
        catch (IOException e) {
        }
        System.exit(0);
    }

    /**
     * This method displays an error message onto the screen.
     *
     * @param message The string to be displayed.
     */
    private void showErrorAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        if (title.equals("Server has closed its connection")) {
            System.exit(0);
        }
    }

    /**
     * This method displays an INFORMATION message onto the screen.
     *
     * @param message The string to be displayed.
     */
    private void showInformationAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
