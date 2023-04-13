
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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiClient extends Application{

	VBox clientBox;
	Client clientConnection;
	
	TextField ip = new TextField("");
	TextField port = new TextField("");
	Button b1 = new Button("Connect");
	Button tempB1 = new Button("Auto Join");
	BorderPane root = new BorderPane();
	MenuBar menu = new MenuBar();
	Menu m = new Menu("Menu");
	MenuItem exitBtn = new MenuItem("Exit");
	ArrayList<ImageView> p1Cards = new ArrayList<>(initializeCards());
	int playWin = 0;
	int opWin = 0;
	private String player;
	private int anteWager = 5;
	private int pairWager = 5;
	String pfSwitch;
	
	//Buttons
	Button anteDown = new Button("-");
	Button anteUp = new Button("+");
	Button playBtn = new Button("Play");
	Button foldBtn = new Button("Fold");
	Button dealBtn = new Button("Deal");
	Button pairDown = new Button("-");
	Button pairUp = new Button("+");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/*
		Platform.runLater(()->{
			listItems.getItems().add(data.toString());
		});
		*/
		
		//listItems = new ListView<String>();
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
		
		b1.setOnAction(e->{	
			try {
				// Sets the width and height of the ip label.
				clientConnection = new Client(ip.getText(), Integer.parseInt(port.getText()), data ->{
					
				});
				clientConnection.start();
				primaryStage.setScene(clientGameScene());
			}
			catch(Exception m) {
				//TO DO
			}
		});
		
		
		
		tempB1.setOnAction(e->{	
			try {
				clientConnection = new Client("127.0.0.1", 5555, x ->{
					Platform.runLater(() -> {
			            Responses r = (Responses) x;
			            if(r.getResponse() == 1) {
			            	//Enable Buttons
			            	anteDown.setDisable(false);
			        		anteUp.setDisable(false);
			        		dealBtn.setDisable(false);
			        		pairDown.setDisable(false);
			        		pairUp.setDisable(false);
			            }
			            else if(r.getResponse() == 2) {
			            	Image temp = new Image(r.cards.get(0).getImageName());
			                ImageView card = new ImageView(temp);
			            	p1Cards.get(0).setImage(card.getImage());
			            	temp = new Image(r.cards.get(1).getImageName());
			                card = new ImageView(temp);
			            	p1Cards.get(1).setImage(card.getImage());
			            	temp = new Image(r.cards.get(2).getImageName());
			                card = new ImageView(temp);
			            	p1Cards.get(2).setImage(card.getImage());
			            	playBtn.setDisable(false);
			            	foldBtn.setDisable(false);
			            }
			            else if(r.getResponse() == 20) {
			            	player = r.getMessage();
			            }
			        });		
				});
				clientConnection.start();
				primaryStage.setScene(clientGameScene());
			}
			catch(Exception m) {
				System.out.println(m);
				//TO DO
			}
		});
		
		exitBtn.setOnAction((ActionEvent t) -> {
			Platform.exit();
	        System.exit(0);
		});

		primaryStage.setMaximized(true);
		primaryStage.setScene(createClientGui());
		primaryStage.setTitle("Client");	
		primaryStage.show();
		
	}
	
	public Scene createClientGui() {

		ip.setFocusTraversable(false);
		port.setMaxWidth(200); // set the maximum width to 200 pixels
		port.setPromptText("Enter port here");
		port.setAlignment(Pos.CENTER);
		port.setFocusTraversable(false);

		ip.setMaxWidth(300); // set the maximum width to 200 pixels
		ip.setPromptText("Enter IP here");
		ip.setAlignment(Pos.CENTER);


		clientBox = new VBox(10, ip, port, b1, tempB1);
		clientBox.setAlignment(Pos.CENTER);
		Image blueBoi = new Image("casino.jpg");
		BackgroundImage backgroundCasino = new BackgroundImage(
				blueBoi,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(
						1.0, // Width relative to the node
						1.0, // Height relative to the node
						true, // Width is relative
						true, // Height is relative
						false, // Cover the entire area of the node
						false // Do not preserve the aspect ratio of the image
				)
		);
		clientBox.setBackground(new Background(backgroundCasino));
		return new Scene(clientBox, 400, 300);
	}
	
	public Scene clientGameScene() {
		anteDown.setDisable(true);
		anteUp.setDisable(true);
		playBtn.setDisable(true);
		foldBtn.setDisable(true);
		dealBtn.setDisable(true);
		pairDown.setDisable(true);
		pairUp.setDisable(true);

		ArrayList<ImageView> dealerCards = new ArrayList<>(initializeCards());
		//ArrayList<ImageView> p1Cards = new ArrayList<>(initializeCards());
		ArrayList<ImageView> p2Cards = new ArrayList<>(initializeCards());

		//
		//Top row
		//
		Label dealerLabel = new Label("Dealer");
		Label p1Win = new Label("Your Winnings");
		Label p2Win = new Label("Opponents Winnings");
		TextField yourWinnings = new TextField(Integer.toString(playWin));
		yourWinnings.setEditable(false);
		yourWinnings.setPrefWidth(50); // set the preferred width to 50 pixels
		TextField opWinnings = new TextField(Integer.toString(opWin));
		opWinnings.setEditable(false);
		opWinnings.setPrefWidth(50); // set the preferred width to 50 pixels


		VBox yourDisplay = new VBox(p1Win, yourWinnings);
		VBox opDisplay = new VBox(p2Win, opWinnings);
		HBox dCards = new HBox(dealerCards.get(0), dealerCards.get(1), dealerCards.get(2));
		dCards.setSpacing(20); // Set spacing between the cards
		VBox dealerDisplay = new VBox(dealerLabel, dCards);
		dealerDisplay.setSpacing(10); // Set spacing between the dealer label and the cards
		dealerDisplay.setAlignment(Pos.CENTER);
		HBox top = new HBox(yourDisplay, dealerDisplay, opDisplay);
		top.setSpacing(30); // Set spacing between the amount winnings and the cards
		top.setPadding(new Insets(10, 0, 30, 0)); // set top, right, bottom, left padding
		
		//Mid row
		Label p1Label = new Label("You");
		Label p2Label = new Label("Opponent");
		
		HBox p1CardsIMG = new HBox(p1Cards.get(0), p1Cards.get(1), p1Cards.get(2));
		p1CardsIMG.setSpacing(20); // Set spacing between the cards
		HBox p2CardsIMG = new HBox(p2Cards.get(0), p2Cards.get(1), p2Cards.get(2));
		p2CardsIMG.setSpacing(20); // Set spacing between the cards


		VBox p1Display = new VBox(p1Label, p1CardsIMG);
		p1Display.setAlignment(Pos.CENTER);
		p1Display.setSpacing(10); // Set spacing between the label and the cards vertical

		VBox p2Display = new VBox(p2Label, p2CardsIMG);
		p2Display.setAlignment(Pos.CENTER);
		p2Display.setSpacing(10); // Set spacing between the label and the cards vertical

		HBox middle = new HBox(p1Display, p2Display);
		middle.setSpacing(50); // Set spacing between the label and the cards vertical



		//Bottom row
		//Ante Col
		Label ante = new Label("Ante");
		TextField anteAmount = new TextField();
		anteAmount.setText(String.valueOf(anteWager));
		anteAmount.setEditable(false);
		//Button anteDown = new Button("-");
		anteDown.setOnAction(event -> {
			if (anteWager > 5) {
				anteWager--;
				anteAmount.setText(String.valueOf(anteWager));
			}
		});
		//Button anteUp = new Button("+");
		anteUp.setOnAction(event -> {
			if (anteWager < 25) {
				anteWager++;
				anteAmount.setText(String.valueOf(anteWager));
			}
		});
		HBox wagerBoxAnte = new HBox(anteDown, anteAmount, anteUp);
		wagerBoxAnte.setSpacing(10); // Set spacing between the ante buttons

		wagerBoxAnte.setAlignment(Pos.CENTER);

		VBox AnteDisplay = new VBox(ante, wagerBoxAnte);
		AnteDisplay.setAlignment(Pos.CENTER);
		AnteDisplay.setSpacing(10); // Set spacing between the label and the wager buttons.

		//play col
		//totalPotAmount.setStyle("-fx-background-color: transparent;");

		playBtn.setOnAction(e->{
			clientConnection.send(new Responses(3, anteWager, pairWager, player));
		});
		
		foldBtn.setOnAction(e->{
			clientConnection.send(new Responses(1));
		});
		
		dealBtn.setOnAction(e->{
			clientConnection.send(new Responses(2, anteWager, pairWager, player));
			anteDown.setDisable(true);
    		anteUp.setDisable(true);
    		dealBtn.setDisable(true);
    		pairDown.setDisable(true);
    		pairUp.setDisable(true);
		});

		Label playWagerTxt = new Label("Play Wager");
		TextField playWagerAmt = new TextField();
		playWagerAmt.setPrefWidth(50);
		VBox playDisplay = new VBox(playWagerTxt, playWagerAmt,playBtn, foldBtn, dealBtn);

		playDisplay.setAlignment(Pos.CENTER);
		playDisplay.setSpacing(10); // Set spacing between the label and the wager buttons.


		Label pair = new Label("Pair");
		TextField pairAmount = new TextField();
		pairAmount.setText(String.valueOf(pairWager));
		pairAmount.setEditable(false);
		//Button pairDown = new Button("-");
		pairDown.setOnAction(event -> {
			if (pairWager > 5) {
				pairWager--;
				pairAmount.setText(String.valueOf(pairWager));
			}
		});
		//Button pairUp = new Button("+");
		pairUp.setOnAction(event -> {
			if (pairWager < 25) {
				pairWager++;
				pairAmount.setText(String.valueOf(pairWager));
			}
		});

		HBox wagerBoxPair = new HBox(pairDown, pairAmount, pairUp);
		wagerBoxPair.setSpacing(10); // Set spacing between all the wager buttons
		wagerBoxPair.setAlignment(Pos.CENTER);
		VBox pairDisplay = new VBox(pair, wagerBoxPair);
		pairDisplay.setAlignment(Pos.CENTER);
		pairDisplay.setSpacing(10); // Set spacing between the label and the wager buttons.


		HBox bottom = new HBox(AnteDisplay, playDisplay, pairDisplay);
		bottom.setSpacing(30); // Set spacing play button, fold button and ante button


		// Set alignment for all nodes in the VBox
		top.setAlignment(Pos.CENTER);
		middle.setAlignment(Pos.CENTER);
		bottom.setAlignment(Pos.CENTER);

		// Set margin between middle and bottom nodes
		VBox.setMargin(bottom, new Insets(100, 0, 0, 0));

		// Create the VBox
		VBox game = new VBox(top, middle, bottom);
		game.setAlignment(Pos.CENTER);
		m.getItems().add(exitBtn);
		menu.getMenus().addAll(m);
		root.setTop(menu);
		root.setCenter(game);
		root.getStylesheets().add(getClass().getResource("ClientBlue.css").toExternalForm());
		Scene scene = new Scene(root, 700, 700);
		return scene;


	}
	
	ArrayList<ImageView> initializeCards(){
		ArrayList<ImageView> temp = new ArrayList<>();
		ImageView c1 = new ImageView(new Image("back.png"));
		ImageView c2 = new ImageView(new Image("back.png"));
		ImageView c3 = new ImageView(new Image("back.png"));
		
		c1.setFitHeight(120);
		c1.setFitWidth(75);
		c2.setFitHeight(120);
		c2.setFitWidth(75);
		c3.setFitHeight(120);
		c3.setFitWidth(75);
		
		temp.add(c1);
		temp.add(c2);
		temp.add(c3);
		
		return temp;
	}
	

}
