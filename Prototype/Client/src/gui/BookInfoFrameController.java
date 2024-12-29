package gui;

import common.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

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
     */
    public void orderBookButtonClicked()
    {
        //todo: implement the orderBookButton click event to order the book
    }

    /**
     * This method handles the backBookButton click event to navigate to the previous page
     */
    public void backButtonClicked()
    {
        //todo: navigate back to the previous page
    }

    //todo: implement loadBookInfo
    private void loadBookInfo()
    {

        bookName.setText(localBook.getBookName());
        bookGenre.setText(localBook.getBookGenre());
        bookdescription.setText(localBook.getBookDescription());
        bookAvailCopyNum.setText(String.valueOf(localBook.getBookCopyNum()));
    }

}
