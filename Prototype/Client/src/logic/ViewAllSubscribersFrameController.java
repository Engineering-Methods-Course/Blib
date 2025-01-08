package logic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import static client.ClientGUIController.navigateTo;

public class ViewAllSubscribersFrameController
{
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TextField filterTextField;
    @FXML
    private TableView<?> subscriberTable;
    @FXML
    private Button backButton;

    /**
     * Handles the Apply Filter button click event.
     * This will allow users to apply the filter based on the selected option.
     * Currently, the filter logic is not implemented.
     *
     * @param actionEvent The ActionEvent triggered by clicking the Apply Filter button.
     */
    public void applyFilter(ActionEvent actionEvent)
    {
        //todo:add sort option by id

    }

    /**
     * Handles the Filter button click event.
     * This method will apply the filter logic when the user clicks the filter button.
     * Currently, the filter logic is not implemented.
     *
     * @param actionEvent The ActionEvent triggered by clicking the Filter button.
     */
    public void onFilterButtonClicked(ActionEvent actionEvent)
    {
        //todo: add applying filter
    }

    /**
     * Handles the Back button click event.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "All Subscribers");
    }
}