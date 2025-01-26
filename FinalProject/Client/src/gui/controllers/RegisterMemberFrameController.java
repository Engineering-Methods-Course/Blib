package gui.controllers;

import main.ClientGUIController;
import common.ClientServerMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;

import static main.ClientGUIController.*;

public class RegisterMemberFrameController
{
    // FXML attributes
    @FXML
    public Label lblPhoneError;
    @FXML
    public Label lblEmailError;
    @FXML
    public Label lblNameError;
    @FXML
    public Label lblLastNameError;
    @FXML
    public Label lblUserNameError;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;


    /**
     * Initializes the listeners for all text fields to handle real-time validation.
     */
    @FXML
    public void initialize()
    {
        // Username field listener
        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty())
            {
                showErrorListenField(txtUsername, lblUserNameError, "Username cannot be empty");
            }
            else
            {
                resetErrorState(txtUsername, lblUserNameError);
            }
        });

        // First Name field listener
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty())
            {
                showErrorListenField(txtFirstName, lblNameError, "First Name cannot be empty");
            }
            else
            {
                resetErrorState(txtFirstName, lblNameError);
            }
        });

        // Last Name field listener
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty())
            {
                showErrorListenField(txtLastName, lblLastNameError, "Last Name cannot be empty");
            }
            else
            {
                resetErrorState(txtLastName, lblLastNameError);
            }
        });

        // Phone field listener
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (regexMatcher("^0(50|52|53|54|58|2|3|4|8|9|7|56|59)\\d{7}$", newValue))
            {
                showErrorListenField(txtPhone, lblPhoneError, "Invalid phone number format");
            }
            else
            {
                resetErrorState(txtPhone, lblPhoneError);
            }
        });

        // Email field listener
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (regexMatcher("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", newValue))
            {
                showErrorListenField(txtEmail, lblEmailError, "Invalid email format");
            }
            else
            {
                resetErrorState(txtEmail, lblEmailError);
            }
        });

        // Create the tooltips for the text fields
        Tooltip usernameTooltip = new Tooltip("Enter a unique username.");
        Tooltip nameTooltip = new Tooltip("Enter your first name.");
        Tooltip lastNameTooltip = new Tooltip("Enter your last name.");
        Tooltip phoneTooltip = new Tooltip("Enter your phone number.");
        Tooltip emailTooltip = new Tooltip("Enter your email address.");

        // Add Tooltips to the text boxes that will appear when the user hovers over them
        txtUsername.setTooltip(usernameTooltip);
        txtFirstName.setTooltip(nameTooltip);
        txtLastName.setTooltip(lastNameTooltip);
        txtPhone.setTooltip(phoneTooltip);
        txtEmail.setTooltip(emailTooltip);
    }

    /**
     * Handles the Register button click event.
     */
    public void clickRegisterButton()
    {
        // Collect data from the form fields
        String username = txtUsername.getText();
        String name = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        // Check for empty fields
        if (username.isEmpty() || name.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty())
        {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill all fields.");
            return;
        }

        // Validate email format
        if (regexMatcher("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email))
        {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        // Validate phone format
        if (regexMatcher("^0(50|52|53|54|58|2|3|4|8|9|7|56|59)\\d{7}$", phone))
        {
            showAlert(Alert.AlertType.WARNING, "Invalid Phone Number", "Please enter a valid phone number.");
            return;
        }

        // Create an ArrayList to store the registration details
        ArrayList<String> messageContent = new ArrayList<>();
        messageContent.add(username);
        messageContent.add(name);
        messageContent.add(lastName);
        messageContent.add(phone);
        messageContent.add(email);

        // Create a message to send to the server
        ClientServerMessage message = new ClientServerMessage(300, messageContent);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * This is a helper method checks if the input matches the regex pattern.
     *
     * @param regex The regex pattern to match.
     * @param input The input to match against the regex pattern.
     * @return True if the input matches the regex pattern, false otherwise.
     */
    private boolean regexMatcher(String regex, String input)
    {
        // matches the string to the regex pattern
        return !input.matches(regex);
    }
}