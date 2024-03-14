// Card.java
package com.cabo.cardgame.models;
public class Card {
    private final String suit;
    private final String rank;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "Rank_" + rank + "." + "Suit_" + suit;
    }

    public String getImagePath() {
        return "/CardImages/" + this + ".png";
    }
}