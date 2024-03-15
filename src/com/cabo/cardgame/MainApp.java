package com.cabo.cardgame;

import com.cabo.cardgame.Game;
import com.cabo.cardgame.models.ComputerPlayer;
import com.cabo.cardgame.models.Player;
import com.cabo.cardgame.models.Card;
import com.cabo.cardgame.listeners.GameEventListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

public class MainApp extends Application implements GameEventListener{

    private BorderPane root;
    private VBox topPlayerBox;
    private HBox leftPlayerBox;
    private HBox rightPlayerBox;
    private VBox bottomPlayerBox;
    private HBox centerBox;
    private VBox drawBox;
    private VBox swapBox;
    private Button drawButton;
    private Button swapButton;
    private Button caboButton;
    private Button powerButton;
    private ImageView drawnCardView;
    private VBox centerDisplay;
    private ImageView trashPileView;

    private Map<Player, Pane> playerUIContainers;
    private Game game;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the game with 1 decks
        game = new Game(1);
        game.setGameEventListener(this);

        drawButton = new Button("Draw");
        swapButton = new Button("Swap");
        caboButton = new Button("Cabo");
        powerButton = new Button("Use Power");

        drawButton.setDisable(true);
        swapButton.setDisable(true);
        caboButton.setDisable(true);
        powerButton.setDisable(true);

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
        root.setPadding(new Insets(20));

        topPlayerBox = new VBox(10); // 10 is spacing
        leftPlayerBox = new HBox(10);
        rightPlayerBox = new HBox(10);
        bottomPlayerBox = new VBox(10);

        // Set alignment for top and bottom player boxes
        topPlayerBox.setAlignment(Pos.CENTER);
        bottomPlayerBox.setAlignment(Pos.CENTER);
        rightPlayerBox.setAlignment(Pos.CENTER);
        leftPlayerBox.setAlignment(Pos.CENTER);

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
        List<Player> players = game.getPlayers();
        playerUIContainers = new HashMap<>();
        // Reset/clear all player boxes before displaying
        topPlayerBox.getChildren().clear();
        leftPlayerBox.getChildren().clear();
        rightPlayerBox.getChildren().clear();
        bottomPlayerBox.getChildren().clear();

