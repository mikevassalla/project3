//  Project 3 -  3 Card Poker
//  Daniel Beben - Dbeben2 & Micheal Vassalla mvassa4
//  CS342 Spring 2023
// This project you will implement a networked version of the popular casino game 3 Card Poker.
// The focus of the project is event driven programing and networking with Java Sockets.

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;
    private final char[] suits = {'C', 'D', 'H', 'S'};
    private final int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

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
    
    public void add(Card c) {
    	deck.add(c);
    }
    public Card remove() {
    	return deck.remove(0);
    }
    
    ArrayList<Card> getDeck() {
    	return deck;
    }
}
