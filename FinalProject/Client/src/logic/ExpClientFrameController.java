package logic;

import common.Librarian;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class ExpClientFrameController {

    @FXML
    public Text UsernameText;
    @FXML
    public AnchorPane SceneChanger;
    @FXML
    public Button searchButton;
    @FXML
    public AnchorPane menu;
    @FXML
    public ImageView userPhoto;
    @FXML
    public Button LoginButton;


    public void initialize() {
        if (Subscriber.getLocalSubscriber()==null && Librarian.getLocalLibrarian()==null)
        {
            UsernameText.setVisible(false);
            Image image =new Image("/resources/icons8-book-50.png");
            userPhoto.setImage(image);

        }
        if (Subscriber.getLocalSubscriber()!=null)
        {
            UsernameText.setText("Hello "+Subscriber.getLocalSubscriber().getFirstName());
            LoginButton.setDisable(true);
            LoginButton.setVisible(false);
        }
    }

    public void Press1(ActionEvent actionEvent) throws IOException {
        Node node;
        node =(Node) FXMLLoader.load(getClass().getResource("/gui/SearchHomePageFrame.fxml"));
        SceneChanger.getChildren().setAll(node);

    }

    public void OpenLogin(ActionEvent actionEvent) throws IOException {
        Node node;
        node =(Node) FXMLLoader.load(getClass().getResource("/gui/UserLoginFrame.fxml"));
        SceneChanger.getChildren().setAll(node);
    }

}
