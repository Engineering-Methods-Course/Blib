package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static client.ClientGUIController.navigateTo;

public class RegisterMemberFrameController {

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button BackButton;


    /**
     * Handles the Register button click event.
     * @param event The ActionEvent triggered by clicking the button.
     */
    public void clickRegisterButton(ActionEvent event)
    {
        //todo: make new member and save him in DB

        // Collect data from the form fields
        String id = txtID.getText();
        String name = txtName.getText();
        String lastName = txtLastName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();


        //alerts

        if (id.isEmpty() || name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please fill all fields.");
        } else {
            // Code to handle saving member to the system
            // After successful registration
            showAlert(AlertType.INFORMATION, "Success", "Member registered successfully.");
        }


    }


    /**
     * Show an alert to the user with a given type, title, and message.
     * @param type The type of the alert (e.g., ERROR, WARNING, INFORMATION)
     * @param title The title of the alert
     * @param message The message to be displayed
     */
    private void showAlert(AlertType type, String title, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the Back button click event.
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception {
        navigateTo(event, "LibrarianProfileFrame.fxml", "Subscriber.css", "Register Member");
    }
}
