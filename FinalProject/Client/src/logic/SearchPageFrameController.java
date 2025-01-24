package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import static client.ClientGUIController.loadFrameIntoPane;

public class SearchPageFrameController
{
    // FXML attributes
    @FXML
    public VBox searchFrame;
    @FXML
    public ChoiceBox<String> categoriesDropDown;
    @FXML
    private SearchType searchType;
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

    // class enums for easier search configuration
    private enum SearchType
    {
        NAME,
        GENRE,
        DESCRIPTION
    }

    // other class attributes
    private static boolean canSearch = false; //Variable to check if we got results from the search

    /**
     * Initializes the SearchPageFrameController.
     */
    public void initialize()
    {
        //set search by name to be the default search when opening the window
        nameRadio.setSelected(true);
        searchType = SearchType.NAME;

        // set the searchField to be visible and the descriptionSearch to be invisible
        searchField.setVisible(true);
        descriptionSearch.setVisible(false);

        // Creates tooltips for the search fields
        Tooltip searchFieldTooltip = new Tooltip("Enter the name of the book you are looking for.");
        Tooltip searchByCategoriesFieldTooltip = new Tooltip("Choose the category of the book you are looking for.");
        Tooltip descriptionSearchTooltip = new Tooltip("Enter the description of the book you are looking for.");

        // Add Tooltips to the text boxes that will appear when the user hovers over them
        searchField.setTooltip(searchFieldTooltip);
        categoriesDropDown.setTooltip(searchByCategoriesFieldTooltip);
        descriptionSearch.setTooltip(descriptionSearchTooltip);

        // Add options to the ChoiceBox
        categoriesDropDown.getItems().addAll(
                "Fantasy",
                "Classic",
                "Historical Fiction",
                "Sci-Fi",
                "Psychological Thriller",
                "Memoir",
                "Science"
        );
        categoriesDropDown.setValue("Classic");
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
     */
    public void searchByName()
    {
        // set the search type to name and change the radio buttons accordingly
        searchType = SearchType.NAME;
        changeAllRadioSelected(true, false, false);

        // set the searchField to be visible and the descriptionSearch to be invisible
        searchField.setVisible(true);
        categoriesDropDown.setVisible(false);
        descriptionSearch.setVisible(false);

        // Change the tooltip's text
        searchField.getTooltip().setText("Enter the name of the book you are looking for.");
    }

    /**
     * This method changes the search configuration to search by genre
     */
    public void searchByGenre()
    {
        // set the search type to genre and change the radio buttons accordingly
        searchType = SearchType.GENRE;
        changeAllRadioSelected(false, true, false);

        // set the searchField to be visible and the descriptionSearch to be invisible
        searchField.setVisible(false);
        categoriesDropDown.setVisible(true);
        descriptionSearch.setVisible(false);



        // Change the tooltip's text
        searchField.getTooltip().setText("Enter the genre of the book you are looking for.");
    }

    /**
     * This method changes the search configuration to search by description
     */
    public void searchByDescription()
    {
        // set the search type to description and change the radio buttons accordingly
        searchType = SearchType.DESCRIPTION;
        changeAllRadioSelected(false, false, true);

        // move the search button to the bottom of the screen and set the searchField to be invisible and the descriptionSearch to be visible
        searchButton.setLayoutY(500);
        searchField.setVisible(false);
        categoriesDropDown.setVisible(false);
        descriptionSearch.setVisible(true);
    }

    /**
     * This method changes the selected radio button to the given values
     *
     * @param name  The value to set the name radio button to
     * @param genre The value to set the genre radio button to
     * @param desc  The value to set the description radio button to
     */
    private void changeAllRadioSelected(boolean name, boolean genre, boolean desc)
    {
        // set the radio buttons to the given values
        nameRadio.setSelected(name);
        genreRadio.setSelected(genre);
        descriptionRadio.setSelected(desc);
    }

    /**
     * This method handles the searchButton click event to search for books
     *
     * @throws Exception if there is an issue with the search
     */
    public void search() throws Exception
    {
        // variables for the message code and content
        int messageCode;
        String messageContent;

        // Switch case for which option of search was chosen
        // sets the message code and content according to it
        switch (searchType)
        {
            // sets the search to work with the name of the book
            case NAME:
                messageCode = 200;
                messageContent = searchField.getText();
                break;
            // sets the search to work with the genre of the book
            case GENRE:
                messageCode = 202;
                messageContent = categoriesDropDown.getValue();
                break;
            // sets the search to work with the description of the book
            case DESCRIPTION:
                messageCode = 204;
                messageContent = descriptionSearch.getText();
                break;
            // if the search type is not one of the above, return
            default:
                return;
        }

        //checks if we are trying to search with empty messageContent and stops it from advancing
        if (messageContent.isEmpty()) return;

        // create a new message to send to the server
        ClientServerMessage searchMessage = new ClientServerMessage(messageCode, messageContent);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(searchMessage);

        //if there were was a result from the server, go to searchResultFrame
        if (canSearch)
        {
            loadFrameIntoPane((AnchorPane) searchFrame.getParent(), "/gui/SearchResultFrame.fxml");
        }
    }
}