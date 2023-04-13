import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	String player;
	ArrayList<Card> cards = new ArrayList<>();
	private int pair;
	private int ante;
	
	public Player() {
		
	}
	
	public void playerAddCards(ArrayList<Card> c) {
		cards.add(c.remove(0));
		cards.add(c.remove(0));
		cards.add(c.remove(0));
	}
	
	public void setPlayer(String p) {
		player = p;
	}
	
	public void dealerAddCards(ArrayList<Card> c) {
		cards = c;
	}
	
	public void setPair(int n) {
		pair = n;
	}
	public void setAnte(int n) {
		ante = n;
	}
	public int getPair() {
		return pair;
	}
	public int getAnte() {
		return ante;
	}
}