package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

import static client.ClientGUIController.*;

public class ReturnBookFrameController
{
    // FXML attributes
    @FXML
    public Label errorLabel;
    @FXML
    public Button btnReturn;
    @FXML
    private TextField idTextField;

    /**
     * Initializes the controller.
     * Adds a listener to the text field to validate the input.
     */
    public void initialize()
    {
        // Listen to changes in the text field
        idTextField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                // Validate the input as the user types
                String copyID = newValue.trim();

                // Check for empty value or invalid format
                if (!copyID.isEmpty() && !copyID.matches("^[0-9]+$"))
                {
                    showErrorListenField(idTextField, errorLabel, "Copy ID must contain only numbers.");
                }
                else
                {
                    resetErrorState(idTextField, errorLabel);
                }
            }
        });
    }

    /**
     * Handles the Return button click event.
     * Sends the copy ID (from the text field) to the server with message code 304.
     * If the copy ID is empty, an error message is printed.
     */
    public void returnButtonClicked()
    {
        // Extract data from the text field
        String copyID = idTextField.getText().trim();

        // Validate fields
        if (copyID.isEmpty())
        {
            // Handle missing data (e.g., show an error dialog)
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid ID,cannot be empty");
            showErrorListenField(idTextField, errorLabel, "please enter a valid ID, cannot be empty");
            return;
        }
        // Use regex to check if the copyID contains only numbers
        if (!copyID.matches("^[0-9]+$"))
        {
            // Handle invalid input (e.g., show an error dialog)
            showAlert(Alert.AlertType.ERROR, "Input Error", "Copy ID must contain only numbers.");
            //showError("Copy ID must contain only numbers.");
            return;
        }

        // Reset the error state
        resetErrorState(idTextField, errorLabel);

        // Create the ClientServerMessage object with code 304 and the copy ID as the message content
        ClientServerMessage message = new ClientServerMessage(304, copyID);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * Shows the response message from the server.
     *
     * @param msg The response message from the server.
     */
    public static void showReturnMessageResponse(ArrayList<String> msg)
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

            // Check the status and show the appropriate message
            if ("true".equals(status))
            {
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book was returned successfully!");
            }
            else if ("false".equals(status))
            {
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Return Failed", "Reason: " + explanation);
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Error", "Unexpected response format from server.");
            }
        });
    }
}