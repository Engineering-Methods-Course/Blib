package gui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ocsf.server.ConnectionToClient;

import java.util.*;

public class ServerMonitorFrameController {
    //
    private static int index = 1;
    private final ObservableList<List<String>> serverMonitorList = FXCollections.observableArrayList();
    private final Property<ObservableList<List<String>>> serverMonitorListProperty = new SimpleObjectProperty<>(serverMonitorList);
    private final Map<ConnectionToClient, Integer> clientMap = new HashMap<>();

    // FXML elements for the server monitor table
    @FXML
    private TableView<List<String>> serverMonitorTable;

    @FXML
    private TableColumn<List<String>, String> clientNum;

    @FXML
    private TableColumn<List<String>, String> ip;

    @FXML
    private TableColumn<List<String>, String> host;

    @FXML
    private TableColumn<List<String>, String> status;
    @FXML
    private Button monitorButton;

    /**
     * This method initializes the server monitor
     */
    @FXML
    private void initialize() {
        serverMonitorTable.itemsProperty().bind(serverMonitorListProperty);


        clientNum.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        ip.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        host.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
    }

    /**
     * This method gets the monitor button
     *
     * @param event The action event triggered by clicking the monitor button
     *
     * @throws Exception If there is an issue with the navigation
     */
    public void getExitBtn(ActionEvent event) throws Exception {
        System.out.println("Exit Blib server");
        System.exit(0);
    }

    /**
     * This method adds a row to the server monitor
     * Contains the host, ip, and status of the client
     *
     * @param host The host name of the client
     * @param ip  The ip address of the client
     */
    private void addRow(String host, String ip) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(index++));
        list.add(ip);
        list.add(host);
        list.add("Connected");

        this.serverMonitorList.add(list);

        System.out.println(this.serverMonitorList);
    }

    /**
     * This method adds a client to the server monitor
     *
     * @param client The client to be added
     */
    public void clientConnected(ConnectionToClient client) {
        try {
            if (clientMap.containsKey(client)) {
                int index = clientMap.get(client) - 1;
                List<String> clientInfo = serverMonitorList.get(index);
                clientInfo.set(3, "Connected");
                serverMonitorList.set(index, clientInfo); // Update the list to reflect the change
            } else {
                clientMap.put(client, index);
                addRow(Objects.requireNonNull(client.getInetAddress()).getHostName(), client.getInetAddress().getHostAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method removes a client from the server monitor
     *
     * @param client The client to be removed
     */
    public void clientDisconnected(ConnectionToClient client) {
        int index = clientMap.get(client) - 1; // Adjust for zero-based index
        List<String> clientInfo = serverMonitorList.get(index);
        clientInfo.set(3, "Disconnected");
        serverMonitorList.set(index, clientInfo); // Update the list to reflect the change
        clientMap.remove(client);
    }


}