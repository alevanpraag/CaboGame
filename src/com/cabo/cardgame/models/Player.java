// Player.java
package com.cabo.cardgame.models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private boolean cardDrawn ;

    private boolean cabo;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.cardDrawn = false;
        this.cabo = false;
    }

    // Method to add a card to the player's hand
    public void receiveCard(Card card) {
        hand.add(card);
    }

    public boolean hasCard(Card targetCard) {
        return hand.stream().anyMatch(card -> card.equals(targetCard));
    }

    public int indexOfCard(Card targetCard) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).equals(targetCard)) {
                return i; // Card found, return its index
            }
        }
        return -1; // Card not found
    }

    public void replaceCardAtIndex(int index, Card newCard) {
        if (index >= 0 && index < hand.size()) {
            hand.set(index, newCard);
        } else {
            System.out.println("Invalid index, cannot replace card.");
        }
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

    public boolean calledCabo(){
        return cabo;
    }

    public void callCabo(){
        System.out.println(getName() + "called Cabo!");
        cabo = true;
    }

    public int getScore() {
        int score = 0;
        for (Card card : hand) {
            String rank = card.getRank();
            String suit = card.getSuit();

            switch (rank) {
                case "A":
                    score += 1;
                    break;
                case "2":
                    score += 2;
                    break;
                case "3":
                    score += 3;
                    break;
                case "4":
                    score += 4;
                    break;
                case "5":
                    score += 5;
                    break;
                case "6":
                    score += 6;
                    break;
                case "7":
                    score += 7;
                    break;
                case "8":
                    score += 8;
                    break;
                case "9":
                    score += 9;
                    break;
                case "10":
                    score += 10;
                    break;
                case "J":
                    score += 11;
                    break;
                case "Q":
                    score += 12;
                    break;
                case "K":
                    // Score 13 only if the suit is Spades or Clubs
                    if (suit.equals("Spades") || suit.equals("Clubs")) {
                        score += 13;
                    }
                    break;
                case "X":
                    score -= 1;
                    break;
                default:
                    // Handle unexpected cases or log a warning
                    break;
            }
        }
        return score;
    }

    public void lookAtOwnCard() {
    }

    public void lookAtOpponentCard(List<Player> players) {
    }

    public void swapCardWithOpponent(List<Player> players) {
    }

    // Getter methods and any other relevant methods
}

