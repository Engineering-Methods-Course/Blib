package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static client.ClientGUIController.loadFrameIntoPane;
import static client.ClientGUIController.navigateTo;

public class ViewAllSubscribersFrameController
{
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
    public TableColumn<Subscriber, Boolean> statusColumn;
    @FXML
    public TableColumn<Subscriber, Void> watchProfileColumn;
    @FXML
    public Button filterButton;
    @FXML
    public TableView<Subscriber> subscribersTable;
    @FXML
    public TextField filterTextField;
    @FXML
    public Button backButton;
    public AnchorPane allSubscribersFrame;

    private List<Subscriber> allSubscribers;

    /**
     * Initializes the ViewAllSubscribersFrameController.
     */
    public void initialize()
    {
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
            private final Button watchButton = new Button("Watch Profile");

            {
                watchButton.setOnAction(event -> {
                    Subscriber subscriber = getTableView().getItems().get(getIndex());
                    // Handle the watch profile action here
                    watchProfileButtonClicked(event, subscriber);
                });
            }

            @Override
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
        allSubscribers = subscribers;
        ObservableList<Subscriber> subscriberList = FXCollections.observableArrayList(subscribers);
        subscribersTable.setItems(subscriberList);
    }

    /**
     * Handles the Filter button click event.
     * This method will apply the filter logic when the user clicks the filter button.
     */
    public void onFilterButtonClicked()
    {
        String searchId = filterTextField.getText();
        if (searchId != null && !searchId.isEmpty())
        {
            try
            {
                int id = Integer.parseInt(searchId);
                List<Subscriber> filteredList = allSubscribers.stream().filter(subscriber -> subscriber.getID() == id).collect(Collectors.toList());
                subscribersTable.setItems(FXCollections.observableArrayList(filteredList));
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid ID format: " + searchId);
            }
        }
        else
        {
            subscribersTable.setItems(FXCollections.observableArrayList(allSubscribers));
        }
    }

    /**
     * Handles the Back button click event.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "All Subscribers");
    }

    /**
     * Handles the watch profile action.
     *
     * @param subscriber The subscriber whose profile is to be watched.
     */
    private void watchProfileButtonClicked(ActionEvent event, Subscriber subscriber)
    {
        // Sets the local watch profile subscriber
        Subscriber.setWatchProfileSubscriber(subscriber);

        // navigates to the watch profile frame
        //wrapped in a try catch block so the button will not crash the program
        try
        {
            loadFrameIntoPane((AnchorPane) allSubscribersFrame.getParent(), "/gui/WatchProfileFrame.fxml");
        }
        catch (Exception e)
        {
            System.out.println("Error navigating to WatchProfileFrame.fxml\n" + e);
        }
    }
}