package gui;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static client.ClientGUIController.navigateTo;

public class SubscriberEditProfileController implements Initializable {

    private static Subscriber localSubscriber;
    // FXML elements for labels
    @FXML
    private Label lblID;
    @FXML
    private Label lblName;
    @FXML
    private Label lblLastName;
    @FXML
    private Label lblHistory;
    @FXML
    private Label lblPhone;
    @FXML
    private Label lblEmail;

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtHistory;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnBack = null;
    @FXML
    private Button btnUpdate = null;
    /**
     * This method initializes the Subscriber edit profile screen
     */
    public static void setLocalSubscriber(Subscriber subscriberFromServer) {
        localSubscriber = subscriberFromServer;
    }

    /**
     * This method handles the back button click event.
     * It navigates to the previous screen (SubscriberProfileOptionsFrame.fxml).
     *
     * @param event The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickBackButton(ActionEvent event) throws Exception {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "Profile Options");
    }

    /**
     * This method handles the update button click event.
     * It sends the updated profile details to the server for updating.
     *
     * @param event The action event triggered by clicking the update button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickUpdateButton(ActionEvent event) throws Exception {
        // Retrieve the text from the TextField components
        int id = Integer.parseInt(txtID.getText());
        String name = txtName.getText();
        String lastName = txtLastName.getText();
        String phoneNumber = txtPhone.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        boolean status = false;
        // Create a new Subscriber object with the updated details
        Subscriber changedSubscriber = new Subscriber(id, name, lastName, phoneNumber, email, password, status);

        // Create a ClientServerMessage with the subscriber and ID 204
        ClientServerMessage editedProfileMessage = new ClientServerMessage(203, changedSubscriber);

        try {
            ClientGUIController.chat.accept(editedProfileMessage);
        } catch (Exception e) {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }
    }

    /**
     * This method loads the profile details into the respective text fields.
     * It sets the values from the Subscriber object into the text fields.
     *
     * @param s1 The Subscriber object containing profile data
     */
    public void loadProfileDetails(Subscriber s1) {
        // Set values in the text fields from the Subscriber object
        this.txtID.setText(String.valueOf(s1.getID()));
        this.txtName.setText(s1.getFirstName());
        this.txtLastName.setText(s1.getLastName());
        this.txtPhone.setText(String.valueOf(s1.getPhoneNumber()));
        this.txtEmail.setText(s1.getEmail());
        this.txtPassword.setText(s1.getPassword());
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file is loaded.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localSubscriber = SubscriberProfileOptionsController.getLocalSubscriber();
        loadProfileDetails(localSubscriber);
    }
}
