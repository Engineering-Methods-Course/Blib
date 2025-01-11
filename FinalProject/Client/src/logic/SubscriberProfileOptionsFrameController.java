package logic;

import client.ClientGUIController;
import common.BorrowedBook;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
//import com.opencsv.bean.CsvToBean;
//import com.opencsv.bean.CsvToBeanBuilder;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;

public class SubscriberProfileOptionsFrameController
{
    @FXML
    public Button logoutButton;
    @FXML
    public Button profileButton;
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
    public Button searchBookButton;
    @FXML
    public Text userIDField;
    @FXML
    public Button changePasswordButton;
    public TableView<BorrowedBook> borrowsTable;
    public TableColumn<BorrowedBook, String> bookNameColumn;
    public TableColumn<BorrowedBook, String> borrowDateColumn;
    public TableColumn<BorrowedBook, String> returnDateColumn;
    public TableColumn<BorrowedBook, Button> extendButtonColumn;
    public Button watchHistoryButton;

    public void initialize() {
        // Load all the user info into the fields
        nameField.setText("Hello: " + Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
        phoneNumberField.setText("Phone number: " + Subscriber.getLocalSubscriber().getPhoneNumber());
        emailField.setText("Email: " + Subscriber.getLocalSubscriber().getEmail());
        statusTextField.setText("Account Status: " + (Subscriber.getLocalSubscriber().getStatusIsFrozen() ? "Frozen" : "Active"));
        profileButton.setText("Hello: " + Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
        userIDField.setText("User ID: " + Subscriber.getLocalSubscriber().getID());

        // Set up the borrowed books table columns
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("bookName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("expectedReturnDate"));

        // Set up the extend button column
        extendButtonColumn.setCellFactory(new Callback<TableColumn<BorrowedBook, Button>, TableCell<BorrowedBook, Button>>() {
            @Override
            public TableCell<BorrowedBook, Button> call(TableColumn<BorrowedBook, Button> param) {
                return new TableCell<BorrowedBook, Button>() {
                    private final Button extendButton = new Button("Extend Borrow");

                    {
                        extendButton.setOnAction((ActionEvent event) -> {
                            try {
                                extendBorrowButtonClicked(event);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(extendButton);
                        }
                    }
                };
            }
        });

        // Sends a message to the server to get the user's borrowed books
        ClientServerMessage message = new ClientServerMessage(210, Subscriber.getLocalSubscriber().getID());

        // Sends the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    public void loadBorrowsTable(ArrayList<BorrowedBook> borrowedBooks)
    {
        // Adds the borrowed books to the table
        for (BorrowedBook borrowedBook : borrowedBooks)
        {
            borrowsTable.getItems().add(borrowedBook);
        }
    }

    /**
     * This method handles the logoutButton click event to navigate to the search home page
     *
     * @param event The action event triggered by clicking the logout button
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
     *
     * @param event The action event triggered by clicking the view profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void viewProfileButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My Profile");
    }

    /**
     * This method handles the editProfileButton click event to navigate to the edit subscriber details frame
     *
     * @param event The action event triggered by clicking the edit profile button
     * @throws Exception If there is an issue with the navigation
     */
    public void editProfileButtonCLicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberEditProfileFrame.fxml", "/gui/Subscriber.css", "Edit Profile");
    }

    /**
     * This method handles the extendBorrowButton click event to navigate to the borrow extension frame
     *
     * @param event The action event triggered by clicking the extend borrow button
     * @throws Exception If there is an issue with the navigation
     */
    public void extendBorrowButtonClicked(ActionEvent event) throws Exception
    {
        //todo: implement the extend borrow button
    }

    /**
     * This method handles the searchBookButton click event to navigate to the search home page
     *
     * @param event The action event triggered by clicking the search book button
     * @throws Exception If there is an issue with the navigation
     */
    public void searchBookButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
    }

    /**
     * This method handles the changePasswordButton click event to navigate to the change password frame
     *
     * @param event The action event triggered by clicking the change password button
     * @throws Exception If there is an issue with the navigation
     */
    public void changePasswordButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/ChangePasswordFrame.fxml", "/gui/Subscriber.css", "Change Password");
    }

    /**
     * This method handles the watchHistoryButton click event to navigate to the borrow history frame
     *
     * @param event      The action event triggered by clicking the watch history button
     * @throws Exception If there is an issue with the navigation
     */
    public void watchHistoryButtonClicked(ActionEvent event)
    {
    }
}