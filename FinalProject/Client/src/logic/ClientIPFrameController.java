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
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/ClientIPFrame.fxml")));
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
     * @throws Exception If there is an issue with the navigation
     */
    @FXML
    public void clickEnterButton(ActionEvent event) throws Exception
    {
        String ipAddress = txtIP.getText().trim();
        String portText = txtPort.getText().trim();

        if (ipAddress.isEmpty() || portText.isEmpty())
        {
            System.out.println("Both IP address and Port must be entered.");
            return;
        }

        try
        {
            int port = Integer.parseInt(portText);

            ClientGUIController.chat = new ChatClient(ipAddress, port, new FXMLLoader());

            navigateTo(event, "/gui/SearchHomePageFrame.fxml", "/gui/Subscriber.css", "Home Page");
        }
        catch (NumberFormatException e)
        {
            System.out.println("Port must be a valid number.");
            throw e;
        }
        catch (Exception e)
        {
            System.out.println("Failed to initialize client: " + e.getMessage());
            throw e;
        }
    }
}