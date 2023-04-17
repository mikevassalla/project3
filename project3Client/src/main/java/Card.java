//  Project 3 -  3 Card Poker
//  Daniel Beben - Dbeben2 & Micheal Vassalla mvassa4
//  CS342 Spring 2023
// This project you will implement a networked version of the popular casino game 3 Card Poker.
// The focus of the project is event driven programing and networking with Java Sockets.

import java.io.Serializable;

public class Card implements Serializable, Comparable<Card> {
	private static final long serialVersionUID = 1L;
    private char suit;
    private int value;
    private String cardName;

    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
        this.cardName = suit + Integer.toString(value) + ".png";
    }
    
    @Override
    public int compareTo(Card otherCard) {
        return Integer.compare(this.value, otherCard.value);
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