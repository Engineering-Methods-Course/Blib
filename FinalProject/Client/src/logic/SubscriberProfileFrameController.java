package logic;

import client.ClientGUIController;
import common.BorrowedBook;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static client.ClientGUIController.showAlert;

public class SubscriberProfileFrameController
{
    // FXML attributes
    @FXML
    public Text nameField;
    @FXML
    public Text phoneNumberField;
    @FXML
    public Text statusTextField;
    @FXML
    public Text emailField;
    @FXML
    public Text userIDField;
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
     * This method is called when the frame is initialized
     */
    public void initialize()
    {
        // Load all the user info into the fields
        nameField.setText( Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
        phoneNumberField.setText( Subscriber.getLocalSubscriber().getPhoneNumber());
        emailField.setText( Subscriber.getLocalSubscriber().getEmail());
        statusTextField.setText( (Subscriber.getLocalSubscriber().getStatusIsFrozen()));
        userIDField.setText(String.valueOf(Subscriber.getLocalSubscriber().getID()));

        // Set up the borrowed books table columns
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedReturnDate"));

        // Set up the watch profile column with a button
        extendButtonColumn.setCellFactory(param -> new TableCell<BorrowedBook, Button>() {
            // The button to watch the profile
            private final Button ExtendBorrow = new Button("Extend Borrow");
            {
                // Set the button's action
                ExtendBorrow.setOnAction(event -> {
                    BorrowedBook selectedBook = getTableView().getItems().get(getIndex());
                    // Handle the extent borrow action here
                    extendBorrowButtonClicked(selectedBook);
                });
            }

            @Override
            // Update the cell item
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(ExtendBorrow);
                    setAlignment(Pos.CENTER); // Center the button
                }
            }
        });

        // Sends a message to the server to get the user's borrowed books
        ClientServerMessage message = new ClientServerMessage(210, Subscriber.getLocalSubscriber().getID());

        // Sends the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * This method loads the borrowed books into the table
     *
     * @param borrowedBooks The list of borrowed books
     */
    public void loadBorrowsTable(ArrayList<BorrowedBook> borrowedBooks)
    {
        // Adds the borrowed books to the table
        for (BorrowedBook borrowedBook : borrowedBooks)
        {
            // Adds a day to the return date and borrow date
            borrowedBook.setExpectedReturnDate(addDay(borrowedBook.getExpectedReturnDate()));
            borrowedBook.setBorrowDate(addDay(borrowedBook.getBorrowDate()));


            /// //////////////////////////  not working    ////////////////////////////////
            // Checks if the return date is today or has passed and if so sets the row to red
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyy-M-dd][yyyy-M-d]");
            LocalDate returnDate = LocalDate.parse(borrowedBook.getExpectedReturnDate(), formatter);
            if (returnDate.isBefore(LocalDate.now()) || returnDate.isEqual(LocalDate.now()))
            {
                // Set the row to red
                borrowsTable.setRowFactory(tv -> new TableRow<BorrowedBook>()
                {
                    @Override
                    protected void updateItem(BorrowedBook item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                        {
                            setStyle("");
                        }
                        else if (returnDate.isBefore(LocalDate.now()))
                        {
                            setStyle("-fx-text-fill: red;");
                        }
                        else
                        {
                            setStyle("");
                        }
                    }
                });
            }
            /// //////////////////////////  not working    ////////////////////////////////

            // Adds the borrowed book to the table
            borrowsTable.getItems().add(borrowedBook);
        }
    }

    /**
     * This method handles the extendBorrowButton click event to send extension request
     */
    public void extendBorrowButtonClicked(BorrowedBook selectedBook)
    {
        // Check if a book is selected
        if (selectedBook == null)
        {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a book to extend.");
            return;
        }

        // Extract subscriberID and copyID from the selected book
        ClientServerMessage message = getClientServerMessage(selectedBook);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);

        // reload the borrows table
        borrowsTable.getItems().clear();
        ClientServerMessage reloadMessage = new ClientServerMessage(210, Subscriber.getLocalSubscriber().getID());
        ClientGUIController.chat.sendToServer(reloadMessage);
    }

    /**
     * creates a ClientServerMessage object with code 212 for extend borrow request
     *
     * @param selectedBook The selected borrowed book
     * @return             The ClientServerMessage object
     */
    private static ClientServerMessage getClientServerMessage(BorrowedBook selectedBook)
    {
        String subscriberID = String.valueOf(Subscriber.getLocalSubscriber().getID());
        String copyID = String.valueOf(selectedBook.getCopyID()); // Assuming BorrowedBook has a getCopyID() method

        // Create the message content as an ArrayList<String>
        ArrayList<String> extendRequestData = new ArrayList<>();
        extendRequestData.add(subscriberID);
        extendRequestData.add(copyID);

        // Create the ClientServerMessage object with code 212 for extend borrow request
        return new ClientServerMessage(212, extendRequestData);
    }

    /**
     * This method handles the extendBorrowButton click event to extend the book borrowing period
     *
     * @param msg The message received from the server
     */
    public static void showExtendMessageResponse(ArrayList<String> msg)
    {
        Platform.runLater(() -> {
            // Check if the response is null or empty, and display an error
            if (msg == null || msg.isEmpty())
            {
                showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
                return;
            }
            // Check if the first element in the message is "True" or "False"
            String status = msg.get(0); // Extract status (True or False)

            // Show the appropriate message based on the status
            if ("true".equals(status))
            {
                // Extract explanation if available
                String explanation = msg.size() > 1 ? msg.get(1) : "No additional information provided.";
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book return date was successfully extended! " + explanation);
            }
            // Show an error message if the extension failed
            else if ("false".equals(status))
            {
                // Extract explanation if available
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Extension Failed", "Reason: " + explanation);
            }
            // Show an error message if the response format is unexpected
            else
            {
                showAlert(Alert.AlertType.ERROR, "Error", "Unexpected response format from server.");
            }
        });
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