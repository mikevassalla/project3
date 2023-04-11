import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	Server serverConnection;
	Client clientConnection;
	private final ObservableList<Player> gameResults = FXCollections.observableArrayList();
	private TableView<Player> tableView;


	ListView<String> listItems;
	int count = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		tableView = new TableView<>();
		TableColumn<Player, String> clientColumn = new TableColumn<>("Client");

		TableColumn<Player, String> resultColumn = new TableColumn<>("Result");

		TableColumn<Player, String> betColumn = new TableColumn<>("Bet");

		TableColumn<Player, String> winLossColumn = new TableColumn<>("Win/Loss");

		// Add the columns to the TableView
		tableView.getColumns().addAll(clientColumn, resultColumn, betColumn, winLossColumn);

		// Add the gameResults to the TableView
		tableView.setItems(gameResults);

		// Create a VBox to hold the TableView
		VBox tableResults = new VBox(tableView);

		listItems = new ListView<>();
		Label clientText = new Label("Client Information");
		VBox clientInfo = new VBox(clientText,listItems);
		clientInfo.setAlignment(Pos.CENTER);

		TextField portField = new TextField();
		portField.setPromptText("Enter port here");
		portField.setAlignment(Pos.CENTER);
		Button startListen = new Button("Start Listening");

		// Creates the action for the listening button.
		startListen.setOnAction(event -> {
			try {
				int port = Integer.parseInt(portField.getText());
				// Do something with the serverSocket, such as accepting connections
			} catch (NumberFormatException e) {
				// Handle the case where the user enters a non-integer port number
			}
		});

		// The following only allows the listen button to be active when something is typed in.
		startListen.setDisable(true);
		portField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.trim().isEmpty()) {
				startListen.setDisable(true);
			} else {
				startListen.setDisable(false);
			}
		});

		ToggleButton toggleButton = new ToggleButton("Turn On/Off Server");
		toggleButton.setStyle("-fx-background-color: red;");

		toggleButton.setOnAction(event -> {
			if (toggleButton.isSelected()) {
				// Set the style to green when the button is toggled on
				toggleButton.setStyle("-fx-background-color: green;");
				System.out.println("Server started.");
			} else {
				// Set the style to red when the button is toggled off
				toggleButton.setStyle("-fx-background-color: red;");
				System.out.println("Server stopped.");
			}
		});


		VBox portCheck = new VBox(portField, startListen, toggleButton);
		portCheck.setAlignment(Pos.CENTER);

		Label clientAmount = new Label("Total Clients");
		TextField clientTot = new TextField();
		clientTot.setEditable(false);
		clientTot.setText(String.valueOf(count));
		clientTot.setAlignment(Pos.CENTER);

		VBox amonClient = new VBox(clientAmount,clientTot);
		amonClient.setAlignment(Pos.CENTER);

		VBox leftPane = new VBox(amonClient, portCheck, toggleButton);
		leftPane.setAlignment(Pos.CENTER);
		leftPane.setSpacing(25);


		primaryStage.setTitle("Server Side");
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		// Set spacing for left pane
		BorderPane.setMargin(leftPane, new Insets(0, 20, 0, 0)); // 10px spacing on the right side

		// Set spacing for center pane
		BorderPane.setMargin(tableResults, new Insets(0, 20, 0, 10)); // 10px spacing on both sides

		pane.setRight(clientInfo);
		pane.setCenter(tableResults);
		pane.setLeft(leftPane);
		primaryStage.setScene(new Scene(pane, 500, 200));

		serverConnection = new Server(data -> {
			Platform.runLater(()->{
				listItems.getItems().add(data.toString());
			});
		});

		//listItems2 = new ListView<String>();

		//c1 = new TextField();
		//b1 = new Button("Send");
		//b1.setOnAction(e->{clientConnection.send(c1.getText()); c1.clear();});

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.setMaximized(true);
		primaryStage.show();

	}

}
