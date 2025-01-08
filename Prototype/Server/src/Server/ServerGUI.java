package Server;

import common.ClientServerMessage;
import logic.ServerController;
import logic.ServerMonitorFrameController;
import logic.ServerPortFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class is the server UI
 */
public class ServerGUI extends Application {
    /**
     * main method
     *
     * @param args the command line arguments
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    /**
     * This method runs the server
     *
     * @param p the port number
     * @throws IOException if an error occurs
     */
    public static void runServer(String p) throws IOException {
        //Port to listen on
        int port = 0;

        try {
            port = Integer.parseInt(p);

        } catch (Throwable t) {
            System.out.println("ERROR - Could not connect!");
        }

        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();

        Pane root = loader.load(ServerGUI.class.getResource("/gui/ServerMonitorFrame.fxml").openStream());
        ServerMonitorFrameController serverMonitorController = loader.getController();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(ServerGUI.class.getResource("/gui/Server.css")).toExternalForm());
        primaryStage.setTitle("Server Monitor");

        primaryStage.setScene(scene);
        primaryStage.show();

        ServerController sv = new ServerController(port, serverMonitorController);

        try {
            //Start listening for connections
            sv.listen();
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
        primaryStage.setOnCloseRequest(event -> {
            try {
                sv.sendMessagesToAllClients(new ClientServerMessage(999, null));
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method starts the server
     *
     * @param primaryStage the stage
     * @throws Exception if an error occurs
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerPortFrameController aFrame = new ServerPortFrameController(); // create StudentFrame
        aFrame.start(primaryStage);
    }
}