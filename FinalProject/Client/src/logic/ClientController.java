package logic;

import client.ClientGUIController;
import common.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import ocsf.client.*;
import common.ChatIF;


import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;


public class ClientController extends AbstractClient
{
    private Connection conn;
    private static ActionEvent storedActionEvent;
    private static FXMLLoader loader;
    ChatIF clientUI;
    public static boolean awaitResponse = false;

    /**
     * Constructs an instance of the chat client.
     *
     * @param host     The server to connect to.
     * @param port     The port number to connect on.
     * @param clientUI The interface type variable.
     */
    public ClientController(String host, int port, ChatIF clientUI, FXMLLoader loader)
            throws IOException
    {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        ClientController.loader = loader;
        openConnection();
    }

    public static void setActionEvent(ActionEvent actionEvent)
    {
        storedActionEvent = actionEvent;
    }

    public static ActionEvent getStoredActionEvent()
    {
        return storedActionEvent;
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
                            if (message.getMessageContent() instanceof ArrayList)
                            {
                                ArrayList<Book> booklst = (ArrayList<Book>) message.getMessageContent();
                                SearchResultFrameController.setBookArray(booklst);
                            }
                            else
                            {
                                SearchHomePageFrameController.changeCanSearch(false);
                            }
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
                        if (message.getMessageContent() instanceof ArrayList) {
                            @SuppressWarnings("unchecked")
                            ArrayList<BorrowedBook> messages = (ArrayList<BorrowedBook>) message.getMessageContent();
                            // Handle SubscriberProfileOptionsFrameController
                            if (loader.getController().getClass().getSimpleName().equals("SubscriberProfileOptionsFrameController")) {
                                SubscriberProfileOptionsFrameController controllerSubscriberOption = loader.getController();
                                controllerSubscriberOption.loadBorrowsTable(messages);
                            }
                            // Handle WatchProfileFrameController
                            if (loader.getController().getClass().getSimpleName().equals("WatchProfileFrameController")) {
                                WatchProfileFrameController controllerFromLibrarianFrames = loader.getController();
                                controllerFromLibrarianFrames.loadBorrowsTable(messages);
                            }
                        }
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
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not update login details");
                            Platform.runLater(() -> showErrorAlert("Update error", "Could not update login details"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            ArrayList<String> serverResponse = (ArrayList<String>) message.getMessageContent();
                            if (Boolean.parseBoolean(serverResponse.get(0)))
                            {
                                Platform.runLater(() -> showInformationAlert("Update successful", "Login details updated successfully"));
                            }
                            else
                            {
                                Platform.runLater(() -> showErrorAlert("Error", serverResponse.get(1)));
                            }
                        }
                        break;
                    //Register new subscriber response
                    case 301:
                        registerNewSubscriberResponse(message);
                        break;
                    //Borrow book response
                    case 303:
                        Object messageContent = message.getMessageContent();
                        if (messageContent instanceof ArrayList<?>)
                        {
                            @SuppressWarnings("unchecked")
                            ArrayList<String> response = (ArrayList<String>) messageContent;

                            // Use the showBorrowMessageResponse method to display the response
                            BorrowBookFrameController.showBorrowMessageResponse(response);
                        }
                        else
                        {
                            // Log an error or display an error message if the response format is unexpected
                            System.err.println("Unexpected format for Borrow book response: " + messageContent);
                            Platform.runLater(() -> showErrorAlert("Borrow Book Error", "Invalid response format from the server."));
                        }
                        break;
                    //Return book response
                    case 305:
                        if (message.getMessageContent() instanceof ArrayList<?>)
                        {
                            @SuppressWarnings("unchecked")
                            ArrayList<String> response = (ArrayList<String>) message.getMessageContent();

                            // Use the showReturnMessageResponse method to display the response
                            ReturnBookFrameController.showReturnMessageResponse(response);
                        }
                        else
                        {
                            // Log an error or display an error message if the response format is unexpected
                            System.err.println("Unexpected format for Return book response: " + message.getMessageContent());
                            Platform.runLater(() -> showErrorAlert("Return Book Error", "Invalid response format from the server."));
                        }
                        break;
                    // Get all subscribers list
                    case 307:
                        if(message.getMessageContent() == null)
                        {
                            System.out.println("Could not get all subscribers list");
                            Platform.runLater(() -> showErrorAlert("Error", "Could not get all subscribers list"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked")
                            ArrayList<Subscriber> subscribers = (ArrayList<Subscriber>) message.getMessageContent();
                            ViewAllSubscribersFrameController controller = loader.getController();
                            controller.addToTable(subscribers);
                        }
                        break;
                    // Watch subscriber details response
                    case 309:
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Wrong Username(id)");
                            Platform.runLater(() -> showErrorAlert("Search Error", "Wrong Username"));
                        }
                        else if (message.getMessageContent() instanceof Subscriber)
                        {
                            Subscriber subscriberFromServer = (Subscriber) message.getMessageContent();
                            Subscriber.setWatchProfileSubscriber(subscriberFromServer);
                            SearchSubscriberFrameController.WatchProfileResponse(subscriberFromServer);
                        }
                        break;
                    // Extend borrow - librarian response
                    case 311:
                        if (message.getMessageContent() instanceof ArrayList) {

                            ArrayList<String> arrayMessageFromServer = (ArrayList<String>) message.getMessageContent();
                            //System.out.println(arrayMessageFromServer.get(0));
                            System.out.println(arrayMessageFromServer);
                            BorrowExtensionFrameController.showExtendMessageResponse(arrayMessageFromServer);
                        } else {
                            System.out.println("Invalid message content for case 311");
                            Platform.runLater(() -> showErrorAlert("Data Error", "Expected an ArrayList for borrow extension."));
                        }
                        break;
                    // Watch logs response
                    case 313:
                        //todo: handle watch logs response
                        break;
                    case 315:
                        if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked")
                            ArrayList<LibrarianMessage> messages = (ArrayList<LibrarianMessage>) message.getMessageContent();
                            ViewMessagesFrameController controller = loader.getController();
                            controller.loadLibrarianMessages(messages);
                        }
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

    public static void setLoader(FXMLLoader loader)
    {
        ClientController.loader = loader;
    }
}
