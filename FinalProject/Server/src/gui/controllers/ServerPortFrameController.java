package gui.controllers;

import main.ServerGUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class ServerPortFrameController {


    String temp = "";
    ObservableList<String> list;
    //FXML elements
    @FXML
    private Button btnDone = null;
    @FXML
    private Label lbllist;
    @FXML
    private TextField portxt;

    /**
     * This method gets the port number
     *
     * @return The port number
     */
    private String getPort() {
        return portxt.getText();
    }

    /**
     * This method handles the Done button click event.
     * It starts the server and hides the port window.
     *
     * @param event The action event triggered by clicking the Done button
     * @throws Exception If there is an issue with starting the server
     */
    public void Done(ActionEvent event) throws Exception {
        String p;

        p = getPort();
        if (p.trim().isEmpty()) {
            System.out.println("You must enter a port number");

        } else {
            ServerGUI.runServer(p);

            // hide port window
            ((Node) event.getSource()).getScene().getWindow().hide();
        }
    }

    /**
     * This method handles the Exit button click event.
     * It exits the server.
     *
     * @param primaryStage The stage of the server
     * @throws Exception If there is an issue with exiting the server
     */
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/fxml/ServerPortFrame.fxml")));

        Scene scene = new Scene(root);

        // Set default values for the text fields
        portxt = (TextField) root.lookup("#portxt");
        portxt.setText("5555");

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/css/Server.css")).toExternalForm());
        primaryStage.setTitle("Set Port Number");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Disable window resizing

        primaryStage.show();
    }

}