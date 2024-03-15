package com.cabo.cardgame.models;
import com.cabo.cardgame.Game;

import java.util.*;

public class ComputerPlayer extends Player {
    private Set<Card> rememberedCards = new HashSet<>();
    public ComputerPlayer(String name) {
        super(name);

    }

    public void rememberInitialCards() {
        if (getHand().size() >= 2) {
            rememberedCards.add(getHand().get(0)); // Remember the first card
            rememberedCards.add(getHand().get(getHand().size() - 1)); // Remember the last card
        }
    }

    public void rememberDrawnCard(Card card) {
        rememberedCards.add(card);
    }

    public void forgetCard(Card card) {
        rememberedCards.remove(card);
    }

    // This method is specific to ComputerPlayer
    public Card getHighestValueCard() {
        Card highestValueCard = null;
        int highestScore = -1; // Assuming the lowest score a card can have is 0

        for (Card card : rememberedCards) {
            int score = getCardScore(card);
            if (score > highestScore) {
                highestScore = score;
                highestValueCard = card;
            }
        }

        return highestValueCard;
    }

    public void makeDecision(Game game, Card drawnCard) {
        System.out.println(getName() + " is thinking...");

        boolean allCardsRemembered = getHand().stream().allMatch(this::isCardRemembered);

        if (allCardsRemembered) {
            // If all cards in the hand are remembered, decide whether to keep the drawn card.
            if (shouldKeepCard(drawnCard)) {
                // If deciding to keep the drawn card, swap it with the highest value remembered card.
                game.selectedCard = getHighestValueCard();
                System.out.print("Computer remembers all cards, keeps the drawn card, and tosses: ");
            } else {
                // If not keeping the drawn card, simply add it to the trash pile.
                game.addToTrashPile(drawnCard);
                System.out.print("Computer remembers all cards and discards the drawn card: ");
            }
        } else {
            game.selectedCard = getUnknownCard(); // Get the first unknown card to swap.
            System.out.print("Computer finds an unknown card, keeps the drawn card, and tosses an unknown card: ");
        }

        // Common logic after decision
        if (game.selectedCard != null) {
            rememberDrawnCard(drawnCard); // Remember the drawn card.
            forgetCard(game.selectedCard); // Forget the card being discarded.
            game.handleEndOfTurn();
            System.out.println(game.selectedCard.toString());
        } else {
            System.out.println(drawnCard.toString());
        }
    }

    private boolean isCardRemembered(Card card) {
        // Implement this method based on your ComputerPlayer's memory management
        return rememberedCards.contains(card);
    }

    private Card getUnknownCard() {
        // Implement this method to return the first card in the hand that is not remembered
        for (Card card : getHand()) {
            if (!rememberedCards.contains(card)) {
                return card; // Return the first unknown card found
            }
        }
        return null; // Fallback if somehow all cards are known, adjust logic as needed
    }
    private int getCardScore(Card card) {
        String rank = card.getRank();
        String suit = card.getSuit();
        int score = 0;
        switch (rank) {
            case "A": return 1;
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "J": return 11;
            case "Q": return 12;
            case "K":
                if (suit.equals("Spades") || suit.equals("Clubs")) {
                    return 13;
                } else {
                    return 0;
                }
            case "X": return -1;
            default: return 0; // Handle unexpected cases
        }
    }

    public boolean shouldKeepCard(Card drawnCard) {
        int drawnCardScore = getCardScore(drawnCard);
        for (Card card : rememberedCards) {
            int cardScore = getCardScore(card);
            if (drawnCardScore < cardScore) {
                return true; // The drawn card is smaller in value than at least one card in the hand
            }
        }
        return false; // No card in the hand has a smaller value than the drawn card
    }
}
