package cardgame.game;

import cardgame.models.Card;
import cardgame.models.Deck;
import cardgame.models.Player;
import cardgame.utils.Console;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Controls the game. Shuffles and deals cards, runs rounds, selects round winners,
 * and checks for eliminated players and the champion.
 * Main methods: start(), playRound(), findRoundWinner(), collectCards().
 *
 * @author Farhiya Ayyub
 * @version October 30, 2026
 */
public class Game {

    public static final int MIN_SHUFFLES = 1;
    public static final int MAX_SHUFFLES = 100;

    private final List<Player> players;
    private final Deck mainDeck;

    /**
     * Creates players, shuffles the supplied cards, and deals the deck.
     *
     * @param originalCards starting cards in their original order
     * @param numberOfPlayers number of players; the caller supplies a value from 2 to 8
     * @param numberOfShuffles number of perfect shuffles to perform
     * @throws IllegalArgumentException if the shuffle count is outside the allowed range
     */
    public Game(List<Card> originalCards, int numberOfPlayers, int numberOfShuffles) {
        if (numberOfShuffles < MIN_SHUFFLES || numberOfShuffles > MAX_SHUFFLES) {
            throw new IllegalArgumentException(
                    "Number of shuffles must be between " + MIN_SHUFFLES + " and " + MAX_SHUFFLES + ".");
        }

        players = new ArrayList<>();
        mainDeck = new Deck(originalCards);

        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player(i));
        }

        prepareDeck(numberOfShuffles);
        distributeCards();
    }

    /**
     * Performs the requested perfect shuffles and displays the resulting deck.
     *
     * @param numberOfShuffles number of shuffles to perform
     */
    private void prepareDeck(int numberOfShuffles) {
        System.out.println();
        System.out.println("Performing " + numberOfShuffles + " perfect shuffle(s)...");

        for (int i = 1; i <= numberOfShuffles; i++) {
            mainDeck.perfectShuffle();
        }

        System.out.println();
        Console.subheading("SHUFFLED DECK:");
        Deck.displayCards(mainDeck.getCards());
    }

    /**
     * Deals cards one at a time in player order until the main deck is empty.
     */
    private void distributeCards() {
        System.out.println();
        Console.centeredHeading("DEALING CARDS TO PLAYERS.....");
        System.out.println();

        int currentPlayer = 0;

        while (!mainDeck.isEmpty()) {

            Player player = players.get(currentPlayer);
            Card card = mainDeck.removeTop();
            player.getDeck().addBottom(card);

            // Move to the next player. After the last player, start at the first again.
            currentPlayer++;
            if (currentPlayer == players.size()) {
                currentPlayer = 0;
            }
        }

        Console.successMessage("All 52 cards have been distributed.");
        System.out.println();

        // Show how many cards each player received.
        for (Player player : players) {
            System.out.println("PLAYER " + player.getPlayerNumber() + " - " + player.getDeck().size() + " CARDS");
        }
    }

    /**
     * Runs rounds until a player holds all 52 cards, then displays the result.
     * There is no round limit or repeated-state detection.
     */
    public void start() {
        System.out.println();
        Console.centeredHeading("THE CARD BATTLE BEGINS!");

        long roundNumber = 1;

        // Repeat this loop until findChampion finds a player with all 52 cards.
        while (true) {
            Player champion = findChampion();

            if (champion != null) {
                displayFinalResult(champion, roundNumber - 1);

                // End start() and return to Main, which asks whether to play again.
                return;
            }

            playRound(roundNumber);
            // Move to the next round, then check for a champion again.
            roundNumber++;
        }
    }

    /**
     * Searches for a player holding all 52 cards.
     * @return the champion, or null if no player has all cards
     */
    private Player findChampion() {
        for (Player player : players) {
            if (player.getDeck().size() == 52) {
                return player;
            }
        }

        return null;
    }

    /**
     * Draws cards, awards them to the round winner, and checks eliminations.
     * @param roundNumber number of the round to play
     */
    private void playRound(long roundNumber) {
        displayRoundHeading(roundNumber);

        // Draw -> show cards -> choose winner -> give cards to winner -> check who is out.
        Map<Player, Card> playedCards = drawCards();
        displayPlayedCards(playedCards);
        Player winner = findRoundWinner(playedCards);
        List<Card> collected = collectCards(winner, playedCards);

        displayRoundWinner(winner, roundNumber, collected);

        checkEliminations();
    }

    /**
     * Displays the heading for a round.
     * @param round number of the round
     */
    private static void displayRoundHeading(long round) {
        Console.heading("START ROUND " + round);
    }

    /**
     * Removes one top card from each active player's deck.
     * @return played cards associated with players in player order
     */
    private Map<Player, Card> drawCards() {
        // Keep each player together with the card they played, in player order.
        Map<Player, Card> playedCards = new LinkedHashMap<>();

        // Check each player one by one.
        for (Player player : players) {
            // If the player has no cards left, skip to the next player.
            if (!player.isActive()) {
                continue;
            }

            // Remove the player's top card and store it with the player in playedCards.
            Card card = player.getDeck().removeTop();
            playedCards.put(player, card);
        }

        return playedCards;
    }

    /**
     * Displays each player's played card.
     * @param playedCards mapping of players to their played cards
     */
    private void displayPlayedCards(Map<Player, Card> playedCards) {
        for (Player player : playedCards.keySet()) {
            Card card = playedCards.get(player);
            System.out.println("Player " + player.getPlayerNumber() + " plays: " + card.toColoredString());
        }
    }

    /**
     * Finds the player with the strongest played card.
     * @param playedCards mapping of players to their played cards
     * @return the winning player, or null if no cards were played
     */
    private Player findRoundWinner(Map<Player, Card> playedCards) {
        Player winner = null;
        Card highestCard = null;

        for (Player player : playedCards.keySet()) {
            Card playedCard = playedCards.get(player);

            if (highestCard == null || playedCard.compareTo(highestCard) > 0) {
                winner = player;
                highestCard = playedCard;
            }
        }

        return winner;
    }

    /**
     * Adds played cards to the winner's deck, starting with the winner and cycling through player order.
     * @param winner player receiving the cards
     * @param playedCards mapping of players to their played cards
     * @return cards in the order they were added to the winner's deck
     */
    private List<Card> collectCards(Player winner, Map<Player, Card> playedCards) {
        List<Card> collected = new ArrayList<>();
        int totalPlayers = players.size();
        // Player 1 is at list position 0, so subtract 1.
        int playerIndex = winner.getPlayerNumber() - 1;

        // Repeat once for each player to collect the cards they played
        for (int offset = 0; offset < totalPlayers; offset++) {
            Player player = players.get(playerIndex);
            Card card = playedCards.get(player);
            if (card != null) {
                collected.add(card);
            }
            // Continue around the players, starting from the winner.
            playerIndex++;
            if (playerIndex == totalPlayers) {
                playerIndex = 0;
            }
        }

        for (Card card : collected) {
            winner.getDeck().addBottom(card);
        }

        return collected;
    }

    /**
     * Displays the winner, collected cards, and updated deck.
     * @param winner player who won the round
     * @param round number of the completed round
     * @param collected cards awarded to the winner
     */
    private static void displayRoundWinner(Player winner, long round, List<Card> collected) {
        System.out.println();
        Console.successMessage("ROUND " + round + " WINNER: PLAYER " + winner.getPlayerNumber());
        System.out.println();

        StringJoiner collectedCards = new StringJoiner(", ");
        for (Card card : collected) {
            collectedCards.add(card.toColoredString());
        }

        Console.subheading("COLLECTED CARDS:");
        System.out.println(collectedCards);

        winner.displayDeck("UPDATED DECK");
    }

    /**
     * Marks and announces newly eliminated players whose decks are empty.
     */
    private void checkEliminations() {
        for (Player player : players) {
            if (!player.isActive() && !player.isEliminated()) {
                player.eliminate();
                System.out.println();
                System.out.println("Player " + player.getPlayerNumber() + " has been eliminated.");
            }
        }
    }

    /**
     * Displays the champion, total rounds, final deck, and closing message.
     * @param champion player holding all 52 cards
     * @param totalRounds number of completed rounds
     */
    private static void displayFinalResult(Player champion, long totalRounds) {
        Console.heading("GAME RESULT");
        Console.centeredSuccessMessage("PLAYER " + champion.getPlayerNumber() + " WINS!");
        Console.centeredSuccessMessage("Congratulations, Player " + champion.getPlayerNumber()
                + "! You collected all 52 cards!");
        System.out.println();
        System.out.println("Total Rounds Played: " + totalRounds);
        champion.displayDeck("FINAL DECK");
        System.out.println();
        Console.centeredSuccessMessage("Thank you for playing the War Card Game!");
    }
}
