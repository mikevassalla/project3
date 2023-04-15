import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


public class GuiServerTests {

    @Test
    public void testEvalCardsStraightFlush() {
        Deck deck = new Deck();
        GuiServer server = new GuiServer();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[0], 1));
        cards.add(new Card(deck.suits[0], 2));
        cards.add(new Card(deck.suits[0], 3));
        int result = server.evalCards(cards);
        assertEquals(5, result, "Straight flush was incorrect");
    }

    @Test
    public void testStraight() {
        Deck deck = new Deck();
        GuiServer server = new GuiServer();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[3], 2));
        cards.add(new Card(deck.suits[0], 3));
        cards.add(new Card(deck.suits[2], 4));
        int result = server.evalCards(cards);
        assertEquals(3, result, "Straight was incorrect");
    }

    @Test
    public void testThreeOfAKind() {
        Deck deck = new Deck();
        GuiServer server = new GuiServer();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[0], 8));
        cards.add(new Card(deck.suits[1], 8));
        cards.add(new Card(deck.suits[2], 8));
        int result = server.evalCards(cards);
        assertEquals(4, result, "Three of a kind of incorrect");
    }

    @Test
    public void testFlush() {
        Deck deck = new Deck();
        GuiServer server = new GuiServer();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[2], 2));
        cards.add(new Card(deck.suits[2], 5));
        cards.add(new Card(deck.suits[2], 10));
        int result = server.evalCards(cards);
        assertEquals(2, result, "Flush was incorrect");
    }

    @Test
    public void testPair() {
        Deck deck = new Deck();
        GuiServer server = new GuiServer();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[3], 6));
        cards.add(new Card(deck.suits[2], 6));
        cards.add(new Card(deck.suits[0], 9));
        int result = server.evalCards(cards);
        assertEquals(1, result, "Pair was incorrect");
    }

    @Test
    public void testNoRank() {
        Deck deck = new Deck();
        GuiServer server = new GuiServer();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[3], 2));
        cards.add(new Card(deck.suits[2], 5));
        cards.add(new Card(deck.suits[0], 10));
        int result = server.evalCards(cards);
        assertEquals(0, result, "No rank was incorrect");
    }
}
