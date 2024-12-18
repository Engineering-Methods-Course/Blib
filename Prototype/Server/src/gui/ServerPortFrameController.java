package gui;

import javafx.scene.layout.Pane;
import server.ServerUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerPortFrameController  {


	String temp="";

	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnDone = null;
	@FXML
	private Label lbllist;

	@FXML
	private TextField portxt;
	ObservableList<String> list;

	private String getport() {
		return portxt.getText();
	}

	public void Done(ActionEvent event) throws Exception {
		String p;

		p=getport();
		if(p.trim().isEmpty()) {
			System.out.println("You must enter a port number");

		}
		else
		{
			ServerUI.runServer(p);

			// hide port window
			((Node)event.getSource()).getScene().getWindow().hide();
		}
	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("exit Academic Tool");
		System.exit(0);
	}

}