package logic;

import common.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import ocsf.client.*;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.ClientGUIController.showAlert;

public class ClientController extends AbstractClient
{
    public static boolean awaitResponse = false;
    private Connection conn;
    private static ActionEvent storedActionEvent;
    private static FXMLLoader loader;
    final ChatIF clientUI;

    /**
     * Constructs an instance of the chat client.
     *
     * @param host     The server to connect to.
     * @param port     The port number to connect on.
     * @param clientUI The interface type variable.
     */
    public ClientController(String host, int port, ChatIF clientUI, FXMLLoader loader) throws IOException
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

    public static void setLoader(FXMLLoader loader)
    {
        ClientController.loader = loader;
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
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Wrong Username(id) or Password");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Login error", "Wrong Username(id) or Password"));
                        }
                        else if (message.getMessageContent() instanceof User)
                        {
                            User user = (User) message.getMessageContent();
                            //if the user is a subscriber, set the local instance of Subscriber to it
                            if (user instanceof Subscriber)
                            {
                                Subscriber subscriberFromServer = (Subscriber) user;
                                Subscriber.setLocalSubscriber(subscriberFromServer);
                            }
                            //if the user is a librarian, set the local instance of Librarian to it
                            else if (user instanceof Librarian)
                            {
                                Librarian librarianFromServer = (Librarian) user;
                                Librarian.setLocalLibrarian(librarianFromServer);
                            }
                        }
                        break;
                    // message to the client
                    case 107:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not get message");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not get message"));
                        }
                        else if (message.getMessageContent() instanceof String)
                        {
                            String response = (String) message.getMessageContent();
                            Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Message", response));
                        }
                        break;
                    //search book response
                    case 201:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Book not found");
                            SearchPageFrameController.changeCanSearch(false);
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Book Not Found", "There aren't any books like that"));
                        }
                        else
                        {
                            if (message.getMessageContent() instanceof ArrayList)
                            {
                                //Indicate that the search results were successful by setting the flag to true.
                                SearchPageFrameController.changeCanSearch(true);
                                @SuppressWarnings("unchecked") ArrayList<Book> bookList = (ArrayList<Book>) message.getMessageContent();

                                //pass the list of books to the searchResultFrameController
                                SearchResultFrameController.setBookArray(bookList);
                            }
                        }
                        break;
                    //Show book details response
                    case 207:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Book Not Found", "Error with db"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked") ArrayList<String> details = (ArrayList<String>) message.getMessageContent();

                            //pass the list with the availability of the book to BookInfoFrameController
                            BookInfoFrameController.setAvailability(details);
                        }
                        break;
                    // Order book response
                    case 209:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Book Not ordered", "Book not ordered"));
                            BookInfoFrameController.orderComplete = false;
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked") ArrayList<String> details = (ArrayList<String>) message.getMessageContent();
                            //if the book order process ended successfully shows a correct message for it
                            if (details.get(0).equals("true"))
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Book ordered", "Book was ordered"));

                                //signals that the order process is done
                                BookInfoFrameController.orderComplete = true;
                                System.out.println("Book Ordered Successfully");
                            }
                            //if the order failed, show an error message for it
                            else
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Book ordered", details.get(1)));

                                //signals that the order process ended in failure
                                BookInfoFrameController.orderComplete = false;
                                System.out.println("Book Ordered Failed");
                            }
                        }
                        break;
                    // Show borrowed list response
                    case 211:
                        if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked") ArrayList<BorrowedBook> messages = (ArrayList<BorrowedBook>) message.getMessageContent();
                            // Handle SubscriberProfileFrameController
                            if (loader.getController().getClass().getSimpleName().equals("SubscriberProfileFrameController"))
                            {
                                SubscriberProfileFrameController controllerSubscriberOption = loader.getController();
                                controllerSubscriberOption.loadBorrowsTable(messages);
                            }
                            // Handle WatchProfileFrameController
                            if (loader.getController().getClass().getSimpleName().equals("WatchProfileFrameController"))
                            {
                                WatchProfileFrameController controllerFromLibrarianFrames = loader.getController();
                                controllerFromLibrarianFrames.loadBorrowsTable(messages);
                            }
                        }
                        break;
                    //Extend book borrow - subscriber response
                    case 213:
                        //if the content is an instance of arraylist, pass it to SubscriberProfileFrameController
                        if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked") ArrayList<String> arrayMessageFromServer = (ArrayList<String>) message.getMessageContent();
                            SubscriberProfileFrameController.showExtendMessageResponse(arrayMessageFromServer);
                        }
                        //display an error message
                        else
                        {
                            System.out.println("Invalid message content for case 311");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Data Error", "Expected an ArrayList for borrow extension."));
                        }
                        break;
                    //Watch history response
                    case 215:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not get history");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not get history"));
                        }
                        //if the content is an instance of List, pass it to WatchHistorySceneController's
                        else if (message.getMessageContent() instanceof List)
                        {
                            @SuppressWarnings("unchecked")
                            List<ArrayList<String>> history = (ArrayList<ArrayList<String>>) message.getMessageContent();
                            WatchHistorySceneController controller = loader.getController();
                            controller.setHistory(history);
                        }
                        break;
                    //Edit subscriber details
                    case 217:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not update subscriber details");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Update error", "Could not update subscriber details"));

                        }
                        //if content is instance of arraylist, pass it to updateSubscriberDetails and display a successful message
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked") ArrayList<String> newDetails = (ArrayList<String>) message.getMessageContent();
                            if (newDetails.get(0).equals("true"))
                            {
                                updateSubscriberDetails(newDetails);
                                Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Update successful", "Subscriber details updated successfully"));
                            }
                            else
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Update error", newDetails.get(1)));
                            }
                        }
                        break;
                    //Edit login details response
                    case 219:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not update login details");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Update error", "Could not update login details"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked") ArrayList<String> serverResponse = (ArrayList<String>) message.getMessageContent();

                            //if the first element is true, display successful update message
                            if (Boolean.parseBoolean(serverResponse.get(0)))
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Update successful", "Login details updated successfully"));
                            }
                            //if false, display an error message
                            else
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", serverResponse.get(1)));
                            }
                        }
                        break;
                    //Register new subscriber response
                    case 301:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not register new subscriber");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Registration error", "Could not register new subscriber"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            // casts the message content to an ArrayList and gives a pop-up message to the librarian
                            @SuppressWarnings("unchecked") ArrayList<String> serverResponse = (ArrayList<String>) message.getMessageContent();

                            //if the first element of the content is true, show a success message
                            if (Boolean.parseBoolean(serverResponse.get(0)))
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Success", serverResponse.get(1)));
                            }
                            //if false, show an error message
                            else
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Registration error", serverResponse.get(1)));
                            }
                        }
                        break;
                    //Borrow book response
                    case 303:
                        Object messageContent = message.getMessageContent();
                        if (messageContent instanceof ArrayList<?>)
                        {
                            @SuppressWarnings("unchecked") ArrayList<String> response = (ArrayList<String>) messageContent;

                            // Use the showBorrowMessageResponse method to display the response
                            BorrowBookFrameController.showBorrowMessageResponse(response);
                        }
                        else
                        {
                            // Log an error or display an error message if the response format is unexpected
                            System.err.println("Unexpected format for Borrow book response: " + messageContent);
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Borrow Book Error", "Invalid response format from the server."));
                        }
                        break;
                    //Return book response
                    case 305:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not return book");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not return book"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList<?>)
                        {
                            @SuppressWarnings("unchecked") ArrayList<String> response = (ArrayList<String>) message.getMessageContent();

                            // Use the showReturnMessageResponse method to display the response
                            ReturnBookFrameController.showReturnMessageResponse(response);
                        }
                        else
                        {
                            // Log an error or display an error message if the response format is unexpected
                            System.err.println("Unexpected format for Return book response: " + message.getMessageContent());
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Return Book Error", "Invalid response format from the server."));
                        }
                        break;
                    // Get all subscribers list
                    case 307:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not get all subscribers list");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not get all subscribers list"));
                        }
                        //if the content is instance of arrayList, pass it to ViewAllSubscribersFrameController's addToTable
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked") ArrayList<Subscriber> subscribers = (ArrayList<Subscriber>) message.getMessageContent();

                            // Retrieve the controller for the "View All Subscribers" frame.
                            ViewAllSubscribersFrameController controller = loader.getController();

                            // Pass the list of subscribers to the controller to populate the table view.
                            controller.addToTable(subscribers);
                        }
                        break;
                    // Watch subscriber details response
                    case 309:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Wrong Username(id)");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Search Error", "Wrong ID,try again"));
                        }
                        else if (message.getMessageContent() instanceof Subscriber)
                        {
                            Subscriber subscriberFromServer = (Subscriber) message.getMessageContent();
                            Subscriber.setWatchProfileSubscriber(subscriberFromServer);

                            //retrieve the controller for "SearchSubscriberFrameController" frame.
                            SearchSubscriberFrameController Controller = loader.getController();

                            //pass the subscriber to the controller
                            Controller.WatchProfileResponse(subscriberFromServer);
                        }
                        break;
                    // Extend borrow - librarian response
                    case 311:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not extend borrow");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not extend borrow"));
                        }
                        //if the content is instance of arraylist, pass it to HandleBorrowedBookFrameController's showExtendMessageResponse
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked")
                            ArrayList<String> arrayMessageFromServer = (ArrayList<String>) message.getMessageContent();
                            HandleBorrowedBookFrameController.showExtendMessageResponse(arrayMessageFromServer);
                        }
                        //if the content is not, show error message
                        else
                        {
                            System.out.println("Invalid message content for case 311");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Data Error", "Expected an ArrayList for borrow extension."));
                        }
                        break;
                    // Watch logs response
                    case 313:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not get logs");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not get logs"));
                            ViewReportsFrameController controller = loader.getController();
                            controller.BorrowChart.setVisible(false);
                            controller.subscriberStatusesChart.setVisible(false);
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked")
                            ArrayList<MonthlyReport> reports = (ArrayList<MonthlyReport>) message.getMessageContent();
                            //retrieve the controller for " ViewReportsFrameController" frame.
                            ViewReportsFrameController controller = loader.getController();
                            //pass the reports to the controller
                            controller.generateBorrowTimeReport(reports);
                            controller.switchChartVisibility();
                        }
                        break;
                    // Handles the Subscriber Status report response
                    case 315:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not get logs");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not get logs"));
                            ViewReportsFrameController controller = loader.getController();
                            controller.BorrowChart.setVisible(false);
                            controller.subscriberStatusesChart.setVisible(false);
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked")
                            ArrayList<MonthlyReport> logs = (ArrayList<MonthlyReport>) message.getMessageContent();

                            //retrieve the controller for " ViewReportsFrameController" frame.
                            ViewReportsFrameController controller = loader.getController();

                            //pass the logs to the controller
                            controller.generateSubscriberStatusReport(logs);
                            controller.switchChartVisibility();
                        }
                        break;
                    // View messages response
                    case 317:
                        //display an error if the message content is null
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not get messages");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not get messages"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            @SuppressWarnings("unchecked")
                            List<ArrayList<String>> messages = (List<ArrayList<String>>) message.getMessageContent();

                            //retrieve the controller for "ViewMessagesFrameController" frame.
                            ViewMessagesFrameController controller = loader.getController();

                            //pass the message to the controller
                            controller.loadLibrarianMessages(messages);
                        }
                        break;
                    // handles a lost book response
                    case 319:
                        if (message.getMessageContent() == null)
                        {
                            System.out.println("Could not get messages");
                            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Could not get messages"));
                        }
                        else if (message.getMessageContent() instanceof ArrayList)
                        {
                            // Cast the message content to an ArrayList and give a pop-up message to the librarian
                            @SuppressWarnings("unchecked") ArrayList<String> serverResponse = (ArrayList<String>) message.getMessageContent();

                            // If the first element of the content is true, show a success message
                            if (Boolean.parseBoolean(serverResponse.get(0)))
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Success", "The book was marked as lost successfully"));
                            }
                            else
                            {
                                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", serverResponse.get(1)));
                            }
                        }
                        break;
                    // Server has closed its connection
                    case 999:
                        Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Server closed", "Server has closed its connection for maintenance"));
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
     * This method updates the subscriber details.
     *
     * @param newDetails The new details to be updated.
     */
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
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Update error", "Could not update subscriber details"));
        }
    }
}