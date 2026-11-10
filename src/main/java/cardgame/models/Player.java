package cardgame.models;

import cardgame.utils.Console;

/**
 * Represents one player. Stores the player number, personal deck, and elimination status.
 * Main methods: getDeck(), isActive(), eliminate().
 *
 * @author Farhiya Ayyub
 * @version October 30, 2026
 */
public class Player {

    private final int playerNumber;
    private final Deck deck = new Deck();
    private boolean eliminated;

    /**
     * Creates a player with an empty personal deck.
     * @param playerNumber number identifying this player
     */
    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    /**
     * Returns the player's identifying number.
     * @return the player number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Returns the player's mutable personal deck.
     * @return the deck owned by this player
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Checks whether the player still has cards to play.
     * @return true if the player's deck is not empty
     */
    public boolean isActive() {
        // A player can play while they still have cards.
        return !deck.isEmpty();
    }

    /**
     * Checks whether elimination has already been recorded.
     * @return true if this player has been marked eliminated
     */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Marks the player as eliminated to prevent duplicate announcements.
     */
    public void eliminate() {
        // Mark this player as out so we announce it only once.
        eliminated = true;
    }

    /**
     * Displays a labeled heading, card count, and the player's cards.
     * @param label label describing the displayed deck
     */
    public void displayDeck(String label) {
        System.out.println();
        Console.subheading("PLAYER " + playerNumber + "'S " + label);
        System.out.println("Total Cards: " + deck.size());
        Deck.displayCards(deck.getCards());
    }
}