        for (Player player : players) {
            if (player.isHuman()) {
                // Display the human player's hand at the bottom
                displayPlayerHand(player, player.getHand(), bottomPlayerBox);
                playerUIContainers.put(player, bottomPlayerBox);
            } else if (player.getName().equals("Computer North")) {
                displayPlayerHand(player, player.getHand(), topPlayerBox); // First computer player
                playerUIContainers.put(player, topPlayerBox);
            }else if (player.getName().equals("Computer West")) {
                displayPlayerHand(player, player.getHand(), leftPlayerBox); // Second computer player
                playerUIContainers.put(player, leftPlayerBox);
            }else{
                displayPlayerHand(player, player.getHand(), rightPlayerBox); // Third computer player
                playerUIContainers.put(player, rightPlayerBox);
            }
        }
    }

    private void updateDeckAndPileViews() {
        // Initialize ImageView for draw deck and trash pile
        ImageView drawDeckView = createCardImageView("/CardImages/CardBack.png",null);

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

        drawnCardView = createCardImageView("/CardImages/Empty.png",null);
        powerButton.setVisible(false); // Initially hidden until a card with power is drawn

        VBox centerDisplay = new VBox(10, caboButton, centerBox, drawnCardView,powerButton); // Add drawnCardView below centerBox
        centerDisplay.setAlignment(Pos.CENTER);

        root.setCenter(centerDisplay);

        drawButton.setOnAction(event -> {
            if (game.getCurrentPlayer().isHuman()) { // Assuming you have a way to check if the current player is human
                Card drawnCard = game.drawCard(); // Method to draw a card from the game deck
                game.setDrawnCard(drawnCard);
                game.getCurrentPlayer().draw(true);
                drawButton.setDisable(true);
                swapButton.setDisable(true);
                caboButton.setDisable(true);
                // Update the ImageView to show the drawn card
                updateDrawnCardImageView(drawnCard);
                // Any additional logic for handling the drawn card
            }
        });

        swapButton.setOnAction(event -> {
            if (game.getCurrentPlayer().isHuman() && !game.trashEmpty()) { // Assuming you have a way to check if the current player is human
                Card drawnCard = game.removeTopCardFromTrashPile(); // Method to draw a card from the game deck
                game.setDrawnCard(drawnCard);
                game.getCurrentPlayer().draw(true);
                drawButton.setDisable(true);
                swapButton.setDisable(true);
                caboButton.setDisable(true);
                // Update the ImageView to show the drawn card
                updateDrawnCardImageView(drawnCard);
                // Any additional logic for handling the drawn card
                updateTrashPileUI();
            }else{
                System.out.println("No cards in discard yet");
            }
        });

        caboButton.setOnAction(event -> {
            if (game.getCurrentPlayer().isHuman()) { // Assuming you have a way to check if the current player is human
                game.getCurrentPlayer().callCabo();
                drawButton.setDisable(true);
                swapButton.setDisable(true);
                caboButton.setDisable(true);
                game.nextTurn();
                // Any additional logic for handling the drawn card
            }
        });
    }

    private void displayPlayerHand(Player player, List<Card> hand, Pane playerBox) {
        playerBox.getChildren().clear(); // Clear previous content to avoid duplicates.

        // Initialize the container for cards with alignment and style.
        Pane cardsContainer = isVertical(playerBox) ? new VBox(5) : new HBox(5); // Adjust spacing as needed.
        cardsContainer.getStyleClass().add("cards-container"); // Apply CSS for styling.
        setContainerAlignment(cardsContainer);

        // Create and configure the player name label, including rotation.
        Label playerNameLabel = createPlayerNameLabel(player.getName(), getRotation(playerBox));

        // Add cards to the container, applying rotation as necessary.
        hand.forEach(card -> cardsContainer.getChildren().add(createRotatedCardView(card, getRotation(playerBox))));

        // Layout configuration based on orientation.
        if (isVertical(playerBox)) {
            // Vertical orientation (left or right player boxes).
            playerNameLabel.setTranslateY(getTranslationY(playerBox)); // Adjust label positioning for vertical player boxes.
            if (playerBox == leftPlayerBox) {
                playerBox.getChildren().addAll(cardsContainer, playerNameLabel);
            } else {
                playerBox.getChildren().addAll(playerNameLabel, cardsContainer);
            }
        } else {
            // Horizontal orientation (top or bottom player boxes).
            playerBox.getChildren().addAll(playerNameLabel, cardsContainer);
        }
    }

    // Helper methods to determine layout specifics based on playerBox.
    private boolean isVertical(Pane playerBox) {
        return playerBox == leftPlayerBox || playerBox == rightPlayerBox;
    }

    private double getRotation(Pane playerBox) {
        if (playerBox == leftPlayerBox) return 90;
        if (playerBox == rightPlayerBox) return -90;
        return 0;
    }

    private double getTranslationY(Pane playerBox) {
        // Custom logic to adjust Y translation based on the box.
        return playerBox == leftPlayerBox ? -50 : (playerBox == rightPlayerBox ? 50 : 0);
    }

    private void setContainerAlignment(Pane container) {
        if (container instanceof VBox) {
            ((VBox) container).setAlignment(Pos.CENTER);
        } else if (container instanceof HBox) {
            ((HBox) container).setAlignment(Pos.CENTER);
        }
    }

    private ImageView createRotatedCardView(Card card, double rotation) {
        ImageView cardView = createCardImageView(card.getImagePath(), card);
        cardView.setRotate(rotation);
        return cardView;
    }

    private Label createPlayerNameLabel(String name, double rotation) {
        Label playerNameLabel = new Label(name);
        playerNameLabel.getStyleClass().add("player-name-label");
        playerNameLabel.setRotate(rotation);
        if (rotation != 0) {
            // Adjust label positioning for vertical player boxes
            playerNameLabel.setTranslateY(rotation == 90 ? -50 : 50); // Example adjustment, customize as needed
        }
        return playerNameLabel;
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

    private void updateDrawnCardImageView(Card card) {
        System.out.println("Updating drawn card view with card: " + card);
        if (card != null) {
            Image image = new Image(getClass().getResourceAsStream(card.getImagePath()));
            drawnCardView.setImage(image);

            // Show the "Power" button only if the card has a power other than NONE
            if (card.getPower() != Card.CardPower.NONE) {
                powerButton.setVisible(true);
                powerButton.setDisable(false);
                powerButton.setOnAction(e -> activateCardPower(card)); // Assuming activateCardPower is implemented
            } else {
                powerButton.setVisible(false);
            }

            // Make the ImageView clickable
            drawnCardView.setOnMouseClicked(event -> {
                // Logic to handle the card click
                handleCardClick(card);
            });
        }
    }
    private void activateCardPower(Card card) {
        switch (card.getPower()) {
            case LOOK_OWN:
                // Implement the logic to "look" at one of your own unknown cards
                break;
            case LOOK_OTHER:
                // Implement the logic to "look" at one of your competitor's cards
                break;
            case SWAP_CARDS:
                // Implement the logic to swap one of your own cards for one of your competitor's
                break;
            default:
                // Handle any other powers or the absence of power
                break;
        }
    }

    private void handleCardClick(Card card) {
        // Example action: discard the clicked card
        if (game.getCurrentPlayer().isHuman() && game.getCurrentPlayer().drewCard()) {
            game.setSelectedCard(card);
            if (game.handleEndOfTurn()){
                drawnCardView.setImage(null);
                System.out.println("Threw out card: " + card);
                updatePlayerHandUI(game.getCurrentPlayer());
                game.nextTurn();
            }else{
                game.setSelectedCard(null);
            }

        }else{
            System.out.println("Draw a card first");
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
            caboButton.setDisable(false);
            powerButton.setDisable(true);
            powerButton.setVisible(false);
            game.getCurrentPlayer().draw(false);
            game.setDrawnCard(null);
            game.setSelectedCard(null);
        });
    }

    @Override
    public void onComputerPlayerTurn() {
        Platform.runLater(() -> {
            drawButton.setDisable(true);
            swapButton.setDisable(true);
            caboButton.setDisable(true);
            powerButton.setDisable(true);
            powerButton.setVisible(false);
            game.setDrawnCard(null);
            game.setSelectedCard(null);
        });

        // Optionally handle AI turn logic or delay here
    }

    private void updateTrashPileUI() {
        Card topCard = game.getTopCardOfTrashPile();
        if (topCard != null) {
            Image image = new Image(getClass().getResourceAsStream(topCard.getImagePath()));
            trashPileView.setImage(image); // Assuming trashPileView is your ImageView for the trash pile
        } else {
            // Optionally set a placeholder image for an empty trash pile
            Image placeholder = new Image(getClass().getResourceAsStream("/CardImages/Blank.png"));
            trashPileView.setImage(placeholder);
        }
    }

    @Override
    public void onTrashPileUpdated() {
        Platform.runLater(this::updateTrashPileUI);
    }

    // Assuming this method is in your UI controller class
    private void updatePlayerHandUI(Player player) {
        Pane handContainer = getHandContainerForPlayer(player);
        if (handContainer != null) {
            // Assuming displayPlayerHand has been updated to include player, hand, and container parameters
            displayPlayerHand(player, player.getHand(), handContainer);
        }
    }

    @Override
    public void onPlayerHandChanged(Player player) {
        Platform.runLater(() -> {
            updatePlayerHandUI(player);
        });
    }

    private Pane getHandContainerForPlayer(Player player) {
        // Implementation to determine the correct UI container for a given player
        // This could be based on the player's position in the game or other identifiers
        // For simplicity, assuming a method that maps players to their corresponding UI containers
        return playerUIContainers.get(player);
    }
    @Override
    public void onGameEnded(Player winner) {
        Platform.runLater(() -> {
            System.out.println("Game is ending...");
            // Disable all buttons
            drawButton.setDisable(true);
            swapButton.setDisable(true);
            // Disable other interactive UI elements as needed

            // Create the label for the winner
            Label winnerLabel = new Label("Winner: " + winner.getName());
            winnerLabel.getStyleClass().add("overlay-label");

            // Create a VBox to hold the winner label and the scores
            VBox scoreDisplay = new VBox(10); // 10 is the spacing between elements
            scoreDisplay.getChildren().add(winnerLabel);

            // Add a label for each player's score
            for (Player player : game.getPlayers()) {
                Label scoreLabel = new Label(player.getName() + ": " + player.getScore()); // Assuming a getScore method
                scoreLabel.getStyleClass().add("overlay-score");
                scoreDisplay.getChildren().add(scoreLabel);
            }

            scoreDisplay.setAlignment(Pos.CENTER);

            // Create an overlay and add the scoreDisplay VBox to it
            StackPane overlay = new StackPane(scoreDisplay);
            overlay.getStyleClass().add("overlay");
            overlay.setMinSize(300, 200);
            StackPane.setAlignment(scoreDisplay, Pos.CENTER);
            //overlay.setMinSize(root.getWidth(), root.getHeight()); // 'root' is your main layout pane
            overlay.setAlignment(Pos.CENTER); // Ensure content in overlay is centered

            root.setCenter(overlay);


            // Optionally, add a button or interaction to the overlay to close it or start a new game
        });
    }
}
