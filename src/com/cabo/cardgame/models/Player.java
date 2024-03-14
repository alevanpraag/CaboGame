// Player.java
package com.cabo.cardgame.models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    // Method to add a card to the player's hand
    public void receiveCard(Card card) {
        hand.add(card);
    }

    // Method to get the player's hand
    public List<Card> getHand() {
        return hand;
    }

    // Getter methods and any other relevant methods
}