package gui;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static client.ClientGUIController.navigateTo;


//import static gui.SubscriberWelcomeFrameController.navigateTo;

public class SubscriberLoginController implements Initializable {

    private static Subscriber localSubscriber = null;

    @FXML
    private TextField txtUsername;  // Reference to the username field

    @FXML
    private PasswordField txtPassword;  // Reference to the password field

    @FXML
    private Button btnLogin;  // Reference to the login button


    public SubscriberLoginController() {

    }

    /**
     * This method returns the local subscriber object
     *
     * @return The local subscriber object
     */
    public static Subscriber getLocalSubscriber() {
        return localSubscriber;
    }

    /**
     * This method sets the local subscriber object
     *
     * @param subscriberToSet The subscriber object to set
     */
    public static void setLocalSubscriber(Subscriber subscriberToSet) {
        localSubscriber = subscriberToSet;
    }

    /**
     * This method initializes the Subscriber login screen
     * not used
     *
     * @param location The primary stage to set the scene
     * @param resources The primary stage to set the scene
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * This method initializes the Subscriber login screen
     *
     * @param primaryStage The primary stage to set the scene
     */
    public void start(Stage primaryStage) throws Exception {
        // Use FXMLLoader to load the FXML
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/SubscriberLoginFrame.fxml")));
        Parent root = loader.load();

        // Get the controller instance from the FXMLLoader
        SubscriberLoginController subscriberLoginController = loader.getController();
        // Set up the scene and stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/Subscriber.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Subscriber Frame");
        primaryStage.show();

    }

    /**
     * This method handles the login button click event.
     * It sends the login details to the server for authentication.
     *
     * @param event The action event triggered by clicking the login button
     * @throws Exception If there is an issue with the navigation
     */
    @FXML
    public void clickLoginButton(ActionEvent event) throws Exception {
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

        ClientServerMessage loginMessage = new ClientServerMessage(201, loginDetails);

        try {
            ClientGUIController.chat.accept(loginMessage);
        } catch (Exception e) {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }

        if (localSubscriber != null) {
            navigateTo(event, "/gui/SubscriberWelcomeFrame.fxml", "/gui/Subscriber.css", "Subscriber Frame");
        } else {
            System.out.println("Something went Wrong try again please: ");
        }

    }

    /**
     * This method handles the view all button click event.
     * It sends a request to the server to retrieve all subscribers.
     *
     * @param actionEvent The action event triggered by clicking the view all button
     */
    public void clickViewAll(ActionEvent actionEvent) {
        ClientServerMessage askForSubscribers = new ClientServerMessage(103, null);
        try {
            ClientGUIController.chat.accept(askForSubscribers);
        } catch (Exception e) {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }

        try {
            // Use the navigateTo method from ClientGUIController to switch to the ViewAll screen
            ClientGUIController.navigateTo(actionEvent, "/gui/PrototypeViewAllFrame.fxml", "/gui/Subscriber.css", "View All Subscribers");
        } catch (Exception e) {
            System.out.println("Error navigating to ViewAll: " + e.getMessage());
            e.printStackTrace();
        }

    }
}


