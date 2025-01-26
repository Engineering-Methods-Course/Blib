package gui.controllers;

import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.ClientGUIController;

import java.util.ArrayList;

import static main.ClientGUIController.*;

public class ChangePasswordFrameController {
    // FXML attributes
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

    // other class attributes
    private boolean isNewPassFieldTouched = false;
    private boolean isConfirmPassFieldTouched = false;


    /**
     * Initializes the controller by setting up field listeners.
     */
    public void initialize() {
        /*
         * Add listener for newPassField
         */

        newPassField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isNewPassFieldTouched) {
                validateNewPassword(newValue);
            }
        });

        /*
         * Add listener for newPassField
         */

        newPassField.setOnMouseClicked(event -> isNewPassFieldTouched = true);

        /*
         * Add listener for newPassField
         */

        newPassField.setOnKeyPressed(event -> isNewPassFieldTouched = true);

        /*
         *Add listener for confirmPassField
         */

        confirmPassField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isConfirmPassFieldTouched) {
                validateConfirmPassword(newValue);
            }
        });

        /*
         *Create the tooltips for the text fields
         */

        Tooltip newPassTooltip = new Tooltip("Enter a new password.\nMust be at least 8 characters long.\nMust have a capital letter.\nMust have a lower case letter.\nMust have numbers and a special character.");
        Tooltip confirmPassTooltip = new Tooltip("Re-enter the new password.");

        /*
         *Add Tooltips to the text boxes that will appear when the user hovers over them
         */

        newPassField.setTooltip(newPassTooltip);
        confirmPassField.setTooltip(confirmPassTooltip);

        /*
         *Add listener for confirmPassField
         */

        confirmPassField.setOnMouseClicked(event -> isConfirmPassFieldTouched = true);

        /*
         *Add listener for confirmPassField
         */

        confirmPassField.setOnKeyPressed(event -> isConfirmPassFieldTouched = true);
    }

    /**
     * This method is called when the update button is clicked
     *
     * @throws Exception If an error occurs during navigation
     */
    public void clickUpdateButton() throws Exception {
        /*
         * Check if the password fields are empty
         */
        if (newPassField.getText().isEmpty() || confirmPassField.getText().isEmpty()) {
            System.out.println("Password fields cannot be empty");
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Password fields cannot be empty"));
        }
        /*
         * checks if the new password matches the "confirm password" field
         */
        else if (!newPassField.getText().equals(confirmPassField.getText())) {
            System.out.println("Passwords do not match");
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match"));
        } else if (!newPassField.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Must be at least 8 characters long, have a capital letter, have a lower case letter, have numbers and a special character.");
        } else {
            /*
             * creates an array list to store the subscriber ID and the new password
             */
            ArrayList<String> messageContent = new ArrayList<>();
            messageContent.add(String.valueOf(Subscriber.getLocalSubscriber().getID()));
            messageContent.add(newPassField.getText());

            /*
             * Update the password
             */
            ClientServerMessage message = new ClientServerMessage(218, messageContent);

            /*
             * Send the message to the server
             */
            ClientGUIController.chat.sendToServer(message);

            /*
             * Navigate back to the profile options page
             * Get the parent container and replace the content
             */
            AnchorPane parentContainer = (AnchorPane) changePasswordFrame.getParent();
            loadFrameIntoPane(parentContainer, "/gui/fxml/SubscriberProfileFrame.fxml");
        }
    }

    /**
     * Validates the new password field.
     *
     * @param newPassword The input value of the new password field.
     */
    private void validateNewPassword(String newPassword) {
        // Check if the new password is empty
        if (newPassword.isEmpty()) {
            showErrorListenField(newPassField, lblNewPasswordError, "New password cannot be empty.");
        }
        // checks if the password matches the requirements
        else if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            showErrorListenField(newPassField, lblNewPasswordError, "Must be at least 8 characters long, have a capital letter, have a lower case letter, have numbers and a special character.");
        }
        // reset the error state
        else {
            resetErrorState(newPassField, lblNewPasswordError);
        }
    }

    /**
     * Validates the confirmation password field.
     *
     * @param confirmPassword The input value of the confirmation password field.
     */
    private void validateConfirmPassword(String confirmPassword) {
        /*
         * Check if the "confirm password" is empty
         */
        if (confirmPassword.isEmpty()) {
            showErrorListenField(confirmPassField, lblConfirmError, "Confirm password cannot be empty.");
        }
        /*
         * check if the "confirm password" field is not equal to the "new password" field
         */
        else if (!confirmPassword.equals(newPassField.getText())) {
            showErrorListenField(confirmPassField, lblConfirmError, "Passwords do not match.");
        }
        /*
         * reset the error state
         */
        else {
            resetErrorState(confirmPassField, lblConfirmError);
        }
    }
}