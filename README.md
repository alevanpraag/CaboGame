# Cabo Card Game

This Java project implements the card game Cabo, designed for both human players and AI opponents. It features a graphical user interface built with JavaFX, making it easy for players to interact with the game.

## Features

- Play against computer-controlled opponents
- Interactive JavaFX GUI
- Strategy and memory elements similar to the physical card game

## Prerequisites

- Java 11 or newer
- JavaFX SDK
- An IDE like IntelliJ IDEA or Eclipse (optional)

## Setup and Running

1. **Clone the repository:**
    ```
    git clone https://github.com/alevanpraag/CaboGame.git
    ```
2. **Configure JavaFX:**
    - Download the JavaFX SDK from [OpenJFX](https://openjfx.io).
    - Configure your project to include the JavaFX library.
    - Add VM options to your IDE's run configuration:
        ```
        --module-path /path/to/javafx-sdk-<version>/lib --add-modules javafx.controls,javafx.fxml
        ```

3. **Compile and Run:**
    - Through IDE:
        - Open the project in your IDE.
        - Ensure the VM options are set correctly for your run configuration.
        - Run `MainApp.java`.
    - Via Command Line:
        ```
        javac --module-path /path/to/javafx-sdk-<version>/lib --add-modules javafx.controls,javafx.fxml -d out src/com/cabo/cardgame/*.java
        java --module-path /path/to/javafx-sdk-<version>/lib:out --add-modules javafx.controls,javafx.fxml com.cabo.cardgame.MainApp
        ```

## How to Play

- On game start, choose to play against computer opponents.
- The goal is to end the game with the lowest total value of cards.
- Players can draw cards, swap them with the deck or discard pile, and use special card powers.
- To call "Cabo", indicating the end of the game, believe you have the lowest score.
- The game calculates final scores, and the player with the lowest score wins.

## Contributing

Feel free to fork this repository and submit pull requests to contribute to the project. Whether it's adding new features, fixing bugs, or improving documentation, your contributions are welcome!

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.
