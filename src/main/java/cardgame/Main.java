package cardgame;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point of the program. Loads cards, reads and validates user input,
 * creates a game, and asks whether to play again.
 * Main methods: main(), createGame(), readNumber(), askPlayAgain().
 */
public class Main {

    // Flow: welcome -> load cards -> set up game -> play -> ask to play again.
    public static void main(String[] args) {
        welcome();

        // Read the cards from the file.
        List<Card> cards;
        try {
            cards = Deck.loadCards("resources/input.txt");
        }
        catch (IOException | IllegalArgumentException exception) {
            // Show the problem and stop if the cards cannot be loaded.
            System.out.println("ERROR: Unable to load resources/input.txt.");
            System.out.println(exception.getMessage());
            return;
        }
        // Read the user's answers. The scanner closes when we finish.
        try (Scanner scanner = new Scanner(System.in)) {

            do {
                // Set up and start a new game.
                Game game = createGame(cards, scanner);
                game.start();

                playAgainHeading();
            } while (askPlayAgain(scanner));
            goodbye();
        } catch (Exception e) {
            System.out.println("Error has been encountered:" + e.getMessage());
            System.exit(0);
        }
    }

    // This method shows the welcome message and explains the game rules.
    public static void welcome() {
        System.out.println();
        Console.centeredHeading("WELCOME TO WAR CARD GAME!");
        System.out.println();
        Console.subheading("GAME RULES:");
        System.out.println("1. Each active player plays their top card.");
        System.out.println("2. The highest card wins the round.");
        System.out.println("3. The winner collects all played cards.");
        System.out.println("4. The first player to collect all 52 cards wins!");
    }

    // Ask for the number of players and shuffles, then create the game.
    private static Game createGame(List<Card> cards, Scanner scanner) {
        Console.heading("GAME SETUP");
        int numberOfPlayers = readNumber(
                scanner, "ENTER NUMBER OF PLAYERS (2-8): ", 2, 8);
        int numberOfShuffles = readNumber(
                scanner, "ENTER NUMBER OF PERFECT SHUFFLES (1-100): ",
                Game.MIN_SHUFFLES, Game.MAX_SHUFFLES);

        // Give the new game back to main.
        return new Game(cards, numberOfPlayers, numberOfShuffles);
    }

    // Ask -> read a number -> check its limits -> accept it or ask again.
    private static int readNumber(Scanner scanner, String message, int minimum, int maximum) {
        while (true) {
            Console.prompt(message);
            // Remove extra spaces before and after the answer.
            String input = scanner.nextLine().trim();

            try {
                // Turn the typed text into a whole number.
                int number = Integer.parseInt(input);

                if (number >= minimum && number <= maximum) {
                    return number; // Accept the number if it is within the limits.
                }
                System.out.println("Please enter a number from " + minimum + " to " + maximum + ".");
            } catch (NumberFormatException exception) {
                // The answer is not a whole number that Java can store as an int.
                System.out.println("Please enter a whole number from " + minimum + " to " + maximum + ".");
            }
            System.out.println();
        }
    }

    // This method shows the heading before asking the user to play again.
    public static void playAgainHeading() {
        Console.heading("START A NEW GAME?");
    }

    // This method asks the user to play again. YES returns true, and NO returns false.
    // It uses the same Scanner to read the answer. Uppercase and lowercase letters both work.
    private static boolean askPlayAgain(Scanner scanner) {
        while (true) {
            Console.prompt("Do you want to play again? (YES/NO): ");

            // answer holds what the user typed so we can check if it is YES or NO.
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("YES")) {
                return true; // Play again.
            }
            if (answer.equalsIgnoreCase("NO")) {
                return false; // Stop playing.
            }

            System.out.println("Invalid input! Please enter YES or NO.");
        }
    }

    // This method shows a thank-you message when the user stops playing.
    public static void goodbye() {
        System.out.println();
        Console.centeredSuccessMessage("THANK YOU FOR PLAYING!");
        Console.centeredSuccessMessage("SEE YOU IN THE NEXT CARD BATTLE!");
    }

}
