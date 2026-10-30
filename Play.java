package cardgame;

// Stores which player played which card during a round.
public class Play {

    private final Player player;
    private final Card card;

    // Records the player and card for one play.
    public Play(Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    // Returns the player who made this play.
    public Player getPlayer() {
        return player;
    }

    // Returns the card that was played.
    public Card getCard() {
        return card;
    }
}
