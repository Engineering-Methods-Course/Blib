package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static client.ClientGUIController.loadFrameIntoPane;
import static client.ClientGUIController.showAlert;

public class ViewAllSubscribersFrameController
{
    // FXML attributes
    @FXML
    public TableColumn<Subscriber, Integer> idColumn;
    @FXML
    public TableColumn<Subscriber, String> firstNameColumn;
    @FXML
    public TableColumn<Subscriber, String> lastNameColumn;
    @FXML
    public TableColumn<Subscriber, String> phoneNumberColumn;
    @FXML
    public TableColumn<Subscriber, String> emailColumn;
    @FXML
    public TableColumn<Subscriber, String> statusColumn;
    @FXML
    public TableColumn<Subscriber, Void> watchProfileColumn;
    @FXML
    public Button filterButton;
    @FXML
    public TableView<Subscriber> subscribersTable;
    @FXML
    public TextField filterTextField;
    @FXML
    public AnchorPane allSubscribersFrame;
    @FXML
    public Button btnRefresh;

    // other class attributes
    private List<Subscriber> allSubscribers;

    /**
     * Initializes the ViewAllSubscribersFrameController.
     */
    public void initialize()
    {
        // Create tooltips for the filter field and button
        Tooltip filterTextFieldTooltip = new Tooltip("Enter the subscriber ID to filter the list.\nLeave empty to show all subscribers.");

        // Add the tooltip to the filter text field and button
        filterTextField.setTooltip(filterTextFieldTooltip);
        filterButton.setTooltip(filterTextFieldTooltip);

        // Set up the columns in the table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusIsFrozen"));

        // Set up the watch profile column with a button
        watchProfileColumn.setCellFactory(param -> new TableCell<Subscriber, Void>()
        {
            // The button to watch the profile
            private final Button watchButton = new Button("Watch Profile");

            {
                // Set the button's action
                watchButton.setOnAction(event -> {
                    Subscriber subscriber = getTableView().getItems().get(getIndex());
                    // Handle the watch profile action here
                    watchProfileButtonClicked(subscriber);
                });
            }

            @Override
            // Update the cell item
            protected void updateItem(Void item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                {
                    setGraphic(null);
                }
                else
                {
                    setGraphic(watchButton);
                    setAlignment(Pos.CENTER); // Center the button
                }
            }
        });

        //creates a message requesting a list of all the subscribers
        ClientServerMessage message = new ClientServerMessage(306, null);

        //sends the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * Adds the list of subscribers to the table.
     *
     * @param subscribers The list of subscribers to add to the table.
     */
    public void addToTable(ArrayList<Subscriber> subscribers)
    {
        // Set the list of all subscribers
        allSubscribers = subscribers;

        // Create an observable list of subscribers and set it to the table
        ObservableList<Subscriber> subscriberList = FXCollections.observableArrayList(subscribers);

        // Set the items in the table
        subscribersTable.setItems(subscriberList);
    }

    /**
     * Handles the Filter button click event.
     * This method will apply the filter logic when the user clicks the filter button.
     */
    public void onFilterButtonClicked()
    {
        // Get the search ID from the filter text field
        String searchId = filterTextField.getText();

        // If the search ID is not empty, filter the subscribers list
        if (searchId != null && !searchId.isEmpty())
        {
            // Try to parse the search ID to an integer
            try
            {
                // Filter the subscribers list by the search ID
                int id = Integer.parseInt(searchId);
                List<Subscriber> filteredList = allSubscribers.stream().filter(subscriber -> subscriber.getID() == id).collect(Collectors.toList());

                // Set the filtered list to the table
                subscribersTable.setItems(FXCollections.observableArrayList(filteredList));
            }
            // Handle the case where the search ID is not a valid integer
            catch (NumberFormatException e)
            {
                showAlert(Alert.AlertType.ERROR, "Invalid ID", "Invalid ID format: ID must be only numbers.");
                System.out.println("Invalid ID format: " + searchId);
            }
        }
        else
        {
            subscribersTable.setItems(FXCollections.observableArrayList(allSubscribers));
        }
    }

    /**
     * Handles the watch profile action.
     *
     * @param subscriber The subscriber whose profile is to be watched.
     */
    private void watchProfileButtonClicked(Subscriber subscriber)
    {
        // Sets the local watch profile subscriber
        Subscriber.setWatchProfileSubscriber(subscriber);

        // navigates to the watch profile frame
        //wrapped in a try catch block so the button will not crash the program
        try
        {
            loadFrameIntoPane((AnchorPane) allSubscribersFrame.getParent(), "/gui/SubscriberProfileFrame.fxml");
        }
        catch (Exception e)
        {
            System.out.println("Error navigating to WatchProfileFrame.fxml\n" + e);
        }
    }

    /**
     * Handles the refresh button click event.
     * This method will reset the filter text field to empty and set the table's data back to the full list of subscribers.
     */
    public void clickRefreshButton()
    {
        // Reset the filter text field to empty
        filterTextField.clear();

        // Set the table's data back to the full list of subscribers
        if (allSubscribers != null)
        {
            subscribersTable.setItems(FXCollections.observableArrayList(allSubscribers));
        }
        else
        {
            System.out.println("No subscribers to display.");
        }
    }
}