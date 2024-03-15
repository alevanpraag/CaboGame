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

    public Card drawnCard = null;

    public Card selectedCard = null;

    public Game(int numberOfDecks) {
        players = new ArrayList<>();
        deck = new Deck(numberOfDecks);
        gameState = true;
        trashPile = new ArrayList<>();

        // Initialize players
        players.add(0,new Player("Human")); // Human player
        players.add(1,new ComputerPlayer("Computer East"));
        players.add(2,new ComputerPlayer("Computer North"));
        players.add(3,new ComputerPlayer("Computer West"));

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
            if (player instanceof ComputerPlayer) {
                ComputerPlayer computerPlayer = (ComputerPlayer) player;
                computerPlayer.rememberInitialCards();
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
        if (players.get(currentPlayerIndex).calledCabo()){
            endGame();
            return;
        }
        // Logic to determine whose turn it is...
        if (players.get(currentPlayerIndex).isHuman()) {
            if (eventListener != null) {
                eventListener.onHumanPlayerTurn();
                System.out.print("playing:");
                System.out.println(getCurrentPlayer().getName());

            }
        } else {
            if (eventListener != null) {
                eventListener.onComputerPlayerTurn();
                System.out.print("playing:");
                System.out.println(getCurrentPlayer().getName());
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
        card.removerPower();
        trashPile.add(card);
        if (eventListener != null) {
            eventListener.onTrashPileUpdated();
        }
    }

    public void setDrawnCard(Card card){
        drawnCard = card;
    }

    public void setSelectedCard(Card card){
        selectedCard = card;
    }

    public Card getSelectedCard(Card card){
        return selectedCard;
    }

    public Card getDrawnCard(){
        return drawnCard;
    }

    // Getters for UI update
    public Card getTopCardOfTrashPile() {
        return trashPile.isEmpty() ? null : trashPile.get(trashPile.size() - 1);
    }

    // Method to remove and return the top card of the trash pile
    public Card removeTopCardFromTrashPile() {
        if (!trashPile.isEmpty()) {
            return trashPile.removeLast(); // Removes and returns the last card
        }
        return null; // Indicates there was no card to remove
    }

    public boolean trashEmpty(){
        return trashPile.isEmpty();
    }

    private void executeComputerTurn() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            if (getCurrentPlayer() instanceof ComputerPlayer) {
                ComputerPlayer computerPlayer = (ComputerPlayer) getCurrentPlayer();
                drawnCard = drawCard();
                computerPlayer.makeDecision(this,drawnCard);
            }
            nextTurn(); // Proceed to the next turn after a brief pause
        });
        pause.play();
    }

    public void swapCardInHand(Player player) {
        int indexOfCardToReplace = player.indexOfCard(selectedCard);

        if (indexOfCardToReplace != -1) {
            player.replaceCardAtIndex(indexOfCardToReplace, drawnCard);
            // Optionally, handle the replaced card (e.g., adding it to the trash pile)
            addToTrashPile(selectedCard);
        } else {
            System.out.println("Card not found in player's hand.");
        }
    }

    public void activateDrawnCardPower(Card drawnCard, Player drawingPlayer) {
        switch (drawnCard.getPower()) {
            case LOOK_OWN:
                drawingPlayer.lookAtOwnCard(); // Method to implement
                break;
            case LOOK_OTHER:
                drawingPlayer.lookAtOpponentCard(players); // Method to implement, passing in list of players
                break;
            case SWAP_CARDS:
                drawingPlayer.swapCardWithOpponent(players); // Method to implement
                break;
            case NONE: // No power, normal gameplay proceeds
                break;
        }
    }

    public boolean handleEndOfTurn(){
        if (selectedCard == drawnCard){
            addToTrashPile(selectedCard);
            if (eventListener != null) {
                eventListener.onPlayerHandChanged(getCurrentPlayer());
            }
            return true;
        }else if (getCurrentPlayer().hasCard(selectedCard)){
            swapCardInHand(getCurrentPlayer());
            if (eventListener != null) {
                eventListener.onPlayerHandChanged(getCurrentPlayer());
            }
            return true;
        }else{
            System.out.println("Not your card.");
            return false;
        }
    }
    private void endGame() {
        // Calculate scores, find winner, etc.
        Player winner = calculateWinner();

        // Notify the UI to update accordingly
        if (eventListener != null) {
            eventListener.onGameEnded(winner);
        }
    }

    private Player calculateWinner() {
        Player winner = null;
        int lowestScore = Integer.MAX_VALUE;

        for (Player player : players) {
            int playerScore = player.getScore();
            if (playerScore < lowestScore) {
                lowestScore = playerScore;
                winner = player;
            }
        }
        return winner; // Placeholder return
    }

}

