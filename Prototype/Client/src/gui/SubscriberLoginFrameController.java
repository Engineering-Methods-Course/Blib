package gui;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static client.ClientGUIController.navigateTo;


public class SubscriberLoginFrameController implements Initializable
{

    private static final Subscriber localSubscriber = null;

    @FXML
    public Button backButton;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;

    /**
     * This method initializes the Subscriber login screen
     * not used
     *
     * @param location  The primary stage to set the scene
     * @param resources The primary stage to set the scene
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //todo: implement this method
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
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Username and password cannot be empty");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password cannot be empty");
            alert.showAndWait();
            return;
        }
        ArrayList<String> loginDetails = new ArrayList<>();
        loginDetails.add(username);
        loginDetails.add(password);

        // Create a new ClientServerMessage with the login details and ID 201
        ClientServerMessage loginMessage = new ClientServerMessage(201, loginDetails);

        try {
            ClientGUIController.chat.accept(loginMessage);
        }
        catch (Exception e) {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }

        if (localSubscriber != null) {
            navigateTo(event, "/gui/PrototypeSubscriberWelcomeFrame.fxml", "/gui/Subscriber.css", "Subscriber Frame");
        }
        else {
            System.out.println("Could not log in subscriber ");
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

