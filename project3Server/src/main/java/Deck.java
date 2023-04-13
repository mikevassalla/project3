import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Deck {
    private ArrayList<Card> deck;
    public final char[] suits = {'C', 'D', 'H', 'S'};
    public final int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    public Deck() {
        newDeck();
    }

    // The following initializes a new deck of 52 cards by iterating through each suit and value
    // and adding a new Card object to the deck.
    public void newDeck() {
        deck = new ArrayList<Card>();
        for (char suit : suits) {
            for (int value : values) {
                deck.add(new Card(suit, value));
            }
        }
    }

    // The following shuffles the deck using the Collections.shuffle() method.
    public void shuffleCards() {
        Collections.shuffle(deck);
    }

    // The following removes and returns the first card from the deck, and throws an exception if the deck is empty.
    public ArrayList<Card> dealCard() {
    	ArrayList<Card> cards = new ArrayList<>();
        if (deck.size() == 0) {
            System.out.println("Deck is empty, cannot deal card.");
            return cards;
        }
        for(int i = 0; i < 3; i++) {
        	cards.add(deck.remove(0));
        }
        return cards;
    }
    
    ArrayList<Card> getDeck() {
    	return deck;
    }
}
