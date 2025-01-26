package logic;

import common.Book;
import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.ClientGUIController;

import java.util.ArrayList;

import static main.ClientGUIController.loadFrameIntoPane;

public class BookInfoFrameController {
    public static boolean orderComplete = false;
    //other controller attributes
    private static Book localBook = null;
    private static ArrayList<String> Availability;
    // FXML attributes
    @FXML
    public Text bookName;
    @FXML
    public Text bookGenre;
    @FXML
    public Text bookdescriptionTxt;
    @FXML
    public Text bookLocation;
    @FXML
    public Text bookAvailCopyNum;
    @FXML
    public Button orderBookButton;
    @FXML
    public Button backButton;
    @FXML
    public Label TextForAvailability;
    @FXML
    public VBox bookInfoFrame;

    /**
     * This method sets the local book object
     *
     * @param bookFromServer The book object to set
     */
    public static void setLocalBook(Book bookFromServer) {
        localBook = bookFromServer;
    }

    /**
     * This method sets the availability of the book
     *
     * @param arrList The availability of the book
     */
    public static void setAvailability(ArrayList<String> arrList) {
        Availability = arrList;
    }

    /**
     * This method initializes the Book Info Frame
     */
    public void initialize() {
        /*
         * Set the text according to the book that is being viewed
         */
        bookName.setText(localBook.getBookName());
        bookGenre.setText(localBook.getBookGenre());
        bookdescriptionTxt.setText(localBook.getBookDescription());

        /*
         * check if the book is available or not and sets its location or closest return copy date accordingly
         */
        if (Availability.get(0).equals("true")) {
            TextForAvailability.setText("Book Location: ");
            bookLocation.setText(Availability.get(1));
        } else {
            TextForAvailability.setText("Closest availability date: ");
            bookLocation.setText(Availability.get(1));
        }

        /*
         * if the user that views the book is subscriber allow him to view order button, other than him hide it
         */

        System.out.println(Availability.get(0));
        if (Librarian.getLocalLibrarian() != null || Subscriber.getLocalSubscriber() == null || Availability.get(0).equals("true")) {
            orderBookButton.setDisable(true);
            orderBookButton.setVisible(false);
        }
    }

    /**
     * This method handles the orderBookButton click event to order the book
     *
     * @throws Exception If there is an issue with the navigation
     */
    public void orderBookButtonClicked() throws Exception {
        ArrayList<String> content = new ArrayList<>();
        /*
         * Adds to the content of message the subscriber id and the serial number of the book
         */
        content.add(String.valueOf(Subscriber.getLocalSubscriber().getID()));
        content.add(String.valueOf(localBook.getBookSerialNumber()));

        /*
         * Create a new message to send to the server
         */
        ClientServerMessage searchMessage = new ClientServerMessage(208, content);

        /*
         * Send the message to the server
         */
        ClientGUIController.chat.sendToServer(searchMessage);

        /*
         * If the order action was completed, move back to the search home page
         */
        if (orderComplete) {
            loadFrameIntoPane((AnchorPane) bookInfoFrame.getParent(), "/gui/SearchPageFrame.fxml");
        }
    }

    /**
     * This method handles the backButton click event to return to the previous page
     *
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked() throws Exception {
        loadFrameIntoPane((AnchorPane) bookInfoFrame.getParent(), "/gui/SearchResultFrame.fxml");
    }
}