package client;

import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import logic.ClientController;
import logic.ClientIPFrameController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
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
     * @param fxmlFilePath The path to the FXML file to load (e.g., "/gui/SearchPageFrame.fxml").
     * @param cssFilePath  The optional path to the CSS file to style the new view (e.g., "/gui/Subscriber.css"). Pass null if no CSS is required.
     * @param stageTitle   The title for the new stage window (e.g., "Profile Options").
     * @throws Exception If the FXML file cannot be loaded or another error occurs during navigation.
     */
    public static void navigateTo(ActionEvent event, String fxmlFilePath, String cssFilePath, String stageTitle) throws Exception
    {
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
            if (ClientGUIController.class.getResource(cssFilePath) != null)
            {
                newRoot.getStylesheets().add(Objects.requireNonNull(ClientGUIController.class.getResource(cssFilePath)).toExternalForm());
            }
            else
            {
                System.out.println("CSS file not found: " + cssFilePath);
            }
        }

        // Update the root node of the existing scene
        /// /////test for setting a sizeable screen
        Screen screen = Screen.getPrimary();
        currentStage.setWidth(screen.getBounds().getWidth());
        currentStage.setHeight(screen.getBounds().getHeight());
        double scaleX = screen.getBounds().getWidth()/1920.0;
        double scaleY = screen.getBounds().getHeight()/1080.0;
        double scaleD = Math.min(scaleX, scaleY);
        Scale scale = new Scale(scaleD, scaleD);
        newRoot.getTransforms().add(scale);
        scale.xProperty().bind(currentStage.widthProperty().divide(1920));
        scale.yProperty().bind(currentStage.heightProperty().divide(1080));

        /// //

        currentStage.getScene().setRoot(newRoot);
        currentStage.setTitle(stageTitle);
        currentStage.setMaximized(true);
        currentStage.setOnCloseRequest(event1 -> {
            // Run exitAction() in a background thread to prevent blocking
            Task<Void> logOutTask = new Task<Void>()
            {
                @Override
                protected Void call()
                {
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
     * Changes the scene to the specified FXML file within the parent container.
     *
     * @param parentContainer The parent container in which to display the new scene.
     * @param scene           The path to the FXML file to load (e.g., "/gui/SearchPageFrame.fxml").
     * @throws IOException If the FXML file cannot be loaded or another error occurs during navigation.
     */
    public static void loadFrameIntoPane(AnchorPane parentContainer, String scene) throws IOException
    {
        parentContainer.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(ClientGUIController.class.getResource(scene));
        ClientController.setLoader(loader);
        Node view = loader.load();
        parentContainer.getChildren().add(view);
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
    }

    /**
     * Show an alert to the user with a given type, title, and message.
     *
     * @param type    The type of the alert (e.g., ERROR, WARNING, INFORMATION)
     * @param title   The title of the alert
     * @param message The message to be displayed
     */
    public static void showAlert(Alert.AlertType type, String title, String message)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * The method is called when the program is closed, it sends a disconnect message to the server
     */
    private static void exitAction()
    {
        ClientServerMessage logoutMessage = new ClientServerMessage(102, 0);
        ClientServerMessage exitMessage = new ClientServerMessage(104, null);

        if (Subscriber.getLocalSubscriber() != null)
        {
            logoutMessage = new ClientServerMessage(102, Subscriber.getLocalSubscriber().getID());
        }
        else if (Librarian.getLocalLibrarian() != null)
        {
            logoutMessage = new ClientServerMessage(102, Librarian.getLocalLibrarian().getID());
        }

        try
        {
            ClientGUIController.chat.sendToServer(logoutMessage);
            ClientGUIController.chat.sendToServer(exitMessage);
            System.exit(0);
        }
        catch (Exception e)
        {
            System.out.println("Error sending login message to server: " + e.getMessage());
        }
    }

    /**
     * Shows the error state: sets the TextField border to red and displays the error label.
     *
     * @param errorMessage The error message to display.
     */
    public static void showErrorListenField(TextField textFieldToChange, Label labelToChange, String errorMessage)
    {
        // Show the error label with the provided message
        labelToChange.setText(errorMessage);
        labelToChange.setVisible(true);
        // Set the TextField border to red
        textFieldToChange.setStyle("-fx-border-color: red;");
        labelToChange.setStyle("-fx-text-fill: red;");
    }

    /**
     * Resets the error state (removes red border and hides the error label).
     */
    public static void resetErrorState(TextField textFieldToReset,Label labelToReset)
    {
        labelToReset.setVisible(false);
        textFieldToReset.setStyle(""); // Reset the style
    }

    /**
     * The start method is called when the program is run, it starts the ClientIPFrameController
     *
     * @param primaryStage The stage to be displayed
     * @throws Exception If an error occurs during the start
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        ClientIPFrameController aFrame = new ClientIPFrameController();
        aFrame.start(primaryStage);
    }
}