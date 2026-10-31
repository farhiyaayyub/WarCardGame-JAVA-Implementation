package cardgame;

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
        return !deck.isEmpty();
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void eliminate() {
        eliminated = true;
    }

    public void displayDeck(String label) {
        System.out.println();
        Main.subheading("PLAYER " + playerNumber + "'S " + label);
        System.out.println("Total Cards: " + deck.size());
        Deck.displayCards(deck.getCards());
    }
}
