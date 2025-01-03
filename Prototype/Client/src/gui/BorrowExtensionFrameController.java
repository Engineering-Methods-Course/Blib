package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import static client.ClientGUIController.navigateTo;

public class BorrowExtensionFrameController
{

    @FXML
    public Button backButton;
    @FXML
    public TableView extensionsTable;
    @FXML
    public TableColumn bookNameColumn;
    @FXML
    public TableColumn authorColumn;
    @FXML
    public TableColumn descriptionColumn;
    @FXML
    public TableColumn extendButtonColumn;

    /**
     * This method initializes the Borrow Extension Frame
     */
    public void initialize()
    {
        //todo: implement loading of the borrowed books
        //todo: generating the extension buttons
        //todo: connect the borrow extension button to the extendButtonClicked method
    }

    /**
     * This method handles the backButton click event to navigate back to the previous frame
     * @param event      The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "My Profile");
    }

    /**
     * This method handles the extendButton click event to extend the book borrowing period
     * @param event      The action event triggered by clicking the extend button
     * @throws Exception If there is an issue with the navigation
     */
    public void extendButtonClicked(ActionEvent event) throws Exception
    {
        //todo: implement the extension mechanics
    }
}
