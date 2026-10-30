
package cardgame;

import java.util.List;
import java.util.stream.Collectors;

// Prints game messages, card lists, round details, and results.
public class ConsoleUI {

    private static final String GREEN = "\u001B[92m";
    private static final String CYAN = "\u001B[96m";
    private static final String YELLOW = "\u001B[93m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String RESET = "\u001B[0m";

    // Prints a long line to separate sections on the screen.
    public static void line() {
        System.out.println(CYAN + "====================================================" + RESET);
    }

    // Prints a shorter line to separate related messages.
    public static void separator() {
        System.out.println(CYAN + "----------------------------------------------------" + RESET);
    }

    // Prints a success message in green, then resets the terminal color.
    public static void successMessage(String message) {
        System.out.println(GREEN + message + RESET);
    }

    // Prints a smaller section label in yellow.
    public static void subheading(String label) {
        System.out.println(YELLOW + label + RESET);
    }

    // Prints a prompt in cyan and leaves the cursor ready for the player's answer.
    public static void prompt(String message) {
        System.out.print(CYAN + message + RESET);
    }

    // Welcomes the player and explains the basic rules.
    public static void welcome() {
        System.out.println();
        System.out.println(
            MAGENTA + "          WELCOME TO WAR CARD GAME!" + RESET
        );
        System.out.println();
        subheading("GAME RULES:");
        System.out.println("1. Each active player plays their top card.");
        System.out.println("2. The highest card wins the round.");
        System.out.println("3. The winner collects all played cards.");
        System.out.println("4. The first player to collect all 52 cards wins!");
        System.out.println();
    }

    // Prints a title surrounded by separating lines.
    public static void heading(String title) {
        System.out.println();
        line();
        System.out.println(CYAN + "             " + title + RESET);
        line();
    }

    // Prints cards in rows, or a message if the list is empty.
    public static void displayCards(List<Card> cards) {
        if (cards.isEmpty()) {
            System.out.println("(No cards)");
            return;
        }

        int count = 0;

        for (Card card : cards) {
            System.out.printf("%-5s", card.toColoredString());
            System.out.print("  ");
            count++;

            if (count % 8 == 0) {
                System.out.println();
            }
        }

        if (count % 8 != 0) {
            System.out.println();
        }
    }

    // Prints a player's deck with its label and card count.
    public static void displayDeck(Player player, String label) {
        heading("PLAYER " + player.getPlayerNumber() + "'S " + label);
        System.out.println();
        subheading("Total Cards: " + player.getDeck().size());
        System.out.println();
        displayCards(player.getDeck().getCards());
    }

    // Prints whether each player is active or eliminated and how many cards they have.
    public static void displayPlayerStatus(List<Player> players) {
        heading("PLAYER STATUS");
        System.out.println();

        for (Player player : players) {
            System.out.printf(
                "Player %-2d : %-12s (%d Cards)%n",
                player.getPlayerNumber(),
                player.isEliminated() ? "ELIMINATED" : "ACTIVE",
                player.getDeck().size()
            );
        }
    }

    // Prints a divider and the number of the round that is starting.
    public static void startRound(long round) {
        System.out.println();
        separator();
        subheading("                START ROUND " + round);
        separator();
    }

    // Announces the round winner, shows the cards collected, and displays their deck.
    public static void roundWinner(
        Player winner,
        long round,
        List<Card> collected
    ) {
        System.out.println();
        successMessage(
            "ROUND " + round + " WINNER: PLAYER " +
            winner.getPlayerNumber()
        );

        String collectedCards = collected.stream()
            .map(Card::toColoredString)
            .collect(Collectors.joining(", "));

        subheading("COLLECTED CARDS:");
        System.out.println(collectedCards);

        displayDeck(winner, "UPDATED DECK");
    }

    // Prints the final player statuses, winner, round count, and winning deck.
    public static void finalResult(
        Player champion,
        long totalRounds,
        List<Player> players
    ) {
        displayPlayerStatus(players);
        heading("GAME RESULT");
        System.out.println();
        successMessage(
            "              PLAYER " +
            champion.getPlayerNumber() + " WINS!"
        );
        System.out.println();
        subheading(
            "Reason: All other players have been eliminated."
        );
        subheading("Total Rounds Played: " + totalRounds);
        displayDeck(champion, "FINAL DECK");
    }

    // Prints the question heading shown before asking to play again.
    public static void playAgainHeading() {
        System.out.println();
        subheading("             START A NEW GAME?");
        separator();
    }

    // Prints the closing message when the player is finished.
    public static void goodbye() {
        System.out.println();
        successMessage("             THANK YOU FOR PLAYING!");
        successMessage("        SEE YOU IN THE NEXT CARD BATTLE!");
    }
}
