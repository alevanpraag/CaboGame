// Deck.java
package com.cabo.cardgame.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

    public class Deck {
        private List<Card> cards;

        public Deck(int numberOfDecks) {
            this.cards = new ArrayList<>();

            for (int d = 0; d < numberOfDecks; d++) {
                initializeDeck();
            }

            shuffle();
        }

        private void initializeDeck() {
            String[] suits = {"Heart", "Diamond", "Clubs", "Spades"};
            String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

            for (String suit : suits) {
                for (String rank : ranks) {
                    cards.add(new Card(rank, suit));
                }
            }

            //add jokers
            cards.add(new Card ("X","Heart"));
            cards.add(new Card ("X","Spades"));
        }

        public void shuffle() {
            Collections.shuffle(cards);
        }

        public Card deal() {
            if (!cards.isEmpty()) {
                return cards.remove(0);
            }
            return null; // Or throw an exception if preferred
        }
}
