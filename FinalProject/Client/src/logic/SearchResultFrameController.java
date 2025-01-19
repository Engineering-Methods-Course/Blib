package logic;

import client.ClientGUIController;
import common.Book;
import common.ClientServerMessage;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static client.ClientGUIController.loadFrameIntoPane;
import static client.ClientGUIController.navigateTo;
import static javafx.application.Application.launch;

public class SearchResultFrameController
{
    @FXML
    public TableView<List<String>> searchResultTable;
    @FXML
    public TableColumn<List<String>, String> bookNameColumn;
    @FXML
    public TableColumn<List<String>, String> genreColumn;
    @FXML
    public TableColumn<List<String>, String> watchDetailsColumn;
    @FXML
    public Button backButton;

    private final ObservableList<List<String>> BookList = FXCollections.observableArrayList();
    private final Property<ObservableList<List<String>>> BookListProperty = new SimpleObjectProperty<>(BookList);

    private static ArrayList<Book> books;
    public VBox SearchResultFrame;

    /**
     * initializes the table with the books that were found
     */
    @FXML
    public void initialize()
    {
        searchResultTable.itemsProperty().bind(BookListProperty);
        searchResultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        bookNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        genreColumn.setCellValueFactory((cellData -> new SimpleStringProperty(cellData.getValue().get(1))));
        watchDetailsColumn.setCellFactory(column -> new TableCell<List<String>, String>()
        {
            private final Button button = new Button("View Details");
            {
                // Set the button action
                button.setOnAction(event -> {
                    try
                    {
                        getBookCopy(getTableView().getItems().get(getIndex()));
                        loadFrameIntoPane((AnchorPane) SearchResultFrame.getParent(), "/gui/BookInfoFrame.fxml");
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

        //runs on the book list and adds each book to the table
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
        Book bk = null;
        for (Book book : books)
        {
            if (listFromRow.contains(book.getBookName()) && listFromRow.contains(book.getBookGenre()))
            {
                bk = book;
            }
        }

        if (bk != null)
        {
            ClientServerMessage searchMessage = new ClientServerMessage(206, bk.getBookSerialNumber());
            System.out.println(searchMessage.getMessageContent());
            try
            {
                ClientGUIController.chat.sendToServer(searchMessage);
            }
            catch (Exception e)
            {
                System.out.println("Error sending search message to server: " + e.getMessage());
            }
            BookInfoFrameController.setLocalBook(bk);
        }
    }

}