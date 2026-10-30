
package cardgame;

import java.util.ArrayList;
import java.util.List;

// Runs one card game, including shuffling, dealing, rounds, and the final result.
public class Game {

    public static final int MIN_SHUFFLES = 1;
    public static final int MAX_SHUFFLES = 100;

    private final List<Player> players;
    private final Deck mainDeck;

    // Creates the players, checks the shuffle count, shuffles, and deals the cards.
    public Game(
        List<Card> originalCards,
        int numberOfPlayers,
        int numberOfShuffles
    ) {
        if (numberOfShuffles < MIN_SHUFFLES ||
            numberOfShuffles > MAX_SHUFFLES) {
            throw new IllegalArgumentException(
                "Number of shuffles must be between " +
                MIN_SHUFFLES + " and " + MAX_SHUFFLES + "."
            );
        }

        players = new ArrayList<>();
        mainDeck = new Deck(originalCards);

        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player(i));
        }

        prepareDeck(numberOfShuffles);
        distributeCards();
    }

    // Shows the deck and performs the requested number of perfect shuffles.
    private void prepareDeck(int numberOfShuffles) {
        ConsoleUI.heading("PREPARING THE 52-CARD DECK");

        System.out.println();
        ConsoleUI.subheading("ORIGINAL DECK:");
        ConsoleUI.displayCards(mainDeck.getCards());

        System.out.println();
        ConsoleUI.subheading(
            "Performing " + numberOfShuffles +
            " perfect shuffle(s)..."
        );

        for (int i = 0; i < numberOfShuffles; i++) {
            mainDeck.perfectShuffle();
        }

        System.out.println();
        ConsoleUI.subheading("SHUFFLED DECK:");
        ConsoleUI.displayCards(mainDeck.getCards());
    }

    // Deals cards one at a time until every card has gone to a player.
    private void distributeCards() {
        ConsoleUI.heading("DEALING CARDS TO PLAYERS");

        int currentPlayer = 0;

        while (!mainDeck.isEmpty()) {
            Player player = players.get(currentPlayer);
            Card card = mainDeck.removeTop();
            player.getDeck().addBottom(card);

            currentPlayer =
                (currentPlayer + 1) % players.size();
        }

        System.out.println();
        ConsoleUI.successMessage(
            "All 52 cards have been distributed."
        );
        System.out.println();

        for (Player player : players) {
            ConsoleUI.subheading(
                "PLAYER " + player.getPlayerNumber() +
                " - " + player.getDeck().size() +
                " CARDS"
            );

            ConsoleUI.displayCards(player.getDeck().getCards());
            System.out.println();
        }
    }

    // Repeats card rounds until a player owns all 52 cards.
    public void start() {
        ConsoleUI.heading("THE CARD BATTLE BEGINS!");

        long roundNumber = 1;

        while (true) {
            Player champion = findChampion();

            if (champion != null) {
                ConsoleUI.finalResult(
                    champion,
                    roundNumber - 1,
                    players
                );
                return;
            }

            ConsoleUI.startRound(roundNumber);

            List<Play> playedCards = new ArrayList<>();
            Play highestPlay = null;

            for (Player player : players) {
                if (!player.isActive()) {
                    continue;
                }

                Card card = player.getDeck().removeTop();
                Play play = new Play(player, card);
                playedCards.add(play);

                ConsoleUI.subheading(
                    "Player " +
                    player.getPlayerNumber() +
                    " plays: " + card.toColoredString()
                );

                if (highestPlay == null ||
                    card.compareTo(
                        highestPlay.getCard()
                    ) > 0) {

                    highestPlay = play;
                }
            }

            Player roundWinner =
                highestPlay.getPlayer();

            List<Card> collected = collectCards(
                roundWinner,
                playedCards
            );

            ConsoleUI.roundWinner(
                roundWinner,
                roundNumber,
                collected
            );

            checkEliminations();
            roundNumber++;
        }
    }

    // Returns the player with all 52 cards, or null if there is no winner yet.
    private Player findChampion() {
        for (Player player : players) {
            if (player.getDeck().size() == 52) {
                return player;
            }
        }

        return null;
    }

    // Gives all cards played in the round to the winner in player order.
    private List<Card> collectCards(
        Player winner,
        List<Play> playedCards
    ) {
        List<Card> collected = new ArrayList<>();
        int totalPlayers = players.size();

        for (int offset = 0;
             offset < totalPlayers;
             offset++) {

            int playerNumber =
                ((winner.getPlayerNumber() - 1 + offset)
                % totalPlayers) + 1;

            for (Play play : playedCards) {
                if (play.getPlayer().getPlayerNumber()
                    == playerNumber) {

                    collected.add(play.getCard());
                    break;
                }
            }
        }

        for (Card card : collected) {
            winner.getDeck().addBottom(card);
        }

        return collected;
    }

    // Marks and announces any players whose decks have become empty.
    private void checkEliminations() {
        for (Player player : players) {
            if (!player.isActive() &&
                !player.isEliminated()) {

                player.eliminate();

                ConsoleUI.subheading(
                    "Player " +
                    player.getPlayerNumber() +
                    " has been eliminated."
                );
            }
        }
    }
}
