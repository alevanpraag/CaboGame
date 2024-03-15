// Card.java
package com.cabo.cardgame.models;
public class Card {
    public enum CardPower {
        LOOK_OWN, // For 7 and 8
        LOOK_OTHER, // For 9 and 10
        SWAP_CARDS, // For J and Q
        NONE // For cards without powers
    }
    private final String suit;
    private final String rank;
    private CardPower power;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
        this.power = determinePower(rank);
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

    private CardPower determinePower(String rank) {
        switch(rank) {
            case "7": case "8": return CardPower.LOOK_OWN;
            case "9": case "10": return CardPower.LOOK_OTHER;
            case "J": case "Q": return CardPower.SWAP_CARDS;
            default: return CardPower.NONE;
        }
    }

    // Getter for power
    public CardPower getPower() {
        return this.power;
    }

    public void removerPower() {
        this.power = CardPower.NONE;
    }
}