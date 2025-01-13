package client;

import common.ClientServerMessage;
import logic.ClientController;
import logic.ClientIPFrameController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ClientGUIController extends Application
{
    public static ChatClient chat;

    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Navigates to a specified FXML destination, optionally applying a CSS file and setting the title of the new stage.
     *
     * @param event        The ActionEvent triggered by the user's interaction (e.g., button click).
     * @param fxmlFilePath The path to the FXML file to load (e.g., "/gui/SearchHomePageFrame.fxml").
     * @param cssFilePath  The optional path to the CSS file to style the new view (e.g., "/gui/Subscriber.css"). Pass null if no CSS is required.
     * @param stageTitle   The title for the new stage window (e.g., "Profile Options").
     * @throws Exception   If the FXML file cannot be loaded or another error occurs during navigation.
     */
    public static void navigateTo(ActionEvent event, String fxmlFilePath, String cssFilePath, String stageTitle) throws Exception {
        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create a new FXMLLoader instance with the specified FXML file path
        FXMLLoader newLoader = new FXMLLoader(ClientGUIController.class.getResource(fxmlFilePath));

        // Update the static loader in ClientController
        ClientController.setLoader(newLoader);

        // Loads the new frame and calls its controller's initialize() method
        Parent newRoot = newLoader.load();


        // Optionally add a CSS file if provided
        if (cssFilePath != null && !cssFilePath.isEmpty())
        {
            if (ClientGUIController.class.getResource(cssFilePath) != null) {
                newRoot.getStylesheets().add(Objects.requireNonNull(ClientGUIController.class.getResource(cssFilePath)).toExternalForm());
            } else {
                System.out.println("CSS file not found: " + cssFilePath);
            }
        }

        // Update the root node of the existing scene
        currentStage.getScene().setRoot(newRoot);
        currentStage.setTitle(stageTitle);
        currentStage.setOnCloseRequest(event1 -> {
            // Run exitAction() in a background thread to prevent blocking
            Task<Void> logOutTask = new Task<Void>() {
                @Override
                protected Void call(){
                    exitAction();  // Call your logout action in the background
                    return null;
                }
            };
            // Start the background task
            new Thread(logOutTask).start();
        });

        // Show the updated stage
        currentStage.setResizable(true); // Enable window resizing
        currentStage.show();
    }

    /**
     *  The method is called when the program is closed, it sends a disconnect message to the server
     */
    private static void exitAction()
    {
        ClientServerMessage logOutMessage = new ClientServerMessage(102, null);
        try
        {
            ClientGUIController.chat.sendToServer(logOutMessage);
            System.exit(0);
        }
        catch (Exception e)
        {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }
    }

    /**
     * The start method is called when the program is run, it starts the ClientIPFrameController
     * @param primaryStage The stage to be displayed
     * @throws Exception   If an error occurs during the start
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        ClientIPFrameController aFrame = new ClientIPFrameController();
        aFrame.start(primaryStage);
    }
}