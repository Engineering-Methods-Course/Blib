package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SubscriberProfileOptionsController implements Initializable {

    // FXML elements for buttons
    @FXML
    private Button btnBack = null; // Button to go back to the main profile screen
    @FXML
    private Button btnEditProfile = null; // Button to navigate to edit profile screen
    @FXML
    private Button btnWatchHistory = null; // Button to view watch history

    /**
     * This method handles the Edit Profile button click event.
     * It navigates to the SubscriberEditProfileFrame.fxml to allow editing of the profile.
     * @param event The action event triggered by clicking the Edit Profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickEditProfileButton(ActionEvent event) throws Exception {
        // Navigate to the desired destination using the navigateTo function
        SubscriberFrameController.navigateTo(event, "/gui/SubscriberEditProfileFrame.fxml", "/gui/Subscriber.css", "Edit Profile");
    }

    /**
     * This method handles the Back button click event.
     * It navigates back to the main Subscriber profile screen (SubscriberFrame.fxml).
     * @param event The action event triggered by clicking the Back button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickBackButton(ActionEvent event) throws Exception {
        // Navigate to the desired destination using the navigateTo function
        SubscriberFrameController.navigateTo(event, "/gui/SubscriberFrame.fxml", "/gui/Subscriber.css", "Profile");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
