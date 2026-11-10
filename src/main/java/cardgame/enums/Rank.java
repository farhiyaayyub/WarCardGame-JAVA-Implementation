package cardgame.enums;

/**
 * Defines allowed card ranks in strongest-to-weakest order, from ace to two.
 *
 * @author Farhiya Ayyub
 * @version October 30, 2026
 */
public enum Rank {
    // Allowed ranks, listed from strongest to weakest.
    ACE("A"),
    KING("K"),
    QUEEN("Q"),
    JACK("J"),
    TEN("10"),
    NINE("9"),
    EIGHT("8"),
    SEVEN("7"),
    SIX("6"),
    FIVE("5"),
    FOUR("4"),
    THREE("3"),
    TWO("2");

    private final String code;

    /**
     * Creates a rank with its input and display code.
     * @param code code representing this rank
     */
    Rank(String code) {
        this.code = code;
    }

    /**
     * Returns the rank's input and display code.
     * @return the rank code
     */
    public String getCode() {
        return code;
    }

    /**
     * Finds a rank by its code, ignoring case.
     * @param code rank code to look up
     * @return the matching rank
     * @throws IllegalArgumentException if the code is unknown
     */
    public static Rank fromCode(String code) {
        for (Rank rank : values()) {
            if (rank.code.equalsIgnoreCase(code)) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid card rank: " + code);
    }
}
