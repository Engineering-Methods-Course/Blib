package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;
import static client.ClientGUIController.showAlert;

public class UserLoginFrameController
{
    @FXML
    public Button backButton;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    /**
     * This method initializes the Subscriber login screen
     */
    public void initialize()
    {
        ActionEvent event = new ActionEvent();
        try
        {
            if (Subscriber.getLocalSubscriber() != null)
            {
                navigateTo(event, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Profile Page");
            }
            else if (Librarian.getLocalLibrarian() != null)
            {
                navigateTo(event, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Librarian Menu");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error initializing login screen: " + e.getMessage());
        }
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

    /**
     * This method handles the back button click event.
     * It navigates to the previous screen (PrototypeWelcomeFrame.fxml).
     *
     * @param event The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }
}