package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
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
    // FXML attributes
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
    @FXML
    public AnchorPane Anchor1;
    @FXML
    public AnchorPane Anchor2;
    @FXML
    public Button borrowButton;
    @FXML
    public Button LogoutButton;
    @FXML
    public Button subscriberProfileButton;
    @FXML
    public Button librarianProfileButton;
    @FXML
    public Button MainSceneButton;

    /**
     * This method initializes the MainFrameController.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void initialize() throws IOException
    {
        // Set the style of the SceneChanger
        SceneChanger.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");

        // Check if the user is a subscriber or librarian
        if (Subscriber.getLocalSubscriber() == null && Librarian.getLocalLibrarian() == null)
        {
            // Load the welcome screen
            loadFrameIntoPane(SceneChanger, "/gui/WelcomeScreen.fxml");
            UsernameText.setVisible(false);
            Image image = new Image("/resources/icons8-book-50.png");
            userPhoto.setImage(image);
        }

        // Check if the user is a subscriber
        if (Subscriber.getLocalSubscriber() != null)
        {
            // Load the subscriber profile frame
            loadFrameIntoPane(SceneChanger, "/gui/SubscriberProfileFrame.fxml");
            UsernameText.setText(Subscriber.getLocalSubscriber().getFirstName() + " " + Subscriber.getLocalSubscriber().getLastName());
            LoginButton.setDisable(true);
            LoginButton.setVisible(false);
            LogoutButton.setVisible(true);
            LogoutButton.setDisable(false);
            Anchor2.setVisible(true);
            Anchor2.setDisable(false);
            MainSceneButton.setVisible(false);
            MainSceneButton.setDisable(true);
            subscriberProfileButton.setVisible(true);
            subscriberProfileButton.setDisable(false);
        }

        // Check if the user is a librarian
        if (Librarian.getLocalLibrarian() != null)
        {
            // Load the librarian profile frame
            loadFrameIntoPane(SceneChanger, "/gui/WelcomeScreen.fxml");
            UsernameText.setText(Librarian.getLocalLibrarian().getFirstName() + " " + Librarian.getLocalLibrarian().getLastName());
            LoginButton.setDisable(true);
            LoginButton.setVisible(false);
            LogoutButton.setVisible(true);
            LogoutButton.setDisable(false);
            Anchor1.setVisible(true);
            Anchor1.setDisable(false);
            MainSceneButton.setVisible(false);
            MainSceneButton.setDisable(true);
            librarianProfileButton.setVisible(true);
            librarianProfileButton.setDisable(false);
        }
    }

    /**
     * This method handles the search button click event.
     *
     * @throws Exception If there is an issue with loading the FXML file
     */
    public void searchButton() throws Exception
    {
        // Load the search page frame
        loadFrameIntoPane(SceneChanger, "/gui/SearchPageFrame.fxml");
    }

    /**
     * This method handles the login button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void openLogin() throws IOException
    {
        // Load the user login frame
        loadFrameIntoPane(SceneChanger, "/gui/UserLoginFrame.fxml");
    }

    /**
     * This method handles the image click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void imageOnClick() throws IOException
    {
        // Check if the user is a subscriber
        if (Subscriber.getLocalSubscriber() != null)
        {
            // Load the subscriber profile frame
            loadFrameIntoPane(SceneChanger, "/gui/SubscriberProfileFrame.fxml");
        }
        else
        {
            // Load the welcome screen
            loadFrameIntoPane(SceneChanger, "/gui/WelcomeScreen.fxml");
        }
    }

    /**
     * This method handles the logout button click event.
     *
     * @param actionEvent The action event triggered by clicking the logout button
     * @throws Exception If there is an issue with the navigation
     */
    public void logout(ActionEvent actionEvent) throws Exception
    {
        // Check if the user is a subscriber or librarian
        if (Librarian.getLocalLibrarian() != null)
        {
            // Send a message to the server to log out the librarian
            ClientGUIController.chat.sendToServer(new ClientServerMessage(102, Librarian.getLocalLibrarian().getID()));
            // Set the local librarian to null
            Librarian.setLocalLibrarian(null);
        }
        // Check if the user is a subscriber
        else if (Subscriber.getLocalSubscriber() != null)
        {
            // Send a message to the server to log out the subscriber
            ClientGUIController.chat.sendToServer(new ClientServerMessage(102, Subscriber.getLocalSubscriber().getID()));
            // Set the local subscriber to null
            Subscriber.setLocalSubscriber(null);
        }
        // resets the frame to the logged-out user state
        navigateTo(actionEvent, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Main Page");
    }

    /**
     * This method handles the borrow button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void loadBorrow() throws IOException
    {
        loadFrameIntoPane(SceneChanger, "/gui/BorrowBookFrame.fxml");
    }

    /**
     * This method handles the subscriber profile button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void searchForSubscriber() throws IOException
    {
        // loads the search subscriber frame
        loadFrameIntoPane(SceneChanger, "/gui/SearchSubscriberFrame.fxml");
    }

    /**
     * This method handles the librarian profile button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void watchSubscribersBtn() throws IOException
    {
        // loads the view all subscribers frame
        loadFrameIntoPane(SceneChanger, "/gui/ViewAllSubscribersFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void returnBook() throws IOException
    {
        // loads the return book frame
        loadFrameIntoPane(SceneChanger, "/gui/ReturnBookFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void RegisterMember() throws IOException
    {
        // loads the register member frame
        loadFrameIntoPane(SceneChanger, "/gui/RegisterMemberFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void viewReports() throws IOException
    {
        // loads the view reports frame
        loadFrameIntoPane(SceneChanger, "/gui/ViewReportsFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void editSubscriberDetails() throws IOException
    {
        // loads the subscriber edit profile frame
        loadFrameIntoPane(SceneChanger, "/gui/SubscriberEditProfileFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void watchHistory() throws IOException
    {
        // loads the watch history frame
        loadFrameIntoPane(SceneChanger, "/gui/WatchHistoryFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void viewMessagesButtonClicked() throws IOException
    {
        // loads the view messages frame
        loadFrameIntoPane(SceneChanger, "/gui/ViewMessagesFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void changePassword() throws IOException
    {
        // loads the change password frame
        loadFrameIntoPane(SceneChanger, "/gui/changePasswordFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void loadSubscriberProfile() throws IOException
    {
        // loads the subscriber profile frame
        loadFrameIntoPane(SceneChanger, "/gui/SubscriberProfileFrame.fxml");
    }

    /**
     * This method handles the view books button click event.
     *
     * @throws IOException If there is an issue with loading the FXML file
     */
    public void loadMainScene() throws IOException
    {
        // loads the welcome screen
        loadFrameIntoPane(SceneChanger, "/gui/WelcomeScreen.fxml");
    }
}