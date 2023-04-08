import javafx.scene.image.ImageView;

public class Card {
    private char suit;
    private int value;
    private ImageView card;

    public Card(char suit, int value, ImageView card) {
        this.suit = suit;
        this.value = value;
        this.card = card;
    }

    public char getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    // Defines the suites DO WE NEED KING/QUEEN
    public char getSuites() {
        return this.suit;
    }

    //Getter for value of card
    public int getValues() {
        return this.value;
    }
    
    //Getter for card
    public ImageView getImage() {
    	return this.card;
    }
}