package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.ArrayList;

import static client.ClientGUIController.showAlert;

public class RegisterMemberFrameController
{
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
        if (regexMatcher("^\\+?[0-9. ()-]{7,}$", phone))
        {
            showAlert(Alert.AlertType.WARNING, "Invalid Phone Number", "Please enter a valid phone number.");
            return;
        }

        ArrayList<String> messageContent = new ArrayList<>();
        messageContent.add(username);
        messageContent.add(name);
        messageContent.add(lastName);
        messageContent.add(phone);
        messageContent.add(email);

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
        return !input.matches(regex);
    }
}