package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.LibrarianMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class ViewMessagesFrameController
{
    // The FXML elements
    @FXML
    public TableView<LibrarianMessage> messagesTableView;
    @FXML
    public TableColumn<LibrarianMessage, String> dateColumn;
    @FXML
    public TableColumn<LibrarianMessage, String> contentColumn;

    /**
     * Initializes the View Messages Frame.
     */
    public void initialize()
    {
        // Set up the columns in the table
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

        // creates the message to request the messages from the server
        ClientServerMessage message = new ClientServerMessage(316, null);

        // sends the message to the server
        ClientGUIController.chat.sendToServer(message);
    }

    /**
     * Loads the messages into the table.
     *
     * @param messages The messages to load into the table.
     */
    public void loadLibrarianMessages(List<ArrayList<String>> messages)
    {
        // Create a list of LibrarianMessage objects
        ArrayList<LibrarianMessage> messageList = new ArrayList<>();

        // Create a LibrarianMessage object for each message and add it to the list
        for (ArrayList<String> message : messages)
        {
            messageList.add(new LibrarianMessage(message.get(0), message.get(1)));
        }

        // converts the list to an observable list and sets it to the table
        ObservableList<LibrarianMessage> data = FXCollections.observableArrayList(messageList);
        messagesTableView.setItems(data);
    }
}