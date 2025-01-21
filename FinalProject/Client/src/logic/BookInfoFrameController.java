package logic;

import client.ClientGUIController;
import common.Book;
import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.ArrayList;

import static client.ClientGUIController.loadFrameIntoPane;

public class BookInfoFrameController
{
    // FXML attributes
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
    @FXML
    public VBox bookInfoFrame;

    //other controller attributes
    private static Book localBook = null;
    private static ArrayList<String> Availability;
    public static boolean orderComplete = false;

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
            orderBookButton.setDisable(true);
            orderBookButton.setVisible(false);
        }
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
     * @throws Exception If there is an issue with the navigation
     */
    public void orderBookButtonClicked() throws Exception
    {
        ArrayList<String> content = new ArrayList<>();
        content.add(String.valueOf(Subscriber.getLocalSubscriber().getID()));
        content.add(String.valueOf(localBook.getBookSerialNumber()));

        // Create a new message to send to the server
        ClientServerMessage searchMessage = new ClientServerMessage(208, content);

        // Send the message to the server
        ClientGUIController.chat.sendToServer(searchMessage);

        if (orderComplete)
        {
            System.out.println(orderComplete);
            loadFrameIntoPane((AnchorPane) bookInfoFrame.getParent(), "/gui/SearchPageFrame.fxml");
        }
    }

    /**
     * This method handles the backButton click event to return to the previous page
     *
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked() throws Exception
    {
        loadFrameIntoPane((AnchorPane) bookInfoFrame.getParent(), "/gui/SearchResultFrame.fxml");
    }
}