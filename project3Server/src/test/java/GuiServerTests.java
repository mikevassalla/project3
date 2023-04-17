//  Project 3 -  3 Card Poker
//  Daniel Beben - Dbeben2 & Micheal Vassalla mvassa4
//  CS342 Spring 2023
// This project you will implement a networked version of the popular casino game 3 Card Poker.
// The focus of the project is event driven programing and networking with Java Sockets.

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Collections;


public class GuiServerTests {


    public int evalCards(ArrayList<Card> c) {
        Collections.sort(c);
        if(c.get(0).getValue() == c.get(1).getValue() - 1 && c.get(1).getValue() == c.get(2).getValue() - 1) {
            if(c.get(0).getSuit() == c.get(1).getSuit() && c.get(1).getSuit() == c.get(2).getSuit()) {
                //Straight Flush
                return 5;
            }
            //Straight
            return 3;
        }
        else if(c.get(0).getValue() == c.get(1).getValue() && c.get(1).getValue() == c.get(2).getValue()) {
            //3 of a kind
            return 4;
        }
        if(c.get(0).getSuit() == c.get(1).getSuit() && c.get(1).getSuit() == c.get(2).getSuit()) {
            //Flush
            return 2;
        }
        else if(c.get(0).getValue() == c.get(1).getValue() || c.get(1).getValue() == c.get(2).getValue() || c.get(0).getValue() == c.get(2).getValue()) {
            //Pair
            return 1;
        }
        return 0;
    }

    @Test
    public void testEvalCardsStraightFlush() {
        Deck deck = new Deck();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[0], 1));
        cards.add(new Card(deck.suits[0], 2));
        cards.add(new Card(deck.suits[0], 3));
        int result = evalCards(cards);
        assertEquals(5, result, "Straight flush was incorrect");
    }

    @Test
    public void testStraight() {
        Deck deck = new Deck();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[3], 2));
        cards.add(new Card(deck.suits[0], 3));
        cards.add(new Card(deck.suits[2], 4));
        int result = evalCards(cards);
        assertEquals(3, result, "Straight was incorrect");
    }

    @Test
    public void testThreeOfAKind() {
        Deck deck = new Deck();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[0], 8));
        cards.add(new Card(deck.suits[1], 8));
        cards.add(new Card(deck.suits[2], 8));
        int result = evalCards(cards);
        assertEquals(4, result, "Three of a kind of incorrect");
    }

    @Test
    public void testFlush() {
        Deck deck = new Deck();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[2], 2));
        cards.add(new Card(deck.suits[2], 5));
        cards.add(new Card(deck.suits[2], 10));
        int result = evalCards(cards);
        assertEquals(2, result, "Flush was incorrect");
    }

    @Test
    public void testPair() {
        Deck deck = new Deck();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[3], 6));
        cards.add(new Card(deck.suits[2], 6));
        cards.add(new Card(deck.suits[0], 9));
        int result = evalCards(cards);
        assertEquals(1, result, "Pair was incorrect");
    }

    @Test
    public void testNoRank() {
        Deck deck = new Deck();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(deck.suits[3], 2));
        cards.add(new Card(deck.suits[2], 5));
        cards.add(new Card(deck.suits[0], 10));
        int result = evalCards(cards);
        assertEquals(0, result, "No rank was incorrect");
    }
}
