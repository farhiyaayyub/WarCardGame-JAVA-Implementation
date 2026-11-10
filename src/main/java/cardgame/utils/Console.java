package cardgame.utils;

/**
 * Provides shared formatting for consistent colors, headings, spacing, and prompts.
 * Main methods: heading(), subheading(), prompt(), successMessage().
 *
 * @author Farhiya Ayyub
 * @version October 30,2026
 */
public final class Console {

    private static final int OUTPUT_WIDTH = 52;
    private static final String GREEN = "\u001B[92m";
    private static final String CYAN = "\u001B[96m";
    private static final String YELLOW = "\u001B[93m";
    private static final String RESET = "\u001B[0m";

    /**
     * Prevents instances of this utility class from being created.
     */
    private Console() {
    }

    /**
     * Prints a cyan heading surrounded by equals signs and blank lines.
     *
     * @param title heading text
     */
    public static void heading(String title) {
        // Put the title in the middle, with equals signs on both sides.
        String label = " " + title + " ";
        int remainingWidth = Math.max(0, OUTPUT_WIDTH - label.length());
        int leftWidth = remainingWidth / 2;
        int rightWidth = remainingWidth - leftWidth;

        System.out.println();
        System.out.println(CYAN + repeat('=', leftWidth) + label + repeat('=', rightWidth) + RESET);
        System.out.println();
    }

    /**
     * Prints a cyan heading centered within the configured output width.
     *
     * @param title heading text
     */
    public static void centeredHeading(String title) {
        int leftPadding = Math.max(0, (OUTPUT_WIDTH - title.length()) / 2);
        System.out.println(CYAN + repeat(' ', leftPadding) + title + RESET);
    }

    /**
     * Prints a yellow subheading followed by a newline.
     *
     * @param label subheading text
     */
    public static void subheading(String label) {
        System.out.println(YELLOW + label + RESET);
    }

    /**
     * Prints a cyan prompt without a trailing newline.
     *
     * @param message prompt text
     */
    public static void prompt(String message) {
        System.out.print(CYAN + message + RESET);
    }

    /**
     * Prints a green message followed by a newline.
     *
     * @param message message text
     */
    public static void successMessage(String message) {
        System.out.println(GREEN + message + RESET);
    }

    /**
     * Prints a green message centered within the configured output width.
     *
     * @param message message text
     */
    public static void centeredSuccessMessage(String message) {
        int leftPadding = Math.max(0, (OUTPUT_WIDTH - message.length()) / 2);
        System.out.println(GREEN + repeat(' ', leftPadding) + message + RESET);
    }

    /**
     * Builds a string containing a character repeated the requested number of times.
     *
     * @param character character to repeat
     * @param count     nonnegative number of repetitions
     * @return the repeated characters
     * @throws NegativeArraySizeException if count is negative
     */
    private static String repeat(char character, int count) {
        // Build text by adding the same character count times.
        StringBuilder result = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            result.append(character);
        }
        return result.toString();
    }
}
