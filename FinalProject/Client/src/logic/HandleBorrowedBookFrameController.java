package logic;

import client.ClientGUIController;
import common.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import static client.ClientGUIController.showAlert;

public class HandleBorrowedBookFrameController
{
    // FXML attributes
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

    // Other attributes
    private static BorrowedBook borrowedBookCopy;

    /**
     * This method initializes the Borrow Extension Frame
     */
    public void initialize()
    {
        Subscriber subscriber = Subscriber.getWatchProfileSubscriber();
        // Set subscriber details on UI
        subscriberNameLabel.setText(subscriber.getFirstName() + " " + subscriber.getLastName());
        subscriberIdLabel.setText(String.valueOf(subscriber.getID()));

        // Set book details on UI
        bookNameLabel.setText(borrowedBookCopy.getBookName());
        bookIdLabel.setText(String.valueOf(borrowedBookCopy.getCopyID()));
        currentReturnDateLabel.setText(borrowedBookCopy.getExpectedReturnDate());

        // Initialize the DatePicker
        initializeDatePicker();
    }

    /**
     * This method gets the borrowed book object
     */
    public static void setBorrowedBook(BorrowedBook borrowedBook)
    {
        HandleBorrowedBookFrameController.borrowedBookCopy = borrowedBook;
    }

    /**
     * This method handles the extendButton click event to extend the book borrowing period
     */
    public void extendButtonClicked()
    {
        // Extract data from the fields
        String subscriberID = subscriberIdLabel.getText().trim();
        String copyID = bookIdLabel.getText().trim();
        LocalDate newReturnDate = newReturnDatePicker.getValue();
        String librarianID = String.valueOf(Librarian.getLocalLibrarian().getID());

        // Validate fields
        if (newReturnDate == null)
        {
            // Handle missing data (e.g., show an error dialog)
            showAlert(Alert.AlertType.WARNING, "Invalid return date", "Please select a new return date");
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

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * This method handles the markLostButton click event to mark the book as lost
     */
    public void markLostButtonPressed()
    {
        //send the book id and the subscriber id to the server
        ArrayList<String> dataToSend = new ArrayList<>();
        dataToSend.add(subscriberIdLabel.getText());
        dataToSend.add(String.valueOf(borrowedBookCopy.getCopyID()));

        // Create the ClientServerMessage object
        ClientServerMessage message = new ClientServerMessage(318, dataToSend);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * This method shows the response message from the server after extending the book return date
     *
     * @param msg The response message from the server
     */
    public static void showExtendMessageResponse(ArrayList<String> msg)
    {
        Platform.runLater(() -> {
            // Check if the response is null or empty, and display an error
            if (msg == null || msg.isEmpty())
            {
                showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
                return;
            }

            // Extract status
            String status = msg.get(0);

            // Show appropriate message based on the status
            if ("true".equals(status))
            {
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book return date was successfully extended!");
            }
            else if ("false".equals(status))
            {
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
     * A helper method to initialize the DatePicker
     */
    private void initializeDatePicker()
    {
        try
        {
            // gets the current return date from the label and parse it to LocalDate
            String currentReturnDateText = currentReturnDateLabel.getText();
            LocalDate currentReturnDate = LocalDate.parse(currentReturnDateText);

            //gets the current date
            LocalDate currentDate = LocalDate.now();

            // sets the minimum date on the DatePicker
            newReturnDatePicker.setDayCellFactory(picker -> new DateCell()
            {
                @Override
                public void updateItem(LocalDate date, boolean empty)
                {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isBefore(currentReturnDate.isAfter(currentDate) ? currentReturnDate : currentDate));
                }
            });
        }
        catch (Exception e)
        {
            showAlert(Alert.AlertType.WARNING, "Invalid date", "Please enter a valid date");
        }
    }
}