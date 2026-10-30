package cardgame;

// Keeps track of one player's number, cards, and elimination status.
public class Player {

    private final int playerNumber;
    private final Deck deck;
    private boolean eliminated;

    // Creates a player with an empty deck and marks them as not eliminated.
    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.deck = new Deck();
        this.eliminated = false;
    }

    // Returns this player's number.
    public int getPlayerNumber() {
        return playerNumber;
    }

    // Returns the deck of cards owned by this player.
    public Deck getDeck() {
        return deck;
    }

    // Says whether this player still has cards to play.
    public boolean isActive() {
        return !deck.isEmpty();
    }

    // Says whether this player has already been marked as eliminated.
    public boolean isEliminated() {
        return eliminated;
    }

    // Marks this player as eliminated.
    public void eliminate() {
        eliminated = true;
    }
}
