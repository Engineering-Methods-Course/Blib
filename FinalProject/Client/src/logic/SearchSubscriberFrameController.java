package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


import static client.ClientGUIController.navigateTo;
import static client.ClientGUIController.showAlert;

public class SearchSubscriberFrameController
{

    @FXML
    private TextField idTextField;

    public void watchProfileButtonClicked(ActionEvent actionEvent)
    {
        // Get the ID from the TextField
        String userIDText = idTextField.getText().trim();

        if (userIDText.isEmpty())
        {
            System.out.println("Please enter a valid Copy ID.");
            return;
        }
        int userID;
        try
        {
            userID = Integer.parseInt(userIDText);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid Copy ID. Please enter a numeric value.");
            return;
        }

        ClientServerMessage message = new ClientServerMessage(308, userID);
        try
        {
            // Store the ActionEvent to pass it to the response handler
            ClientController.setActionEvent(actionEvent);
            // Send the request to the server
            ClientGUIController.chat.sendToServer(message);
        }
        catch (Exception e)
        {
            System.out.println("Error sending request to the server: " + e.getMessage());
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
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "Return");
    }

    public static void WatchProfileResponse(Subscriber subscriberFromServer)
    {
        Platform.runLater(() -> {
            if (subscriberFromServer == null)
            {
                showAlert(Alert.AlertType.ERROR, "Error", "Subscriber not found or invalid ID.");
            }
            else
            {
                // Set the subscriber for further use
                Subscriber.setWatchProfileSubscriber(subscriberFromServer);

                // Retrieve the stored ActionEvent and navigate
                ActionEvent storedEvent = ClientController.getStoredActionEvent();
                if (storedEvent != null)
                {
                    try
                    {
                        navigateTo(storedEvent, "/gui/WatchProfileFrame.fxml", "/gui/Subscriber.css", "Watch Profile");
                    }
                    catch (Exception e)
                    {
                        showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the subscriber details.");
                    }
                }
                else
                {
                    showAlert(Alert.AlertType.ERROR, "Error", "Action context is missing for navigation.");
                }
            }
        });
    }
}