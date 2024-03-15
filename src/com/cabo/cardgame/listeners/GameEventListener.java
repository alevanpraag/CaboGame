package com.cabo.cardgame.listeners;

import com.cabo.cardgame.models.Player;

public interface GameEventListener {
    void onHumanPlayerTurn();
    void onComputerPlayerTurn();
    void onTrashPileUpdated(); // Add this method
    void onPlayerHandChanged(Player player);
    void onGameEnded(Player winner);
}
