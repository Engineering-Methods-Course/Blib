package gui.controllers;

import main.ClientGUIController;
import common.Book;
import common.ClientServerMessage;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static main.ClientGUIController.loadFrameIntoPane;

public class SearchResultFrameController
{
    //FXML attributes
    @FXML
    public TableView<List<String>> searchResultTable;
    @FXML
    public TableColumn<List<String>, String> bookNameColumn;
    @FXML
    public TableColumn<List<String>, String> genreColumn;
    @FXML
    public TableColumn<List<String>, String> watchDetailsColumn;
    @FXML
    public VBox SearchResultFrame;


    //other class attributes
    private final ObservableList<List<String>> BookList = FXCollections.observableArrayList();
    private final Property<ObservableList<List<String>>> BookListProperty = new SimpleObjectProperty<>(BookList);
    private static ArrayList<Book> books;
    public Button backButtom;

    /**
     * initializes the table with the books that were found
     */
    public void initialize()
    {
        // bind the table to the list
        searchResultTable.itemsProperty().bind(BookListProperty);
        searchResultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // set the columns
        bookNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        genreColumn.setCellValueFactory((cellData -> new SimpleStringProperty(cellData.getValue().get(1))));
        watchDetailsColumn.setCellFactory(column -> new TableCell<List<String>, String>()
        {
            // Create the button
            private final Button button = new Button("View Details");
            {
                // Set the button action
                button.setOnAction(event -> {
                    try
                    {
                        getBookCopy(getTableView().getItems().get(getIndex()));
                        loadFrameIntoPane((AnchorPane) SearchResultFrame.getParent(), "/gui/fxml/BookInfoFrame.fxml");
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                });
            }

            // Display button if the row is not empty
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null)
                {
                    setGraphic(null);
                }
                else
                {
                    setGraphic(button);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        //iterates on the book list and adds each book to the table
        for (Book book : books)
        {
            List<String> row = new ArrayList<>();
            row.add(book.getBookName());
            row.add(book.getBookGenre());
            this.BookList.add(row);
        }
    }

    /**
     * Sets the book list to the input
     *
     * @param booksList - the book list we want to set it to
     */
    public static void setBookArray(ArrayList<Book> booksList)
    {
        books = booksList;
    }

    /**
     * This method gets the book copy from the server
     *
     * @param listFromRow - the list of strings from the row
     */
    private void getBookCopy(List<String> listFromRow)
    {
        // Create a book object
        Book bk = null;

        // search for the book in the list
        for (Book book : books)
        {
            if (listFromRow.contains(book.getBookName()) && listFromRow.contains(book.getBookGenre()))
            {
                bk = book;
            }
        }

        // If the book was found
        if (bk != null)
        {
            // Create the message to send to the server
            ClientServerMessage searchMessage = new ClientServerMessage(206, bk.getBookSerialNumber());

            // Print the message content
            System.out.println(searchMessage.getMessageContent());

            // Send the message to the server
            ClientGUIController.chat.sendToServer(searchMessage);

            // Set the local book
            BookInfoFrameController.setLocalBook(bk);
        }
    }

    /**
     * This method handles the back button click event to return to the previous page
     *
     */
    public void goBack() {
        try
        {
            loadFrameIntoPane((AnchorPane) SearchResultFrame.getParent(), "/gui/fxml/SearchPageFrame.fxml");
        }
        catch (Exception e) {
            System.out.println("Failed to load the search page.");
        }
    }
}