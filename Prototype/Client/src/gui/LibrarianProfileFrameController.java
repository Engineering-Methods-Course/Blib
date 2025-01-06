package gui;

import common.Librarian;
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
        //updates the username text field to display the librarian's name
        usernameTextField.setText("Welcome, " + Librarian.getLocalLibrarian().getFirstName() + " " + Librarian.getLocalLibrarian().getLastName());
    }

    /**
     * This method is called when the borrow button is clicked
     *
     * @param event The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void logoutButtonClicked(ActionEvent event) throws Exception
    {
        //sets the local librarian object to null to log out the librarian
        Librarian.setLocalLibrarian(null);

        //navigates to the home page
        navigateTo(event, "SearchHomePageFrame.fxml", "Subscriber.css", "Home Page");
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
        navigateTo(event, "BorrowBookFrame.fxml", "Subscriber.css", "Borrow Book");
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
        navigateTo(event, "ReturnBookFrame.fxml", "Subscriber.css", "Return Book");
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
        navigateTo(event, "SearchHomePageFrame.fxml", "Subscriber.css", "Home Page");
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
        navigateTo(event, "ViewAllSubscribersFrame.fxml", "Subscriber.css", "All Subscribers");
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
        navigateTo(event, "ViewReportsFrame.fxml", "Subscriber.css", "View Reports");
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
        navigateTo(event, "RegisterMemberFrame.fxml", "Subscriber.css", "Register Member");
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
        navigateTo(event, "ViewMessagesFrame.fxml", "Subscriber.css", "View Messages");
    }
}
