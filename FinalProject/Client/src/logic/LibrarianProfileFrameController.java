package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
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
    @FXML
    public Button btnSearchSubscriber;

    public void initialize()
    {
        //updates the username text field to display the librarian's name
        usernameTextField.setText(Librarian.getLocalLibrarian().getFirstName() + " " + Librarian.getLocalLibrarian().getLastName());
    }

    /**
     * This method is called when the borrow button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void logoutButtonClicked(ActionEvent event) throws Exception
    {
        //sets the local subscriber to null
        Librarian.setLocalLibrarian(null);

        // Sends a message to the server to log out
        ClientServerMessage message = new ClientServerMessage(102, null);

        // Sends the message to the server
        ClientGUIController.chat.sendToServer(message);

        // Navigates to the search home page
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }

    /**
     * This method is called when the borrow button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void borrowButtonClicked(ActionEvent event) throws Exception
    {
        //navigates to the borrow book frame
        navigateTo(event, "/gui/BorrowBookFrame.fxml", "/gui/Subscriber.css", "Borrow Book");
    }

    /**
     * This method is called when the return button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void returnBookButtonClicked(ActionEvent event) throws Exception
    {
        //navigates to the return book frame
        navigateTo(event, "/gui/ReturnBookFrame.fxml", "/gui/Subscriber.css", "Return Book");
    }

    /**
     * This method is called when the search button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void searchButtonClicked(ActionEvent event) throws Exception
    {
        //navigates to the search home page frame
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }

    /**
     * This method is called when the view all subscribers button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void viewAllSubscribersButtonClicked(ActionEvent event) throws Exception
    {
        //navigates to the view all subscribers frame
        navigateTo(event, "/gui/ViewAllSubscribersFrame.fxml", "/gui/Subscriber.css", "All Subscribers");
    }

    /**
     * This method is called when the view reports button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void viewReportsButtonClicked(ActionEvent event) throws Exception
    {
        //navigates to the view reports frame
        navigateTo(event, "/gui/ViewReportsFrame.fxml", "/gui/Subscriber.css", "View Reports");
    }

    /**
     * This method is called when the register member button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void registerMemberButtonClicked(ActionEvent event) throws Exception
    {
        //navigates to the register member frame
        navigateTo(event, "/gui/RegisterMemberFrame.fxml", "/gui/Subscriber.css", "Register Member");
    }

    /**
     * This method is called when the view messages button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void viewMessagesButtonClicked(ActionEvent event) throws Exception
    {
        //navigates to the view messages frame
        navigateTo(event, "/gui/ViewMessagesFrame.fxml", "/gui/Subscriber.css", "View Messages");
    }

    /**
     * This method is called when the search subscriber button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void clickSearchSubscriber(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchSubscriberFrame.fxml", "/gui/Subscriber.css", "Search Subscriber");
    }
}