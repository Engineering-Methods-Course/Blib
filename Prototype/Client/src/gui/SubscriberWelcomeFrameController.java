package gui;

import client.ClientController;
import client.ClientUI;
import common.ClientServerMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import common.Subscriber;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static client.ClientUI.navigateTo;
import static java.lang.System.exit;

public class SubscriberWelcomeFrameController implements Initializable  {

    private static Subscriber localSubscriber;
    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblWelcomeUserName;

    @FXML
    private Button btnProfile;
    @FXML
    private Button btnBorrowExtension;
    @FXML
    private Button btnOrder;
    @FXML
    private Button btnSearchBook;
    @FXML
    private Button btnLogout;


    /*
     * Navigates to a specified FXML destination, optionally applying a CSS file and setting the title of the new stage.
     *
     * @param event           The ActionEvent triggered by the user's interaction (e.g., button click).
     * @param fxmlDestination The path to the FXML file for the destination view (e.g., "/GUI/SubscriberProfileOptions.fxml").
     * @param cssFilePath     The optional path to the CSS file to style the new view (e.g., "/GUI/Subscriber.css"). Pass null if no CSS is required.
     * @param stageTitle      The title for the new stage window (e.g., "Profile Options").
     * @throws Exception If the FXML file cannot be loaded or another error occurs during navigation.

    public static void navigateTo(ActionEvent event, String fxmlDestination, String cssFilePath, String stageTitle) throws Exception {
        // Close the current stage
        Stage currentStage;
        currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Load the destination FXML
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(SubscriberWelcomeFrameController.class.getResource(fxmlDestination)));

        // Create the new scene
        Scene scene = new Scene(root);

        // Optionally add a CSS file if provided
        if (cssFilePath != null && !cssFilePath.isEmpty()) {
            scene.getStylesheets().add(Objects.requireNonNull(SubscriberWelcomeFrameController.class.getResource(cssFilePath)).toExternalForm());
        }
        // Set up the new stage
        newStage.setTitle(stageTitle);
        newStage.setScene(scene);
        newStage.setOnCloseRequest(event1 -> {
            // Run logOutAction() in a background thread to prevent blocking
            Task<Void> logOutTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    logOutAction();  // Call your logout action in the background
                    return null;
                }
            };
            // Start the background task
            new Thread(logOutTask).start();
        });
        // Show the new stage
        newStage.show();
    }
    */


    private static void logOutAction() throws Exception {
        ClientServerMessage logOutMessage=new ClientServerMessage(999,null);
        try {
            ClientUI.chat.accept(logOutMessage);
        } catch (Exception e) {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localSubscriber = LoginController.getLocalSubscriber();
        loadSubscriberName(localSubscriber);
    }

    /**
     * Loads the subscriber's name into the welcome text field.
     *
     * @param s1 The Subscriber object containing user details.
     */
    public void loadSubscriberName(Subscriber s1) {
        this.localSubscriber = s1;
        System.out.println("loadSubscriberName() called");  // Check if this is printed

        if (lblWelcomeUserName != null && localSubscriber != null) {
            lblWelcomeUserName.setText(localSubscriber.getFirstName() + " " + localSubscriber.getLastName());
            System.out.println("Welcome text set to: " + localSubscriber.getFirstName() + " " + localSubscriber.getLastName());
        } else {
            System.out.println("lblWelcomeUserName is null or subscriber is null.");
        }
    }
    public static void setLocalSubscriber(Subscriber subscriberToSet) {
        localSubscriber = subscriberToSet;
    }

    /**
     * This method handles the Borrow Extension button click event.
     * It navigates to the BorrowExtensionFrame.fxml to allow the user to extend a book's borrowing period.
     *
     * @param event The action event triggered by clicking the Borrow Extension button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickProfileButton(ActionEvent event) throws Exception {
        System.out.println("exit Subscriber Frame");
        navigateTo(event, "/gui/ProfileOptionsFrame.fxml", "/gui/Subscriber.css", "Profile Options");
    }
    /**
     * This method handles the Borrow Extension button click event.
     * It navigates to the BorrowExtensionFrame.fxml to allow the user to extend a book's borrowing period.
     *
     * @param event The action event triggered by clicking the Borrow Extension button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickLogoutButton(ActionEvent event) throws Exception {
        System.out.println("exit Subscriber Frame");
        setLocalSubscriber(null);
        LoginController.setLocalSubscriber(null);

    }


}
