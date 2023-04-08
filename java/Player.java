import java.util.ArrayList;

public class Player {
	ArrayList<Card> cards = new ArrayList<>();
	
	public Player() {
		
	}
	
	public void playerAddCards(Card c1, Card c2, Card c3) {
		cards.add(c1);
		cards.add(c2);
		cards.add(c3);
	}
	
	public void dealerAddCards(ArrayList<Card> c) {
		cards = c;
	}
}