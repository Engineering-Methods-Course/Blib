package gui.controllers;

import main.ClientGUIController;
import common.ClientServerMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static main.ClientGUIController.*;

public class BorrowBookFrameController {
    // FXML attributes
    @FXML
    public TextField bookIDTextField;
    @FXML
    public TextField subscriberIDTextField;
    @FXML
    public DatePicker returnDatePicker;
    @FXML
    public Button borrowButton;
    @FXML
    public Label ErrorSubscriberID;
    @FXML
    public Label ErrorBookID;

    /**
     * The method displays an appropriate response message for the borrowing process.
     *
     * @param msg -msg an ArrayList of strings containing the server's response.
     *            {Success/failure,explanation}
     */
    public static void showBorrowMessageResponse(ArrayList<String> msg) {

        /*
         * Ensure the UI update runs on the JavaFX Application Thread.
         */

        Platform.runLater(() -> {
            //checks if the response is null or empty, and display an error
            if (msg == null || msg.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
                return;
            }

            /*
             * Extract the status from the response
             */

            String status = msg.get(0);

            /*
             * If the borrow was successful, show the correct message
             */

            if ("true".equals(status)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book was borrowed successfully!");
            }

            /*
             * If the borrow failed, show an error with the reason to it
             */

            else if ("false".equals(status)) {
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Borrow Failed", "Reason: " + explanation);
            }

            /*
             * Handles unexpected response, generates a generic error
             */

            else {
                showAlert(Alert.AlertType.ERROR, "Error", "Unexpected response format from server.");
            }
        });
    }

    /**
     * This method is called when the borrow button is clicked
     */
    public void initialize() {

        /*
         * Set the default return date to 14 days from today
         */

        returnDatePicker.setValue(LocalDate.now().plusDays(14));

        /*
         * Prevent the user from selecting a return date earlier than or equal to borrow date
         */

        returnDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Disable return dates before or including the borrow date
                setDisable(empty || date.isBefore(LocalDate.now().plusDays(1)));
            }
        });

        /*
         * Add listeners to validate subscriberID and bookID fields as the user types
         */

        subscriberIDTextField.textProperty().addListener((observable, oldValue, newValue) -> validateSubscriberID(newValue));
        bookIDTextField.textProperty().addListener((observable, oldValue, newValue) -> validateBookID(newValue));

        /*
         * Creates Tooltips to the text boxes and date picker that will appear when the user hovers over them
         */

        Tooltip subscriberIDTooltip = new Tooltip("Enter the subscribers unique ID number (e.g. 100001), Or scan member card.");
        Tooltip bookIDTooltip = new Tooltip("Enter the books unique copy number, Or scan library barcode.");
        Tooltip returnDateTooltip = new Tooltip("Enter the return date of the book (Default is set for 2 weeks).");

        /*
         * Adds the tooltips to the text boxes and date picker
         */

        subscriberIDTextField.setTooltip(subscriberIDTooltip);
        bookIDTextField.setTooltip(bookIDTooltip);
        returnDatePicker.setTooltip(returnDateTooltip);
    }

    /**
     * This method is called when the borrow button is clicked
     */
    public void borrowButtonClicked() {
        /*
         * Extract data from the fields
         */
        String subscriberID = subscriberIDTextField.getText().trim();
        String copyID = bookIDTextField.getText().trim();
        LocalDate returnDate = returnDatePicker.getValue();

        /*
         * Validate fields
         */

        if (subscriberID.isEmpty() || copyID.isEmpty() || returnDate == null) {
            // Handle missing data (e.g., show an error dialog)
            showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill in all the fields: Subscriber ID, Book ID, and Return Date.");
            return;
        }

        /*
         * Format the return date
         */

        String formattedReturnDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(returnDate);

        /*
         * Create the ArrayList for message content
         */

        ArrayList<String> dataToSend = new ArrayList<>();
        dataToSend.add(subscriberID);
        dataToSend.add(copyID);
        dataToSend.add(formattedReturnDate);

        /*
         * Create the ClientServerMessage object
         */

        ClientServerMessage message = new ClientServerMessage(302, dataToSend);

        /*
         * Send the message to the server
         */

        ClientGUIController.chat.sendToServer(message);

    }

    /**
     * Validate the subscriber ID field.
     *
     * @param subscriberID The input value of the subscriber ID field.
     */
    private void validateSubscriberID(String subscriberID) {
        /*
         * checks if the subscriber ID is empty
         */
        if (subscriberID.isEmpty()) {
            showErrorListenField(subscriberIDTextField, ErrorSubscriberID, "Subscriber ID cannot be empty.");
            return;
        }
        /*
         * checks if the subscriber ID contains only numbers
         */
        else if (!subscriberID.matches("^[0-9]+$")) {
            showErrorListenField(subscriberIDTextField, ErrorSubscriberID, "Subscriber ID must contain only numbers.");
            return;
        }

        /*
         * Reset error state if the input is valid
         */
        resetErrorState(subscriberIDTextField, ErrorSubscriberID);
    }

    /**
     * Validate the book ID field.
     *
     * @param bookID The input value of the book ID field.
     */
    private void validateBookID(String bookID) {
        /*
         * checks if the book ID is empty
         */
        if (bookID.isEmpty()) {
            showErrorListenField(bookIDTextField, ErrorBookID, "Book ID cannot be empty.");
            return;
        }
        /*
         * checks if the book ID contains only numbers
         */
        else if (!bookID.matches("^[0-9]+$")) {
            showErrorListenField(bookIDTextField, ErrorBookID, "Book ID must contain only numbers.");
            return;
        }

        /*
         * Reset error state if the input is valid
         */
        resetErrorState(bookIDTextField, ErrorBookID);
    }
}