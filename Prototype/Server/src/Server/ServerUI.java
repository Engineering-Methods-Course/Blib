package Server;

import gui.ServerMonitorFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import gui.ServerPortFrameController;
/**
 * This class is the server UI
 */
public class ServerUI extends Application {
    final public static int DEFAULT_PORT = 5555;

    /**
     * main method
     *
     * @param args the command line arguments
     * @throws Exception if an error occurs
     */
    public static void main(String args[]) throws Exception {
        launch(args);
    } // end main

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
            //Set port to 5555
            port = Integer.parseInt(p);

        } catch (Throwable t) {
            System.out.println("ERROR - Could not connect!");
        }

        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();

        Pane root = loader.load(ServerUI.class.getResource("/gui/ServerMonitor.fxml").openStream());
        ServerMonitorFrameController serverMonitorController = loader.getController();

        Scene scene = new Scene(root);
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