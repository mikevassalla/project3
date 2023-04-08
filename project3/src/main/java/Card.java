public class Card {
    private char suit;
    private int value;

    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public char getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    // Defines the suites DO WE NEED KING/QUEEN
    public static char[] getSuites() {
        return new char[] {'C', 'D', 'H', 'S'};
    }

    // Defines the values of the cards
    public static int[] getValues() {
        return new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    }
}