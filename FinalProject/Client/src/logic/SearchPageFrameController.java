package logic;

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
import javafx.scene.layout.VBox;

import static client.ClientGUIController.loadFrameIntoPane;
import static client.ClientGUIController.navigateTo;

public class SearchPageFrameController
{
    public VBox searchFrame;

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

    private static boolean canSearch = false; //Variable to check if we got results from the search
    private UserType user_type = UserType.User;

    public void initialize()
    {
        //set search by name to be the default search when opening the window
        nameRadio.setSelected(true);
        searchType = SearchType.NAME;

        searchField.setVisible(true);
        descriptionSearch.setVisible(false);
    }

    /**
     * This method changes the canSearch variable to the given boolean
     *
     * @param bool The boolean to change the canSearch variable to
     */
    public static void changeCanSearch(Boolean bool)
    {
        canSearch = bool;
    }

    /**
     * This method changes the search configuration to search by name
     *
     * @param event The ActionEvent triggered by the button click
     */
    public void searchByName(ActionEvent event)
    {
        searchType = SearchType.NAME;
        changeAllRadioSelected(true, false, false);
        searchField.setVisible(true);
        descriptionSearch.setVisible(false);

    }

    /**
     * This method changes the search configuration to search by genre
     *
     * @param event The ActionEvent triggered by the button click
     */
    public void searchByGenre(ActionEvent event)
    {
        searchType = SearchType.GENRE;
        changeAllRadioSelected(false, true, false);
        searchField.setVisible(true);
        descriptionSearch.setVisible(false);
    }

    /**
     * This method changes the search configuration to search by description
     *
     * @param event The ActionEvent triggered by the button click
     */
    public void searchByDescription(ActionEvent event)
    {
        searchType = SearchType.DESCRIPTION;
        searchButton.setLayoutY(500);
        searchField.setVisible(false);
        descriptionSearch.setVisible(true);

        changeAllRadioSelected(false, false, true);
    }

    /**
     * This method changes the selected radio button to the given values
     *
     * @param name  The value to set the name radio button to
     * @param genre The value to set the genre radio button to
     * @param desc  The value to set the description radio button to
     */
    private void changeAllRadioSelected(Boolean name, Boolean genre, boolean desc)
    {
        nameRadio.setSelected(name);
        genreRadio.setSelected(genre);
        descriptionRadio.setSelected(desc);
    }

    /**
     * This method handles the searchButton click event to search for books
     *
     * @param event The ActionEvent triggered by the search button click
     * @throws Exception if there is an issue with the search
     */
    public void search(ActionEvent event) throws Exception
    {
        int messageCode;
        String messageContent;

        /// Switch case for which option of search was chosen
        /// sets the message code and content according to it
        switch (searchType)
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
                return;
        }

        //checks if we are trying to search with empty messageContent and stops it from advancing
        if (messageContent.isEmpty()) return;

        ClientServerMessage searchMessage = new ClientServerMessage(messageCode, messageContent);

        try
        {
            ClientGUIController.chat.sendToServer(searchMessage);
        }
        catch (Exception e)
        {
            System.out.println("Error sending search message to server: " + e.getMessage());
        }

        //if there were was a result from the server, go to searchResultFrame
        if (canSearch)
        {
            loadFrameIntoPane((AnchorPane) searchFrame.getParent(), "/gui/SearchResultFrame.fxml");
        }
    }

}