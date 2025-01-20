package logic;

import client.ClientGUIController;
import common.Librarian;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


import java.io.IOException;

import static client.ClientGUIController.navigateTo;
import static client.ClientGUIController.loadFrameIntoPane;

public class MainFrameController
{

    @FXML
    public Text UsernameText;
    @FXML
    public AnchorPane SceneChanger;
    @FXML
    public Button searchButton;
    @FXML
    public AnchorPane menu;
    @FXML
    public ImageView userPhoto;
    @FXML
    public Button LoginButton;
    public AnchorPane Anchor1;
    public AnchorPane Anchor2;
    public Button borrowButton;
    public Button LogoutButton;
    public Text Hello;

    /**
     * This method initializes the MainFrameController.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void initialize() throws IOException
    {

        if (Subscriber.getLocalSubscriber() == null && Librarian.getLocalLibrarian() == null)
        {
            loadFrameIntoPane(SceneChanger, "/gui/WelcomeScreen.fxml");
            UsernameText.setVisible(false);
            Image image = new Image("/resources/icons8-book-50.png");
            userPhoto.setImage(image);
            Hello.setVisible(false);
        }

        if (Subscriber.getLocalSubscriber() != null)
        {
            loadFrameIntoPane(SceneChanger, "/gui/SubscriberProfileFrame.fxml");
            UsernameText.setText(Subscriber.getLocalSubscriber().getFirstName());
            LoginButton.setDisable(true);
            LoginButton.setVisible(false);
            LogoutButton.setVisible(true);
            LogoutButton.setDisable(false);
            Anchor2.setVisible(true);
            Anchor2.setDisable(false);

        }

        if (Librarian.getLocalLibrarian() != null)
        {
            loadFrameIntoPane(SceneChanger, "/gui/WelcomeScreen.fxml");
            UsernameText.setText(Librarian.getLocalLibrarian().getFirstName());
            LoginButton.setDisable(true);
            LoginButton.setVisible(false);
            LogoutButton.setVisible(true);
            LogoutButton.setDisable(false);
            Anchor1.setVisible(true);
            Anchor1.setDisable(false);
        }
    }

    public void Press1() throws Exception
    {
        loadFrameIntoPane(SceneChanger, "/gui/SearchPageFrame.fxml");

    }

    public void OpenLogin() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/UserLoginFrame.fxml");

    }

    public void ImageOnClick() throws IOException
    {

        loadFrameIntoPane(SceneChanger, "/gui/SubscriberProfileFrame.fxml");

    }

    public void logout(ActionEvent actionEvent) throws Exception
    {

        if (Librarian.getLocalLibrarian() != null)
        {
            ClientGUIController.chat.sendToServer(new common.ClientServerMessage(102, Librarian.getLocalLibrarian().getID()));
            Librarian.setLocalLibrarian(null);
        }
        else if (Subscriber.getLocalSubscriber() != null)
        {
            ClientGUIController.chat.sendToServer(new common.ClientServerMessage(102, Subscriber.getLocalSubscriber().getID()));
            Subscriber.setLocalSubscriber(null);
        }
        navigateTo(actionEvent, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Main Page");
    }

    public void loadBorrow() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/BorrowBookFrame.fxml");
    }

    public void searchForSubscriber() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/SearchSubscriberFrame.fxml");
    }

    public void watchSubscribersBtn() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/ViewAllSubscribersFrame.fxml");
    }

    public void returnBook() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/ReturnBookFrame.fxml");

    }

    public void RegisterMember() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/RegisterMemberFrame.fxml");
    }

    public void viewReports() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/ViewReportsFrame.fxml");
    }

    public void editSubscriberDetails() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/SubscriberEditProfileFrame.fxml");
    }

    public void watchHistory() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/WatchHistoryFrame.fxml");
    }

    public void viewMessagesButtonClicked() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/ViewMessagesFrame.fxml");
    }

    public void changePassword() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/changePasswordFrame.fxml");
    }
}