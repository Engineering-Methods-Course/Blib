package gui;

import common.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import static client.ClientGUIController.navigateTo;

public class ReturnBookFrameController {

    @FXML
    private TextField idTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button backButton;
    @FXML
    private TableView<Book> booksTableView;
    @FXML
    private TableColumn<Book, String> serialNumberColumn;
    @FXML
    private TableColumn<Book, String> returnDateColumn;
    @FXML
    private TableColumn<Book, Button> actionColumn;

    /**
     * Initializes the table and sets up column bindings.
     */
    public void initialize() {
        //todo:

    }

    /**
     * Handles the Search button click event.
     * @param event The ActionEvent triggered by clicking the button.
     */
    public void searchButtonClicked(ActionEvent event) {
        //todo: get all member's books into the table

    }

    /**
     * Handles the Back button click event.
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception {
        navigateTo(event, "LibrarianProfileFrame.fxml", "Subscriber.css", "Return");
    }
}
