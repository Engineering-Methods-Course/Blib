package gui;

import client.ClientGUIController;
import common.Book;
import common.ClientServerMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;

public class ReturnBookFrameController {

    @FXML
    private TextField idTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button backButton;
    @FXML
    private TableView<Book> booksTableView;
    @FXML
    private TableColumn<Book, String> serialNumberColumn;
    @FXML
    private TableColumn<Book, String> returnDateColumn;
    @FXML
    private TableColumn<Book, Button> actionColumn;

    /**
     * Initializes the table and sets up column bindings.
     */
    public void initialize() {
        //todo:

    }


    /**
     * Handles the Back button click event.
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception {
        navigateTo(event, "LibrarianProfileFrame.fxml", "Subscriber.css", "Return");
    }


    /**
     * Handles the Return button click event.
     * Sends the copy ID (from the text field) to the server with message code 304.
     * If the copy ID is empty, an error message is printed.
     *
     * @param actionEvent The ActionEvent triggered by the Return button.
     */
    public void returnButtonClicked(ActionEvent actionEvent) {
        // Extract data from the text field
        String copyID = idTextField.getText().trim();

        // Validate fields
        if (copyID.isEmpty()) {
            // Handle missing data (e.g., show an error dialog)
            System.out.println("Please enter a valid Copy ID.");
            return;
        }

        // Create the ClientServerMessage object with code 304 and the copy ID as the message content
        ClientServerMessage message = new ClientServerMessage(304, copyID);

        try {
            // Send the message to the server
            ClientGUIController.chat.sendToServer(message);
        } catch (Exception e) {
            System.out.println("Error sending return request to the server: " + e.getMessage());
        }
    }

    public static void showReturnMessageResponse(ArrayList<String> msg) {
        Platform.runLater(() -> {
            if (msg == null || msg.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
                return;
            }

            String status = msg.get(0);
            if ("true".equals(status)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book was returned successfully!");
            } else if ("false".equals(status)) {
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Return Failed", "Reason: " + explanation);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Unexpected response format from server.");
            }
        });
    }

    /**
     * Helper method to display an alert.
     *
     * @param alertType The type of the alert (e.g., INFORMATION, ERROR)
     * @param title     The title of the alert
     * @param message   The message to display in the alert
     */
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
