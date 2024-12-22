package gui;

import javafx.event.ActionEvent;
import common.Subscriber;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import static client.ClientUI.navigateTo;

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
        navigateTo(event, "/gui/LoginFrame.fxml", "/gui/Subscriber.css", "Login");

    }


}
