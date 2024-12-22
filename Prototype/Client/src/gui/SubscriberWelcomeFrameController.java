package gui;

import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static client.ClientGUIController.navigateTo;

public class SubscriberWelcomeFrameController implements Initializable {

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

    /**
     * This method returns the local subscriber object
     *
     * @return The local subscriber object
     */
    public static void setLocalSubscriber(Subscriber subscriberToSet) {
        localSubscriber = subscriberToSet;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localSubscriber = SubscriberLoginController.getLocalSubscriber();
        loadSubscriberName(localSubscriber);
    }

    /**
     * Loads the subscriber's name into the welcome text field.
     *
     * @param subscriberFromLogin The Subscriber object containing user details.
     */
    public void loadSubscriberName(Subscriber subscriberFromLogin) {
        localSubscriber = subscriberFromLogin;
        if (lblWelcomeUserName != null && localSubscriber != null) {
            lblWelcomeUserName.setText(localSubscriber.getFirstName() + " " + localSubscriber.getLastName());
            System.out.println("Welcome text set to: " + localSubscriber.getFirstName() + " " + localSubscriber.getLastName());
        } else {
            System.out.println("lblWelcomeUserName is null or subscriber is null.");
        }
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
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "Profile Options");
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
        SubscriberLoginController.setLocalSubscriber(null);
        navigateTo(event, "/gui/SubscriberLoginFrame.fxml", "/gui/Subscriber.css", "Login");

    }
    public void willbeImplemented(ActionEvent event) throws Exception {
        System.out.println("Will be implemented in the future");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Will be implemented in the future");
        alert.setTitle("Watch History");
        alert.showAndWait();
    }


}
