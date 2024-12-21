package gui;

import client.ClientUI;
import common.ClientServerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import common.Subscriber;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static client.ClientUI.navigateTo;


//import static gui.SubscriberWelcomeFrameController.navigateTo;

public class LoginController implements Initializable {
    private static Subscriber localSubscriber = null;

    @FXML
    private TextField txtUsername;  // Reference to the username field

    @FXML
    private PasswordField txtPassword;  // Reference to the password field

    @FXML
    private Button btnLogin;  // Reference to the login button
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }

    public LoginController() {

    }

    public static Subscriber getLocalSubscriber() {
        return localSubscriber;
    }

    public static void setLocalSubscriber(Subscriber subscriberToSet) {
        localSubscriber = subscriberToSet;
    }

    public void start(Stage primaryStage) throws Exception {
        // Use FXMLLoader to load the FXML
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/LoginFrame.fxml")));
        Parent root = loader.load();

        // Get the controller instance from the FXMLLoader
        LoginController loginController = loader.getController();
        // Set up the scene and stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/Subscriber.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Subscriber Frame");
        primaryStage.show();

    }

    @FXML
    public void clickLoginButton(ActionEvent event) throws Exception {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Username and password cannot be empty");
            return;
        }
        ArrayList<String> loginDetails = new ArrayList<>();
        loginDetails.add(username);
        loginDetails.add(password);

        ClientServerMessage loginMessage = new ClientServerMessage(201, loginDetails);

        try {
            ClientUI.chat.accept(loginMessage);
        } catch (Exception e) {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }

        if (localSubscriber != null) {
            navigateTo(event, "/gui/SubscriberWelcomeFrame.fxml", "/gui/Subscriber.css", "Subscriber Frame");
        } else {
            System.out.println("Something went Wrong try again please: ");
        }

    }

}


