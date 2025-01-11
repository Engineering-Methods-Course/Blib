package logic;

import client.ClientGUIController;
import common.BorrowedBook;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;

public class WatchProfileFrameController
{
    @FXML
    private Text txtFullName;
    @FXML
    private Text txtPhoneNumber;
    @FXML
    private Text txtStatus;
    @FXML
    private Text txtEmail;
    @FXML
    private Text txtUserID;
    @FXML
    public Button btnBack;
    @FXML
    public TableView<BorrowedBook> borrowsTable;
    @FXML
    public TableColumn<BorrowedBook, String> bookNameColumn;
    @FXML
    public TableColumn<BorrowedBook, String> borrowDateColumn;
    @FXML
    public TableColumn<BorrowedBook, String> returnDateColumn;
    @FXML
    public TableColumn<BorrowedBook, Button> extendButtonColumn;


    public void initialize()
    {
        Subscriber subscriber = Subscriber.getWatchProfileSubscriber();
        if (subscriber == null)
        {
            System.out.println("Subscriber is null in WatchProfileFrameController.");
        }
        else
        {
            updateSubscriberInfo(subscriber);
        }
        // Set up the borrowed books table columns
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("bookName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("expectedReturnDate"));

        // Set up the extend button column
        extendButtonColumn.setCellFactory(new Callback<TableColumn<BorrowedBook, Button>, TableCell<BorrowedBook, Button>>() {
            @Override
            public TableCell<BorrowedBook, Button> call(TableColumn<BorrowedBook, Button> param) {
                return new TableCell<BorrowedBook, Button>() {
                    private final Button extendButton = new Button("Extend Borrow");

                    {
                        extendButton.setOnAction((ActionEvent event) -> {
                            try {
                                extendBorrowButtonClicked(event);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    @Override
                    protected void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(extendButton);
                        }
                    }
                };
            }
        });

        // Sends a message to the server to get the user's borrowed books
        requestBorrowedBooks(Subscriber.getWatchProfileSubscriber().getID());
    }

    public void updateSubscriberInfo(Subscriber subscriber)
    {
        txtFullName.setText(subscriber.getFirstName() + " " + subscriber.getLastName());
        txtEmail.setText("Email: " + subscriber.getEmail());
        txtPhoneNumber.setText("Phone number: " + subscriber.getPhoneNumber());
        txtUserID.setText("User ID: " + subscriber.getID());
        txtStatus.setText("Account Status: " + (subscriber.getStatusIsFrozen() ? "Frozen" : "Active"));
    }

    /**
     * Send the server request for borrowed books list
     */
    private void requestBorrowedBooks(int userID)
    {
        // Create a message with code 210 and userID
        ClientServerMessage message = new ClientServerMessage(210, userID);
        try
        {
            // Send the message to the server
            ClientGUIController.chat.sendToServer(message);
        }
        catch (Exception e)
        {
            System.out.println("Error sending request for borrowed books: " + e.getMessage());
        }
    }


    public void loadBorrowsTable(ArrayList<BorrowedBook> borrowedBooks)
    {
        // Adds the borrowed books to the table
        for (BorrowedBook borrowedBook : borrowedBooks)
        {
            borrowsTable.getItems().add(borrowedBook);
        }
    }

    public void extendBorrowButtonClicked(ActionEvent event) throws Exception
    {
        // Get the selected BorrowedBook from the clicked row
        BorrowedBook selectedBook = borrowsTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            // Save the selected book using the set method or pass it directly
            BorrowExtensionFrameController.setBorrowedBook(selectedBook); // Set the selected book in the Subscriber object, or use a different method
            // Navigate to the BorrowExtensionFrameController, passing the selected book

            navigateTo(event, "/gui/BorrowExtensionFrame.fxml", "/gui/Subscriber.css", "Extend Borrow Period");

        } else {
            // If no book is selected, show an alert
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to extend the borrow period.");
        }
    }


    /**
     * Helper method to display an alert.
     *
     * @param alertType The type of the alert (e.g., INFORMATION, ERROR)
     * @param title     The title of the alert
     * @param message   The message to display in the alert
     */
    private static void showAlert(Alert.AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the Back button click event.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        Subscriber.setWatchProfileSubscriber(null);
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "Return");
    }
}