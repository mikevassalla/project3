import java.util.ArrayList;

public class Player {
	ArrayList<Card> cards = new ArrayList<>();
	
	public Player() {
		
	}
	
	public void playerAddCards(ArrayList<Card> c) {
		cards.add(c.remove(0));
		cards.add(c.remove(0));
		cards.add(c.remove(0));
	}
	
	public void dealerAddCards(ArrayList<Card> c) {
		cards = c;
	}
}