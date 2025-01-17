package logic;

import client.ClientGUIController;
import common.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;

import static client.ClientGUIController.navigateTo;
import static client.ClientGUIController.showAlert;

public class BorrowExtensionFrameController
{
    @FXML
    public Button backButton;
    @FXML
    public Button extendButton;
    @FXML
    public Label subscriberNameLabel;
    @FXML
    public Label subscriberIdLabel;
    @FXML
    public Label bookNameLabel;
    @FXML
    public Label bookIdLabel;
    @FXML
    public Label currentReturnDateLabel;
    @FXML
    public DatePicker newReturnDatePicker;

    private static BorrowedBook borrowedBookCopy;

    /**
     * This method initializes the Borrow Extension Frame
     */
    public void initialize() {
        Subscriber subscriber = Subscriber.getWatchProfileSubscriber();

        // Set subscriber details on UI
        subscriberNameLabel.setText(subscriber.getFirstName() + subscriber.getLastName());
        subscriberIdLabel.setText(String.valueOf(subscriber.getID()));

        // Set book details on UI
        bookNameLabel.setText(borrowedBookCopy.getBookName());
        bookIdLabel.setText(String.valueOf(borrowedBookCopy.getCopyID()));
        currentReturnDateLabel.setText(borrowedBookCopy.getExpectedReturnDate());

        // Get the current return date from the label and parse it to LocalDate
        String currentReturnDateText = currentReturnDateLabel.getText();
        LocalDate currentReturnDate = LocalDate.parse(currentReturnDateText);

        // Set the minimum date on the DatePicker (restricting dates before the current return date)
        newReturnDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Disable dates before the current return date
                setDisable(empty || date.isBefore(currentReturnDate));
            }
        });
    }

    /**
     * This method gets the borrowed book object
     *
     * @return The borrowed book object
     */
    public static void setBorrowedBook(BorrowedBook borrowedBook) {
        BorrowExtensionFrameController.borrowedBookCopy = borrowedBook;
    }


    /**
     * This method handles the backButton click event to navigate back to the previous frame
     *
     * @param event The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/WatchProfileFrame.fxml", "/gui/Subscriber.css", "Watch Profile");
    }

    /**
     * This method handles the extendButton click event to extend the book borrowing period
     *
     * @param event The action event triggered by clicking the extend button
     */
    public void extendButtonClicked(ActionEvent event)
    {
        // Extract data from the fields
        String subscriberID = subscriberIdLabel.getText().trim();
        String copyID = bookIdLabel.getText().trim();
        LocalDate newReturnDate = newReturnDatePicker.getValue();
        String librarianID = String.valueOf(Librarian.getLocalLibrarian().getID());

        // Validate fields
        if (newReturnDate == null) {
            // Handle missing data (e.g., show an error dialog)
            System.out.println("Please select a new return date.");
            return;
        }

        // Format the new return date
        String formattedNewReturnDate = newReturnDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Create the ArrayList for message content
        ArrayList<String> dataToSend = new ArrayList<>();
        dataToSend.add(subscriberID);
        dataToSend.add(copyID);
        dataToSend.add(formattedNewReturnDate);
        dataToSend.add(librarianID);

        // Create the ClientServerMessage object
        ClientServerMessage message = new ClientServerMessage(310, dataToSend);

        try {
            // Send the message to the server
            ClientGUIController.chat.sendToServer(message);
        } catch (Exception e) {
            System.out.println("Error sending extension request to the server: " + e.getMessage());
        }
    }

    /**
     * This method shows the response message from the server after extending the book return date
     *
     * @param msg
     */
    public static void showExtendMessageResponse(ArrayList<String> msg) {
        Platform.runLater(() -> {
            if (msg == null || msg.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
                return;
            }
            String status = msg.get(0); // Extract status
            if ("true".equals(status)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book return date was successfully extended!");
            } else if ("false".equals(status)) {
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Extension Failed", "Reason: " + explanation);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Unexpected response format from server.");
            }
        });
    }
}