package cardgame;

// Add colors and spacing to the messages shown on screen.
public final class Console {

    private static final int OUTPUT_WIDTH = 52;
    private static final String GREEN = "\u001B[92m";
    private static final String CYAN = "\u001B[96m";
    private static final String YELLOW = "\u001B[93m";
    private static final String RESET = "\u001B[0m";

    private Console() {
    }

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

    public static void centeredHeading(String title) {
        int leftPadding = Math.max(0, (OUTPUT_WIDTH - title.length()) / 2);
        System.out.println(CYAN + repeat(' ', leftPadding) + title + RESET);
    }

    public static void subheading(String label) {
        System.out.println(YELLOW + label + RESET);
    }

    public static void prompt(String message) {
        System.out.print(CYAN + message + RESET);
    }

    public static void successMessage(String message) {
        System.out.println(GREEN + message + RESET);
    }

    public static void centeredSuccessMessage(String message) {
        int leftPadding = Math.max(0, (OUTPUT_WIDTH - message.length()) / 2);
        System.out.println(GREEN + repeat(' ', leftPadding) + message + RESET);
    }

    private static String repeat(char character, int count) {
        // Build text by adding the same character count times.
        StringBuilder result = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            result.append(character);
        }
        return result.toString();
    }
}
