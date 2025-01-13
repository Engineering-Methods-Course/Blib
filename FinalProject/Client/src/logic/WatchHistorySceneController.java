package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
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

    public void initialize()
    {
        // set up the table to accept SubscriberHistory objects
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        actionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // send the request to the server to get the history of the user
        ClientServerMessage message = new ClientServerMessage(214, null);

        ClientGUIController.chat.sendToServer(message);
    }

    public void backButtonClicked(ActionEvent event) throws Exception
    {
        // go back to the previous scene
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "Watch Profile");
    }

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
