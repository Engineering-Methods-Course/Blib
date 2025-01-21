package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static client.ClientGUIController.*;

public class SubscriberEditProfileFrameController
{
    // FXML elements for labels
    @FXML
    public TextField txtLastName;
    @FXML
    public TextField txtFirstName;
    @FXML
    public VBox editInfoFrame;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    public Label lblLastNameError;
    @FXML
    public Label lblFirstNameError;
    @FXML
    public Label lblPhoneError;
    @FXML
    public Label lblEmailError;

    /**
     * This method initializes the controller class.
     */
    public void initialize()
    {
        // Load the profile details into the text fields
        loadProfileDetails();

        // Add validation listeners to the text fields
        addValidationListeners();
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
        if (firstName.isEmpty() || lastName.isEmpty())
        {
            // Show an alert if either field is empty
            showAlert(Alert.AlertType.WARNING, "Input Error", "First name and last name cannot be empty.");
            return;
        }

        // Create a new ArrayList to store the updated subscriber details
        ArrayList<String> changedSubscriber = new ArrayList<>();
        changedSubscriber.add(String.valueOf(Subscriber.getLocalSubscriber().getID()));
        changedSubscriber.add(txtPhone.getText());
        changedSubscriber.add(txtEmail.getText());
        changedSubscriber.add(firstName);
        changedSubscriber.add(lastName);

        // Print the updated subscriber details for logging
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

    /**
     * This method adds validation listeners to the text fields.
     * It checks for empty fields and invalid formats.
     */
    private void addValidationListeners()
    {
        // Listener for First Name TextField
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the field is empty and show an error if it is
            if (newValue.trim().isEmpty())
            {
                showErrorListenField(txtFirstName, lblFirstNameError, "First name cannot be empty.");
            }
            else
            {
                resetErrorState(txtFirstName, lblFirstNameError);
            }
        });

        // Listener for Last Name TextField
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the field is empty and show an error if it is
            if (newValue.trim().isEmpty())
            {
                showErrorListenField(txtLastName, lblLastNameError, "Last name cannot be empty.");
            }
            else
            {
                resetErrorState(txtLastName, lblLastNameError);
            }
        });

        // Listener for Phone TextField
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the field is empty and show an error if it is
            if (newValue.trim().isEmpty())
            {
                showErrorListenField(txtPhone, lblPhoneError, "Phone number cannot be empty.");
            }
            // Match phone number starting with '05' and followed by 8 digits using regex
            else if (!newValue.matches("^05\\d{8}$"))
            {
                showErrorListenField(txtPhone, lblPhoneError, "Invalid phone number. It must start with '05' and be followed by 8 digits.");
            }
            else
            {
                resetErrorState(txtPhone, lblPhoneError);
            }
        });

        // Listener for Email TextField (Check for a valid email format)
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the field is empty and show an error if it is
            if (newValue.trim().isEmpty())
            {
                showErrorListenField(txtEmail, lblEmailError, "Email cannot be empty.");
            }
            // Check for a valid email format using regex
            else if (!newValue.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            {
                showErrorListenField(txtEmail, lblEmailError, "Invalid email format.");
            }
            else
            {
                resetErrorState(txtEmail, lblEmailError);
            }
        });
    }
}