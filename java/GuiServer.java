
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	
	TextField s1,s2,s3,s4, c1;
	Button serverChoice,clientChoice;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Client clientConnection;
	
	TextField ip = new TextField("IP");
	TextField port = new TextField("Port");
	Button b1 = new Button("Connect");
	Button tempB1 = new Button("Auto Join");
	BorderPane root = new BorderPane();
	
	MenuBar menu = new MenuBar();
	Menu m = new Menu("Menu");
	MenuItem exitBtn = new MenuItem("Exit");
	
	ListView<String> listItems, listItems2;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");
		
		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");
		
		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
											primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});
				});								
		});
		
		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");
		
		this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
											primaryStage.setTitle("This is a client");							
		});
		
		this.buttonBox = new HBox(400, serverChoice, clientChoice);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);
		
		startScene = new Scene(startPane, 800,800);
		
		listItems = new ListView<String>();
		//listItems2 = new ListView<String>();
		
		//c1 = new TextField();
		//b1 = new Button("Send");
		//b1.setOnAction(e->{clientConnection.send(c1.getText()); c1.clear();});
		
		sceneMap = new HashMap<String, Scene>();
		
		sceneMap.put("server",  createServerGui());
		sceneMap.put("client",  createClientGui());
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		b1.setOnAction(e->{	
			try {
				clientConnection = new Client(ip.getText(), Integer.parseInt(port.getText()));
				clientConnection.start();
				primaryStage.setScene(clientGameScene());
			}
			catch(Exception m) {
				//TO DO
			}
		});
		
		tempB1.setOnAction(e->{	
			try {
				clientConnection = new Client("127.0.0.1", 5555);
				clientConnection.start();
				primaryStage.setScene(clientGameScene());
			}
			catch(Exception m) {
				//TO DO
			}
		});
		exitBtn.setOnAction((ActionEvent t) -> {
			Platform.exit();
	        System.exit(0);
		});
		 
		
		primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");
		
		pane.setCenter(listItems);
	
		return new Scene(pane, 500, 400);
		
		
	}
	
	public Scene createClientGui() {
		clientBox = new VBox(10, ip, port, b1, tempB1);
		clientBox.setStyle("-fx-background-color: blue");
		return new Scene(clientBox, 400, 300);
	}
	
	public Scene clientGameScene() {
		ArrayList<ImageView> dealerCards = new ArrayList<>(initializeCards());
		ArrayList<ImageView> p1Cards = new ArrayList<>(initializeCards());
		ArrayList<ImageView> p2Cards = new ArrayList<>(initializeCards());
		
		//Top row
		Label d = new Label("Dealer");
		Label p1Win = new Label("Winnings p1: 0");
		Label p2Win = new Label("Winnings p2: 0");
		HBox dCards = new HBox(dealerCards.get(0), dealerCards.get(1), dealerCards.get(2));
		VBox dDisplay = new VBox(d, dCards);
		dDisplay.setAlignment(Pos.CENTER);
		HBox top = new HBox(p1Win, dDisplay, p2Win);
		
		//Mid row
		Label p1Label = new Label("Player 1");
		Label p2Label = new Label("Player 2");
		
		HBox p1CardsIMG = new HBox(p1Cards.get(0), p1Cards.get(1), p1Cards.get(2));
		HBox p2CardsIMG = new HBox(p2Cards.get(0), p2Cards.get(1), p2Cards.get(2));
		
		VBox p1Display = new VBox(p1Label, p1CardsIMG);
		p1Display.setAlignment(Pos.CENTER);
		VBox p2Display = new VBox(p2Label, p2CardsIMG);
		p2Display.setAlignment(Pos.CENTER);
		
		HBox middle = new HBox(p1Display, p2Display);
		
		//Bottom row
		//Ante Col
		Label ante = new Label("Ante");
		Label anteWager = new Label("Wager: 5");
		Button anteDown = new Button("Down");
		Button anteUp = new Button("Up");
		HBox wagerBoxAnte = new HBox(anteDown, anteWager, anteUp);
		wagerBoxAnte.setAlignment(Pos.CENTER);
		VBox AnteDisplay = new VBox(ante, wagerBoxAnte);
		AnteDisplay.setAlignment(Pos.CENTER);
		
		//play col
		Label totalPot = new Label("Total Pot: 1 MILLY");
		Button playBtn = new Button("Play");
		playBtn.setOnAction(e->{
			clientConnection.send("Hello World");
		});
		Button foldBtn = new Button("Fold");
		HBox btns = new HBox(playBtn, foldBtn);
		btns.setAlignment(Pos.CENTER);
		VBox playDisplay = new VBox(totalPot, btns);
		playDisplay.setAlignment(Pos.CENTER);
		
		Label pair = new Label("pair");
		Label pairWager = new Label("Wager: 5");
		Button pairDown = new Button("Down");
		Button pairUp = new Button("Up");
		HBox wagerBoxPair = new HBox(pairDown, pairWager, pairUp);
		wagerBoxPair.setAlignment(Pos.CENTER);
		VBox pairDisplay = new VBox(pair, wagerBoxPair);
		pairDisplay.setAlignment(Pos.CENTER);
		
		HBox bottom = new HBox(AnteDisplay, playDisplay, pairDisplay);
		
		top.setAlignment(Pos.CENTER);
		middle.setAlignment(Pos.CENTER);
		bottom.setAlignment(Pos.CENTER);
		VBox game = new VBox(top, middle, bottom);
		game.setAlignment(Pos.CENTER);
		m.getItems().add(exitBtn);
		menu.getMenus().addAll(m);
		root.setTop(menu);
		root.setCenter(game);
		return new Scene(root, 400, 300);
	}
	
	ArrayList<ImageView> initializeCards(){
		ArrayList<ImageView> temp = new ArrayList<>();
		ImageView c1 = new ImageView(new Image("back.png"));
		ImageView c2 = new ImageView(new Image("back.png"));
		ImageView c3 = new ImageView(new Image("back.png"));
		
		c1.setFitHeight(100);
		c1.setFitWidth(50);
		c2.setFitHeight(100);
		c2.setFitWidth(50);
		c3.setFitHeight(100);
		c3.setFitWidth(50);
		
		temp.add(c1);
		temp.add(c2);
		temp.add(c3);
		
		return temp;
	}
	

}
