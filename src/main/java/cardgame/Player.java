package cardgame;

/**
 * Represents one player. Stores the player number, personal deck, and elimination status.
 * Main methods: getDeck(), isActive(), eliminate().
 */
public class Player {

    private final int playerNumber;
    private final Deck deck = new Deck();
    private boolean eliminated;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Deck getDeck() {
        return deck;
    }

    public boolean isActive() {
        // A player can play while they still have cards.
        return !deck.isEmpty();
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void eliminate() {
        // Mark this player as out so we announce it only once.
        eliminated = true;
    }

    public void displayDeck(String label) {
        System.out.println();
        Console.subheading("PLAYER " + playerNumber + "'S " + label);
        System.out.println("Total Cards: " + deck.size());
        Deck.displayCards(deck.getCards());
    }
}
