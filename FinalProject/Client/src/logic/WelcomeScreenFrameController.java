package logic;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class WelcomeScreenFrameController
{
    // The number of clicks on the image
    private int clickCount = 0;

    /**
     * handles the image click event and counts the number of clicks for the popup
     */
    @FXML
    private void handleImageClick()
    {
        clickCount++;
        if (clickCount == 5)
        {
            showPopup();
            clickCount = 0; // Reset the click count
        }
    }

    /**
     * Shows a popup with an image and a close button.
     * It's an Easter egg!
     */
    private void showPopup()
    {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED); // Remove window decorations

        // Load the image
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/MakeBlibGrateAgain.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(731);
        imageView.setFitWidth(1019);

        // Create a close button with styles
        Button closeButton = new Button("Close");
        closeButton.setLayoutX(950); // Adjust the position as needed
        closeButton.setLayoutY(700); // Adjust the position as needed
        closeButton.setOnAction(e -> stage.close());

        // Create an AnchorPane and add the ImageView and Button
        AnchorPane pane = new AnchorPane(imageView, closeButton);
        Scene scene = new Scene(pane);

        // Set the scene and show the stage
        stage.setScene(scene);
        stage.setX(600);
        stage.setY(150);
        stage.show();
    }
}