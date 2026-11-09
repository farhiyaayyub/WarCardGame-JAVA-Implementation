package cardgame;

import cardgame.enums.Rank;
import cardgame.enums.Suit;

/**
 * Represents one card with a suit and rank. Compares itself with other cards
 * and provides plain and colored text representations.
 * Main methods: compareTo(), toString(), toColoredString(), equals(), hashCode().
 */
public class Card implements Comparable<Card> {

    private static final String RED = "\u001B[91m";
    private static final String WHITE = "\u001B[97m";
    private static final String RESET = "\u001B[0m";

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // Compare rank first. If ranks match, compare suits.
    // Positive = this card wins, negative = other card wins, zero = same card.
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

    public String toColoredString() {
        String color = WHITE;
        if (suit.isRed()) {
            color = RED;
        }
        return color + suit.getSymbol() + " " + toString() + RESET;
    }

    @Override
    public String toString() {
        return suit.getCode() + "-" + rank.getCode();
    }

    // Two cards are equal if they have the same suit and rank.
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

    // Equal cards must also have the same hash code, as required by Java.
    @Override
    public int hashCode() {

        // Build a number using both the suit and rank.
        return 31 * suit.hashCode() + rank.hashCode();
    }
}
