package cardgame;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int OUTPUT_WIDTH = 52;
    private static final String GREEN = "\u001B[92m";
    private static final String CYAN = "\u001B[96m";
    private static final String YELLOW = "\u001B[93m";
    private static final String RESET = "\u001B[0m";

    // Loads the cards once, runs each game, and repeats until the user chooses NO.
    public static void main(String[] args) {
        welcome();

        List<Card> cards;
        try {
            cards = Deck.loadCards("resources/input.txt");
        } catch (IOException | IllegalArgumentException exception) {
            System.out.println("ERROR: Unable to load resources/input.txt.");
            System.out.println(exception.getMessage());
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            do {
                Game game = createGame(cards, scanner);
                game.start();
                playAgainHeading();
            } while (askPlayAgain(scanner));
            goodbye();
        }
    }

    public static void welcome() {
        System.out.println();
        centeredHeading("WELCOME TO WAR CARD GAME!");
        System.out.println();
        subheading("GAME RULES:");
        System.out.println("1. Each active player plays their top card.");
        System.out.println("2. The highest card wins the round.");
        System.out.println("3. The winner collects all played cards.");
        System.out.println("4. The first player to collect all 52 cards wins!");
    }

    // Reads valid player and shuffle counts, then creates a Game with those settings.
    private static Game createGame(List<Card> cards, Scanner scanner) {
        heading("GAME SETUP");
        int numberOfPlayers = readNumber(
                scanner, "ENTER NUMBER OF PLAYERS (2-8): ", 2, 8);
        int numberOfShuffles = readNumber(
                scanner, "ENTER NUMBER OF PERFECT SHUFFLES (1-100): ",
                Game.MIN_SHUFFLES, Game.MAX_SHUFFLES);

        return new Game(cards, numberOfPlayers, numberOfShuffles);
    }

    // Repeats the prompt until digit-only input fits an int and the allowed range.
    private static int readNumber(Scanner scanner, String message, int minimum, int maximum) {
        while (true) {
            prompt(message);
            String input = scanner.nextLine();

            if (!input.matches("[0-9]+")) {
                System.out.println("Invalid input! Enter numbers only.");
                System.out.println("Spaces, letters, and symbols are not allowed.");
                System.out.println();
                continue;
            }

            try {
                int number = Integer.parseInt(input);

                if (number >= minimum && number <= maximum) {
                    return number;
                }
            } catch (NumberFormatException exception) {
                System.out.println("The number is too large.");
                System.out.println();
                continue;
            }

            System.out.println(
                    "Invalid number! Please enter a value from " + minimum + " to " + maximum + ".");
            System.out.println();
        }
    }

    public static void playAgainHeading() {
        heading("START A NEW GAME?");
    }

    // Accepts YES or NO regardless of case and returns whether to start another game.
    private static boolean askPlayAgain(Scanner scanner) {
        while (true) {
            prompt("Do you want to play again? (YES/NO): ");

            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("NO")) {
                return answer.equalsIgnoreCase("YES");
            }

            System.out.println("Invalid input! Please enter YES or NO.");
        }
    }

    public static void goodbye() {
        System.out.println();
        centeredSuccessMessage("THANK YOU FOR PLAYING!");
        centeredSuccessMessage("SEE YOU IN THE NEXT CARD BATTLE!");
    }

    public static void heading(String title) {
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
        StringBuilder result = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            result.append(character);
        }
        return result.toString();
    }
}
