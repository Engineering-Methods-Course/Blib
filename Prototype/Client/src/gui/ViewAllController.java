package gui;

import common.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

import static client.ClientUI.navigateTo;

public class ViewAllController {

    private static ArrayList<Subscriber> subscribers;

    private final ObservableList<Subscriber> subscriberList = FXCollections.observableArrayList();


    // FXML elements for the subscriber table
    @FXML
    private TableView<Subscriber> subscriberTable;

    @FXML
    private TableColumn<Subscriber, String> idColumn;
    @FXML
    private TableColumn<Subscriber, String> firstNameColumn;
    @FXML
    private TableColumn<Subscriber, String> lastNameColumn;
    @FXML
    private TableColumn<Subscriber, String> phoneNumberColumn;
    @FXML
    private TableColumn<Subscriber, String> emailColumn;
    @FXML
    private TableColumn<Subscriber, String> passwordColumn;
    @FXML
    private TableColumn<Subscriber, String> statusColumn;
    @FXML
    private TableColumn<Subscriber, String> historyColumn;

    @FXML
    private void initialize() {
        loadSubscribersIntoTable();

    }
    public static void setSubscribers(ArrayList<Subscriber> subscribersFromServer) {
        subscribers = subscribersFromServer;
    }

    private void loadSubscribersIntoTable() {
        // Ensure the subscriber list is not null
        if (subscribers != null) {
            // Add all subscribers to the ObservableList
            subscriberList.setAll(subscribers);

            // Set up the cell value factories for each column
            idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getID())));
            firstNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFirstName()));
            lastNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLastName()));
            phoneNumberColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhoneNumber()));
            emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
            passwordColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPassword()));
            statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(getFrozenMessage(cellData.getValue().getStatusIsFrozen())));
            historyColumn.setCellFactory(column -> new TableCell<Subscriber, String>() {
                        private final Button button = new Button("Watch History");
                        {
                            button.setOnAction(event -> {
                            });
                        }
                    });

            // Assign the ObservableList to the TableView
            subscriberTable.setItems(subscriberList);
        } else {
            System.out.println("Subscribers list is null!");
        }
    }
    private String getFrozenMessage(Boolean isFrozen) {
        if (isFrozen) {
            return "Frozen";
        }
        return "Active";
    }
    public void clickBack(ActionEvent actionEvent) throws Exception{
        navigateTo(actionEvent, "/gui/LoginFrame.fxml", "/gui/Subscriber.css", "Login");
    }
}
