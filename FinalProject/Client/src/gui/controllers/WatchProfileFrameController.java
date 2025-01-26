package gui.controllers;

import common.*;
import main.ClientGUIController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static main.ClientGUIController.loadFrameIntoPane;
import static main.ClientGUIController.showAlert;

public class WatchProfileFrameController
{
    // The FXML elements
    @FXML
    public VBox viewSubscriberProfileLibrarian;
    @FXML
    public VBox backButton;
    @FXML
    public RadioButton rdBorrowedBooks;
    @FXML
    public RadioButton rdHistory;
    @FXML
    public RadioButton rdReserves;


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

    @FXML
    public TableView<SubscriberHistory> tblHistory;
    @FXML
    public TableColumn<SubscriberHistory, String> dateColumn;
    @FXML
    public TableColumn<SubscriberHistory, String> actionColumn;
    @FXML
    public TableColumn<SubscriberHistory, String> descriptionColumn;

    @FXML
    public TableView<ActiveReserves> tblReserves;
    @FXML
    public TableColumn<ActiveReserves,String> clmnSerialNumber;
    @FXML
    public TableColumn<ActiveReserves,String> clmnBookName;
    @FXML
    public TableColumn<ActiveReserves,String> clmnReserveDate;


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
        ToggleGroup toggleGroup = new ToggleGroup();
        rdBorrowedBooks.setToggleGroup(toggleGroup);
        rdHistory.setToggleGroup(toggleGroup);
        rdReserves.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == rdBorrowedBooks) {
                requestBorrowedBooks(Subscriber.getWatchProfileSubscriber().getID());
            } else if (newValue == rdHistory) {
                requestHistory(Subscriber.getWatchProfileSubscriber().getID());
            } else if (newValue == rdReserves) {
                requestReserves(Subscriber.getWatchProfileSubscriber().getID());
            }
        });
        // Ensure a default state
        rdBorrowedBooks.setSelected(true);
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
     * Request the history for the user
     *
     * @param userID The user ID to request the history for
     */
    private void requestHistory(int userID)
    {
        // Create a message with code 210 and userID
        ClientServerMessage message = new ClientServerMessage(214, userID);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * Request the reserves for the user
     *
     * @param userID The user ID to request the reserves for
     */
    private void requestReserves(int userID)
    {
        // Create a message with code 210 and userID
        ClientServerMessage message = new ClientServerMessage(320, userID);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(message);

    }

    /**
     * Load the borrowed books into the table.
     *
     * @param borrowedBooks The list of borrowed books.
     */
    public void loadBorrowsTable(ArrayList<BorrowedBook> borrowedBooks) {
        //set borrow table to visible and the rest
        borrowsTable.setVisible(true);
        borrowsTable.setManaged(true);
        tblHistory.setVisible(false);
        tblHistory.setManaged(false);
        tblReserves.setVisible(false);
        tblReserves.setManaged(false);


        // Set up the borrowed books table columns
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedReturnDate"));

        // Set up the watch profile column with a button
        extendButtonColumn.setCellFactory(param -> new TableCell<BorrowedBook, Button>() {
            private final Button ExtendBorrow = new Button("Handle Book");

            {
                ExtendBorrow.setOnAction(event -> {
                    BorrowedBook selectedBook = getTableView().getItems().get(getIndex());
                    try {
                        HandleBorrowedBookButtonClicked(selectedBook);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(ExtendBorrow);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        // Adds the borrowed books to the table
        for (BorrowedBook borrowedBook : borrowedBooks) {
            borrowedBook.setBorrowDate(addDay(borrowedBook.getBorrowDate()));
            borrowedBook.setExpectedReturnDate(addDay(borrowedBook.getExpectedReturnDate()));
            borrowsTable.getItems().add(borrowedBook);
        }

        // Customize the return date column
        returnDateColumn.setCellFactory(column -> new TableCell<BorrowedBook, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyy-M-dd][yyyy-M-d][yyyy-MM-d]");
                    LocalDate returnDate = LocalDate.parse(item, formatter);
                    setText(item);
                    if (returnDate.isBefore(LocalDate.now())) {
                        setStyle("-fx-text-fill: #e51b1b;");
                    } else {
                        setStyle("");
                    }
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        });
    }

    /**
     * Sets the history of the subscriber in the table.
     *
     * @param history The history of the subscriber.
     */
    public void loadHistoryTable(List<ArrayList<String>> history)
    {
        //set borrow table to visible and the rest
        borrowsTable.setVisible(false);
        borrowsTable.setManaged(false);
        tblHistory.setVisible(true);
        tblHistory.setManaged(true);
        tblReserves.setVisible(false);
        tblReserves.setManaged(false);

        // set up the table to accept SubscriberHistory objects
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Create a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");



        // create a list of SubscriberHistory objects
        List<SubscriberHistory> subscriberHistoryList = new ArrayList<>();
        for (ArrayList<String> historyItem : history)
        {
            LocalDateTime localDateTime = LocalDateTime.parse(historyItem.get(0), formatter);
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            subscriberHistoryList.add(new SubscriberHistory(date, historyItem.get(1), historyItem.get(2)));
        }

        // set the items in the table
        tblHistory.getItems().setAll(subscriberHistoryList);
    }

    /**
     * Load the reserves into the table
     *
     * @param reservedBooks The list of reserved books
     */
    public void loadReservesTable(ArrayList<ActiveReserves> reservedBooks) {

        //set borrow table to visible and the rest
        borrowsTable.setVisible(false);
        borrowsTable.setManaged(false);
        tblHistory.setVisible(false);
        tblHistory.setManaged(false);
        tblReserves.setVisible(true);
        tblReserves.setManaged(true);

        // Set up the reserve table columns
        clmnSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        clmnBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        clmnReserveDate.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));
        // Adds the reserved books to the table
        tblReserves.getItems().setAll(reservedBooks);

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
            loadFrameIntoPane((AnchorPane) viewSubscriberProfileLibrarian.getParent(), "/gui/fxml/HandleBorrowedBookFrame.fxml");
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