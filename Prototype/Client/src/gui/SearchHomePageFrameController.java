package gui;

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

    public void initialize()
    {
        //todo: implement
    }

    /**
     * This method changes the search configuration to search by name
     * @param event     The ActionEvent triggered by the button click
     */
    private void searchByName(ActionEvent event)
    {
        searchType = SearchType.NAME;
    }

    /**
     * This method changes the search configuration to search by genre
     * @param event     The ActionEvent triggered by the button click
     */
    private void searchByGenre(ActionEvent event)
    {
        searchType = SearchType.GENRE;
    }

    /**
     * This method changes the search configuration to search by description
     * @param event     The ActionEvent triggered by the button click
     */
    private void searchByDescription(ActionEvent event)
    {
        searchType = SearchType.DESCRIPTION;
        searchButton.setLayoutY(500);
        searchField.setVisible(false);
        descriptionSearch.setVisible(true);
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
    }

    /**
     * This method handles the loginButton click event to navigate to the login page
     * @param event      The ActionEvent triggered by the login button click
     * @throws Exception if there is an issue with the navigation
     */
    public void login(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/LoginFrame.fxml", "/gui/Subscriber.css", "Login");
    }

    /**
     * This method handles the profileButton click event to navigate to the profile page
     * @param event      the ActionEvent triggered by the profile button click
     * @throws Exception if there is an issue with the navigation
     */
    public void goToProfile(ActionEvent event) throws Exception
    {
        //todo: implement logic to check if there's a user logged in and navigate to the appropriate page
    }
}
