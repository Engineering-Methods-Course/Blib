package logic;

import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import static client.ClientGUIController.navigateTo;

public class WatchProfileFrameController
{
    @FXML
    private Text txtName;
    @FXML
    private Text txtPhoneNumber;
    @FXML
    private Text txtStatus;
    @FXML
    private Text txtEmail;
    @FXML
    private Text txtUserID;
    @FXML
    public Button btnBack;


    //210 returns the borrowed list

    public void initialize()
    {
        fillFields();
        //todo:add borrow history into table
    }

    public void fillFields(){
        Subscriber subscriber = Subscriber.getWatchProfileSubscriber();
        //todo:fill the field
    }


    /**
     * Handles the Back button click event.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        Subscriber.setWatchProfileSubscriber(null);
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "Return");
    }
}