package cardgame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Game {

    public static final int MIN_SHUFFLES = 1;
    public static final int MAX_SHUFFLES = 100;

    private final List<Player> players;
    private final Deck mainDeck;

    // Validates the shuffle count, creates the deck and players, then shuffles and deals.
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

    // Performs the requested perfect shuffles, then displays the shuffled deck.
    private void prepareDeck(int numberOfShuffles) {
        System.out.println();
        System.out.println("Performing " + numberOfShuffles + " perfect shuffle(s)...");

        for (int i = 0; i < numberOfShuffles; i++) {
            mainDeck.perfectShuffle();
        }

        System.out.println();
        Main.subheading("SHUFFLED DECK:");
        Deck.displayCards(mainDeck.getCards());
    }

    // Deals top cards to players in turn until the main deck is empty, then shows counts.
    private void distributeCards() {
        System.out.println();
        Main.centeredHeading("DEALING CARDS TO PLAYERS.....");
        System.out.println();

        int currentPlayer = 0;

        while (!mainDeck.isEmpty()) {
            Player player = players.get(currentPlayer);
            Card card = mainDeck.removeTop();
            player.getDeck().addBottom(card);

            currentPlayer = (currentPlayer + 1) % players.size();
        }

        Main.successMessage("All 52 cards have been distributed.");
        System.out.println();

        for (Player player : players) {
            System.out.println(
                    "PLAYER " + player.getPlayerNumber() + " - " + player.getDeck().size() + " CARDS");
        }
    }

    // Checks for a champion before each round; displays the final result when one is found.
    public void start() {
        System.out.println();
        Main.centeredHeading("THE CARD BATTLE BEGINS!");

        long roundNumber = 1;

        while (true) {
            Player champion = findChampion();

            if (champion != null) {
                finalResult(champion, roundNumber - 1, players);
                return;
            }

            playRound(roundNumber);
            roundNumber++;
        }
    }

    // Returns the player holding all 52 cards, or null if there is no champion yet.
    private Player findChampion() {
        for (Player player : players) {
            if (player.getDeck().size() == 52) {
                return player;
            }
        }

        return null;
    }

    // 1. Opens the round, draws cards, and displays each player's card.
    // 2. Finds the winner and adds the collected cards to their deck.
    // 3. Displays the result and updated deck, then checks eliminations.
    private void playRound(long roundNumber) {
        startRound(roundNumber);

        Map<Player, Card> playedCards = drawCards();
        displayPlayedCards(playedCards);
        Player winner = findRoundWinner(playedCards);
        List<Card> collected = collectCards(winner, playedCards);

        roundWinner(winner, roundNumber, collected);

        checkEliminations();
    }

    public static void startRound(long round) {
        Main.heading("START ROUND " + round);
    }

    // Removes each active player's top card and records it in player order.
    private Map<Player, Card> drawCards() {
        Map<Player, Card> playedCards = new LinkedHashMap<>();

        for (Player player : players) {
            if (!player.isActive()) {
                continue;
            }

            Card card = player.getDeck().removeTop();
            playedCards.put(player, card);
        }

        return playedCards;
    }

    private void displayPlayedCards(Map<Player, Card> playedCards) {
        for (Player player : playedCards.keySet()) {
            Card card = playedCards.get(player);
            System.out.println("Player " + player.getPlayerNumber() + " plays: " + card.toColoredString());
        }
    }

    // 1. Uses the first played card as the initial highest card.
    // 2. Compares the remaining cards using Card.compareTo(), keeping the highest.
    // 3. Returns its player, or null if no cards were played.
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

    // 1. Collects the winner's card first, then follows player order with wraparound.
    // 2. Skips players who did not play and appends the cards to the winner's deck bottom.
    // 3. Returns the ordered collection for display.
    private List<Card> collectCards(Player winner, Map<Player, Card> playedCards) {
        List<Card> collected = new ArrayList<>();
        int totalPlayers = players.size();
        int winnerIndex = winner.getPlayerNumber() - 1;

        for (int offset = 0; offset < totalPlayers; offset++) {
            int playerIndex = (winnerIndex + offset) % totalPlayers;
            Player player = players.get(playerIndex);
            Card card = playedCards.get(player);
            if (card != null) {
                collected.add(card);
            }
        }

        for (Card card : collected) {
            winner.getDeck().addBottom(card);
        }

        return collected;
    }

    public static void roundWinner(Player winner, long round, List<Card> collected) {
        System.out.println();
        Main.successMessage("ROUND " + round + " WINNER: PLAYER " + winner.getPlayerNumber());
        System.out.println();

        StringJoiner collectedCards = new StringJoiner(", ");
        for (Card card : collected) {
            collectedCards.add(card.toColoredString());
        }

        Main.subheading("COLLECTED CARDS:");
        System.out.println(collectedCards);

        winner.displayDeck("UPDATED DECK");
    }

    // After collection, marks players with empty decks as eliminated and announces each once.
    private void checkEliminations() {
        for (Player player : players) {
            if (!player.isActive() && !player.isEliminated()) {
                player.eliminate();
                System.out.println();
                System.out.println("Player " + player.getPlayerNumber() + " has been eliminated.");
            }
        }
    }

    public static void finalResult(Player champion, long totalRounds, List<Player> players) {
        Main.heading("GAME RESULT");
        Main.centeredSuccessMessage("PLAYER " + champion.getPlayerNumber() + " WINS!");
        Main.centeredSuccessMessage("Congratulations, Player " + champion.getPlayerNumber()
                + "! You collected all 52 cards!");
        System.out.println();
        System.out.println("Total Rounds Played: " + totalRounds);
        champion.displayDeck("FINAL DECK");
        System.out.println();
        Main.centeredSuccessMessage("Thank you for playing the War Card Game!");
    }
}