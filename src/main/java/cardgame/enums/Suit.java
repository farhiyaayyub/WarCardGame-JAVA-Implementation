package cardgame.enums;

public enum Suit {
    // Allowed suits, listed from strongest to weakest when ranks are tied.
    DIAMOND("D", "\u2666"),
    HEART("H", "\u2665"),
    SPADE("S", "\u2660"),
    CLUB("C", "\u2663");

    private final String code;
    private final String symbol;

    Suit(String code, String symbol) {
        this.code = code;
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isRed() {
        return this == DIAMOND || this == HEART;
    }

    // Find the suit for a code such as D or H. Report an error if it is unknown.
    public static Suit fromCode(String code) {
        for (Suit suit : values()) {
            if (suit.code.equalsIgnoreCase(code)) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Invalid card suit: " + code);
    }
}
