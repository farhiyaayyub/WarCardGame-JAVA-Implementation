package cardgame.models;

import cardgame.enums.Rank;
import cardgame.enums.Suit;

/**
 * Represents one card with a suit and rank. Compares itself with other cards
 * and provides plain and colored text representations.
 * Main methods: compareTo(), toString(), toColoredString(), equals(), hashCode().
 *
 * @author Farhiya Ayyub
 * @version October 30, 2026
 */
public class Card implements Comparable<Card> {

    private static final String RED = "\u001B[91m";
    private static final String WHITE = "\u001B[97m";
    private static final String RESET = "\u001B[0m";

    private final Suit suit;
    private final Rank rank;

    /**
     * Creates a card with the given suit and rank.
     * @param suit the card's suit
     * @param rank the card's rank
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Compares cards by rank, using suit to break rank ties.
     * @param otherCard card to compare with this card
     * @return a positive value if this card is stronger, a negative value if weaker, or zero if equal
     */
    @Override
    public int compareTo(Card otherCard) {
        // ordinal() is the position in the enum list. Earlier positions are stronger.
        int rankComparison = Integer.compare(otherCard.rank.ordinal(), this.rank.ordinal());

        // If the ranks are different, return the comparison result to show which card has the higher rank.
        if (rankComparison != 0) {
            return rankComparison;
        }

        return Integer.compare(otherCard.suit.ordinal(), this.suit.ordinal());
    }

    /**
     * Formats the card with a suit symbol and ANSI color codes.
     * @return the colored card representation
     */
    public String toColoredString() {
        String color = WHITE;
        if (suit.isRed()) {
            color = RED;
        }
        return color + suit.getSymbol() + " " + toString() + RESET;
    }

    /**
     * Formats the card using its suit and rank codes.
     * @return a card code such as H-A
     */
    @Override
    public String toString() {
        return suit.getCode() + "-" + rank.getCode();
    }

    /**
     * Checks whether another object is a card with the same suit and rank.
     * @param object object to compare with this card
     * @return true if both cards have the same suit and rank
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            // Both names refer to the exact same card object.
            return true;
        }

        if (!(object instanceof Card)) {
            return false;
        }
        // Treat the object as a Card and return true if both cards have the same suit and rank.
        Card otherCard = (Card) object;
        return suit == otherCard.suit && rank == otherCard.rank;
    }

    /**
     * Combines suit and rank hashes so equal cards have equal hash codes.
     * @return the hash code for this card
     */
    @Override
    public int hashCode() {

        // Build a number using both the suit and rank.
        return 31 * suit.hashCode() + rank.hashCode();
    }
}
