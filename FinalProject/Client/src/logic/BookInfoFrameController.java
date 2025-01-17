package logic;

import client.ClientGUIController;
import common.Book;
import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.util.ArrayList;

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
    @FXML
    public Text TextForAvailability;

    /**
     * This method initializes the Book Info Frame
     */
    public void initialize()
    {
        //loadBookInfo();
        bookName.setText(localBook.getBookName());
        bookGenre.setText(localBook.getBookGenre());
        bookdescription.setText(localBook.getBookDescription());
        if (Availability.get(0).equals("true"))
        {
            TextForAvailability.setText("Book Location: ");
            bookLocation.setText(Availability.get(1));
        }
        else
        {
            TextForAvailability.setText("Closest Date: ");
            bookLocation.setText(Availability.get(1));
        }

        if (Subscriber.getLocalSubscriber() == null && Librarian.getLocalLibrarian() == null || Availability.get(0).equals("true"))
        {
            orderBookButton.setDisable(false);
            orderBookButton.setVisible(false);
        }
    }

    private static Book localBook = null;
    private static ArrayList<String> Availability;
    public static boolean orderComplete = false;

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
     * This method gets the availability of the book
     *
     * @return The availability of the book
     */
    public static ArrayList<String> getAvailability()
    {
        return Availability;
    }

    /**
     * This method sets the availability of the book
     *
     * @param arrList The availability of the book
     */
    public static void setAvailability(ArrayList<String> arrList)
    {
        Availability = arrList;
    }

    /**
     * This method handles the orderBookButton click event to order the book
     *
     * @param event The ActionEvent triggered by the user's interaction (e.g., button click).
     * @throws Exception If there is an issue with the navigation
     */
    public void orderBookButtonClicked(ActionEvent event) throws Exception
    {
        ArrayList<String> content = new ArrayList<>();
        content.add(String.valueOf(Subscriber.getLocalSubscriber().getID()));
        content.add(String.valueOf(localBook.getBookSerialNumber()));

        ClientServerMessage searchMessage = new ClientServerMessage(208, content);

        try
        {
            ClientGUIController.chat.sendToServer(searchMessage);
        }
        catch (Exception e)
        {
            System.out.println("Error sending search message to server: " + e.getMessage());
        }

        if (orderComplete)
        {
            System.out.println(orderComplete);
            navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
        }
    }

    /**
     * This method handles the backButton click event to return to the previous page
     *
     * @param event The ActionEvent triggered by the user's interaction (e.g., button click).
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SearchResultFrame.fxml", "/gui/Subscriber.css", "Search Results");
    }

    /**
     * This method loads the book information
     */
    private void loadBookInfo()
    {
        bookName.setText(localBook.getBookName());
        bookGenre.setText(localBook.getBookGenre());
        bookdescription.setText(localBook.getBookDescription());
        bookAvailCopyNum.setText(String.valueOf(localBook.getBookCopyNum()));
    }
}