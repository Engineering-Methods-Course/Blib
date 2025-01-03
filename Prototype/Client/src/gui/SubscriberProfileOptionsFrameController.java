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



    public void initialize()
    {
        //todo: load all the user info into the fields
        //todo: make sure to call viewHistory method here to update the history table
    }

    /**
     * This method handles the logoutButton click event to navigate to the search home page
     * @param event      The action event triggered by clicking the logout button
     * @throws Exception If there is an issue with the navigation
     */
    public void logoutButtonClicked(ActionEvent event) throws Exception
    {
        //todo: log the user out
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }

    /**
     * This method handles the viewProfileButton click event to navigate to the subscriber profile options frame
     * @param event      The action event triggered by clicking the view profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void viewProfileButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My Profile");

        //todo(optional): refresh the history table to make it look like it navigated
    }

    /**
     * This method handles the editProfileButton click event to navigate to the edit subscriber details frame
     * @param event      The action event triggered by clicking the edit profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void editProfileButtonCLicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/EditSubscriberDetailsFrame.fxml", "/gui/Subscriber.css", "Edit Profile");
    }

    /**
     * This method handles the extendBorrowButton click event to navigate to the borrow extension frame
     * @param event      The action event triggered by clicking the extend borrow button
     * @throws Exception If there is an issue with the navigation
     */
    public void extendBorrowButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/BorrowExtensionFrame.fxml", "/gui/Subscriber.css", "Extend Borrow");
    }

    /**
     * This method handles the searchBookButton click event to navigate to the search home page
     * @param event      The action event triggered by clicking the search book button
     * @throws Exception If there is an issue with the navigation
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
