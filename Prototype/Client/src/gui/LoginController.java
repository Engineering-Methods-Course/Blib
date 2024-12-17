package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Subscriber;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static gui.SubscriberFrameController.navigateTo;

public class LoginController implements Initializable  {

    @FXML
    private TextField txtUsername;  // Reference to the username field

    @FXML
    private PasswordField txtPassword;  // Reference to the password field

    @FXML
    private Button btnLogin;  // Reference to the login button

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SubscriberFrameController.class.getResource("/gui/LoginFrame.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/Subscriber.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Subscriber Frame");
        primaryStage.show();

    }
    @FXML
    public void clickLoginButton(ActionEvent event) throws Exception {
        // Create a test subscriber object
        Subscriber testSubscriber = new Subscriber("123", "Mona", "Lisa", 1, "0541234567", "MonaLisa@e.braude.ac.il", "abc");

        // Pass the Subscriber object to the SubscriberFrameController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SubscriberFrame.fxml"));
        Parent root = loader.load();

        // Get the SubscriberFrameController instance
        SubscriberFrameController subscriberFrameController = loader.getController();

        // Pass the Subscriber object to the controller's method
        subscriberFrameController.loadSubscriberName(testSubscriber);

        // Use the navigateTo method to handle the transition
        navigateTo(event, "/gui/SubscriberFrame.fxml", "/gui/Subscriber.css", "Subscriber Frame");
    }

}
