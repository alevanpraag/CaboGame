package com.cabo.cardgame;

import com.cabo.cardgame.models.Card;
import com.cabo.cardgame.models.ComputerPlayer;
import com.cabo.cardgame.models.Deck;
import com.cabo.cardgame.models.Player;
import com.cabo.cardgame.listeners.GameEventListener;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameEventListener eventListener;
    private List<Player> players;
    private Deck deck;
    private int currentPlayerIndex = 0;
    private boolean gameState;
    private List<Card> trashPile;

    public Game(int numberOfDecks) {
        players = new ArrayList<>();
        deck = new Deck(numberOfDecks);
        gameState = true;
        trashPile = new ArrayList<>();

        // Initialize players
        players.add(new Player("Human")); // Human player
        for (int i = 1; i <= 3; i++) {
            players.add(new ComputerPlayer("Computer " + i)); // Computer players
        }

        // Example: Dealing cards to players
        dealCards();
        Collections.shuffle(players);
    }

    private void dealCards() {
        // Iterate through each player
        for (Player player : players) {
            // Deal 4 cards to this player
            for (int i = 0; i < 4; i++) {
                Card card = deck.deal();
                if (card != null) { // Check if the deck is not empty
                    player.receiveCard(card);
                } else {
                    // Handle case where deck is empty before each player has 4 cards
                    System.out.println("The deck does not have enough cards.");
                    return;
                }
            }
        }
    }

    // Method to get the list of players
    public List<Player> getPlayers() {
        return players;
    }

    public void setGameEventListener(GameEventListener listener) {
        this.eventListener = listener;
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        // Logic to determine whose turn it is...
        if (players.get(currentPlayerIndex).isHuman()) {
            if (eventListener != null) {
                eventListener.onHumanPlayerTurn();
                System.out.print("playing:");
                System.out.print(getCurrentPlayer().getName());

            }
        } else {
            if (eventListener != null) {
                eventListener.onComputerPlayerTurn();
                System.out.print("playing:");
                System.out.print(getCurrentPlayer().getName());
                executeComputerTurn();
            }
            // Additional logic for handling computer turn...
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean isGameOver() {
        return !gameState;
    }
    // Methods to draw a card from the drawDeck and to add a card to the trashPile
    public Card drawCard() {
        return deck.deal();
    }

    public void addToTrashPile(Card card) {
        trashPile.add(card);
    }

    // Getters for UI update
    public Card getTopCardOfTrashPile() {
        return trashPile.isEmpty() ? null : trashPile.get(trashPile.size() - 1);
    }

    public boolean trashEmpty(){
        return trashPile.isEmpty();
    }

    private void executeComputerTurn() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            Card card = drawCard();
            addToTrashPile(card);

            nextTurn(); // Proceed to the next turn after a brief pause
        });
        pause.play();
    }

}

