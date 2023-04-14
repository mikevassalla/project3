import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	String player;
	ArrayList<Card> cards = new ArrayList<>();
	private int pair;
	private int ante;
	private int play;
	Deck deck = new Deck();
	ArrayList<Card> dealerCards = new ArrayList<>();

	
	public Player() {
		deck.shuffleCards();
	}
	
	public void playerAddCards() {
		cards.add(deck.remove());
		cards.add(deck.remove());
		cards.add(deck.remove());
	}
	
	public void setPlayer(String p) {
		player = p;
	}
	
	public void dealerAddCards() {
		dealerCards.add(deck.remove());
		dealerCards.add(deck.remove());
		dealerCards.add(deck.remove());
	}
	
	public void resetCards() {
		deck.add(cards.remove(0));
		deck.add(cards.remove(0));
		deck.add(cards.remove(0));
		deck.add(dealerCards.remove(0));
		deck.add(dealerCards.remove(0));
		deck.add(dealerCards.remove(0));
		deck.shuffleCards();
	}
	
	public void setPair(int n) {
		pair = n;
	}
	public void setPlay(int n) {
		play = n;
	}
	public void setAnte(int n) {
		ante = n;
	}
	public int getPlay() {
		return play;
	}
	public int getPair() {
		return pair;
	}
	public int getAnte() {
		return ante;
	}
}