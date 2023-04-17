//  Project 3 -  3 Card Poker
//  Daniel Beben - Dbeben2 & Micheal Vassalla mvassa4
//  CS342 Spring 2023
// This project you will implement a networked version of the popular casino game 3 Card Poker.
// The focus of the project is event driven programing and networking with Java Sockets.

import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiClient extends Application{

	VBox clientBox;
	Client clientConnection;
	TextField ip = new TextField("");
	TextField port = new TextField("");
	Button b1 = new Button("Connect");
	//Button tempB1 = new Button("Auto Join");
	BorderPane root = new BorderPane();
	MenuBar menu = new MenuBar();
	Menu m = new Menu("Menu");
	MenuItem exitBtn = new MenuItem("Exit");
	MenuItem themeBtn = new MenuItem("Change Theme");
	MenuItem freshStart = new MenuItem("Restart");
	ArrayList<ImageView> p1Cards = new ArrayList<>(initializeCards());
	ArrayList<ImageView> p2Cards = new ArrayList<>(initializeCards());
	int playWin = 0;
	private Integer player;
	private int anteWager = 5;
	private int pairWager = 5;

	//Buttons
	Button anteDown = new Button("-");
	Button anteUp = new Button("+");
	Button playBtn = new Button("Play");
	Button foldBtn = new Button("Fold");
	Button dealBtn = new Button("Deal");
	Button pairDown = new Button("-");
	Button pairUp = new Button("+");
	TextField anteAmount = new TextField();
	TextField pairAmount = new TextField();
	private Integer changeTheme = 0;
	TextField yourWinnings = new TextField(Integer.toString(playWin));
	HBox p1CardsIMG = new HBox(p1Cards.get(0), p1Cards.get(1), p1Cards.get(2));
	Image temp = new Image("back.png");
	BorderPane root2 = new BorderPane();
	BorderPane root3 = new BorderPane();
	VBox organizer;
	Label qwe = new Label("HEre");
	Label asd = new Label("Here2");
	Button playAgain = new Button("Play Again");
	Stage popupStage = new Stage();
	Button folded = new Button("Play Again");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
		
		playAgain.setOnAction(x -> {popupStage.hide();});
		folded.setOnAction(x -> {
			playBtn.setDisable(true);
			foldBtn.setDisable(true);
			anteDown.setDisable(false);
			anteUp.setDisable(false);
			dealBtn.setDisable(false);
			pairDown.setDisable(false);
			pairUp.setDisable(false);
			
			playWin = 0;
			yourWinnings.setText(Integer.toString(playWin));
			
			anteAmount.setText("5");
			anteWager = 5;
			
			pairAmount.setText("5");
			pairWager = 5;
			
			Image temp = new Image("back.png");
			ImageView card = new ImageView(temp);
			p1Cards.get(0).setImage(card.getImage());
			p1Cards.get(1).setImage(card.getImage());
			p1Cards.get(2).setImage(card.getImage());
			p2Cards.get(0).setImage(card.getImage());
			p2Cards.get(1).setImage(card.getImage());
			p2Cards.get(2).setImage(card.getImage());
			
			clientConnection.send(new Responses(5, player));
			popupStage.hide();
			});
		
		b1.setOnAction(e->{
			try {
				// Sets the width and height of the ip label.
				clientConnection = new Client(ip.getText(), Integer.parseInt(port.getText()), x ->{
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
							//Sends Player Card
							temp = new Image(r.cs.get(0));
							ImageView card = new ImageView(temp);
							p1Cards.get(0).setImage(card.getImage());
							temp = new Image(r.cs.get(1));
							card = new ImageView(temp);
							p1Cards.get(1).setImage(card.getImage());
							temp = new Image(r.cs.get(2));
							card = new ImageView(temp);
							p1Cards.get(2).setImage(card.getImage());
							playBtn.setDisable(false);
							foldBtn.setDisable(false);
						}
						else if(r.getResponse() == 3) {
							//Sends Dealer Cards
							Image temp = new Image(r.cs.get(0));
							ImageView card = new ImageView(temp);
							p2Cards.get(0).setImage(card.getImage());
							temp = new Image(r.cs.get(1));
							card = new ImageView(temp);
							p2Cards.get(1).setImage(card.getImage());
							temp = new Image(r.cs.get(2));
							card = new ImageView(temp);
							p2Cards.get(2).setImage(card.getImage());
						}
						else if(r.getResponse() == 4) {
							//Tie
							asd.setText("Won 0 this round");

							playWin += r.getAnte();
							playWin += r.getAnte();
							playWin += r.getPair();

							yourWinnings.setText(Integer.toString(playWin));
							playBtn.setDisable(true);
							foldBtn.setDisable(true);
							anteDown.setDisable(false);
							anteUp.setDisable(false);
							dealBtn.setDisable(false);
							pairDown.setDisable(false);
							pairUp.setDisable(false);
							popupStage.show();
						}
						else if(r.getResponse() == 5) {
							//Player 1 Wins
							int won = 0;
							won += r.getAnte();
							won += r.getPair();

							asd.setText("Won " + won + " this round");
							
							playWin += r.getAnte();
							playWin += r.getPair();
							yourWinnings.setText(Integer.toString(playWin));
							playBtn.setDisable(true);
							foldBtn.setDisable(true);
							anteDown.setDisable(false);
							anteUp.setDisable(false);
							dealBtn.setDisable(false);
							pairDown.setDisable(false);
							pairUp.setDisable(false);
							popupStage.show();
						}
						else if(r.getResponse() == 6) {
							//Dealer Wins
							int won = 0;

							won -= r.getAnte();
							won -= r.getAnte();
							won += r.getPair();

							asd.setText("Won " + won + " this round");
							
							playWin += r.getPair();
							yourWinnings.setText(Integer.toString(playWin));
							playBtn.setDisable(true);
							foldBtn.setDisable(true);
							anteDown.setDisable(false);
							anteUp.setDisable(false);
							dealBtn.setDisable(false);
							pairDown.setDisable(false);
							pairUp.setDisable(false);
							popupStage.setScene(clientReplayScene());
							popupStage.show();
						}
						else if(r.getResponse() == 7) {
							//Dealer does not have queen or higher
							int won = 0;
							won += r.getAnte();
							won += r.getAnte();
							won += r.getPair();
							
							asd.setText("Won " + won + " this round");
							
							playWin += r.getAnte();
							playWin += r.getAnte();
							playWin += r.getPair();

							anteWager = r.getAnte();
							yourWinnings.setText(Integer.toString(playWin));
							anteAmount.setText(Integer.toString(r.getAnte()));
							anteDown.setDisable(true);
							anteUp.setDisable(true);
							playBtn.setDisable(true);
							foldBtn.setDisable(true);
							dealBtn.setDisable(false);
							pairDown.setDisable(false);
							pairUp.setDisable(false);
							popupStage.show();
						}
						else if(r.getResponse() == 8) {
							//gameWin.setText(r.getMessage());
							qwe.setText(r.getMessage());
						}
						else if(r.getResponse() == 20) {
							player = Integer.parseInt(r.getMessage());
						}
					});
				});
				clientConnection.start();
				primaryStage.setScene(clientGameScene());
				popupStage.setScene(clientReplayScene());
				popupStage.initModality(Modality.APPLICATION_MODAL);
				primaryStage.setMaximized(true);
			}
			catch(Exception m) {
				System.out.println(e);
			}
		});

		/* Auto Join Button for testing purposes
		 
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
							//Sends Player Card
							temp = new Image(r.cs.get(0));
							System.out.println(r.cs.get(0));
							System.out.println(r.cs.get(1));
							System.out.println(r.cs.get(2));
							ImageView card = new ImageView(temp);
							p1Cards.get(0).setImage(card.getImage());
							temp = new Image(r.cs.get(1));
							card = new ImageView(temp);
							p1Cards.get(1).setImage(card.getImage());
							temp = new Image(r.cs.get(2));
							card = new ImageView(temp);
							p1Cards.get(2).setImage(card.getImage());
							playBtn.setDisable(false);
							foldBtn.setDisable(false);
						}
						else if(r.getResponse() == 3) {
							//Sends Dealer Cards
							Image temp = new Image(r.cs.get(0));
							ImageView card = new ImageView(temp);
							p2Cards.get(0).setImage(card.getImage());
							temp = new Image(r.cs.get(1));
							card = new ImageView(temp);
							p2Cards.get(1).setImage(card.getImage());
							temp = new Image(r.cs.get(2));
							card = new ImageView(temp);
							p2Cards.get(2).setImage(card.getImage());
						}
						else if(r.getResponse() == 4) {
							//Tie
							//roundWon.setText("Won 0 this round");
							asd.setText("Won 0 this round");

							playWin += r.getAnte();
							playWin += r.getAnte();
							playWin += r.getPair();

							yourWinnings.setText(Integer.toString(playWin));
							playBtn.setDisable(true);
							foldBtn.setDisable(true);
							anteDown.setDisable(false);
							anteUp.setDisable(false);
							dealBtn.setDisable(false);
							pairDown.setDisable(false);
							pairUp.setDisable(false);
							popupStage.show();
						}
						else if(r.getResponse() == 5) {
							//Player 1 Wins
							int won = 0;
							won += r.getAnte();
							won += r.getPair();

							//roundWon.setText("Won " + won + " this round");
							asd.setText("Won " + won + " this round");
							
							playWin += r.getAnte();
							playWin += r.getPair();
							yourWinnings.setText(Integer.toString(playWin));
							playBtn.setDisable(true);
							foldBtn.setDisable(true);
							anteDown.setDisable(false);
							anteUp.setDisable(false);
							dealBtn.setDisable(false);
							pairDown.setDisable(false);
							pairUp.setDisable(false);
							popupStage.show();
						}
						else if(r.getResponse() == 6) {
							//Dealer Wins
							int won = 0;

							won -= r.getAnte();
							won -= r.getAnte();
							won += r.getPair();

							//roundWon.setText("Won " + won + " this round");
							asd.setText("Won " + won + " this round");
							
							playWin += r.getPair();
							yourWinnings.setText(Integer.toString(playWin));
							playBtn.setDisable(true);
							foldBtn.setDisable(true);
							anteDown.setDisable(false);
							anteUp.setDisable(false);
							dealBtn.setDisable(false);
							pairDown.setDisable(false);
							pairUp.setDisable(false);
							popupStage.setScene(clientReplayScene());
							popupStage.show();
						}
						else if(r.getResponse() == 7) {
							//Dealer does not have queen or higher
							int won = 0;
							won += r.getAnte();
							won += r.getAnte();
							won += r.getPair();
							
							//roundWon.setText("Won " + won + " this round");
							asd.setText("Won " + won + " this round");
							
							playWin += r.getAnte();
							playWin += r.getAnte();
							playWin += r.getPair();

							anteWager = r.getAnte();
							yourWinnings.setText(Integer.toString(playWin));
							anteAmount.setText(Integer.toString(r.getAnte()));
							anteDown.setDisable(true);
							anteUp.setDisable(true);
							playBtn.setDisable(true);
							foldBtn.setDisable(true);
							dealBtn.setDisable(false);
							pairDown.setDisable(false);
							pairUp.setDisable(false);
							popupStage.show();
						}
						else if(r.getResponse() == 8) {
							//gameWin.setText(r.getMessage());
							qwe.setText(r.getMessage());
						}
						else if(r.getResponse() == 20) {
							System.out.println(r.getMessage());
							player = Integer.parseInt(r.getMessage());
						}
					});
				});
				clientConnection.start();
				primaryStage.setScene(clientGameScene());
				popupStage.setScene(clientReplayScene());
				popupStage.initModality(Modality.APPLICATION_MODAL);
				primaryStage.setMaximized(true);
			}
			catch(Exception m) {
				System.out.println(m);
				//TO DO
			}
		});
		*/
		
		exitBtn.setOnAction((ActionEvent t) -> {
			Platform.exit();
			System.exit(0);
		});
		
		freshStart.setOnAction((ActionEvent t) -> {
			
			playBtn.setDisable(true);
			foldBtn.setDisable(true);
			anteDown.setDisable(false);
			anteUp.setDisable(false);
			dealBtn.setDisable(false);
			pairDown.setDisable(false);
			pairUp.setDisable(false);
			
			playWin = 0;
			yourWinnings.setText(Integer.toString(playWin));
			
			anteAmount.setText("5");
			anteWager = 5;
			
			pairAmount.setText("5");
			pairWager = 5;
			
			Image temp = new Image("back.png");
			ImageView card = new ImageView(temp);
			p1Cards.get(0).setImage(card.getImage());
			p1Cards.get(1).setImage(card.getImage());
			p1Cards.get(2).setImage(card.getImage());
			p2Cards.get(0).setImage(card.getImage());
			p2Cards.get(1).setImage(card.getImage());
			p2Cards.get(2).setImage(card.getImage());
			
			clientConnection.send(new Responses(5, player));
		});
		

		primaryStage.setScene(createClientGui());
		primaryStage.setTitle("Client");
		primaryStage.show();
	}

	
	
	public Scene createClientGui() {
		ip.setFocusTraversable(false);
		port.setMaxWidth(100); // set the maximum width to 200 pixels
		port.setPromptText("Enter port here");
		port.setAlignment(Pos.CENTER);
		port.setFocusTraversable(false);

		ip.setMaxWidth(200); // set the maximum width to 200 pixels
		ip.setPromptText("Enter IP here");
		ip.setAlignment(Pos.CENTER);


		clientBox = new VBox(10, ip, port, b1);
		clientBox.setAlignment(Pos.CENTER);
		changeTheme = 1;
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
		return new Scene(clientBox, 500, 400);
	}
	
	public Scene clientReplayScene() {
		//playAgain = new Button("Play Again?");
		Button escape = new Button("Exit");
		escape.setOnAction((ActionEvent t) -> {
			Platform.exit();
			System.exit(0);
		});
		HBox butones = new HBox(playAgain, escape);
		butones.setAlignment(Pos.CENTER);
		butones.setSpacing(10);
		organizer = new VBox(qwe, asd, butones);
		organizer.setAlignment(Pos.CENTER);
		organizer.setSpacing(10);
		root2 = new BorderPane(organizer);
		root2.setCenter(organizer);
		Scene scene = new Scene(root2, 350, 300);
		return scene;
	}
	
	public Scene foldScene(int val) {
		Label textStuff = new Label("You lose");
		Label textStuff2 = new Label("You lost " + val + " this round");
		Button esp = new Button("Exit");
		esp.setOnAction((ActionEvent t) -> {
			Platform.exit();
			System.exit(0);
		});
		
		
		HBox butones2 = new HBox(folded, esp);
		butones2.setAlignment(Pos.CENTER);
		butones2.setSpacing(10);
		VBox organizers = new VBox(textStuff, textStuff2, butones2);
		organizers.setAlignment(Pos.CENTER);
		organizers.setSpacing(10);
		root3 = new BorderPane(organizers);
		root3.setCenter(organizers);
		Scene scene = new Scene(root3, 350, 300);
		return scene;
	}
	
	public Scene clientGameScene() {
		playBtn.setDisable(true);
		foldBtn.setDisable(true);


		//
		//Top row
		//
		Label p1Win = new Label("Your Winnings");
		yourWinnings.setEditable(false);
		yourWinnings.setPrefWidth(50); // set the preferred width to 50 pixels

		VBox yourDisplay = new VBox(p1Win, yourWinnings);
		HBox top = new HBox(yourDisplay);
		top.setSpacing(30); // Set spacing between the amount winnings and the cards
		top.setPadding(new Insets(10, 0, 30, 0)); // set top, right, bottom, left padding

		//Mid row
		Label p1Label = new Label("You");
		Label p2Label = new Label("Dealer");

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
		AnteDisplay.setSpacing(10); // Set spacing between the label and the wager buttons.;

		playBtn.setOnAction(e->{
			playWin -= anteWager;
			yourWinnings.setText(Integer.toString(playWin));
			clientConnection.send(new Responses(3, anteWager, pairWager, player));
		});

		foldBtn.setOnAction(e->{
			popupStage.setScene(foldScene(anteWager+anteWager+pairWager));
			popupStage.show();
			clientConnection.send(new Responses(4, anteWager, pairWager, player));
		});

		dealBtn.setOnAction(e->{
			Image temp = new Image("back.png");
			ImageView card = new ImageView(temp);
			p2Cards.get(0).setImage(card.getImage());
			p2Cards.get(1).setImage(card.getImage());
			p2Cards.get(2).setImage(card.getImage());
			playWin -= anteWager;
			playWin -= pairWager;
			yourWinnings.setText(Integer.toString(playWin));
			clientConnection.send(new Responses(2, anteWager, pairWager, player));
			anteDown.setDisable(true);
			anteUp.setDisable(true);
			dealBtn.setDisable(true);
			pairDown.setDisable(true);
			pairUp.setDisable(true);
		});

		VBox playDisplay = new VBox(playBtn, foldBtn, dealBtn);

		playDisplay.setAlignment(Pos.CENTER);
		playDisplay.setSpacing(10); // Set spacing between the label and the wager buttons.

		Label pair = new Label("Pair");
		pairAmount.setText(String.valueOf(pairWager));
		pairAmount.setEditable(false);
		pairDown.setOnAction(event -> {
			if (pairWager > 5) {
				pairWager--;
				pairAmount.setText(String.valueOf(pairWager));
			}
		});
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
		VBox.setMargin(bottom, new Insets(75, 0, 0, 0));

		//
		// The following code is to change the theme of the program interface
		//
		themeBtn.setOnAction((ActionEvent t) -> {
			if (changeTheme == 0) {
				root.getStylesheets().clear();
				root.getStylesheets().add(getClass().getResource("white.css").toExternalForm());
				changeTheme++;
			}
			else if(changeTheme == 1){
				root.getStylesheets().clear();
				root.getStylesheets().add(getClass().getResource("gray.css").toExternalForm());
				changeTheme++;
			}
			else if(changeTheme == 2) {
				root.getStylesheets().clear();
				root.getStylesheets().add(getClass().getResource("clientBlue.css").toExternalForm());
				changeTheme = 0;
			}

		});

		// Create the VBox
		VBox game = new VBox(top, middle, bottom);
		game.setAlignment(Pos.CENTER);
		m.getItems().add(exitBtn);
		m.getItems().add(themeBtn);
		m.getItems().add(freshStart);
		menu.getMenus().addAll(m);
		root.setTop(menu);
		root.setCenter(game);
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
