package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Subscriber;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SubscriberFrameController implements Initializable {
    private Subscriber subscriber;
    @FXML
    private Label lblWelcome;

    @FXML
    private TextField txtWelcomeUserName;

    @FXML
    private Button btnProfile;
    @FXML
    private Button btnBorrowExtension;
    @FXML
    private Button btnOrder;
    @FXML
    private Button btnSearchBook;
    @FXML
    private Button btnLogout;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }

    /**
     * Loads the subscriber's name into the welcome text field.
     *
     * @param s1 The Subscriber object containing user details.
     */
    public void loadSubscriberName(Object s1) {
        try{
            // Set the text of the txtWelcomeUserName field to the subscriber's full name
            this.subscriber = (Subscriber) s1;
            txtWelcomeUserName.setText(subscriber.getName() + " " + subscriber.getLastName());
        }
        catch (ClassCastException e){
            e.getMessage();
        }


    }
    public void clickProfileButton(ActionEvent event) throws Exception{
        System.out.println("exit Subscriber Frame");
        navigateTo(event, "/gui/SubscriberProfileOptionsFrame.fxml", "/gui/Subscriber.css", "Profile Options");
    }

    /**
     * Navigates to a specified FXML destination, optionally applying a CSS file and setting the title of the new stage.
     *
     * @param event The ActionEvent triggered by the user's interaction (e.g., button click).
     * @param fxmlDestination The path to the FXML file for the destination view (e.g., "/GUI/SubscriberProfileOptions.fxml").
     * @param cssFilePath The optional path to the CSS file to style the new view (e.g., "/GUI/Subscriber.css"). Pass null if no CSS is required.
     * @param stageTitle The title for the new stage window (e.g., "Profile Options").
     * @throws Exception If the FXML file cannot be loaded or another error occurs during navigation.
     */
    public static void navigateTo(ActionEvent event, String fxmlDestination, String cssFilePath, String stageTitle) throws Exception {
        // Close the current stage
        Stage currentStage;
        currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Load the destination FXML
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(SubscriberFrameController.class.getResource(fxmlDestination)));

        // Create the new scene
        Scene scene = new Scene(root);

        // Optionally add a CSS file if provided
        if (cssFilePath != null && !cssFilePath.isEmpty()) {
            scene.getStylesheets().add(Objects.requireNonNull(SubscriberFrameController.class.getResource(cssFilePath)).toExternalForm());
        }
        // Set up the new stage
        newStage.setTitle(stageTitle);
        newStage.setScene(scene);

        // Show the new stage
        newStage.show();
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SubscriberFrameController.class.getResource("/gui/SubscriberFrame.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/Subscriber.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Subscriber Frame");
        primaryStage.show();

    }
}
