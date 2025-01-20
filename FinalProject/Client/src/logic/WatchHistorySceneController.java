package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Subscriber;
import common.SubscriberHistory;
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

        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // sends the server a request for the user's history
        ClientServerMessage message = new ClientServerMessage(214, Subscriber.getLocalSubscriber().getID());
        ClientGUIController.chat.sendToServer(message);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime localDateTime;

        // create a list of SubscriberHistory objects
        List<SubscriberHistory> subscriberHistoryList = new ArrayList<>();
        for (ArrayList<String> historyItem : history)
        {
            System.out.println(historyItem);
            localDateTime = LocalDateTime.parse(historyItem.get(0), formatter);
            date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            subscriberHistoryList.add(new SubscriberHistory(date, historyItem.get(1), historyItem.get(2)));
        }

        // set the items in the table
        historyTable.getItems().setAll(subscriberHistoryList);
    }
}