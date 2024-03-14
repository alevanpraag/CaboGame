// Player.java
package com.cabo.cardgame.models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;

    private boolean cardDrawn = false;

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

    public String getName(){
        return name;
    }

    public boolean isHuman(){
        return name.equals("Human");
    }

    public boolean drewCard() {
        return cardDrawn;
    }

    public void draw(boolean didDraw) {
        cardDrawn = didDraw;
    }

    // Getter methods and any other relevant methods
}

