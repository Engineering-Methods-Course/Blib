package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import static client.ClientGUIController.navigateTo;

public class LibrarianProfileFrameController
{
    @FXML
    public Button logoutButton;
    @FXML
    public Text usernameTextField;
    @FXML
    public Button borrowButton;
    @FXML
    public Button returnBookButton;
    @FXML
    public Button searchButton;
    @FXML
    public Button viewAllSubscribersButton;
    @FXML
    public Button viewReportsButton;
    @FXML
    public Button registerMemberButton;
    @FXML
    public Button viewMessagesButton;

    public void initialize()
    {
        //todo: fetch the username

        //todo: update the username text field
    }

    /**
     * This method is called when the borrow button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void logoutButtonClicked(ActionEvent event) throws Exception
    {
        //todo: logout the user

        navigateTo(event, "SearchHomePageFrame.fxml", "Subscriber.css", "Home Page");
    }

    /**
     * This method is called when the borrow button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void borrowButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "BorrowBookFrame.fxml", "Subscriber.css", "Borrow Book");
    }

    /**
     * This method is called when the return button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void returnBookButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "ReturnBookFrame.fxml", "Subscriber.css", "Return Book");
    }

    /**
     * This method is called when the search button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void searchButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "SearchBookFrame.fxml", "Subscriber.css", "Home Page");
    }

    /**
     * This method is called when the view all subscribers button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void viewAllSubscribersButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "ViewAllSubscribersFrame.fxml", "Subscriber.css", "All Subscribers");
    }

    /**
     * This method is called when the view reports button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void viewReportsButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "ViewReportsFrame.fxml", "Subscriber.css", "Reports");
    }

    /**
     * This method is called when the register member button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void registerMemberButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "RegisterMemberFrame.fxml", "Subscriber.css", "Register Member");
    }

    /**
     * This method is called when the view messages button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void viewMessagesButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "ViewMessagesFrame.fxml", "Subscriber.css", "Messages");
    }
}
