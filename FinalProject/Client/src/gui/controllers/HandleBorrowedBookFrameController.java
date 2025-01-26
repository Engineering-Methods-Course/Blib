package gui.controllers;

import main.ClientGUIController;
import common.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import static main.ClientGUIController.loadFrameIntoPane;
import static main.ClientGUIController.showAlert;

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
    public Text bookNameLabel;
    @FXML
    public Label bookIdLabel;
    @FXML
    public Label currentReturnDateLabel;
    @FXML
    public DatePicker newReturnDatePicker;
    @FXML
    public AnchorPane handleBorrow;

    // Other attributes
    private static BorrowedBook borrowedBookCopy;
    private static String previousFrame;

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
     *
     * @param borrowedBook The borrowed book object
     */
    public static void setBorrowedBook(BorrowedBook borrowedBook)
    {
        HandleBorrowedBookFrameController.borrowedBookCopy = borrowedBook;
    }

    /**
     * This method handles the extendButton click event to extend the book borrowing period
     *
     * @throws IOException
     */
    public void extendButtonClicked() throws IOException {
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
        loadFrameIntoPane((AnchorPane) handleBorrow.getParent(), previousFrame);
    }

    /**
     * This method handles the markLostButton click event to mark the book as lost
     *
     * @throws IOException
     */
    public void markLostButtonPressed() throws IOException {
        //send the book id and the subscriber id to the server
        ArrayList<String> dataToSend = new ArrayList<>();
        dataToSend.add(subscriberIdLabel.getText());
        dataToSend.add(String.valueOf(borrowedBookCopy.getCopyID()));

        // Create the ClientServerMessage object
        ClientServerMessage message = new ClientServerMessage(318, dataToSend);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);
        loadFrameIntoPane((AnchorPane) handleBorrow.getParent(), previousFrame);
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
     * This method initializes the DatePicker
     */
    private void initializeDatePicker()
    {
        // gets the current return date from the label and parse it to LocalDate
        String currentReturnDateText = currentReturnDateLabel.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyy-M-d][yyyy-M-dd][yyyy-MM-d]");
        LocalDate currentReturnDate = LocalDate.parse(currentReturnDateText, formatter);

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

        // sets the date format
        newReturnDatePicker.setConverter(new StringConverter<LocalDate>()
        {
            // the date format
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date)
            {
                if (date != null)
                {
                    return formatter.format(date);
                }
                else
                {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string)
            {
                if (string != null && !string.isEmpty())
                {
                    return LocalDate.parse(string, formatter);
                }
                else
                {
                    return null;
                }
            }
        });

        //sets the initial date as the return date or the current date the latter of them
        newReturnDatePicker.setValue(currentReturnDate.isAfter(currentDate) ? currentReturnDate : currentDate);
    }

    /**
     * This method goes back to the previous frame
     *
     * @throws IOException
     */
    public void goBack() throws IOException
    {
        loadFrameIntoPane((AnchorPane) handleBorrow.getParent(), "/gui/fxml/WatchProfileFrame.fxml");
    }

    /**
     * Sets the previous frame
     *
     * @param frame The previous frame
     */
    public static void setPreviousFrame(String frame)
    {
        previousFrame = frame;
    }
}