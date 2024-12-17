package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Server.ClientInfo;

public class ServerConnectedClientsController {

    @FXML
    private TableView<ClientInfo> clientsTable;

    @FXML
    private TableColumn<ClientInfo, String> column1;

    @FXML
    private TableColumn<ClientInfo, String> column2;

    @FXML
    private TableColumn<ClientInfo, String> column3;

    @FXML
    private Label clientCountLabel;

   private ObservableList<ClientInfo> clientList;

    public void initialize() {
        clientList = FXCollections.observableArrayList();
        //clientsTable.setItems(clientList);

       // column1.setCellValueFactory(new PropertyValueFactory<>("column1"));
       // column2.setCellValueFactory(new PropertyValueFactory<>("column2"));
//column3.setCellValueFactory(new PropertyValueFactory<>("column3"));
    }

    public void updateClientList(ObservableList<ClientInfo> newClientList) {
        clientList.setAll(newClientList);
        clientCountLabel.setText("Connected Clients: " + clientList.size());
    }

    @FXML
    private void handleExitButton() {
        System.out.println("Closing Server");
        System.exit(0);
    }
}