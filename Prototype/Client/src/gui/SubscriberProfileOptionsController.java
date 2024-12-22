package gui;

import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.net.URL;
import java.util.ResourceBundle;

import static client.ClientGUIController.navigateTo;

//import static gui.SubscriberWelcomeFrameController.navigateTo;

public class SubscriberProfileOptionsController implements Initializable {
    // Subscriber object to store the profile details
    private static Subscriber localSubscriber;
    // FXML elements for buttons
    @FXML
    private Button btnBack = null; // Button to go back to the main profile screen
    @FXML
    private Button btnEditProfile = null; // Button to navigate to edit profile screen
    @FXML
    private Button btnWatchHistory = null; // Button to view watch history

    /**
     * This method initializes the Subscriber profile options screen
     */
    public static Subscriber getLocalSubscriber() {
        return localSubscriber;
    }

    /**
     * This method handles the Edit Profile button click event.
     * It navigates to the SubscriberEditProfileFrame.fxml to allow editing of the profile.
     *
     * @param event The action event triggered by clicking the Edit Profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickEditProfileButton(ActionEvent event) throws Exception {
        // Navigate to the desired destination using the navigateTo function
        navigateTo(event, "/gui/SubscriberEditProfileFrame.fxml", "/gui/Subscriber.css", "Edit Profile");
    }

    /**
     * This method handles the Back button click event.
     * It navigates back to the main Subscriber profile screen (SubscriberWelcomeFrame.fxml).
     *
     * @param event The action event triggered by clicking the Back button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickBackButton(ActionEvent event) throws Exception {
        // Navigate to the desired destination using the navigateTo function
        navigateTo(event, "/gui/SubscriberWelcomeFrame.fxml", "/gui/Subscriber.css", "Profile");
    }

    /**
     * This method handles the Watch History button click event.
     * It navigates to the SubscriberWatchHistoryFrame.fxml to view the watch history.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     * @throws Exception If there is an issue with the navigation
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localSubscriber = SubscriberLoginController.getLocalSubscriber();

    }

    /**
     * This method handles the Watch History button click event.
     * It navigates to the SubscriberWatchHistoryFrame.fxml to view the watch history.
     *
     * @param actionEvent The action event triggered by clicking the Watch History button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickOnWatchHistory(ActionEvent actionEvent) throws Exception {
        System.out.println("Watch History");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Will be implemented in the future");
        alert.setTitle("Watch History");
        alert.showAndWait();
    }
}
