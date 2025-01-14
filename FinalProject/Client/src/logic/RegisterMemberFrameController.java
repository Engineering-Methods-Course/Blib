package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;
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
     *
     * @param event The ActionEvent triggered by clicking the button.
     */
    public void clickRegisterButton(ActionEvent event) throws Exception
    {
        // Collect data from the form fields
        String username = txtUsername.getText();
        String name = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        // alerts the user if any of the fields are empty
        if (username.isEmpty() || name.isEmpty() || lastName.isEmpty() || regexMatcher("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email) || regexMatcher("^\\+?[0-9. ()-]{7,}$", phone))
        {
            showAlert(AlertType.WARNING, "Input Error", "Please fill all fields.");
        }

        ArrayList<String> messageContent = new ArrayList<>();
        messageContent.add(username);
        messageContent.add(name);
        messageContent.add(lastName);
        messageContent.add(email);
        messageContent.add(phone);

        ClientServerMessage message = new ClientServerMessage(300, messageContent);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);

        // navigate to the librarian profile page
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "Librarian Page");
    }

    /**
     * Handles the Back button click event.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "Register Member");
    }

    private boolean regexMatcher(String regex, String input)
    {
        return input.matches(regex);
    }
}