package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SearchHomePageFrameController
{

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

    private void searchByName()
    {
        //todo: implement
    }

    private void searchByGenre()
    {
        //todo: implement
    }

    //todo: make sure searchButton moves to layoutY = 500 when option is selected
    private void searchByDescription()
    {
        //todo: implement
    }

    public void search()
    {
        //todo: implement
        //todo: make sure searchButton moves to layoutY = 500 when option is selected
        //todo: use radio and either use the private methods of just send it from here
    }

    public void login()
    {
        //todo: implement
    }

    public void goToProfile()
    {
        //todo: implement
    }
}
