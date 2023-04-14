import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	Server serverConnection;
	Client clientConnection;
	private final ObservableList<Player> gameResults = FXCollections.observableArrayList();
	private TableView<Player> tableView;
	private int clientsCount = 0;
	
	ArrayList<Player> players = new ArrayList<>();
	
	int responses = 0;
	int cc = 0;
	
	ListView<String> listItems;
	int count = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		TableView<Player> tableView = new TableView<>();

		TableColumn<Player, String> clientColumn = new TableColumn<>("Client");

		TableColumn<Player, String> resultColumn = new TableColumn<>("Result");

		TableColumn<Player, String> betColumn = new TableColumn<>("Bet");

		TableColumn<Player, String> winLossColumn = new TableColumn<>("Win/Loss");

		// Set the table's column resize policy to CONSTRAINED_RESIZE_POLICY
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Add the columns to the table
		tableView.getColumns().addAll(clientColumn, resultColumn, betColumn, winLossColumn);


		// Create a VBox to hold the TableView
		VBox tableResults = new VBox(tableView);
		tableResults.setAlignment(Pos.CENTER);

		listItems = new ListView<>();
		Label clientText = new Label("Client Information");
		Font fontC = Font.font("Arial", FontWeight.BOLD, 16);
		Color colorC = Color.WHITE;
		clientText.setTextFill(colorC);
		clientText.setFont(fontC);
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


		VBox portCheck = new VBox(portField, startListen, toggleButton);
		portCheck.setAlignment(Pos.CENTER);

		Label clientAmount = new Label("Total Clients");
		Font font = Font.font("Arial", FontWeight.BOLD, 16);
		Color color = Color.WHITE;
		clientAmount.setTextFill(color);
		clientAmount.setFont(font);
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

		Image image = new Image("room.jpg");

		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(Region.USE_PREF_SIZE);
		imageView.setFitHeight(Region.USE_PREF_SIZE);
		imageView.setPreserveRatio(true);

		// Create a pane and set its background to the ImageView
		pane.setBackground(new Background(new BackgroundImage(
				image,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(
						1.0,
						1.0,
						true,
						true,
						false,
						false
				)
		)));


		pane.setRight(clientInfo);
		pane.setCenter(tableResults);
		pane.setLeft(leftPane);
		primaryStage.setScene(new Scene(pane, 500, 200));

		toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				toggleButton.setStyle("-fx-background-color: green;");
			serverConnection = new Server(x -> {
				Responses r = (Responses) x;
				Platform.runLater(() -> {
					if (r.getResponse() == 1) {
						//Updating Client Info
						listItems.getItems().add(r.getMessage());
						//serverConnection.sendResponse(new Responses(20, Integer.toString(clientsCount)), clientsCount);
						clientsCount++;
						clientTot.setText(Integer.toString(clientsCount));
					} else if (r.getResponse() == 2) {
						//Deal Cards
						players.get(r.getPlayer()).setAnte(r.getAnte());
						players.get(r.getPlayer()).setPair(r.getPair());
						players.get(r.getPlayer()).playerAddCards();
						System.out.println(players.get(r.getPlayer()).cards.get(0).getImageName() + " " + players.get(r.getPlayer()).cards.get(1).getImageName() + " " + players.get(r.getPlayer()).cards.get(2).getImageName());
						players.get(r.getPlayer()).dealerAddCards();
						serverConnection.sendResponse(new Responses(2, players.get(r.getPlayer()).cards), r.getPlayer());
					} else if (r.getResponse() == 3) {
						//Calculating Points
						serverConnection.sendResponse(new Responses(3, players.get(r.getPlayer()).dealerCards), r.getPlayer());
						int winner = compareHand(players.get(r.getPlayer()).cards, players.get(r.getPlayer()).dealerCards);

						//Tie
						if (winner == 0) {
							System.out.println("Tie");

							int check = evalCards(players.get(r.getPlayer()).cards);
							int pairCalc = 0;
							String msg = "Player Wins and";
							if (check == 5) {
								msg.concat(" Wins pair plus");
								pairCalc = 40 * players.get(r.getPlayer()).getPair();
							} else if (check == 4) {
								msg.concat(" Wins pair plus");
								pairCalc = 30 * players.get(r.getPlayer()).getPair();
							} else if (check == 3) {
								msg.concat(" Wins pair plus");
								pairCalc = 6 * players.get(r.getPlayer()).getPair();
							} else if (check == 2) {
								msg.concat(" Wins pair plus");
								pairCalc = 3 * players.get(r.getPlayer()).getPair();
							} else if (check == 1) {
								msg.concat(" Wins pair plus");
								pairCalc = 1 * players.get(r.getPlayer()).getPair();
							} else if (check == 0) {
								msg.concat(" Loses pair plus");
								pairCalc = 0;
							}

							serverConnection.sendResponse(new Responses(4, players.get(r.getPlayer()).getAnte(), pairCalc, r.getPlayer()), r.getPlayer());
						}
						//Player 1 Wins
						else if (winner == 1) {
							System.out.println("P1 Win");
							int anteCalc = players.get(r.getPlayer()).getAnte() * 4;
							int check = evalCards(players.get(r.getPlayer()).cards);
							int pairCalc = 0;
							String msg = "Player Wins and";
							if (check == 5) {
								msg.concat(" Wins pair plus");
								pairCalc = 40 * players.get(r.getPlayer()).getPair();
							} else if (check == 4) {
								msg.concat(" Wins pair plus");
								pairCalc = 30 * players.get(r.getPlayer()).getPair();
							} else if (check == 3) {
								msg.concat(" Wins pair plus");
								pairCalc = 6 * players.get(r.getPlayer()).getPair();
							} else if (check == 2) {
								msg.concat(" Wins pair plus");
								pairCalc = 3 * players.get(r.getPlayer()).getPair();
							} else if (check == 1) {
								msg.concat(" Wins pair plus");
								pairCalc = 1 * players.get(r.getPlayer()).getPair();
							} else if (check == 0) {
								msg.concat(" Loses pair plus");
								pairCalc = 0;
							}
							serverConnection.sendResponse(new Responses(5, anteCalc, pairCalc, r.getPlayer()), r.getPlayer());
						}
						//Dealer Wins
						else if (winner == -1) {
							String msg = "Dealer Wins!";
							System.out.println("Dealer Win");
							int pairCalc = 0;
							int check = evalCards(players.get(r.getPlayer()).cards);
							if (check == 5) {
								msg.concat(" Wins pair plus");
								pairCalc = 40 * players.get(r.getPlayer()).getPair();
							} else if (check == 4) {
								msg.concat(" Wins pair plus");
								pairCalc = 30 * players.get(r.getPlayer()).getPair();
							} else if (check == 3) {
								msg.concat(" Wins pair plus");
								pairCalc = 6 * players.get(r.getPlayer()).getPair();
							} else if (check == 2) {
								msg.concat(" Wins pair plus");
								pairCalc = 3 * players.get(r.getPlayer()).getPair();
							} else if (check == 1) {
								msg.concat(" Wins pair plus");
								pairCalc = 1 * players.get(r.getPlayer()).getPair();
							} else if (check == 0) {
								msg.concat(" Loses pair plus");
								pairCalc = 0;
							}

							serverConnection.sendResponse(new Responses(6, 0, pairCalc, r.getPlayer()), r.getPlayer());
						}
						//Dealer does not have queen or higher
						else if (winner == 2) {
							String msg = "Dealer does not have queen or higher and";
							System.out.println("Dealer Doesnt have queen or high");

							int pairCalc = 0;
							int check = evalCards(players.get(r.getPlayer()).cards);
							if (check == 5) {
								msg.concat(" Wins pair plus");
								pairCalc = 40 * players.get(r.getPlayer()).getPair();
							} else if (check == 4) {
								msg.concat(" Wins pair plus");
								pairCalc = 30 * players.get(r.getPlayer()).getPair();
							} else if (check == 3) {
								msg.concat(" Wins pair plus");
								pairCalc = 6 * players.get(r.getPlayer()).getPair();
							} else if (check == 2) {
								msg.concat(" Wins pair plus");
								pairCalc = 3 * players.get(r.getPlayer()).getPair();
							} else if (check == 1) {
								msg.concat(" Wins pair plus");
								pairCalc = 1 * players.get(r.getPlayer()).getPair();
							} else if (check == 0) {
								msg.concat(" Loses pair plus");
								pairCalc = 0;
							}

							serverConnection.sendResponse(new Responses(7, players.get(r.getPlayer()).getAnte(), pairCalc, r.getPlayer()), r.getPlayer());
						}

						players.get(r.getPlayer()).resetCards();

					} else if (r.getResponse() == 4) {

					} else if (r.getResponse() == 21) {
						players.add(new Player());
						serverConnection.sendResponse(new Responses(20, Integer.toString(cc)), cc);
						cc++;
					} else if (r.getResponse() == 400) {
						listItems.getItems().add(r.getMessage());
						clientsCount--;
						clientTot.setText(Integer.toString(clientsCount));
					} else if (r.getResponse() == 404) {
						listItems.getItems().add(r.getMessage());
					}
				});
			});
			} else {
				toggleButton.setStyle("-fx-background-color: red;");
			}
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
	public int evalCards(ArrayList<Card> c) {
		Collections.sort(c);
		if(c.get(0).getValue() == c.get(1).getValue() - 1 && c.get(1).getValue() == c.get(2).getValue() - 1) {
			if(c.get(0).getSuit() == c.get(1).getSuit() && c.get(1).getSuit() == c.get(2).getSuit()) {
				//Straight Flush
				return 5;
			}
			//Straight
			return 3;
		}
		else if(c.get(0).getValue() == c.get(1).getValue() && c.get(1).getValue() == c.get(2).getValue()) {
			//3 of a kind
			return 4;
		}
		if(c.get(0).getSuit() == c.get(1).getSuit() && c.get(1).getSuit() == c.get(2).getSuit()) {
			//Flush
			return 2;
		}
		else if(c.get(0).getValue() == c.get(1).getValue() || c.get(1).getValue() == c.get(2).getValue() || c.get(0).getValue() == c.get(2).getValue()) {
			//Pair
			return 1;
		}
		return 0;
	}
	
	/*Here is the ranking of poker hands from lowest to highest:
    High card: The hand contains no pair or any other combination of cards. The hand is ranked by the highest card, with an ace being the highest and a two being the lowest.
    One pair: The hand contains two cards of the same rank.
    Two pair: The hand contains two different pairs of cards.
    Three of a kind: The hand contains three cards of the same rank.
            Straight: The hand contains five cards of sequential rank, but not of the same suit.
            Flush: The hand contains any five cards of the same suit, but not in sequence.
    Full house: The hand contains three cards of one rank and two cards of another rank.
    Four of a kind: The hand contains four cards of the same rank.
    Straight flush: The hand contains five cards of sequential rank, all of the same suit.
    Royal flush: The hand contains the Ace, King, Queen, Jack, and 10 of the same suit.*/

    public int compareHand(ArrayList<Card> playerHand, ArrayList<Card> dealerHand) {
    	/* Tie: 0
    	 * Player Wins: 1
    	 * Dealer Wins: -1
    	 * Dealer does not have queen or high: 2
    	 */
    	
    	
        int playerRank = evalCards(playerHand);
        int dealerRank = evalCards(dealerHand);
        
        
        if(dealerHand.get(0).getValue() >= 11 || dealerHand.get(1).getValue() >= 11 || dealerHand.get(2).getValue() >= 11) {
        	if (playerRank > dealerRank) {
                // Player wins
                return 1;
            } else if (playerRank < dealerRank) {
                // Dealer wins
                return -1;
            } else {
                // Same rank, compare high card
                Collections.sort(playerHand);
                Collections.sort(dealerHand);
                if (playerHand.get(2).getValue() > dealerHand.get(2).getValue()) {
                	// Player wins
                    return 1;
                } else if (playerHand.get(2).getValue() < dealerHand.get(2).getValue()) {
                	// Dealer wins
                    return -1;
                }
            }
            // Tie
            return 0;
        }
        else {
        	return 2;
        }
    }
	
}

