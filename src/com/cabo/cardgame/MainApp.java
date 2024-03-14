package com.cabo.cardgame;

import com.cabo.cardgame.Game;
import com.cabo.cardgame.models.ComputerPlayer;
import com.cabo.cardgame.models.Player;
import com.cabo.cardgame.models.Card;
import com.cabo.cardgame.listeners.GameEventListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.List; // For handling lists of players and cards

public class MainApp extends Application implements GameEventListener{

    private BorderPane root;
    private HBox topPlayerBox;
    private VBox leftPlayerBox;
    private VBox rightPlayerBox;
    private HBox bottomPlayerBox;
    private HBox centerBox;
    private VBox drawBox;
    private VBox swapBox;
    private Button drawButton;
    private Button swapButton;
    private ImageView drawnCardView;
    private VBox centerDisplay;
    private Game game;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the game with 1 decks
        game = new Game(1);
        game.setGameEventListener(this);

        drawButton = new Button("Draw");
        swapButton = new Button("Swap");

        drawButton.setDisable(true);
        swapButton.setDisable(true);

        initUI();

        // Create a scene and show it
        Scene scene = new Scene(root, 800, 800); // Adjust size as needed
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Card Game Table");
        primaryStage.show();

        // Update UI with initial game state
        updateUI();

        game.nextTurn();
    }

    private void initUI(){
        // Initialize UI components
        root = new BorderPane();
        topPlayerBox = new HBox(10); // 10 is spacing
        leftPlayerBox = new VBox(10);
        rightPlayerBox = new VBox(10);
        bottomPlayerBox = new HBox(10);

        // Set alignment for top and bottom player boxes
        topPlayerBox.setAlignment(Pos.CENTER);
        bottomPlayerBox.setAlignment(Pos.CENTER);

        // Set padding for containers
        topPlayerBox.setPadding(new Insets(10, 10, 10, 10)); // Adjust these values as needed
        bottomPlayerBox.setPadding(new Insets(10, 10, 10, 10));
        leftPlayerBox.setPadding(new Insets(10, 10, 10, 10));
        rightPlayerBox.setPadding(new Insets(10, 10, 10, 10));

        // Setup the layout in 'root'
        root.setTop(topPlayerBox);
        root.setLeft(leftPlayerBox);
        root.setRight(rightPlayerBox);
        root.setBottom(bottomPlayerBox);
    }
    private void updateUI(){
        updateDeckAndPileViews();
        updatePlayersUI();
    }

    private void updatePlayersUI() {

        // Clear existing cards
        topPlayerBox.getChildren().clear();
        leftPlayerBox.getChildren().clear();
        rightPlayerBox.getChildren().clear();
        bottomPlayerBox.getChildren().clear();

        List<Player> players = game.getPlayers();
        // Assuming players.size() is 4 for simplicity

        // Update player hands; assuming player order: top, left, right, bottom
        displayPlayerHand(players.get(1).getHand(), topPlayerBox);
        displayPlayerHand(players.get(2).getHand(), leftPlayerBox);
        displayPlayerHand(players.get(3).getHand(), rightPlayerBox);
        displayPlayerHand(players.get(0).getHand(), bottomPlayerBox);
    }

    private void updateDeckAndPileViews() {
        // Initialize ImageView for draw deck and trash pile
        ImageView drawDeckView = createCardImageView("/CardImages/CardBack.png",null);
        ImageView trashPileView;

        // Update trash pile view (show top card or a placeholder if empty)
        Card topTrashCard = game.getTopCardOfTrashPile();
        if (topTrashCard != null) {
            trashPileView = createCardImageView(topTrashCard.getImagePath(),topTrashCard);
        } else {
            // Optional: Set a placeholder image for empty trash pile
            trashPileView = createCardImageView("/CardImages/Blank.png",null);
        }

        // VBox for draw deck and its button
        drawBox = new VBox(10, drawDeckView, drawButton); // 10 is the spacing between elements
        drawBox.setAlignment(Pos.CENTER); // Center-align the contents

        // VBox for trash pile and its button
        swapBox = new VBox(10, trashPileView, swapButton);
        swapBox.setAlignment(Pos.CENTER);

        // HBox to hold both VBoxes
        centerBox = new HBox(20, drawBox, swapBox); // 20 is the spacing between the two VBoxes
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        drawnCardView = createCardImageView("/CardImages/CardBack.png",null);

        VBox centerDisplay = new VBox(10, centerBox, drawnCardView); // Add drawnCardView below centerBox
        centerDisplay.setAlignment(Pos.CENTER);

        root.setCenter(centerDisplay);

        drawButton.setOnAction(event -> {
            if (game.getCurrentPlayer().isHuman()) { // Assuming you have a way to check if the current player is human
                Card drawnCard = game.drawCard(); // Method to draw a card from the game deck
                game.getCurrentPlayer().draw(true);
                drawButton.setDisable(true);
                swapButton.setDisable(true);
                // Update the ImageView to show the drawn card
                Image cardImage = new Image(getClass().getResourceAsStream(drawnCard.getImagePath()));
                drawnCardView.setImage(cardImage);
                // Any additional logic for handling the drawn card
            }
        });

        swapButton.setOnAction(event -> {
            if (game.getCurrentPlayer().isHuman() && !game.trashEmpty()) { // Assuming you have a way to check if the current player is human
                Card drawnCard = game.getTopCardOfTrashPile(); // Method to draw a card from the game deck
                game.getCurrentPlayer().draw(true);
                drawButton.setDisable(true);
                swapButton.setDisable(true);
                // Update the ImageView to show the drawn card
                Image cardImage = new Image(getClass().getResourceAsStream(drawnCard.getImagePath()));
                drawnCardView.setImage(cardImage);
                // Any additional logic for handling the drawn card
            }
        });
    }

    private void displayPlayerHand(List<Card> hand, Pane playerBox) {
        for (Card card : hand) {
            ImageView cardView = createCardImageView(card.getImagePath(),card);
            playerBox.getChildren().add(cardView);
        }
    }

    private ImageView createCardImageView(String imagePath, Card card) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);

        if (card != null) {
            // Make the ImageView clickable
            imageView.setOnMouseClicked(event -> {
                // Logic to handle the card click
                handleCardClick(card);
            });
        } else {
            imageView.setOnMouseClicked(event -> {
                System.out.println("Placeholder or empty card clicked.");
                // Implement any specific logic for clicking on a placeholder, if needed
            });
        }

        return imageView;
    }

    private void handleCardClick(Card card) {
        // Example action: discard the clicked card
        if (game.getCurrentPlayer().isHuman() && game.getCurrentPlayer().drewCard()) {
            game.addToTrashPile(card);
            drawnCardView.setImage(null);
            System.out.println("Threw out card: " + card);
            game.nextTurn();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void onHumanPlayerTurn() {
        Platform.runLater(() -> {
            drawButton.setDisable(false);
            swapButton.setDisable(false);
            game.getCurrentPlayer().draw(false);
        });
    }

    @Override
    public void onComputerPlayerTurn() {
        Platform.runLater(() -> {
            drawButton.setDisable(true);
            swapButton.setDisable(true);
        });

        // Optionally handle AI turn logic or delay here
    }
}
