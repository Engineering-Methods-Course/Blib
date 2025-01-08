package gui;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
//import com.opencsv.bean.CsvToBean;
//import com.opencsv.bean.CsvToBeanBuilder;

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
    public Text nameField;
    @FXML
    public Text phoneNumberField;
    @FXML
    public Text statusTextField;
    @FXML
    public Text emailField;
    @FXML
    public Button editProfileButton;
    @FXML
    public Button extendBorrowButton;
    @FXML
    public Button searchBookButton;
    @FXML
    public Text userIDField;
    @FXML
    public TableColumn actionDateColumn;
    @FXML
    public TableColumn actionTypeColumn;
    @FXML
    public TableColumn detailsColumn;
    public Button changePasswordButton;

    public void initialize()
    {
        //loads all the user info into the fields
        nameField.setText("Hello: " + Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
        phoneNumberField.setText("Phone number: " + Subscriber.getLocalSubscriber().getPhoneNumber());
        emailField.setText("Email: " + Subscriber.getLocalSubscriber().getEmail());
        statusTextField.setText("Account Status: " + (Subscriber.getLocalSubscriber().getStatusIsFrozen() ? "Frozen" : "Active"));
        profileButton.setText("Hello: " + Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
        userIDField.setText("User ID: " + Subscriber.getLocalSubscriber().getID());

        //calls the viewHistory method to update the history table
        //viewHistory();
    }

    /**
     * This method handles the logoutButton click event to navigate to the search home page
     * @param event      The action event triggered by clicking the logout button
     * @throws Exception If there is an issue with the navigation
     */
    public void logoutButtonClicked(ActionEvent event) throws Exception
    {
        //sets the local subscriber to null
        Subscriber.setLocalSubscriber(null);
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
    }

    /**
     * This method handles the editProfileButton click event to navigate to the edit subscriber details frame
     * @param event      The action event triggered by clicking the edit profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void editProfileButtonCLicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberEditProfileFrame.fxml", "/gui/Subscriber.css", "Edit Profile");
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

    /**
     * This method handles the viewHistoryButton click event to view the user's activity history
     */
    //todo: implement viewHistory method after the server infrastructure is in place
    public void viewHistory()
    {
        //pulls info about user activity from the database
        ClientServerMessage message = new ClientServerMessage(214, Subscriber.getLocalSubscriber().getID());
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * This method handles the changePasswordButton click event to navigate to the change password frame
     * @param event      The action event triggered by clicking the change password button
     * @throws Exception If there is an issue with the navigation
     */
    public void changePasswordButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/ChangePasswordFrame.fxml", "/gui/Subscriber.css", "Change Password");
    }
}
