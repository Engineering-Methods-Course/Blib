package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.LibrarianMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;

public class ViewMessagesFrameController
{
    @FXML
    public TableView<LibrarianMessage> messagesTableView;
    @FXML
    public TableColumn<LibrarianMessage, String> dateColumn;
    @FXML
    public TableColumn<LibrarianMessage, String> contentColumn;
    @FXML
    private Button backButton;

    public void initialize()
    {
        ClientServerMessage message = new ClientServerMessage(314, null);

        ClientGUIController.chat.sendToServer(message);
    }

    /**
     *
     */
    public void loadLibrarianMessages(ArrayList<LibrarianMessage> messages)
    {
        ObservableList<LibrarianMessage> data = FXCollections.observableArrayList(messages);
        messagesTableView.setItems(data);
    }

    /**
     * Handles the Back button click event.
     *
     * @param actionEvent The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent actionEvent) throws Exception
    {
        // Navigate back to the previous screen (you can change the target screen as needed)
        navigateTo(actionEvent, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "Messages");
    }
}