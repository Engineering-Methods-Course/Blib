package gui;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static gui.SubscriberWelcomeFrameController.navigateTo;

public class ClientIPFrameController {

    @FXML
    private TextField txtIP;  // Reference to the IP address field

    @FXML
    private TextField txtPort;  // Reference to the port field

    @FXML
    private Button btnEnter;  // Reference to the Enter button

    @FXML
    private Button btnExit;  // Reference to the Exit button

    /**
     * Start the application with the ClientIPFrame as the first page.
     */
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/EnterClientIPFrame.fxml")));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/Subscriber.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Enter Server Details");
        primaryStage.show();
    }

    /**
     * Handle the Enter button click event.
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
            ClientUI.chat=new ClientController(ipAddress, port);
            // Navigate to the next frame
            navigateTo(event, "/gui/LoginFrame.fxml", "/gui/Subscriber.css", "Login");
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
     */
    @FXML
    public void clickExitButton(ActionEvent event) {
        System.exit(0);
    }


}
