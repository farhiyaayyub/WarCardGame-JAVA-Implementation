package cardgame;

import cardgame.enums.Rank;
import cardgame.enums.Suit;

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

    // Compares rank first, then suit on a tie; earlier enum values are stronger.
    // Returns a positive value when this card is stronger than the other card.
    @Override
    public int compareTo(Card otherCard) {
        int rankComparison = Integer.compare(otherCard.rank.ordinal(), this.rank.ordinal());

        if (rankComparison != 0) {
            return rankComparison;
        }

        return Integer.compare(otherCard.suit.ordinal(), this.suit.ordinal());
    }

    public String toColoredString() {
        String color = suit.isRed() ? RED : WHITE;
        return color + suit.getSymbol() + " " + toString() + RESET;
    }

    @Override
    public String toString() {
        return suit.getCode() + "-" + rank.getCode();
    }

    // Treats cards with the same suit and rank as equal for duplicate detection.
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Card)) {
            return false;
        }

        Card otherCard = (Card) object;
        return suit == otherCard.suit && rank == otherCard.rank;
    }

    // Combines suit and rank so equal cards share a hash code for HashSet lookup.
    @Override
    public int hashCode() {
        return 31 * suit.hashCode() + rank.hashCode();
    }
}
