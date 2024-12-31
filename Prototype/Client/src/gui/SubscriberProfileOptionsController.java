package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class SubscriberProfileOptionsController {

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

    public void initialize() {
        //todo: load all the user info into the fields
        //todo: make sure to call viewHistory method here to update the history table
    }

    public void logout() {
        //todo: log the user out and navigate to the login/home page
    }

    public void viewProfile() {
        //todo: implement (navigate back to this page)
        //todo other option: refresh the history table to make it look like it navigated
    }

    public void editProfile() {
        //todo: send to another window (edit profile)
    }

    public void extendBorrow() {
        //todo: implement
    }

    public void searchBook() {
        //todo: navigate to the search page
    }

    private void viewHistory() {
        //todo: pull info about user activity from the database
    }
}
