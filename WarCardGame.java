package cardgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

// Starts the program and guides the player through each game.
public class WarCardGame {

    // Welcomes the player, loads the cards, starts games, and then exits.
    public static void main(String[] args) {
        // 1. Welcome the player.
        ConsoleUI.welcome();

        // 2. Load and validate the deck before starting.
        List<Card> originalDeck;

        try {
            originalDeck = loadCards("input.txt");
        } catch (
            IOException |
            IllegalArgumentException exception
        ) {
            System.out.println(
                "ERROR: Unable to load input.txt."
            );
            System.out.println(exception.getMessage());
            return;
        }

        // 3. Set up and play games until the player chooses to stop.
        Scanner scanner = new Scanner(System.in);
        playGamesUntilQuit(originalDeck, scanner);

        // 4. Finish the program.
        ConsoleUI.goodbye();
        scanner.close();
    }

    // Reads cards from a file and checks that the deck has 52 different cards.
    public static List<Card> loadCards(String filename)
        throws IOException {

        List<Card> cards = new ArrayList<>();
        Set<Card> uniqueCards = new HashSet<>();

        try (
            BufferedReader reader =
                Files.newBufferedReader(Paths.get(filename))
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer =
                    new StringTokenizer(line, ", \t\r\n");

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    String[] parts = token.split("-", -1);

                    if (parts.length != 2) {
                        throw new IllegalArgumentException(
                            "Invalid card format: " + token
                        );
                    }

                    Suit suit = Suit.fromCode(parts[0]);
                    Rank rank = Rank.fromCode(parts[1]);
                    Card card = new Card(suit, rank);

                    if (!uniqueCards.add(card)) {
                        throw new IllegalArgumentException(
                            "Duplicate card found: " + card
                        );
                    }

                    cards.add(card);
                }
            }
        }

        if (cards.size() != 52) {
            throw new IllegalArgumentException(
                "Invalid deck! Expected 52 cards, found " +
                cards.size()
            );
        }

        return cards;
    }

    // Keeps starting new games until the player says they want to stop.
    private static void playGamesUntilQuit(
        List<Card> originalDeck,
        Scanner scanner
    ) {
        boolean playAgain;

        do {
            playOneGame(originalDeck, scanner);

            ConsoleUI.playAgainHeading();
            playAgain = askPlayAgain(scanner);
        } while (playAgain);
    }

    // Asks for game settings, creates one game, and starts it.
    private static void playOneGame(
        List<Card> originalDeck,
        Scanner scanner
    ) {
        ConsoleUI.heading("GAME SETUP"  );
        System.out.println();

        int numberOfPlayers = readNumber(
            scanner,
            "ENTER NUMBER OF PLAYERS (2-8): ",
            2,
            8
        );

        int numberOfShuffles = readNumber(
            scanner,
            "ENTER NUMBER OF PERFECT SHUFFLES (1-100): ",
            Game.MIN_SHUFFLES,
            Game.MAX_SHUFFLES
        );

        Game game = new Game(
            originalDeck,
            numberOfPlayers,
            numberOfShuffles
        );

        game.start();
    }

    // Keeps asking until the player enters a whole number in the allowed range.
    public static int readNumber(
        Scanner scanner,
        String message,
        int minimum,
        int maximum
    ) {
          while (true) {
            ConsoleUI.prompt(message);
            String input = scanner.nextLine();

            if (!input.matches("[0-9]+")) {
                System.out.println(
                    "Invalid input! Enter numbers only."
                );
                System.out.println(
                    "Spaces, letters, and symbols are not allowed."
                );
                System.out.println();
                continue;
            }

            try {
                int number = Integer.parseInt(input);

                if (number >= minimum &&
                    number <= maximum) {
                    return number;
                }

            } catch (NumberFormatException exception) {
                System.out.println("The number is too large.");
                System.out.println();
                continue;
            }

            System.out.println(
                "Invalid number! Please enter a value from " +
                minimum + " to " + maximum + "."
            );
            System.out.println();
        }
    }

    // Asks whether to play again and returns true for YES or false for NO.
    public static boolean askPlayAgain(Scanner scanner) {
        while (true) {
            ConsoleUI.prompt(
                "Do you want to play again? (YES/NO): "
            );

            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("YES")) {
                return true;
            }

            if (answer.equalsIgnoreCase("NO")) {
                return false;
            }

            System.out.println(
                "Invalid input! Please enter YES or NO."
            );
        }
    }

}
