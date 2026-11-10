package cardgame.enums;

/**
 * Defines suits in strongest-to-weakest tie-break order: diamond, heart, spade, club.
 *
 * @author Farhiya Ayyub
 * @version October 30, 2026
 */
public enum Suit {
    // Allowed suits, listed from strongest to weakest when ranks are tied.
    DIAMOND("D", "\u2666"),
    HEART("H", "\u2665"),
    SPADE("S", "\u2660"),
    CLUB("C", "\u2663");

    private final String code;
    private final String symbol;

    /**
     * Creates a suit with its code and Unicode symbol.
     * @param code code representing this suit
     * @param symbol symbol used when displaying this suit
     */
    Suit(String code, String symbol) {
        this.code = code;
        this.symbol = symbol;
    }

    /**
     * Returns the suit's input and display code.
     * @return the suit code
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the suit's Unicode symbol.
     * @return the suit symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Checks whether this suit is a diamond or heart.
     * @return true for diamonds and hearts
     */
    public boolean isRed() {
        return this == DIAMOND || this == HEART;
    }

    /**
     * Finds a suit by its code, ignoring case.
     * @param code suit code to look up
     * @return the matching suit
     * @throws IllegalArgumentException if the code is unknown
     */
    public static Suit fromCode(String code) {
        for (Suit suit : values()) {
            if (suit.code.equalsIgnoreCase(code)) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Invalid card suit: " + code);
    }
}
