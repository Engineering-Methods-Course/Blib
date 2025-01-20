package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML
    public Label lblNewPasswordError;
    @FXML
    public Label lblConfirmError;

    private boolean isNewPassFieldTouched = false;
    private boolean isConfirmPassFieldTouched = false;


    /**
     * Initializes the controller by setting up field listeners.
     */
    public void initialize() {
        // Add listener for newPassField
        newPassField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isNewPassFieldTouched) {
                validateNewPassword(newValue);
            }
        });

        newPassField.setOnMouseClicked(event -> isNewPassFieldTouched = true);
        newPassField.setOnKeyPressed(event -> isNewPassFieldTouched = true);

        // Add listener for confirmPassField
        confirmPassField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isConfirmPassFieldTouched) {
                validateConfirmPassword(newValue);
            }
        });

        confirmPassField.setOnMouseClicked(event -> isConfirmPassFieldTouched = true);
        confirmPassField.setOnKeyPressed(event -> isConfirmPassFieldTouched = true);
    }
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

    /**
     * Validates the new password field.
     *
     * @param newPassword The input value of the new password field.
     */
    private void validateNewPassword(String newPassword) {
        if (newPassword.isEmpty()) {
            showErrorListenField(newPassField, lblNewPasswordError, "New password cannot be empty.");
        } else if (newPassword.length() < 6) {
            showErrorListenField(newPassField, lblNewPasswordError, "Password must be at least 6 characters long.");
        } else {
            resetErrorState(newPassField, lblNewPasswordError);
        }
    }

    /**
     * Validates the confirmation password field.
     *
     * @param confirmPassword The input value of the confirmation password field.
     */
    private void validateConfirmPassword(String confirmPassword) {
        if (confirmPassword.isEmpty()) {
            showErrorListenField(confirmPassField, lblConfirmError, "Confirm password cannot be empty.");
        } else if (!confirmPassword.equals(newPassField.getText())) {
            showErrorListenField(confirmPassField, lblConfirmError, "Passwords do not match.");
        } else {
            resetErrorState(confirmPassField, lblConfirmError);
        }
    }

}