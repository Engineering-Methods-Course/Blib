package gui;

import client.ChatClient;
import client.ClientGUIController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

import static client.ClientGUIController.navigateTo;

public class PrototypeClientIPFrameController {

    @FXML
    private TextField txtIP;
    @FXML
    private TextField txtPort;
    @FXML
    private Button btnEnter;
    @FXML
    private Button btnExit;

    /**
     * Start the application with the ClientIPFrame as the first page.
     *
     * @param primaryStage The primary stage for the application
     */
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/PrototypeEnterClientIPFrame.fxml")));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/Subscriber.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Enter Server Details");
        primaryStage.setResizable(false); // Disable window resizing
        primaryStage.show();
    }

    /**
     * Handle the Enter button click event.
     *
     * @param event The action event triggered by clicking the Enter button
     */
    @FXML
    public void clickEnterButton(ActionEvent event) throws Exception {
        String ipAddress = txtIP.getText().trim();
        String portText = txtPort.getText().trim();

        // Check if either field is empty
        if (ipAddress.isEmpty() || portText.isEmpty()) {
            System.out.println("Both IP address and Port must be entered.");
            return;
        }

        // Proceed with the entered IP and Port
        try {
            int port = Integer.parseInt(portText);
            ClientGUIController.chat = new ChatClient(ipAddress, port);
            // Navigate to the next frame
            navigateTo(event, "/gui/SubscriberLoginFrame.fxml", "/gui/Subscriber.css", "Login");
        } catch (NumberFormatException e) {
            System.out.println("Port must be a valid number.");
            throw e;

        } catch (Exception e) {
            System.out.println("Failed to initialize client: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Handle the Exit button click event.
     *
     * @param event The action event triggered by clicking the Exit button
     */
    @FXML
    public void clickExitButton(ActionEvent event) throws Exception {
        System.exit(0);
    }


}
