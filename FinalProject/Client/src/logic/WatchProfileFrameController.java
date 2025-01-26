package logic;

import main.ClientGUIController;
import common.BorrowedBook;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static main.ClientGUIController.loadFrameIntoPane;
import static main.ClientGUIController.showAlert;

public class WatchProfileFrameController
{
    // The FXML elements
    @FXML
    public VBox viewSubscriberProfileLibrarian;
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
    public TableView<BorrowedBook> borrowsTable;
    @FXML
    public TableColumn<BorrowedBook, String> bookNameColumn;
    @FXML
    public TableColumn<BorrowedBook, String> borrowDateColumn;
    @FXML
    public TableColumn<BorrowedBook, String> returnDateColumn;
    @FXML
    public TableColumn<BorrowedBook, Button> extendButtonColumn;

    private static String previousFrame;


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

        // Set up the watch profile column with a button
        extendButtonColumn.setCellFactory(param -> new TableCell<BorrowedBook, Button>()
        {
            // The button to watch the profile
            private final Button ExtendBorrow = new Button("Handle Book");

            {
                // Set the button's action
                ExtendBorrow.setOnAction(event -> {
                    BorrowedBook selectedBook = getTableView().getItems().get(getIndex());
                    // Handle the extent borrow action here
                    try
                    {
                        HandleBorrowedBookButtonClicked(selectedBook);
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            // Update the cell item
            protected void updateItem(Button item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                {
                    setGraphic(null);
                }
                else
                {
                    setGraphic(ExtendBorrow);
                    setAlignment(Pos.CENTER); // Center the button
                }
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
        txtEmail.setText(subscriber.getEmail());
        txtPhoneNumber.setText(subscriber.getPhoneNumber());
        txtUserID.setText(String.valueOf(subscriber.getID()));
        txtStatus.setText((subscriber.getStatusIsFrozen()));
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

        returnDateColumn.setCellFactory(column -> new TableCell<BorrowedBook, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty && item != null) {
                    // Parse the expected return date
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyy-M-dd][yyyy-M-d]");
                    LocalDate returnDate = LocalDate.parse(item, formatter);

                    // Set the text for the cell
                    setText(item);

                    // Apply the style if the date is before today
                    if (returnDate.isBefore(LocalDate.now())) {
                        setStyle("-fx-text-fill: #e51b1b;");
                    } else {
                        setStyle(""); // Reset style for cells that don't meet the condition
                    }
                } else {
                    setText(null);
                    setStyle(""); // Reset style for empty cells
                }
            }
        });
    }

    /**
     * Handles the extendBorrowButton click event to extend the book borrowing period
     *
     * @param selectedBook The selected book to extend the borrow period for
     * @throws IOException If an error occurs during navigation
     */
    public void HandleBorrowedBookButtonClicked(BorrowedBook selectedBook) throws IOException
    {
        if (selectedBook != null)
        {
            // Save the selected book using the set method or pass it directly
            HandleBorrowedBookFrameController.setBorrowedBook(selectedBook); // Set the selected book in the Subscriber object, or use a different method

            // Navigate to the HandleBorrowedBookFrameController, passing the selected book
            HandleBorrowedBookFrameController.setPreviousFrame(previousFrame);
            loadFrameIntoPane((AnchorPane) viewSubscriberProfileLibrarian.getParent(), "/gui/HandleBorrowedBookFrame.fxml");
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

    /**
     * Handles the back button click event to navigate back to the previous frame
     */
    public void goBack()
    {
        try
        {
            loadFrameIntoPane((AnchorPane) viewSubscriberProfileLibrarian.getParent(), previousFrame);
        }
        catch (Exception e)
        {
            System.out.println("Error loading SearchSubscriberFrame.fxml");
        }
    }

    /**
     * Sets the previous frame
     *
     * @param frame The previous frame
     */
    public static void setPreviousFrame(String frame)
    {
        previousFrame = frame;
    }
}