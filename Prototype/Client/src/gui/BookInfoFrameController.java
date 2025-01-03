package gui;

import common.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import static client.ClientGUIController.navigateTo;

public class BookInfoFrameController
{

    @FXML
    public Text bookName;
    @FXML
    public Text bookGenre;
    @FXML
    public TextArea bookdescription;
    @FXML
    public Text bookLocation;
    @FXML
    public Text bookAvailCopyNum;
    @FXML
    public Button orderBookButton;
    @FXML
    public Button backButton;

    private static Book localBook = null;

    /**
     * This method gets the local book object
     *
     * @return The local book object
     */
    public static Book getLocalBook()
    {
        return localBook;
    }

    /**
     * This method sets the local book object
     *
     * @param bookFromServer The book object to set
     */
    public static void setLocalBook(Book bookFromServer)
    {
        localBook = bookFromServer;
    }

    /**
     * This method initializes the Book Info Frame
     */
    public void initialize()
    {
        loadBookInfo();
    }

    /**
     * This method handles the orderBookButton click event to order the book
     * @param event      The ActionEvent triggered by the user's interaction (e.g., button click).
     * @throws Exception If there is an issue with the navigation
     */
    public void orderBookButtonClicked(ActionEvent event) throws Exception
    {
        //todo: implement the order book system

        navigateTo(event, "/gui/SearchResultsFrame.fxml", "/gui/Subscriber.css", "Search Results");
    }

    /**
     * This method handles the backButton click event to return to the previous page
     * @param event      The ActionEvent triggered by the user's interaction (e.g., button click).
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchResultsFrame.fxml", "/gui/Subscriber.css", "Search Results");
    }

    private void loadBookInfo()
    {
        //todo: implement loadBookInfo

        bookName.setText(localBook.getBookName());
        bookGenre.setText(localBook.getBookGenre());
        bookdescription.setText(localBook.getBookDescription());
        bookAvailCopyNum.setText(String.valueOf(localBook.getBookCopyNum()));
    }

}
