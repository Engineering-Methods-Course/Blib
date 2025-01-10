package logic;

import client.ClientGUIController;
import common.Book;
import common.BorrowedBook;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

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
    public static TableView<BorrowedBook> borrowedBooksTable;
    @FXML
    public static TableColumn<BorrowedBook, Button> extendOptionsColumn;


    //210 returns the borrowed list

    public void initialize()
    {
        Subscriber subscriber = Subscriber.getWatchProfileSubscriber();
        if (subscriber == null) {
            System.out.println("Subscriber is null in WatchProfileFrameController.");
        } else {
            updateSubscriberInfo(subscriber);
        }
        //requestBorrowedBooks(subscriber.getID());
    }

    public void updateSubscriberInfo(Subscriber subscriber) {
        txtFullName.setText(subscriber.getFirstName() + " " + subscriber.getLastName());
        txtEmail.setText("Email: " + subscriber.getEmail());
        txtPhoneNumber.setText("Phone number: " + subscriber.getPhoneNumber());
        txtUserID.setText("User ID: " + subscriber.getID());
        txtStatus.setText("Account Status: " + (subscriber.getStatusIsFrozen() ? "Frozen" : "Active"));

    }

    /**
     * Send the server request for borrowed books list
     */
    private void requestBorrowedBooks(int userID) {
        // Create a message with code 210 and userID
        ClientServerMessage message = new ClientServerMessage(210, userID);

        try {
            // Send the message to the server
            ClientGUIController.chat.sendToServer(message);
        } catch (Exception e) {
            System.out.println("Error sending request for borrowed books: " + e.getMessage());
        }
    }

    /**
    //todo: make the table when server will do the request
    //todo: add functionality of extend button
    public static void handleBorrowedBooksResponse(ArrayList<BorrowedBook> borrowedBooks) {
        Platform.runLater(() -> {
            if (borrowedBooks == null || borrowedBooks.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Borrowed Books", "The subscriber has not borrowed any books.");
            } else {
                borrowedBooksTable.setItems(FXCollections.observableList(borrowedBooks));

                // Add an "Extend" button in the last column for each borrowed book
                for (BorrowedBook book : borrowedBooks) {
                    Button extendButton = new Button("Extend");
                    extendButton.setDisable(false); // Make the button visible
                    extendOptionsColumn.setCellValueFactory(cellData -> {
                        return new SimpleObjectProperty<>(extendButton); // Add the button to the table
                    });
                }

            }
        });
    }
     */


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