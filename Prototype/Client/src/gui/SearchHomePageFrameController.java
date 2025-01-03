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
     *
     * @param event
     * @throws Exception
     */
    private void searchByName(ActionEvent event) throws Exception
    {
        searchType = SearchType.NAME;
    }

    private void searchByGenre(ActionEvent event) throws Exception
    {
        searchType = SearchType.GENRE;
    }

    /**
     * This method changes the search configuration to search by description
     * @param event
     * @throws Exception
     */
    private void searchByDescription(ActionEvent event) throws Exception
    {
        searchType = SearchType.DESCRIPTION;
        searchButton.setLayoutY(500);
        searchField.setVisible(false);
        descriptionSearch.setVisible(true);
    }

    /**
     * This method handles the searchButton click event to search for books
     * @param event
     * @throws Exception
     */
    public void search(ActionEvent event) throws Exception
    {
        //todo: implement
        //todo: use radio and either use the private methods of just send it from here
    }

    /**
     * This method handles the loginButton click event to navigate to the login page
     * @param event the ActionEvent triggered by the login button click
     * @throws Exception
     */
    public void login(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/LoginFrame.fxml", "/gui/Subscriber.css", "Login");
    }

    /**
     * This method handles the profileButton click event to navigate to the profile page
     * @param event the ActionEvent triggered by the profile button click
     * @throws Exception
     */
    public void goToProfile(ActionEvent event) throws Exception
    {
        //todo: implement logic to check if there's a user logged in and navigate to the appropriate page
    }
}
