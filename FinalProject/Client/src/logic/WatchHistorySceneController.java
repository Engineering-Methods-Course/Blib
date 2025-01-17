package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import common.SubscriberHistory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static client.ClientGUIController.navigateTo;

public class WatchHistorySceneController
{
    @FXML
    public TableColumn<SubscriberHistory, Date> dateColumn;
    @FXML
    public TableColumn<SubscriberHistory, String> actionTypeColumn;
    @FXML
    public TableColumn<SubscriberHistory, String> descriptionColumn;
    @FXML
    public Button backButton;
    @FXML
    public TableView<SubscriberHistory> historyTable;

    /**
     * Initializes the Watch History Scene.
     */
    public void initialize()
    {
        // set up the table to accept SubscriberHistory objects
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        actionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // sends the server a request for the user's history
        ClientServerMessage message = new ClientServerMessage(214, Subscriber.getLocalSubscriber().getSubscriptionHistory());
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * Handles the Back button click event.
     *
     * @param event      The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        // go back to the previous scene
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "Watch Profile");
    }

    /**
     * Sets the history of the subscriber in the table.
     *
     * @param history The history of the subscriber.
     */
    public void setHistory(List<ArrayList<String>> history)
    {
        // helper Date object for easier reading
        Date date;

        // create a list of SubscriberHistory objects
        List<SubscriberHistory> subscriberHistoryList = new ArrayList<>();
        for (ArrayList<String> historyItem : history)
        {
            date = Date.from(LocalDateTime.parse(historyItem.get(0), DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault()).toInstant());
            subscriberHistoryList.add(new SubscriberHistory(date, historyItem.get(1), historyItem.get(2)));
        }

        // set the items in the table
        historyTable.getItems().setAll(subscriberHistoryList);
    }
}
