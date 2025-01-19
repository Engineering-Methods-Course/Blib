package logic;

import client.ClientGUIController;
import common.BorrowedBook;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
        nameField.setText("Hello: " + Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
        phoneNumberField.setText("Phone number: " + Subscriber.getLocalSubscriber().getPhoneNumber());
        emailField.setText("Email: " + Subscriber.getLocalSubscriber().getEmail());
        statusTextField.setText("Account Status: " + (Subscriber.getLocalSubscriber().getStatusIsFrozen() ? "Frozen" : "Active"));
        userIDField.setText("User ID: " + Subscriber.getLocalSubscriber().getID());

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

            // Adds the borrowed book to the table
            borrowsTable.getItems().add(borrowedBook);
        }
    }

    /**
     * This method handles the extendBorrowButton click event to send extension request
     */
    public void extendBorrowButtonClicked()
    {
        // Get the selected borrowed book from the table
        BorrowedBook selectedBook = borrowsTable.getSelectionModel().getSelectedItem();

        // Check if a book is selected
        if (selectedBook == null)
        {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a book to extend.");
            return;
        }

        // Extract subscriberID and copyID from the selected book
        ClientServerMessage message = getClientServerMessage(selectedBook);

        // Send the message to the server
        try
        {
            ClientGUIController.chat.sendToServer(message);
        }
        catch (Exception e)
        {
            showAlert(Alert.AlertType.ERROR, "Error", "Error sending extend borrow request to the server: " + e.getMessage());
        }
    }

    /**
     * creates a ClientServerMessage object with code 212 for extend borrow request
     *
     * @param selectedBook The selected borrowed book
     * @return The ClientServerMessage object
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
            if (msg == null || msg.isEmpty())
            {
                showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
                return;
            }
            // Check if the first element in the message is "True" or "False"
            String status = msg.get(0); // Extract status (True or False)

            if ("true".equals(status))
            {
                // Extract explanation if available
                String explanation = msg.size() > 1 ? msg.get(1) : "No additional information provided.";
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book return date was successfully extended! " + explanation);
            }
            else if ("false".equals(status))
            {
                // Extract explanation if available
                String explanation = msg.size() > 1 ? msg.get(1) : "Unknown error occurred.";
                showAlert(Alert.AlertType.ERROR, "Extension Failed", "Reason: " + explanation);
            }
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
        String[] dateParts = date.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        day++;
        if (day > 31)
        {
            day = 1;
            month++;
            if (month > 12)
            {
                month = 1;
                year++;
            }
        }
        return year + "-" + month + "-" + day;
    }
}