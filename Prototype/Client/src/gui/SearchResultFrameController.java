package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import static client.ClientGUIController.navigateTo;

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

    /**
     * This method handles the backButton click event to navigate back to the previous frame
     * @param event
     * @throws Exception
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Search Home Page");
    }

    /**
     * This method adds a button to each row that will open a new frame with the book details in the watchDetailsColumn
     */
    private void addBookDetailsButton()
    {
        //todo: add a button to each row that will open a new frame with the book details in the watchDetailsColumn
    }

    /**
     * This method handles the watchDetailsButton click event to navigate to the book info page
     * @param event
     * @throws Exception
     */
    public void watchDetailsButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/BookInfoFrame.fxml", "/gui/Subscriber.css", "Book Info");
    }
}
