package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static client.ClientGUIController.navigateTo;

public class SubscriberEditProfileFrameController implements Initializable
{
    // FXML elements for labels
    @FXML
    public TextField txtLastName;
    @FXML
    public TextField txtFirstName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;

    /**
     * This method handles the back button click event.
     * It navigates to the previous screen (PrototypeSubscriberProfileOptionsFrame.fxml).
     *
     * @param event The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickBackButton(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My profile");
    }

    /**
     * This method handles the update button click event.
     * It sends the updated profile details to the server for updating.
     *
     * @param event The action event triggered by clicking the update button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickUpdateButton(ActionEvent event) throws Exception
    {
        // Create a new Subscriber object with the updated details
        ArrayList<String> changedSubscriber = new ArrayList<>();
        changedSubscriber.add(String.valueOf(Subscriber.getLocalSubscriber().getID()));
        changedSubscriber.add(txtPhone.getText());
        changedSubscriber.add(txtEmail.getText());
        changedSubscriber.add(txtFirstName.getText());
        changedSubscriber.add(txtLastName.getText());

        System.out.println(changedSubscriber);
        // Create a ClientServerMessage with the subscriber and ID 203
        ClientServerMessage message = new ClientServerMessage(216, changedSubscriber);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);

        // Navigate to the previous screen
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "Profile Page");
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
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file is loaded.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadProfileDetails();
    }
}