package gui.controllers;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ocsf.server.ConnectionToClient;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.URL;
import java.util.*;

public class ServerMonitorFrameController {
    //
    private static int index = 1;
    private final ObservableList<List<String>> serverMonitorList = FXCollections.observableArrayList();
    private final Property<ObservableList<List<String>>> serverMonitorListProperty = new SimpleObjectProperty<>(serverMonitorList);
    private final Map<ConnectionToClient, Integer> clientMap = new HashMap<>();
    public TextArea consoleTextArea;
    public Label LocalIP;
    public Label ExternalIP;

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
        serverMonitorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        clientNum.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        ip.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        host.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));


        Console console = new Console(consoleTextArea);
        PrintStream ps = new PrintStream(console, true);
        System.setOut(ps);
        System.setErr(ps);

        // Get the local and external IP addresses
        try (final DatagramSocket socket = new DatagramSocket())
        {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            LocalIP.setText("Server IP: " + socket.getLocalAddress().getHostAddress());
        }
        catch (Exception ignored)
        {
        }
        try {
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = br.readLine();
            ExternalIP.setText("External IP: " + ip);
        } catch (Exception ignored)
        {
        }


    }

    /**
     * This method adds a row to the server monitor
     * Contains the host, ip, and status of the client
     *
     * @param host The host name of the client
     * @param ip   The ip address of the client
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
        clientMap.put(client, index);
        addRow(Objects.requireNonNull(client.getInetAddress()).getHostName(), client.getInetAddress().getHostAddress());
        System.out.println("Client connected: " + client.getInetAddress().getHostName());

    }

    /**
     * clientDisconnected
     * This method removes a client from the server monitor
     *
     * @param client The client to be removed
     */
    public void clientDisconnected(ConnectionToClient client) {
        int clientIndex = clientMap.get(client); // Adjust for zero-based index
        for (List<String> connectedClient : serverMonitorList) {
            int currentIndex = Integer.parseInt(connectedClient.get(0));
            if (currentIndex == clientIndex) {
                serverMonitorList.remove(connectedClient);
                break;
            }
        }
        // Adjust the index of the remaining clients
        for (List<String> connectedClient : serverMonitorList) {
            int currentIndex = Integer.parseInt(connectedClient.get(0));
            if (currentIndex > clientIndex) {
                connectedClient.set(0, String.valueOf(currentIndex - 1));
            }
        }
        clientMap.remove(client);
        index--;
    }

    /**
     * Inner class to redirect System.out and System.err to the console
     */
    public static class Console extends OutputStream {
        private final TextArea console;
        private final StringBuilder buffer = new StringBuilder();

        /**
         * Constructor for the Console class
         *
         * @param console The TextArea to redirect the output to
         */
        public Console(TextArea console) {
            this.console = console;
        }

        /**
         * Writes the output to the console
         *
         * @param b The byte to write
         * @throws IOException If there is an issue writing the byte
         */
        @Override
        public void write(int b) throws IOException {
            buffer.append((char) b);
            updateConsole();
        }

        /**
         * Writes the output to the console
         *
         * @param b   The byte array to write
         * @param off The offset to start writing
         * @param len The length of the byte array
         * @throws IOException If there is an issue writing the byte
         */
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            buffer.append(new String(b, off, len));
            updateConsole();
        }

        /**
         * Updates the console with the output
         */
        private void updateConsole() {
            Platform.runLater(() -> {
                console.appendText(buffer.toString());
                buffer.setLength(0);
            });
        }
    }
}