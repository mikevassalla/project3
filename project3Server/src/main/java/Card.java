import java.io.Serializable;

public class Card implements Serializable{
	private static final long serialVersionUID = 1L;
    private char suit;
    private int value;
    private String cardName;

    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
        this.cardName = suit + Integer.toString(value) + ".png";
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
    public String getImageName() {
    	return this.cardName;
    }
}