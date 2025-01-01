package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SearchResultFrameController
{

    @FXML
    public TableView searchResultTable;
    @FXML
    public TableColumn bookNameColumn;
    @FXML
    public TableColumn genreColumn;
    @FXML
    public TableColumn locationColumn;
    @FXML
    public TableColumn watchDetailsColumn;
    @FXML
    public Button backButton;

    public void initialize()
    {
        //todo: load search results from the database into the table
    }

    public void backButtonClicked()
    {
        //todo: go back to the search frame
    }

    private void addBookDetailsButton()
    {
        //todo: add a button to each row that will open a new frame with the book details in the watchDetailsColumn
    }
}
