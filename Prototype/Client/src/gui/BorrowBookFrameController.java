package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

import static client.ClientGUIController.navigateTo;

public class BorrowBookFrameController
{
    @FXML
    public TextField bookIDTextField;
    @FXML
    public TextField subscriberIDTextField;
    @FXML
    public DatePicker borrowDatePicker;
    @FXML
    public DatePicker returnDatePicker;
    @FXML
    public Button backButton;
    @FXML
    public Button borrowButton;

    /**
     *  This method is called when the borrow button is clicked
     */
    public void initialize()
    {
        borrowDatePicker.setValue(LocalDate.now());
        returnDatePicker.setValue(LocalDate.now().plusDays(14));
    }

    /**
     * This method is called when the borrow button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "LibrarianProfileFrame.fxml", "Librarian.css", "Librarian Profile");
    }

    /**
     * This method is called when the borrow button is clicked
     * @param event      The event that triggered this method
     * @throws Exception If an error occurs during navigation
     */
    public void borrowButtonClicked(ActionEvent event) throws Exception
    {
        //todo: borrow the book and send a message to the server with the book information
    }
}
