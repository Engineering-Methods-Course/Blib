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

import java.util.ArrayList;
import java.util.List;

import static client.ClientGUIController.navigateTo;

public class SearchResultFrameController
{
    @FXML
    public TableView<List<String>>  searchResultTable;
    @FXML
    public TableColumn<List<String>,String> bookNameColumn;
    @FXML
    public TableColumn<List<String>,String> genreColumn;
    //@FXML
    //public TableColumn locationColumn;
    @FXML
    public TableColumn<List<String>,String> watchDetailsColumn;
    @FXML
    public Button backButton;

    private final ObservableList<List<String>> BookList = FXCollections.observableArrayList();
    private final Property<ObservableList<List<String>>> BookListProperty = new SimpleObjectProperty<>(BookList);

    private static ArrayList<Book> books;

    /**
     * initializes the table with the books that were found
     */
    @FXML
    public void initialize()
    {
        //creates the table in order to load the results into it
        searchResultTable.itemsProperty().bind(BookListProperty);
        searchResultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bookNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        genreColumn.setCellValueFactory((cellData -> new SimpleStringProperty(cellData.getValue().get(1))));
        watchDetailsColumn.setCellFactory(column -> new TableCell<List<String>, String>() {
            private final Button button = new Button("View Details"); //create a button inorder to view if the book can be ordered
            {
                // Set the button action
                button.setOnAction(event -> {
                    try {
                        getBookCopy(getTableView().getItems().get(getIndex()));
                        navigateTo(event, "/gui/BookInfoFrame.fxml","/gui/Subscriber.css", "Book information");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            // Display button if the row is not empty
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty ||  getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        //runs on the book list and adds each book to the table
        for(Book book : books)
        {
            List<String> row = new ArrayList<>();
            row.add(book.getBookName());
            row.add(book.getBookGenre());
            this.BookList.add(row);
        }
    }

    /**
     * Sets the book list to the input
     * @param bookslst - the book list we want to set it to
     */
    public static void setBookArray(ArrayList<Book> bookslst)
    {
        books = bookslst;
    }

    /**
     *Method for setting a book for BookInfoFrameController window
     *
     * @param listFromRow -The book we want to load
     */
    private void getBookCopy(List<String> listFromRow)
    {
        Book bk=null;
        //search for the book we want to load
        for(Book book : books)
        {
            if(listFromRow.contains(book.getBookName()) && listFromRow.contains(book.getBookGenre()))
            {
                bk=book;
            }
        }

        //enter as long as the book exists in the book list
        if(bk!=null)
        {
            ClientServerMessage searchMessage = new ClientServerMessage(206,bk.getBookSerialNumber());
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

    /**
     * This method handles the backButton click event to navigate back to the previous frame
     * @param event      The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
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
     * @param event      The action event triggered by clicking the watch details button
     * @throws Exception If there is an issue with the navigation
     */
    public void watchDetailsButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/BookInfoFrame.fxml", "/gui/Subscriber.css", "Book Info");
    }
}