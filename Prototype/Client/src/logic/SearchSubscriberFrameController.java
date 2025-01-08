package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;

public class SearchSubscriberFrameController
{
    @FXML
    private TextField idTextField;

    public void watchProfileButtonClicked(ActionEvent actionEvent)
    {
        // Get the ID from the TextField
        String userIDText = idTextField.getText().trim();

        // Validate fields
        if (userIDText.isEmpty())
        {
            // Handle missing data (e.g., show an error dialog)
            System.out.println("Please enter a valid Copy ID.");
            return;
        }

        int userID;
        try
        {
            // Parse the text input to an integer
            userID = Integer.parseInt(userIDText);
        }
        catch (NumberFormatException e)
        {
            // Handle invalid number input
            System.out.println("Invalid Copy ID. Please enter a numeric value.");
            return;
        }

        // Create the ClientServerMessage object with code 308 and the integer ID as the message content
        ClientServerMessage message = new ClientServerMessage(308, userID);

        try
        {
            // Send the message to the server
            ClientGUIController.chat.sendToServer(message);
        }
        catch (Exception e)
        {
            System.out.println("Error sending return request to the server: " + e.getMessage());
        }
    }

    /**
     * Handles the Back button click event.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "LibrarianProfileFrame.fxml", "Subscriber.css", "Return");
    }

    /**
     * @param msg
     */
    public static void WatchProfileResponse(ArrayList<String> msg)
    {
        Platform.runLater(() -> {
            if (msg == null)
            {
                // Null response indicates failure
                showAlert(Alert.AlertType.ERROR, "Error", "Subscriber not found or invalid ID.");
                return;
            }

            // Check for success or failure
            String status = msg.get(0);
            if ("true".equals(status))
            {
                // Navigate to the next window (assume appropriate navigation logic)
                try
                {
                    navigateTo(null, "watchProfileFrame.fxml", "Subscriber.css", "Watch Profile");
                }
                catch (Exception e)
                {
                    showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the subscriber details.");
                }
            }
            else if ("false".equals(status))
            {
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Search Failed", "Reason: " + explanation);
            }
            else
            {
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
    private static void showAlert(Alert.AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}