package client;

import common.*;
import gui.SearchHomePageFrameController;
import gui.SearchResultFrameController;
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
        try
        {
            if (msg instanceof ClientServerMessage)
            {
                ClientServerMessage message = (ClientServerMessage) msg;

                /*
                 * The following switch case is used to handle the different responses from the server:
                 * 101 - login response from the server
                 * 201 - search book response
                 * 207 - Show book details response
                 * 209 - Order book response
                 * 211 - Show borrowed list response
                 * 213 - Extend book borrow - subscriber response
                 * 215 - Watch history response
                 * 217 - Edit subscriber details
                 * 219 - Edit login details response
                 * 301 - Register new subscriber response
                 * 303 - Borrow book response
                 * 305 - Return book response
                 * 307 - Get all subscribers list
                 * 309 - Watch subscriber details response
                 * 311 - Extend borrow - librarian response
                 * 313 - Watch logs response
                 * Default - Invalid command id
                 */
                switch (message.getId())
                {
                    //login response from the server
                    case 101:
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Wrong Username(id) or Password");
                            Platform.runLater(() -> showErrorAlert("Login error", "Wrong Username(id) or Password"));
                        }
                        else if (message.getMessageContent() instanceof User)
                        {
                            User user = (User) message.getMessageContent();
                            if (user instanceof Subscriber)
                            {
                                Subscriber subscriberFromServer = (Subscriber) user;
                                Subscriber.setLocalSubscriber(subscriberFromServer);
                            }
                            else if (user instanceof Librarian)
                            {
                                Librarian librarianFromServer = (Librarian) user;
                                System.out.println(librarianFromServer.getLibrarianID() + " " + librarianFromServer.getFirstName() + " " + librarianFromServer.getLastName());
                                Librarian.setLocalLibrarian(librarianFromServer);
                            }
                        }
                        break;
                    //search book response
                    case 201:
                        //todo: handle search book response
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Book not found");
                            SearchHomePageFrameController.changeCanSearch(false);
                            //todo: make it better
                            Platform.runLater(() -> showErrorAlert("Book Not Found", "There aren't any books like that"));
                        }
                        else
                        {
                            SearchHomePageFrameController.changeCanSearch(true);
                            System.out.println("bob3");
                            ArrayList<Book> booklst = (ArrayList<Book>) message.getMessageContent();
                            System.out.println("bob4");
                            SearchResultFrameController.setBookArray(booklst);
                        }
                        break;
                    //Show book details response
                    case 207:
                        //todo: handle show book details response
                        break;
                    // Order book response
                    case 209:
                        //todo: handle order book response
                        break;
                    // Show borrowed list response
                    case 211:
                        //todo: handle show borrowed list to the subscriber
                        break;
                    //Extend book borrow - subscriber response
                    case 213:
                        //todo: handle extend book borrow response
                        break;
                    //Watch history response
                    case 215:
                        //todo: handle watch history response
                        break;
                    //Edit subscriber details
                    case 217:
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not update subscriber details");
                            Platform.runLater(() -> showErrorAlert("Update error", "Could not update subscriber details"));

                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            ArrayList<String> newDetails = (ArrayList<String>) message.getMessageContent();
                            updateSubscriberDetails(newDetails);
                            Platform.runLater(() -> showInformationAlert("Update successful", "Subscriber details updated successfully"));
                        }
                        break;
                    //Edit login details response
                    case 219:
                        //todo: handle edit login details response
                        break;
                    //Register new subscriber response
                    case 301:
                        registerNewSubscriberResponse(message);
                        break;
                    //Borrow book response
                    case 303:
                        //todo: handle Borrow book response
                        break;
                    //Return book response
                    case 305:
                        //todo: handle return book response
                        break;
                    // Get all subscribers list
                    case 307:
//                        if (message.getMessageContent() == null) {
//                            System.out.println("Unable to present Subscribers");
//                            Platform.runLater(() -> showErrorAlert("No Subscribers", "Unable to present Subscribers"));
//                        } else if (message.getMessageContent() instanceof ArrayList<?>) {
//                            ArrayList<Subscriber> subscribersFromServer = (ArrayList<Subscriber>) message.getMessageContent();
//                            //?PrototypeViewAllController.setSubscribers(subscribersFromServer);
//                        }
                        //todo: handle get all subscribers list
                        break;
                    // Watch subscriber details response
                    case 309:
                        //todo: handle watch subscriber details response
                        break;
                    // Extend borrow - librarian response
                    case 311:
                        //todo: handle extend borrow - librarian response
                        break;
                    // Watch logs response
                    case 313:
                        //todo: handle watch logs response
                        break;
                    // Server has closed its connection
                    case 999:
                        Platform.runLater(() -> showErrorAlert("Server closed", "Server has closed its connection for maintenance"));
                        break;
                    default:
                        System.out.println("Invalid command id");
                }
            }
        }
        catch (Exception e)
        {
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
        try
        {
            openConnection(); // Ensure the connection is open before sending
            awaitResponse = true; // Mark as waiting for a response
            sendToServer(message); // Send the ClientServerMessage object
            // Wait for the server response
            while (awaitResponse)
            {
                try
                {
                    Thread.sleep(100); // Poll for the response
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            clientUI.display("Could not send message to server: Terminating client. " + e);
            quit();
        }
    }

    /**
     * This method terminates the client.
     */
    public void quit()
    {
        try
        {
            closeConnection();
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
        if (title.equals("Server has closed its connection"))
        {
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

    private void updateSubscriberDetails(ArrayList<String> newDetails)
    {
        if (Boolean.parseBoolean(newDetails.get(0)))
        {
            Subscriber.getLocalSubscriber().setPhoneNumber(newDetails.get(1));
            Subscriber.getLocalSubscriber().setEmail(newDetails.get(2));
            Subscriber.getLocalSubscriber().setFirstName(newDetails.get(3));
            Subscriber.getLocalSubscriber().setLastName(newDetails.get(4));
        }
        else
        {
            Platform.runLater(() -> showErrorAlert("Update error", "Could not update subscriber details"));
        }
    }

    private void registerNewSubscriberResponse(ClientServerMessage message)
    {
        if (message.getMessageContent() == null)
        {
            System.out.println("Could not register new subscriber");
            Platform.runLater(() -> showErrorAlert("Registration error", "Could not register new subscriber"));
        }
        else if (message.getMessageContent() instanceof ArrayList)
        {
            // casts the message content to an ArrayList and gives a pop up message to the librarian
            ArrayList<String> serverResponse = (ArrayList<String>) message.getMessageContent();
            if (Boolean.parseBoolean(serverResponse.get(0)))
            {
                Platform.runLater(() -> {
                    showInformationAlert("Success", serverResponse.get(1));
                });
            }
            else
            {
                Platform.runLater(() -> {
                    showErrorAlert("Registration error", serverResponse.get(1));
                });
            }
        }
    }
}
