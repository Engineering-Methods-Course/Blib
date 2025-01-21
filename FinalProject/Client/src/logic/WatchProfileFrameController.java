package logic;

import client.ClientGUIController;
import common.BorrowedBook;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.ArrayList;

import static client.ClientGUIController.loadFrameIntoPane;
import static client.ClientGUIController.showAlert;

public class WatchProfileFrameController
{
    // The FXML elements
    @FXML
    public VBox extendBorrowLibrarian;
    @FXML
    private Text txtFullName;
    @FXML
    private Text txtPhoneNumber;
    @FXML
    private Text txtStatus;
    @FXML
    private Text txtEmail;
    @FXML
    private Text txtUserID;
    @FXML
    public Button btnBack;
    @FXML
    public TableView<BorrowedBook> borrowsTable;
    @FXML
    public TableColumn<BorrowedBook, String> bookNameColumn;
    @FXML
    public TableColumn<BorrowedBook, String> borrowDateColumn;
    @FXML
    public TableColumn<BorrowedBook, String> returnDateColumn;
    @FXML
    public TableColumn<BorrowedBook, Button> extendButtonColumn;

    /**
     * Initializes the WatchProfileFrameController.
     */
    public void initialize()
    {
        // Get the static subscriber object
        Subscriber subscriber = Subscriber.getWatchProfileSubscriber();
        if (subscriber == null)
        {
            System.out.println("Subscriber is null in WatchProfileFrameController.");
        }
        else
        {
            // Update the subscriber information on the UI
            updateSubscriberInfo(subscriber);
        }

        // Set up the borrowed books table columns
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedReturnDate"));

        // Set up the "extend button" column
        extendButtonColumn.setCellFactory(new Callback<TableColumn<BorrowedBook, Button>, TableCell<BorrowedBook, Button>>()
        {
            @Override
            public TableCell<BorrowedBook, Button> call(TableColumn<BorrowedBook, Button> param)
            {
                return new TableCell<BorrowedBook, Button>()
                {
                    private final Button extendButton = new Button("Extend Borrow");

                    {
                        extendButton.setOnAction((ActionEvent event) -> {
                            try
                            {
                                extendBorrowButtonClicked();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Button item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (empty)
                        {
                            setGraphic(null);
                        }
                        else
                        {
                            setGraphic(extendButton);
                        }
                    }
                };
            }
        });

        // Sends a message to the server to get the user's borrowed books
        requestBorrowedBooks(Subscriber.getWatchProfileSubscriber().getID());
    }

    /**
     * Update the subscriber information on the UI by setting the test fields
     *
     * @param subscriber The subscriber object to display
     */
    public void updateSubscriberInfo(Subscriber subscriber)
    {
        txtFullName.setText(subscriber.getFirstName() + " " + subscriber.getLastName());
        txtEmail.setText("Email: " + subscriber.getEmail());
        txtPhoneNumber.setText("Phone number: " + subscriber.getPhoneNumber());
        txtUserID.setText("User ID: " + subscriber.getID());
        txtStatus.setText("Account Status: " + (subscriber.getStatusIsFrozen() ? "Frozen" : "Active"));
    }

    /**
     * Request the borrowed books for the user
     *
     * @param userID The user ID to request the borrowed books for
     */
    private void requestBorrowedBooks(int userID)
    {
        // Create a message with code 210 and userID
        ClientServerMessage message = new ClientServerMessage(210, userID);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * Load the borrowed books into the table
     *
     * @param borrowedBooks The list of borrowed books
     */
    public void loadBorrowsTable(ArrayList<BorrowedBook> borrowedBooks)
    {
        // Adds the borrowed books to the table
        for (BorrowedBook borrowedBook : borrowedBooks)
        {
            // Add a day to the return date
            borrowedBook.setBorrowDate(addDay(borrowedBook.getBorrowDate()));
            borrowedBook.setExpectedReturnDate(addDay(borrowedBook.getExpectedReturnDate()));

            // Add the borrowed book to the table
            borrowsTable.getItems().add(borrowedBook);
        }
    }

    /**
     * Handles the extendBorrowButton click event to extend the book borrowing period
     *
     * @throws Exception If there is an issue with the navigation
     */
    public void extendBorrowButtonClicked() throws Exception
    {
        // Get the selected BorrowedBook from the clicked row
        BorrowedBook selectedBook = borrowsTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null)
        {
            // Save the selected book using the set method or pass it directly
            BorrowExtensionFrameController.setBorrowedBook(selectedBook); // Set the selected book in the Subscriber object, or use a different method

            // Navigate to the BorrowExtensionFrameController, passing the selected book
            loadFrameIntoPane((AnchorPane) extendBorrowLibrarian.getParent(), "/gui/BorrowExtensionFrame.fxml");
        }
        else
        {
            // If no book is selected, show an alert
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to extend the borrow period.");
        }
    }

    /**
     * Adds a day to the borrow and return date
     *
     * @param date The date to add a day to
     * @return The date with one day added
     */
    private String addDay(String date)
    {
        // Split the date into parts
        String[] dateParts = date.split("-");

        //parses the date parts into integers
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        // Adds a day to the date
        day++;

        // Checks if the day is greater than 31 and if so adds a month and resets the day
        if (day > 31)
        {
            day = 1;
            month++;

            // Checks if the month is greater than 12 and if so adds a year and resets the month
            if (month > 12)
            {
                month = 1;
                year++;
            }
        }

        // Returns the new date
        return year + "-" + month + "-" + day;
    }
}