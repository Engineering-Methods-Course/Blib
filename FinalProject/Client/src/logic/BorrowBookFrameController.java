package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;
import static client.ClientGUIController.showAlert;

public class BorrowBookFrameController
{
    @FXML
    public TextField bookIDTextField;
    @FXML
    public TextField subscriberIDTextField;
    @FXML
    public DatePicker borrowDatePicker;
    @FXML
    public DatePicker returnDatePicker;
    @FXML
    public Button btnBack;
    @FXML
    public Button borrowButton;

    /**
     * This method is called when the borrow button is clicked
     */
    public void initialize()
    {
        borrowDatePicker.setValue(LocalDate.now());
        returnDatePicker.setValue(LocalDate.now().plusDays(14));

        // disable Borrow date picker date selection
        borrowDatePicker.setDisable(true);

        // Disable return dates before or equal to borrow date
        returnDatePicker.setDayCellFactory(picker -> new DateCell()
        {
            @Override
            public void updateItem(LocalDate date, boolean empty)
            {
                super.updateItem(date, empty);
                // Disable return dates before or including the borrow date
                setDisable(empty || date.isBefore(borrowDatePicker.getValue().plusDays(1)));
            }
        });
    }

    /**
     * This method is called when the borrow button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "Librarian Profile");
    }

    /**
     * This method is called when the borrow button is clicked
     *
     * @param event The event that triggered this method
     */
    public void borrowButtonClicked(ActionEvent event)
    {
        // Extract data from the fields
        String subscriberID = subscriberIDTextField.getText().trim();
        String copyID = bookIDTextField.getText().trim();
        LocalDate returnDate = returnDatePicker.getValue();

        // Validate fields
        if (subscriberID.isEmpty() || copyID.isEmpty() || returnDate == null)
        {
            // Handle missing data (e.g., show an error dialog)
            System.out.println("Please fill all fields.");
            return;
        }

        // Format the return date
        String formattedReturnDate = returnDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Create the ArrayList for message content
        ArrayList<String> dataToSend = new ArrayList<>();
        dataToSend.add(subscriberID);
        dataToSend.add(copyID);
        dataToSend.add(formattedReturnDate);

        // Create the ClientServerMessage object
        ClientServerMessage message = new ClientServerMessage(302, dataToSend);

        try
        {
            ClientGUIController.chat.sendToServer(message);
        }
        catch (Exception e)
        {
            System.out.println("Error sending borrow request to the server: " + e.getMessage());
        }
    }

    public static void showBorrowMessageResponse(ArrayList<String> msg)
    {
        Platform.runLater(() -> {
            if (msg == null || msg.isEmpty())
            {
                showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
                return;
            }

            String status = msg.get(0);
            if ("true".equals(status))
            {
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book was borrowed successfully!");
            }
            else if ("false".equals(status))
            {
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Borrow Failed", "Reason: " + explanation);
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Error", "Unexpected response format from server.");
            }
        });
    }
}