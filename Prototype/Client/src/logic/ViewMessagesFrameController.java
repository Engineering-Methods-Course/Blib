package logic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static client.ClientGUIController.navigateTo;

public class ViewMessagesFrameController
{
    @FXML
    private Button backButton;

    public void ViewAllMessages()
    {
        //todo: show all messages from DB in the table
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