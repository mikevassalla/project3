import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
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