package cardgame;

// Lists the four card suits and their short file codes.
public enum Suit {
    DIAMOND("D"),
    HEART("H"),
    SPADE("S"),
    CLUB("C");

    private final String code;

    // Saves the short code used to represent this suit in the card file.
    Suit(String code) {
        this.code = code;
    }

    // Returns this suit's short code, such as H for hearts.
    public String getCode() {
        return code;
    }

    // Finds the suit matching a code, or reports an error for an unknown code.
    public static Suit fromCode(String code) {
        for (Suit suit : values()) {
            if (suit.code.equalsIgnoreCase(code)) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Invalid card suit: " + code);
    }
}
