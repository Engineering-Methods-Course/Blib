package logic;

import common.Librarian;
import common.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import sun.font.TrueTypeFont;

import java.io.IOException;

import static client.ClientGUIController.navigateTo;

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
    public AnchorPane Anchor1;
    public Button logoutButton;
    public AnchorPane Ancor2;
    public Button borrowButton;


    public void initialize() {

        if (Subscriber.getLocalSubscriber()==null && Librarian.getLocalLibrarian()==null)
        {
            UsernameText.setVisible(false);
            Image image =new Image("/resources/icons8-book-50.png");
            userPhoto.setImage(image);
            //Anchor1.setVisible(false);
            //Anchor1.setDisable(true);

        }
        if (Subscriber.getLocalSubscriber()!=null) {
            UsernameText.setText(Subscriber.getLocalSubscriber().getFirstName());
            LoginButton.setDisable(true);
            LoginButton.setVisible(false);
            Ancor2.setVisible(true);
            Ancor2.setDisable(false);

        }
        if(Librarian.getLocalLibrarian()!=null){
            UsernameText.setText(Librarian.getLocalLibrarian().getFirstName());
            LoginButton.setDisable(true);
            LoginButton.setVisible(false);
            Anchor1.setVisible(true);
            Anchor1.setDisable(false);
        }
    }

    public void Press1(ActionEvent actionEvent) throws Exception {
        loadFrameIntoPane("/gui/SearchHomePageFrame.fxml");

    }

    public void OpenLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/UserLoginFrame.fxml"));
        Parent newContent = loader.load();
        SceneChanger.getChildren().clear();
        SceneChanger.getChildren().add(newContent);
        AnchorPane.setTopAnchor(newContent, 0.0);
        AnchorPane.setRightAnchor(newContent, 0.0);
        AnchorPane.setBottomAnchor(newContent, 0.0);
        AnchorPane.setLeftAnchor(newContent, 0.0);
    }

    public void ImageOnClick(MouseEvent mouseEvent) {
    }

    public void logout(ActionEvent actionEvent) throws Exception {

        if(Librarian.getLocalLibrarian()!=null){
            Librarian.setLocalLibrarian(null);
        }
        else if(Subscriber.getLocalSubscriber()!=null){
            Subscriber.setLocalSubscriber(null);
        }
        navigateTo(actionEvent, "/gui/ExpClientFrame.fxml", "/gui/Exp.css", "Main Page");
    }

    public void loadBorrow(ActionEvent actionEvent) throws IOException {
        loadFrameIntoPane("/gui/BorrowBookFrame.fxml");
    }

    public void searchForSubscriber(ActionEvent actionEvent) throws IOException {
        loadFrameIntoPane("/gui/SearchSubscriberFrame.fxml");
    }

    public void watchSubscribersBtn(ActionEvent actionEvent) throws IOException {
       loadFrameIntoPane("/gui/ViewAllSubscribersFrame.fxml");
    }

    public void returnBook(ActionEvent actionEvent) throws IOException {
        loadFrameIntoPane("/gui/ReturnBookFrame.fxml");

    }

    public void RegisterMember(ActionEvent actionEvent) throws IOException {
        loadFrameIntoPane("/gui/RegisterMemberFrame.fxml");
    }

    public void viewReports(ActionEvent actionEvent) throws IOException {
        loadFrameIntoPane("/gui/ViewReportsFrame.fxml");
    }

    public void loadFrameIntoPane(String dest) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(dest));
        Parent newContent = loader.load();
        SceneChanger.getChildren().clear();
        SceneChanger.getChildren().add(newContent);

        AnchorPane.setTopAnchor(newContent, 0.0);
        AnchorPane.setRightAnchor(newContent, 0.0);
        AnchorPane.setBottomAnchor(newContent, 0.0);
        AnchorPane.setLeftAnchor(newContent, 0.0);
    }


    public void editSubscriberDetails(ActionEvent actionEvent) throws IOException {
        loadFrameIntoPane("/gui/SubscriberEditProfileFrame.fxml");
    }

    public void watchHistory(ActionEvent actionEvent) throws IOException {
        ///
    }
}


