package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    public void initialize()
    {
        //todo: implement
    }

    public void backButtonClicked(ActionEvent actionEvent)
    {
        //todo: navigate back to the previous frame
    }

    public void extendButtonClicked(ActionEvent actionEvent)
    {
        //todo: implement
    }
}
