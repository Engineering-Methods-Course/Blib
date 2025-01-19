package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static client.ClientGUIController.*;

public class ChangePasswordFrameController
{
    @FXML
    public TextField newPassField;
    @FXML
    public TextField confirmPassField;
    @FXML
    public Button btnUpdate;
    @FXML
    public VBox changePasswordFrame;

    /**
     * This method is called when the update button is clicked
     *
     * @throws Exception If an error occurs during navigation
     */
    public void clickUpdateButton() throws Exception
    {
        if (newPassField.getText().isEmpty() || confirmPassField.getText().isEmpty())
        {
            System.out.println("Password fields cannot be empty");
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Password fields cannot be empty"));
        }
        else if (!newPassField.getText().equals(confirmPassField.getText()))
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
            // Get the parent container and replace the content
            AnchorPane parentContainer = (AnchorPane) changePasswordFrame.getParent();
            loadFrameIntoPane(parentContainer, "/gui/SubscriberProfileFrame.fxml");

        }
    }

}