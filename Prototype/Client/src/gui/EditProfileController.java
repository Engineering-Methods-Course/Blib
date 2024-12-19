package gui;

import common.Subscriber;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    // Subscriber object to store the profile details
    private Subscriber subscriber;

    // FXML elements for labels and text fields
    @FXML
    private Label lblID; // Label for displaying ID
    @FXML
    private Label lblName; // Label for displaying Name
    @FXML
    private Label lblLastName; // Label for displaying Last Name
    @FXML
    private Label lblHistory; // Label for displaying History
    @FXML
    private Label lblPhone; // Label for displaying Phone number
    @FXML
    private Label lblEmail; // Label for displaying Email address

    @FXML
    private TextField txtID; // TextField for ID input
    @FXML
    private TextField txtName; // TextField for Name input
    @FXML
    private TextField txtLastName; // TextField for Last Name input
    @FXML
    private TextField txtHistory; // TextField for History input
    @FXML
    private TextField txtPhone; // TextField for Phone input
    @FXML
    private TextField txtEmail; // TextField for Email input

    // FXML elements for buttons
    @FXML
    private Button btnBack = null; // Button to go back to previous screen
    @FXML
    private Button btnUpdate = null; // Button to update the profile

    /**
     * This method handles the back button click event.
     * It navigates to the previous screen (ProfileOptionsFrame.fxml).
     *
     * @param event The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickBackButton(ActionEvent event) throws Exception {
        // Navigate to the desired destination using the navigateTo function
        SubscriberWelcomeFrameController.navigateTo(event, "/gui/ProfileOptionsFrame.fxml", "/gui/Subscriber.css", "Profile Options");
    }

    public void clickUpdateButton(ActionEvent event) throws Exception {

    }

    /**
     * This method loads the profile details into the respective text fields.
     * It sets the values from the Subscriber object into the text fields.
     *
     * @param s1 The Subscriber object containing profile data
     */
    public void loadProfileDetails(Subscriber s1) {
        // Set values in the text fields from the Subscriber object
        this.txtID.setText(String.valueOf(subscriber.getID()));
        this.txtName.setText(subscriber.getFirstName());
        this.txtLastName.setText(subscriber.getLastName());
        this.txtPhone.setText(String.valueOf(subscriber.getPhoneNumber()));
        this.txtEmail.setText(subscriber.getEmail());
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subscriber = ProfileOptionsController.getS2();
        loadProfileDetails(subscriber);
    }
}