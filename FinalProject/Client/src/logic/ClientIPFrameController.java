package logic;

import client.ChatClient;
import client.ClientGUIController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

import static client.ClientGUIController.navigateTo;

public class ClientIPFrameController
{
    // FXML attributes
    @FXML
    private TextField txtIP;
    @FXML
    private TextField txtPort;

    /**
     * This method initializes the Client IP Frame
     *
     * @param primaryStage The primary stage of the application
     * @throws Exception If there is an issue with the navigation
     */
    public void start(Stage primaryStage) throws Exception
    {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/ClientIPFrame.fxml")));
        Parent root = loader.load();

        // Set the scene
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/MainFrame.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Enter Server Details");
        primaryStage.setResizable(false); // Disable window resizing
        primaryStage.show();
    }

    /**
     * Handle the Enter button click event.
     *
     * @param event The action event triggered by clicking the Enter button
     * @throws Exception If there is an issue with the navigation
     */
    @FXML
    public void clickEnterButton(ActionEvent event) throws Exception
    {
        // Get the IP address and port from the text fields
        String ipAddress = txtIP.getText().trim();
        String portText = txtPort.getText().trim();

        // Check if the IP address and port are empty
        if (ipAddress.isEmpty() || portText.isEmpty())
        {
            System.out.println("Both IP address and Port must be entered.");
            return;
        }

        try
        {
            // Check if the port is a valid number
            int port = Integer.parseInt(portText);

            // Initialize the chat client
            ClientGUIController.chat = new ChatClient(ipAddress, port, new FXMLLoader());

            // Navigate to the main frame
            navigateTo(event, "/gui/MainFrame.fxml", "/gui/MainFrame.css", "Home Page");
        }
        // Handle the case where the port is not a valid number
        catch (NumberFormatException e)
        {
            System.out.println("Port must be a valid number.");
            throw e;
        }
        // Handle the case where the client cannot be initialized
        catch (Exception e)
        {
            System.out.println("Failed to initialize client: " + e.getMessage());
            throw e;
        }
    }
}