package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;

public class ChangePasswordFrameController
{
    @FXML
    public TextField newPassField;
    @FXML
    public TextField confirmPassField;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnBack;

    /**
     * This method is called when the update button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void clickUpdateButton(ActionEvent event) throws Exception
    {
        if (!newPassField.getText().equals(confirmPassField.getText()))
        {
            System.out.println("Passwords do not match");
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match"));
        }
        else
        {
            ArrayList<String> messageContent = new ArrayList<>();
            messageContent.add(String.valueOf(Subscriber.getLocalSubscriber().getID()));
            messageContent.add(newPassField.getText());
            // Update the password
            ClientServerMessage message = new ClientServerMessage(218, messageContent);

            // Send the message to the server
            ClientGUIController.chat.sendToServer(message);

            // Navigate back to the profile options page
            navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My profile");
        }
    }

    /**
     * This method is called when the back button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void clickBackButton(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My profile");
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