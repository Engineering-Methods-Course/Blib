package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import common.Subscriber;

import java.net.URL;
import java.util.ResourceBundle;

import static gui.SubscriberWelcomeFrameController.navigateTo;

public class ProfileOptionsController implements Initializable {
    private static Subscriber localSubscriber;
    // FXML elements for buttons
    @FXML
    private Button btnBack = null; // Button to go back to the main profile screen
    @FXML
    private Button btnEditProfile = null; // Button to navigate to edit profile screen
    @FXML
    private Button btnWatchHistory = null; // Button to view watch history

    public static Subscriber getLocalSubscriber() {
        return localSubscriber;
    }

    /**
     * This method handles the Edit Profile button click event.
     * It navigates to the EditProfileFrame.fxml to allow editing of the profile.
     *
     * @param event The action event triggered by clicking the Edit Profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickEditProfileButton(ActionEvent event) throws Exception {
        // Navigate to the desired destination using the navigateTo function
        navigateTo(event, "/gui/EditProfileFrame.fxml", "/gui/Subscriber.css", "Edit Profile");
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
    //! missing documentation
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localSubscriber = LoginController.getLocalSubscriber();

    }
}
