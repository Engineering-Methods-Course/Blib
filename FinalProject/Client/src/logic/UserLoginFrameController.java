package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

import static client.ClientGUIController.*;

public class UserLoginFrameController
{
    // FXML attributes
    @FXML
    public Button btnLogin;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblUserNameError;
    @FXML
    private Label lblPasswordError;

    /**
     * This method initializes the Subscriber login screen
     */
    public void initialize()
    {
        // Create an ActionEvent object
        ActionEvent event = new ActionEvent();

        // Check if the user is already logged in and redirect accordingly
        try
        {
            // Check if the user is a subscriber that's already logged in
            if (Subscriber.getLocalSubscriber() != null)
            {
                navigateTo(event, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Profile Page");
            }
            // Check if the user is a librarian that's already logged in
            else if (Librarian.getLocalLibrarian() != null)
            {
                navigateTo(event, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Librarian Menu");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error initializing login screen: " + e.getMessage());
        }

        // Create the tooltips for the text fields
        Tooltip usernameTooltip = new CustomTooltip("Enter your username.");
        Tooltip passwordTooltip = new CustomTooltip("Enter your password.\nDefaulted set as Aa123456");

        // Add Tooltips to the text boxes that will appear when the user hovers over them
        txtUsername.setTooltip(usernameTooltip);
        txtPassword.setTooltip(passwordTooltip);

        // Add listeners to the fields for error handling
        // Check if the username field is empty with a listener
        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty())
            {
                showErrorListenField(txtUsername, lblUserNameError, "Username cannot be empty");
            }
            else
            {
                resetErrorState(txtUsername, lblUserNameError);
            }
        });

        // Check if the password field is empty with a listener
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty())
            {
                showErrorListenField(txtPassword, lblPasswordError, "Password cannot be empty");
            }
            else
            {
                resetErrorState(txtPassword, lblPasswordError);
            }
        });
    }

    /**
     * This method handles the login button click event.
     * It sends the login details to the server for authentication.
     *
     * @param event The action event triggered by clicking the login button
     * @throws Exception If there is an issue with the navigation
     */
    @FXML
    public void clickLoginButton(ActionEvent event) throws Exception
    {
        // Collect data from the form fields
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // Check for empty fields
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty())
        {
            System.out.println("Username and password cannot be empty");
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill all fields.");
            return;
        }

        // Create an ArrayList to store the login details
        ArrayList<String> loginDetails = new ArrayList<>();
        loginDetails.add(username);
        loginDetails.add(password);

        // Create a new ClientServerMessage with the login details and ID 100
        ClientServerMessage loginMessage = new ClientServerMessage(100, loginDetails);

        // Send the login message to the server
        ClientGUIController.chat.sendToServer(loginMessage);

        // Wait for the server to respond and then check what user was logged in and redirect accordingly
        if (Subscriber.getLocalSubscriber() != null)
        {
            System.out.println("Subscriber logged in" + Subscriber.getLocalSubscriber());
            navigateTo(event, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Profile Page");
        }
        else if (Librarian.getLocalLibrarian() != null)
        {
            System.out.println("Librarian logged in" + Librarian.getLocalLibrarian());
            navigateTo(event, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Librarian Menu");
        }
        else
        {
            System.out.println("Could not log in user ");
        }
    }
}