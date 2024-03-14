package com.cabo.cardgame;

import com.cabo.cardgame.Game;
import com.cabo.cardgame.models.Player;
import com.cabo.cardgame.models.Card;

import javafx.application.Application;
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

public class MainApp extends Application {

    private BorderPane root;
    private HBox topPlayerBox;
    private VBox leftPlayerBox;
    private VBox rightPlayerBox;
    private HBox bottomPlayerBox;
    private HBox centerBox;
    private Game game;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the game with 2 decks
        game = new Game(2);

        // Initialize UI components
        root = new BorderPane();
        topPlayerBox = new HBox(10); // 10 is spacing
        leftPlayerBox = new VBox(10);
        rightPlayerBox = new VBox(10);
        bottomPlayerBox = new HBox(10);
        centerBox = new HBox(20); // Spacing between draw deck and trash pile

        // Set alignment for top and bottom player boxes
        topPlayerBox.setAlignment(Pos.CENTER);
        bottomPlayerBox.setAlignment(Pos.CENTER);
        centerBox.setAlignment(Pos.CENTER);

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
        root.setCenter(centerBox);

        // Create a scene and show it
        Scene scene = new Scene(root, 800, 800); // Adjust size as needed
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Card Game Table");
        primaryStage.show();

        // Update UI with initial game state
        updateUI();
        // Example game loop
        while (!game.isGameOver()) {
            Player currentPlayer = game.getCurrentPlayer();
            // Handle current player's actions

            game.nextTurn(); // Advance to the next player's turn
        }
    }

    private void updateUI() {
        updateDeckAndPileViews();

        // Clear existing cards
        topPlayerBox.getChildren().clear();
        leftPlayerBox.getChildren().clear();
        rightPlayerBox.getChildren().clear();
        bottomPlayerBox.getChildren().clear();

        List<Player> players = game.getPlayers();
        // Assuming players.size() is 4 for simplicity

        // Update player hands; assuming player order: top, left, right, bottom
        displayPlayerHand(players.get(0).getHand(), topPlayerBox);
        displayPlayerHand(players.get(1).getHand(), leftPlayerBox);
        displayPlayerHand(players.get(2).getHand(), rightPlayerBox);
        displayPlayerHand(players.get(3).getHand(), bottomPlayerBox);
    }

    private void updateDeckAndPileViews() {
        // Initialize ImageView for draw deck and trash pile
        ImageView drawDeckView = createCardImageView("/CardImages/CardBack.png");
        ImageView trashPileView;

        // Update trash pile view (show top card or a placeholder if empty)
        Card topTrashCard = game.getTopCardOfTrashPile();
        if (topTrashCard != null) {
            trashPileView = createCardImageView(topTrashCard.getImagePath());
        } else {
            // Optional: Set a placeholder image for empty trash pile
            trashPileView = createCardImageView("/CardImages/Blank.png");
        }
        centerBox.getChildren().addAll(drawDeckView, trashPileView);
    }

    private void displayPlayerHand(List<Card> hand, Pane playerBox) {
        for (Card card : hand) {
            ImageView cardView = createCardImageView(card.getImagePath());
            playerBox.getChildren().add(cardView);
        }
    }

    private ImageView createCardImageView(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
