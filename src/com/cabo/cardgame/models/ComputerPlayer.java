package com.cabo.cardgame.models;
import com.cabo.cardgame.Game;

public class ComputerPlayer extends Player {
    public ComputerPlayer(String name) {
        super(name);
    }

    public void makeDecision(Game game, Card drawnCard) {
        // Example: AI logic to decide an action based on the current game state
        // This is highly dependent on your game's rules
        System.out.println(getName() + " is thinking...");

        // Decide whether to keep the drawn card based on some criteria
        if (shouldKeepCard(drawnCard)) {
            // Keep the card (swap with one in hand or take some action)
            System.out.println(getName() + " decides to keep the drawn card.");
        } else {
            // Discard or ignore the drawn card
            System.out.println(getName() + " discards the drawn card.");
        }
    }
    private boolean shouldKeepCard(Card drawnCard) {
        // Implement logic to determine if the drawn card should be kept
        // This could be based on the card's value, the current hand's total score, etc.
        return false; // Placeholder return value
    }
}
