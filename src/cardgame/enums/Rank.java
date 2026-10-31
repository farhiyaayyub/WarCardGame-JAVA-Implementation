package cardgame.enums;

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

    Rank(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    // Matches a rank code regardless of case, or throws an exception for an unknown code.
    public static Rank fromCode(String code) {
        for (Rank rank : values()) {
            if (rank.code.equalsIgnoreCase(code)) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid card rank: " + code);
    }
}
