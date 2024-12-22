package gui;

import common.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

import static client.ClientGUIController.navigateTo;

public class PrototypeViewAllController {

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

    /**
     * This method sets the subscribers list
     *
     * @param subscribersFromServer The list of subscribers to set
     */
    public static void setSubscribers(ArrayList<Subscriber> subscribersFromServer) {
        subscribers = subscribersFromServer;
    }

    @FXML
    private void initialize() {
        loadSubscribersIntoTable();

    }

    /**
     * This method loads the subscribers into the table
     */
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
                    // Set the button action
                    button.setOnAction(event -> {
                        // Handle button action here, e.g., show subscriber's history
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Will be implemented in the future");
                        alert.setTitle("Watch History");
                        alert.showAndWait();
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);
                    }
                }
            });
            // Assign the ObservableList to the TableView
            subscriberTable.setItems(subscriberList);
        } else {
            System.out.println("Subscribers list is null!");
        }
    }

    /**
     * This method returns a string indicating whether the subscriber is frozen or active
     *
     * @param isFrozen The status of the subscriber
     * @return The string indicating the status
     */
    private String getFrozenMessage(Boolean isFrozen) {
        if (isFrozen) {
            return "Frozen";
        }
        return "Active";
    }

    /**
     * This method handles the back button click event.
     * It navigates to the previous screen (SubscriberLoginFrame.fxml).
     *
     * @param actionEvent The action event triggered by clicking the back button
     * @throws Exception If there is an issue with the navigation
     */
    public void clickBack(ActionEvent actionEvent) throws Exception {
        navigateTo(actionEvent, "/gui/SubscriberLoginFrame.fxml", "/gui/Subscriber.css", "Login");
    }
}
