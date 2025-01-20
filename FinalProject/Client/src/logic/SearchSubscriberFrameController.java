package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import static client.ClientGUIController.*;

public class SearchSubscriberFrameController
{
    // FXML attributes
    @FXML
    public AnchorPane searchSubscriberFrame;
    @FXML
    public Button watchProfileButton;
    @FXML
    public Label lblSubscriberIDError;
    @FXML
    private TextField idTextField;

    @FXML
    public void initialize() {
        // Adding listener to the ID TextField
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateSubscriberID(newValue);
        });
    }

    private void validateSubscriberID(String newValue) {
        if (newValue.trim().isEmpty()) {
            // Call showErrorListenField to display the error
            showErrorListenField(idTextField, lblSubscriberIDError, "ID cannot be empty.");
        } else if (!newValue.matches("\\d+")) {  // Check if the input is numeric
            // Call showErrorListenField to display the error
            showErrorListenField(idTextField, lblSubscriberIDError, "Invalid ID. Please enter a numeric value.");
        } else {
            // Reset error state if the input is valid
            resetErrorState(idTextField, lblSubscriberIDError);
        }
    }

    /**
     * handles the watch profile button click event
     *
     * @param actionEvent the event that triggered the method
     */
    public void watchProfileButtonClicked(ActionEvent actionEvent)
    {
        // Get the ID from the TextField
        String userIDText = idTextField.getText().trim();

        if (userIDText.isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID cannot be empty.");
            return;
        }
        int userID;
        try
        {
            userID = Integer.parseInt(userIDText);
        }
        catch (NumberFormatException e)
        {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid Copy ID. Please enter a numeric value.");
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
     * Handles the response from the server for the watch profile request
     *
     * @param subscriberFromServer the subscriber that was returned from the server
     */
    public void WatchProfileResponse(Subscriber subscriberFromServer)
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
                        loadFrameIntoPane((AnchorPane) searchSubscriberFrame.getParent(), "/gui/WatchProfileFrame.fxml");
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