package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static client.ClientGUIController.navigateTo;

public class EditSubscriberDetailsFrameController
{
    @FXML
    public TextField phoneNumberBox;
    @FXML
    public TextField emailAddressBox;
    @FXML
    public Button submitButton;
    @FXML
    public Button backButton;

    /**
     * This method initializes the Edit Subscriber Details Frame
     */
    public void initialize()
    {
        //todo: implement this method
    }

    /**
     * This method handles the submitButton click event to submit the subscriber details and return to the subscriber page
     * @param event      The action event triggered by clicking the submit button
     * @throws Exception If there is an issue with the navigation
     */
    public void submitButtonClicked(ActionEvent event) throws Exception
    {
        //todo: implement the submitButton click event to submit the subscriber details and return to the subscriber page
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My Profile");
    }

    /**
     * This method handles the backButton click event to return to the subscriber page
     * @param event      The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My Profile");
    }
}