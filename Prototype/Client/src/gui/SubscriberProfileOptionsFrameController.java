package gui;

import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import static client.ClientGUIController.navigateTo;

public class SubscriberProfileOptionsFrameController
{

    // Subscriber object to store the profile details
    private static Subscriber localSubscriber;

    @FXML
    public Button logoutButton;
    @FXML
    public Button profileButton;
    @FXML
    public TableView historyTable;
    @FXML
    public Text usernameField;
    @FXML
    public Text phoneNumberField;
    @FXML
    public Text detailField4;
    @FXML
    public Text emailField;
    @FXML
    public Button editProfileButton;
    @FXML
    public Button extendBorrowButton;
    @FXML
    public Button searchBookButton;

    /**
     * This method gets the local subscriber object
     * @return The local subscriber object
     */
    public static Subscriber getLocalSubscriber()
    {
        return localSubscriber;
    }

    /**
     * This method sets the local subscriber object
     * @param subscriberFromServer The subscriber object to set
     */
    public static void setLocalSubscriber(Subscriber subscriberFromServer)
    {
        localSubscriber = subscriberFromServer;
    }

    public void initialize()
    {
        //todo: load all the user info into the fields
        //todo: make sure to call viewHistory method here to update the history table
    }

    /**
     *
     * @param event
     * @throws Exception
     */
    public void logoutButtonClicked(ActionEvent event) throws Exception
    {
        //todo: log the user out
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }

    /**
     *
     * @param event
     * @throws Exception
     */
    public void viewProfileButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My Profile");

        //todo(optional): refresh the history table to make it look like it navigated
    }

    /**
     *
     * @param event
     * @throws Exception
     */
    public void editProfileButtonCLicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/EditSubscriberDetailsFrame.fxml", "/gui/Subscriber.css", "Edit Profile");
    }

    /**
     *
     * @param event
     * @throws Exception
     */
    public void extendBorrowButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/BorrowExtensionFrame.fxml", "/gui/Subscriber.css", "Extend Borrow");
    }

    /**
     *
     * @param event
     * @throws Exception
     */
    public void searchBookButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }

    private void viewHistory()
    {
        //todo: pull info about user activity from the database
    }
}
