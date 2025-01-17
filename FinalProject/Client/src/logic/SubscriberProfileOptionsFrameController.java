package logic;

import client.ClientGUIController;
import common.BorrowedBook;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
//import com.opencsv.bean.CsvToBean;
//import com.opencsv.bean.CsvToBeanBuilder;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;
import static client.ClientGUIController.showAlert;

public class SubscriberProfileOptionsFrameController
{
    @FXML
    public Button logoutButton;
    @FXML
    public Button profileButton;
    @FXML
    public Text nameField;
    @FXML
    public Text phoneNumberField;
    @FXML
    public Text statusTextField;
    @FXML
    public Text emailField;
    @FXML
    public Button editProfileButton;
    @FXML
    public Button searchBookButton;
    @FXML
    public Text userIDField;
    @FXML
    public Button changePasswordButton;
    public TableView<BorrowedBook> borrowsTable;
    public TableColumn<BorrowedBook, String> bookNameColumn;
    public TableColumn<BorrowedBook, String> borrowDateColumn;
    public TableColumn<BorrowedBook, String> returnDateColumn;
    public TableColumn<BorrowedBook, Button> extendButtonColumn;
    public Button watchHistoryButton;

    public void initialize()
    {
        // Load all the user info into the fields
        nameField.setText("Hello: " + Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
        phoneNumberField.setText("Phone number: " + Subscriber.getLocalSubscriber().getPhoneNumber());
        emailField.setText("Email: " + Subscriber.getLocalSubscriber().getEmail());
        statusTextField.setText("Account Status: " + (Subscriber.getLocalSubscriber().getStatusIsFrozen() ? "Frozen" : "Active"));
        profileButton.setText("Hello: " + Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
        userIDField.setText("User ID: " + Subscriber.getLocalSubscriber().getID());

        // Set up the borrowed books table columns
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedReturnDate"));

        // Set up the "extend button" column
        extendButtonColumn.setCellFactory(new Callback<TableColumn<BorrowedBook, Button>, TableCell<BorrowedBook, Button>>()
        {
            @Override
            public TableCell<BorrowedBook, Button> call(TableColumn<BorrowedBook, Button> param)
            {
                return new TableCell<BorrowedBook, Button>()
                {
                    private final Button extendButton = new Button("Extend Borrow");

                    {
                        extendButton.setOnAction((ActionEvent event) -> {
                            try
                            {
                                extendBorrowButtonClicked(event);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Button item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (empty)
                        {
                            setGraphic(null);
                        }
                        else
                        {
                            setGraphic(extendButton);
                        }
                    }
                };
            }
        });

        // Sends a message to the server to get the user's borrowed books
        ClientServerMessage message = new ClientServerMessage(210, Subscriber.getLocalSubscriber().getID());

        // Sends the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    public void loadBorrowsTable(ArrayList<BorrowedBook> borrowedBooks)
    {
        // Adds the borrowed books to the table
        for (BorrowedBook borrowedBook : borrowedBooks)
        {
            borrowsTable.getItems().add(borrowedBook);
        }
    }

    /**
     * This method handles the logoutButton click event to navigate to the search home page
     *
     * @param event The action event triggered by clicking the logout button
     * @throws Exception If there is an issue with the navigation
     */
    public void logoutButtonClicked(ActionEvent event) throws Exception
    {
        //sets the local subscriber to null
        Subscriber.setLocalSubscriber(null);

        // Sends a message to the server to log out
        ClientServerMessage message = new ClientServerMessage(102, null);

        // Sends the message to the server
        ClientGUIController.chat.sendToServer(message);

        // Navigates to the search home page
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }

    /**
     * This method handles the viewProfileButton click event to navigate to the subscriber profile options frame
     *
     * @param event The action event triggered by clicking the view profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void viewProfileButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My Profile");
    }

    /**
     * This method handles the editProfileButton click event to navigate to the edit subscriber details frame
     *
     * @param event The action event triggered by clicking the edit profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void editProfileButtonCLicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberEditProfileFrame.fxml", "/gui/Subscriber.css", "Edit Profile");
    }

    /**
     * This method handles the extendBorrowButton click event to send extension request
     *
     * @param event The action event triggered by clicking the extend borrow button
     */
    public void extendBorrowButtonClicked(ActionEvent event)
    {
        // Get the selected borrowed book from the table
        BorrowedBook selectedBook = borrowsTable.getSelectionModel().getSelectedItem();

        // Check if a book is selected
        if (selectedBook == null)
        {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a book to extend.");
            return;
        }

        // Extract subscriberID and copyID from the selected book
        ClientServerMessage message = getClientServerMessage(selectedBook);

        // Send the message to the server
        try
        {
            ClientGUIController.chat.sendToServer(message);
        }
        catch (Exception e)
        {
            showAlert(Alert.AlertType.ERROR, "Error", "Error sending extend borrow request to the server: " + e.getMessage());
        }
    }

    private static ClientServerMessage getClientServerMessage(BorrowedBook selectedBook)
    {
        String subscriberID = String.valueOf(Subscriber.getLocalSubscriber().getID());
        String copyID = String.valueOf(selectedBook.getCopyID()); // Assuming BorrowedBook has a getCopyID() method

        // Create the message content as an ArrayList<String>
        ArrayList<String> extendRequestData = new ArrayList<>();
        extendRequestData.add(subscriberID);
        extendRequestData.add(copyID);

        // Create the ClientServerMessage object with code 212 for extend borrow request
        return new ClientServerMessage(212, extendRequestData);
    }

    public static void showExtendMessageResponse(ArrayList<String> msg)
    {
        Platform.runLater(() -> {
            if (msg == null || msg.isEmpty())
            {
                showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
                return;
            }
            // Check if the first element in the message is "True" or "False"
            String status = msg.get(0); // Extract status (True or False)

            if ("true".equals(status))
            {
                // Extract explanation if available
                String explanation = msg.size() > 1 ? msg.get(1) : "No additional information provided.";
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book return date was successfully extended! " + explanation);
            }
            else if ("false".equals(status))
            {
                // Extract explanation if available
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Extension Failed", "Reason: " + explanation);
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Error", "Unexpected response format from server.");
            }
        });
    }

    /**
     * This method handles the searchBookButton click event to navigate to the search home page
     *
     * @param event The action event triggered by clicking the search book button
     * @throws Exception If there is an issue with the navigation
     */
    public void searchBookButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }

    /**
     * This method handles the changePasswordButton click event to navigate to the change password frame
     *
     * @param event The action event triggered by clicking the change password button
     * @throws Exception If there is an issue with the navigation
     */
    public void changePasswordButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/ChangePasswordFrame.fxml", "/gui/Subscriber.css", "Change Password");
    }

    /**
     * This method handles the watchHistoryButton click event to navigate to the borrow history frame
     *
     * @param event The action event triggered by clicking the watch history button
     * @throws Exception If there is an issue with the navigation
     */
    public void watchHistoryButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/WatchHistoryScene.fxml", "/gui/Subscriber.css", "Watch History");
    }
}