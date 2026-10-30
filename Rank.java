package cardgame;

// Lists the card ranks from highest to lowest and their short file codes.
public enum Rank {
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

    // Saves the short code used to represent this rank in the card file.
    Rank(String code) {
        this.code = code;
    }

    // Returns this rank's short code, such as A or 10.
    public String getCode() {
        return code;
    }

    // Finds the rank matching a code, or reports an error for an unknown code.
    public static Rank fromCode(String code) {
        for (Rank rank : values()) {
            if (rank.code.equalsIgnoreCase(code)) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid card rank: " + code);
    }
}
