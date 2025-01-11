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

import java.util.ArrayList;

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
    public TableColumn<Subscriber, String> statusColumn;
    @FXML
    public TableColumn watchProfileColumn;
    @FXML
    public Button filterButton;
    @FXML
    public TableView subscribersTable;
    @FXML
    public TextField filterTextField;
    @FXML
    public Button backButton;

    /**
     * Initializes the ViewAllSubscribersFrameController.
     */
    public void initialize()
    {
        // Set up the columns in the table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

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
        ObservableList<Subscriber> subscriberList = FXCollections.observableArrayList(subscribers);
        subscribersTable.setItems(subscriberList);
    }

    /**
     * Handles the Apply Filter button click event.
     * This will allow users to apply the filter based on the selected option.
     * Currently, the filter logic is not implemented.
     *
     * @param actionEvent The ActionEvent triggered by clicking the Apply Filter button.
     */
    public void applyFilter(ActionEvent actionEvent)
    {
        //todo:add sort option by id

    }

    /**
     * Handles the Filter button click event.
     * This method will apply the filter logic when the user clicks the filter button.
     * Currently, the filter logic is not implemented.
     *
     * @param actionEvent The ActionEvent triggered by clicking the Filter button.
     */
    public void onFilterButtonClicked(ActionEvent actionEvent)
    {
        //todo: add applying filter
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
}