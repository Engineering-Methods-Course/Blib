package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

import static client.ClientGUIController.loadFrameIntoPane;
import static client.ClientGUIController.showAlert;

public class SubscriberEditProfileFrameController
{
    // FXML elements for labels
    @FXML
    public TextField txtLastName;
    @FXML
    public TextField txtFirstName;
    public VBox editInfoFrame;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;

    /**
     * This method initializes the controller class.
     * It is automatically called after the FXML file is loaded.
     */
    public void initialize(){
        loadProfileDetails();
    }

    /**
     * This method handles the update button click event.
     * It sends the updated profile details to the server for updating.
     *
     * @throws Exception If there is an issue with the navigation
     */
    public void clickUpdateButton() throws Exception
    {
        // Validate that the first name and last name are not empty
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();

        // Check if the first name and last name are empty and if so show an alert
        if (firstName.isEmpty() || lastName.isEmpty()) {
            // Show an alert if either field is empty
            showAlert(Alert.AlertType.WARNING, "Input Error", "First name and last name cannot be empty.");
            return;
        }

        // Create a new Subscriber object with the updated details
        ArrayList<String> changedSubscriber = new ArrayList<>();
        changedSubscriber.add(String.valueOf(Subscriber.getLocalSubscriber().getID()));
        changedSubscriber.add(txtPhone.getText());
        changedSubscriber.add(txtEmail.getText());
        changedSubscriber.add(firstName);
        changedSubscriber.add(lastName);

        System.out.println(changedSubscriber);
        // Create a ClientServerMessage with the subscriber and ID 203
        ClientServerMessage message = new ClientServerMessage(216, changedSubscriber);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);

        // Navigate to the previous screen
        loadFrameIntoPane((AnchorPane) editInfoFrame.getParent(), "/gui/SubscriberProfileFrame.fxml");
    }

    /**
     * This method loads the profile details into the respective text fields.
     * It sets the values from the Subscriber object into the text fields.
     */
    public void loadProfileDetails()
    {
        // Set values in the text fields from the Subscriber object
        this.txtPhone.setText(Subscriber.getLocalSubscriber().getPhoneNumber());
        this.txtEmail.setText(Subscriber.getLocalSubscriber().getEmail());
        this.txtFirstName.setText(Subscriber.getLocalSubscriber().getFirstName());
        this.txtLastName.setText(Subscriber.getLocalSubscriber().getLastName());
    }
}