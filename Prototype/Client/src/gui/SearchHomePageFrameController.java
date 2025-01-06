package gui;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import static client.ClientGUIController.navigateTo;

public class SearchHomePageFrameController
{

    private enum SearchType
    {
        NAME,
        GENRE,
        DESCRIPTION
    }

    private enum UserType
    {
        Subscriber,
        Librarian,
        User
    }

    private SearchType searchType;
    @FXML
    public Pane menuPane;
    @FXML
    public Button loginButton;
    @FXML
    public Button profileButton;
    @FXML
    public AnchorPane searchPane;
    @FXML
    public RadioButton nameRadio;
    @FXML
    public RadioButton genreRadio;
    @FXML
    public RadioButton descriptionRadio;
    @FXML
    public TextField searchField;
    @FXML
    public TextArea descriptionSearch;
    @FXML
    public Button searchButton;
    @FXML
    public Button watchProfileButton;

    private static boolean canSearch=false; //Variable to check if we got results from the search
    private UserType user_type = UserType.User;

    public void initialize()
    {
        String name="";
        //need to check what user (user/client/librarian) enters the search
        if(Subscriber.getLocalSubscriber()!=null)
        {
            user_type = UserType.Subscriber;
            name = Subscriber.getLocalSubscriber().getFirstName();
        }
        else if(Librarian.getLocalLibrarian()!=null)
        {
            user_type = UserType.Librarian;
            name = Librarian.getLocalLibrarian().getFirstName();
        }

        profileButton.setDisable(!user_type.equals(UserType.User));
        profileButton.setVisible(!user_type.equals(UserType.User));
        profileButton.setText("Hello "+name);
        watchProfileButton.setDisable(!user_type.equals(UserType.User));
        watchProfileButton.setVisible(!user_type.equals(UserType.User));
        //watchProfileButton.setVisible()

        //set search by name to be the default search when opening the window
        nameRadio.setSelected(true);
        searchType = SearchType.NAME;

        searchField.setVisible(true);
        descriptionSearch.setVisible(false);

    }
    //allows the system to change the ability to allow search process
    public static void changeCanSearch(Boolean bool)
    {
        canSearch=bool;
    }

    /**
     * This method changes the search configuration to search by name
     * @param event     The ActionEvent triggered by the button click
     */
    public void searchByName(ActionEvent event)
    {
        searchType = SearchType.NAME;
        changeAllRadioSelected(true,false,false);
        searchField.setVisible(true);
        descriptionSearch.setVisible(false);

    }

    /**
     * This method changes the search configuration to search by genre
     * @param event     The ActionEvent triggered by the button click
     */
    public void searchByGenre(ActionEvent event)
    {
        searchType = SearchType.GENRE;
        changeAllRadioSelected(false,true,false);
        searchField.setVisible(true);
        descriptionSearch.setVisible(false);
    }

    /**
     * This method changes the search configuration to search by description
     * @param event     The ActionEvent triggered by the button click
     */
    public void searchByDescription(ActionEvent event)
    {
        searchType = SearchType.DESCRIPTION;
        searchButton.setLayoutY(500);
        searchField.setVisible(false);
        descriptionSearch.setVisible(true);

        changeAllRadioSelected(false,false,true);
    }

    private void changeAllRadioSelected(Boolean name, Boolean genre, boolean desc)
    {
        nameRadio.setSelected(name);
        genreRadio.setSelected(genre);
        descriptionRadio.setSelected(desc);
    }

    /**
     * This method handles the searchButton click event to search for books
     * @param event      The ActionEvent triggered by the search button click
     * @throws Exception if there is an issue with the search
     */
    public void search(ActionEvent event) throws Exception
    {
        //todo: implement
        //todo: use radio and either use the private methods of just send it from here
        int messageCode=0 ;
        String messageContent ="";

        /// Switch case for which option of search was chosen
        /// sets the message code and content according to it
        switch(searchType)
        {
            case NAME:
                messageCode = 200;
                messageContent = searchField.getText();
                break;
            case GENRE:
                messageCode = 202;
                messageContent = searchField.getText();
                break;
            case DESCRIPTION:
                messageCode = 204;
                messageContent = descriptionSearch.getText();
                break;
            default:
                System.out.println("choose a category");
                return;
        }

        ClientServerMessage searchMessage = new ClientServerMessage(messageCode,messageContent);
        try {
            ClientGUIController.chat.sendToServer(searchMessage);
        }
        catch (Exception e) {
            System.out.println("Error sending search message to server: " + e.getMessage());
        }
        if(canSearch)
        {
            navigateTo(event, "/gui/SearchResultFrame.fxml","/gui/Subscriber.css", "Search results");
        }


    }

    /**
     * This method handles the loginButton click event to navigate to the login page
     * @param event      The ActionEvent triggered by the login button click
     * @throws Exception if there is an issue with the navigation
     */
    public void login(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/UserLoginFrame.fxml", "/gui/Subscriber.css", "Login");
    }

    /**
     * This method handles the profileButton click event to navigate to the profile page
     * @param event      the ActionEvent triggered by the profile button click
     * @throws Exception if there is an issue with the navigation
     */
    public void goToProfile(ActionEvent event) throws Exception
    {
        //todo: implement logic to check if there's a user logged in and navigate to the appropriate page
        //check the instance of the current user and according to it navigate
    }
}
