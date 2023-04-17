//  Project 3 -  3 Card Poker
//  Daniel Beben - Dbeben2 & Micheal Vassalla mvassa4
//  CS342 Spring 2023
// This project you will implement a networked version of the popular casino game 3 Card Poker.
// The focus of the project is event driven programing and networking with Java Sockets.

import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
	ObservableList<gridPlayer> clientsList = FXCollections.observableArrayList();
	private TableView<gridPlayer> tableView = new TableView<>();
	private int clientsCount = 0;
	ArrayList<PokerInfo> players = new ArrayList<>();
	int cc = 0;
	
	ListView<String> listItems;
	int count = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Font fontC = Font.font("Arial", FontWeight.BOLD, 16);

		TableColumn<gridPlayer, String> clientColumn = new TableColumn<>("Client");
		clientColumn.setStyle("-fx-font-family: " + fontC.getFamily() + "; -fx-font-size: " + fontC.getSize() + "px; -fx-font-weight: " + fontC.getStyle());
		clientColumn.setCellValueFactory(new PropertyValueFactory<gridPlayer, String>("client"));

		TableColumn<gridPlayer, String> resultColumn = new TableColumn<>("Win/Loss");
		resultColumn.setStyle("-fx-font-family: " + fontC.getFamily() + "; -fx-font-size: " + fontC.getSize() + "px; -fx-font-weight: " + fontC.getStyle());
		resultColumn.setCellValueFactory(new PropertyValueFactory<gridPlayer, String>("wl"));

		TableColumn<gridPlayer, String> betColumn = new TableColumn<>("Bet");
		betColumn.setStyle("-fx-font-family: " + fontC.getFamily() + "; -fx-font-size: " + fontC.getSize() + "px; -fx-font-weight: " + fontC.getStyle());
		betColumn.setCellValueFactory(new PropertyValueFactory<gridPlayer, String>("bet"));
		
		TableColumn<gridPlayer, String> winLossColumn = new TableColumn<>("Is Playing");
		winLossColumn.setStyle("-fx-font-family: " + fontC.getFamily() + "; -fx-font-size: " + fontC.getSize() + "px; -fx-font-weight: " + fontC.getStyle());
		winLossColumn.setCellValueFactory(new PropertyValueFactory<gridPlayer, String>("isPlaying"));
		
		tableView.getColumns().addAll(clientColumn, resultColumn, betColumn, winLossColumn);

		// Set the table's column resize policy to CONSTRAINED_RESIZE_POLICY
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Add the columns to the table
		tableView.getItems().clear();
        tableView.getItems().addAll(clientsList);

		// Create a VBox to hold the TableView
		VBox tableResults = new VBox(tableView);
		tableResults.setAlignment(Pos.CENTER);

		listItems = new ListView<>();
		Label clientText = new Label("Client Information");
		Color colorC = Color.WHITE;
		clientText.setTextFill(colorC);
		clientText.setFont(fontC);
		VBox clientInfo = new VBox(clientText,listItems);
		clientInfo.setAlignment(Pos.CENTER);

		TextField portField = new TextField();
		portField.setPromptText("Enter port here");
		portField.setFont(fontC);
		portField.setAlignment(Pos.CENTER);
		Button startListen = new Button("Start Listening");
		startListen.setFont(fontC);

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
		Font fontT = Font.font("Arial", FontWeight.BOLD, 16);
		toggleButton.setFont(fontT);
		toggleButton.setStyle("-fx-background-color: red;");

		toggleButton.setOnAction(event -> {
			if (toggleButton.isSelected()) {
				// Set the style to green when the button is toggled on
				toggleButton.setStyle("-fx-background-color: green;");
			} else {
				// Set the style to red when the button is toggled off
				toggleButton.setStyle("-fx-background-color: red;");
			}
		});


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

		Image image = new Image(getClass().getClassLoader().getResourceAsStream("room.jpg"));


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
		serverConnection = new Server(Integer.parseInt(portField.getText()), x -> {
			Responses r = (Responses) x;
			Platform.runLater(()->{
				if(r.getResponse() == 1) {
					//Updating Client Info
					listItems.getItems().add(r.getMessage());
					clientsCount++;
					clientTot.setText(Integer.toString(clientsCount));
				}
				else if(r.getResponse() == 2) {
					//Deal Cards
					players.get(r.getPlayer()).setAnte(r.getAnte());
					players.get(r.getPlayer()).setPair(r.getPair());
					players.get(r.getPlayer()).playerAddCards();

					players.get(r.getPlayer()).dealerAddCards();
					ArrayList<String> t = convert(players.get(r.getPlayer()).cards);
					serverConnection.sendResponse(new Responses(2, t, ""), r.getPlayer());
					clientsList.get(r.getPlayer()).isPlaying = "True";
					updateGrid(tableView, clientsList);
				}
				else if(r.getResponse() == 3) {
					//Calculating Points
					ArrayList<String> n= convert(players.get(r.getPlayer()).dealerCards);
					serverConnection.sendResponse(new Responses(3, n, ""), r.getPlayer());
					int winner = compareHand(players.get(r.getPlayer()).cards, players.get(r.getPlayer()).dealerCards);
					
					//Tie
					if(winner == 0) {
						int check = evalCards(players.get(r.getPlayer()).cards);
						int pairCalc = 0;
						
						String msg = "Ties and";
						if(check == 5) {
							msg += " Wins pair plus";
							pairCalc = 40 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 4) {
							msg += " Wins pair plus";
							pairCalc = 30 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 3) {
							msg += " Wins pair plus";
							pairCalc = 6 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 2) {
							msg += " Wins pair plus";
							pairCalc = 3 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 1) {
							msg += " Wins pair plus";
							pairCalc = 1 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 0) {
							msg += " Loses pair plus";
							pairCalc = 0;
						}
						else {
							msg += "Should Not be here";
						}
						clientsList.get(r.getPlayer()).bet = Integer.toString(players.get(r.getPlayer()).getAnte() + players.get(r.getPlayer()).getAnte() + players.get(r.getPlayer()).getPair());
						clientsList.get(r.getPlayer()).wl = msg + "\n" + Integer.toString(0);
						updateGrid(tableView, clientsList);
						serverConnection.sendResponse(new Responses(8, msg), r.getPlayer());
						serverConnection.sendResponse(new Responses(4, players.get(r.getPlayer()).getAnte(), pairCalc, r.getPlayer()), r.getPlayer());
					}
					//Player 1 Wins
					else if(winner == 1) {

						int anteCalc = players.get(r.getPlayer()).getAnte() * 4;
						int check = evalCards(players.get(r.getPlayer()).cards);
						int pairCalc = 0;
						String msg = "Player Wins and";
						if(check == 5) {
							msg += " Wins pair plus";
							pairCalc = 40 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 4) {
							msg += " Wins pair plus";
							pairCalc = 30 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 3) {
							msg += " Wins pair plus";
							pairCalc = 6 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 2) {
							msg += " Wins pair plus";
							pairCalc = 3 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 1) {
							msg += " Wins pair plus";
							pairCalc = 1 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 0) {
							msg += " Loses pair plus";
							pairCalc = 0;
						}
						else {
							msg.concat("SHould Not be here");
						}
						clientsList.get(r.getPlayer()).bet = Integer.toString(players.get(r.getPlayer()).getAnte() + players.get(r.getPlayer()).getAnte() + players.get(r.getPlayer()).getPair());
						clientsList.get(r.getPlayer()).wl = msg + "\n" + Integer.toString(anteCalc + pairCalc);
						updateGrid(tableView, clientsList);
						serverConnection.sendResponse(new Responses(8, msg), r.getPlayer());
						serverConnection.sendResponse(new Responses(5, anteCalc, pairCalc, r.getPlayer()), r.getPlayer());
					}
					//Dealer Wins
					else if(winner == -1) {
						String msg = "Dealer Wins!";

						int pairCalc = 0;
						int check = evalCards(players.get(r.getPlayer()).cards);
						if(check == 5) {
							msg += " Wins pair plus";
							pairCalc = 40 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 4) {
							msg += " Wins pair plus";
							pairCalc = 30 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 3) {
							msg += " Wins pair plus";
							pairCalc = 6 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 2) {
							msg += " Wins pair plus";
							pairCalc = 3 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 1) {
							msg += " Wins pair plus";
							pairCalc = 1 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 0) {
							msg += " Loses pair plus";
							pairCalc = 0;
						}
						else {
							msg.concat("SHould Not be here");
						}
						clientsList.get(r.getPlayer()).bet = Integer.toString(players.get(r.getPlayer()).getAnte() + players.get(r.getPlayer()).getAnte() + players.get(r.getPlayer()).getPair());
						clientsList.get(r.getPlayer()).wl = msg + "\n" + Integer.toString(pairCalc - players.get(r.getPlayer()).getAnte() - players.get(r.getPlayer()).getAnte());
						updateGrid(tableView, clientsList);
						serverConnection.sendResponse(new Responses(8, msg), r.getPlayer());
						serverConnection.sendResponse(new Responses(6, 0, pairCalc, r.getPlayer()), r.getPlayer());
					}
					//Dealer does not have queen or higher
					else if(winner == 2) {
						String msg = "Dealer does not have queen or higher and";

						int pairCalc = 0;
						int check = evalCards(players.get(r.getPlayer()).cards);
						if(check == 5) {
							msg += " Wins pair plus";
							pairCalc = 40 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 4) {
							msg += " Wins pair plus";
							pairCalc = 30 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 3) {
							msg += " Wins pair plus";
							pairCalc = 6 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 2) {
							msg += " Wins pair plus";
							pairCalc = 3 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 1) {
							msg += " Wins pair plus";
							pairCalc = 1 * players.get(r.getPlayer()).getPair();
						}
						else if(check == 0) {
							msg += " Loses pair plus";
							pairCalc = 0;
						}
						else {
							msg += " Should Not be here";
						}
						clientsList.get(r.getPlayer()).bet = Integer.toString(players.get(r.getPlayer()).getAnte() + players.get(r.getPlayer()).getAnte() + players.get(r.getPlayer()).getPair());
						clientsList.get(r.getPlayer()).wl = msg + "\n" + Integer.toString(players.get(r.getPlayer()).getAnte()+ players.get(r.getPlayer()).getAnte() + pairCalc);
						updateGrid(tableView, clientsList);
						serverConnection.sendResponse(new Responses(8, msg), r.getPlayer());
						serverConnection.sendResponse(new Responses(7, players.get(r.getPlayer()).getAnte(), pairCalc, r.getPlayer()), r.getPlayer());
					}
					
					players.get(r.getPlayer()).resetCards();
					
				}
				else if(r.getResponse() == 5) {
					clientsList.get(r.getPlayer()).isPlaying = "False";
					updateGrid(tableView, clientsList);
					players.set(r.getPlayer(), new PokerInfo());
				}
				else if(r.getResponse() == 21) { 
					players.add(new PokerInfo());
					clientsList.add(new gridPlayer());
					clientsList.get(cc).client = "Client #" + cc;
					clientsList.get(cc).isPlaying = "True";
					updateGrid(tableView, clientsList);
					serverConnection.sendResponse(new Responses(20, Integer.toString(cc)), cc);
					cc++;
				}
				else if(r.getResponse() == 400) {
					listItems.getItems().add(r.getMessage());
					clientsCount--;
					clientTot.setText(Integer.toString(clientsCount));
				}
				else if(r.getResponse() == 404) {
					listItems.getItems().add(r.getMessage());
				}
			});
		});
			} else {
				toggleButton.setStyle("-fx-background-color: red;");
			}
		});


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
	
	public ArrayList<String> convert(ArrayList<Card> c) {
		ArrayList<String> n = new ArrayList<>();
		for(int i = 0; i < 3; i++) {
			n.add(c.get(i).getImageName());
		}
		return n; 
	}
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
    
    public void updateGrid(TableView<gridPlayer> t, ObservableList<gridPlayer> g) {
    	t.getItems().clear();
        t.getItems().addAll(g);
    }
    
	public class gridPlayer {
		String client;
		String wl;
		String bet;
		String isPlaying;
		
		
		public gridPlayer() {
			client = "";
			wl = "";
			bet = "";
			isPlaying = "";
		}
		
		public String getClient() {
			return client;
		}
		public String getWl() {
			return wl;
		}
		public String getBet() {
			return bet;
		}
		public String getIsPlaying() {
			return isPlaying;
		}
	}
}
