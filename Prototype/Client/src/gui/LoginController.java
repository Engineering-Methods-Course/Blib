package gui;

import common.ClientServerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import common.Subscriber;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static gui.SubscriberWelcomeFrameController.navigateTo;

public class LoginController implements Initializable {
    private static Subscriber s1;

    @FXML
    private TextField txtUsername;  // Reference to the username field

    @FXML
    private PasswordField txtPassword;  // Reference to the password field

    @FXML
    private Button btnLogin;  // Reference to the login button

    public static Subscriber getS1() {
        return s1;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }
    /**
     * This method handles the login button click event.
     * It navigates to the SubscriberWelcomeFrame.fxml if the login is successful.
     *
     * @param event The action event triggered by clicking the login button
     * @throws Exception If there is an issue with the navigation
     */
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SubscriberWelcomeFrameController.class.getResource("/gui/LoginFrame.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/Subscriber.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Subscriber Frame");
        primaryStage.show();

    }
    /**
     * This method handles the login button click event.
     * It navigates to the SubscriberWelcomeFrame.fxml if the login is successful.
     *
     * @param event The action event triggered by clicking the login button
     * @throws Exception If there is an issue with the navigation
     */
    @FXML
    public void clickLoginButton(ActionEvent event) throws Exception {
        Subscriber testSubscriber = new Subscriber(123, "Mona", "Lisa", "0541234567", "MonaLisa@e.braude.ac.il", "Aa123456");
        ClientServerMessage dataToTransfer = new ClientServerMessage("12", testSubscriber);
        s1 = testSubscriber;

        // Pass the Subscriber object to the next controller using the navigateTo method
        navigateTo(event, "/gui/SubscriberWelcomeFrame.fxml", "/gui/Subscriber.css", "Subscriber Frame");
    }

}
