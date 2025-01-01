package client;

import common.ClientServerMessage;
import gui.ClientIPFrameController;
import gui.PrototypeSubscriberWelcomeFrameController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Objects;


public class ClientGUIController extends Application
{
    public static ChatClient chat; //only one instance

    public static void main(String[] args) throws Exception
    {
        launch(args);
    } // end main

    /**
     * Navigates to a specified FXML destination, optionally applying a CSS file and setting the title of the new stage.
     *
     * @param event           The ActionEvent triggered by the user's interaction (e.g., button click).
     * @param fxmlDestination The path to the FXML file for the destination view (e.g., "/GUI/SubscriberProfileOptions.fxml").
     * @param cssFilePath     The optional path to the CSS file to style the new view (e.g., "/GUI/Subscriber.css"). Pass null if no CSS is required.
     * @param stageTitle      The title for the new stage window (e.g., "Profile Options").
     * @throws Exception If the FXML file cannot be loaded or another error occurs during navigation.
     */
    public static void navigateTo(ActionEvent event, String fxmlDestination, String cssFilePath, String stageTitle) throws Exception
    {
        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the destination FXML
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(PrototypeSubscriberWelcomeFrameController.class.getResource(fxmlDestination)));

        // Optionally add a CSS file if provided
        if (cssFilePath != null && !cssFilePath.isEmpty()) {
            newRoot.getStylesheets().add(Objects.requireNonNull(PrototypeSubscriberWelcomeFrameController.class.getResource(cssFilePath)).toExternalForm());
        }

        // Update the root node of the existing scene
        currentStage.getScene().setRoot(newRoot);
        currentStage.setTitle(stageTitle);
        currentStage.setOnCloseRequest(event1 -> {
            // Run exitAction() in a background thread to prevent blocking
            Task<Void> logOutTask = new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
                {
                    exitAction();  // Call your logout action in the background
                    return null;
                }
            };
            // Start the background task
            new Thread(logOutTask).start();
        });

        // Show the updated stage
        currentStage.setResizable(false); // Disable window resizing
        currentStage.show();
    }

    private static void exitAction() throws Exception
    {
        ClientServerMessage logOutMessage = new ClientServerMessage(999, null);
        try {
            ClientGUIController.chat.accept(logOutMessage);
            System.exit(0);
        }
        catch (Exception e) {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        ClientIPFrameController aFrame = new ClientIPFrameController();
        aFrame.start(primaryStage);

    }


}
