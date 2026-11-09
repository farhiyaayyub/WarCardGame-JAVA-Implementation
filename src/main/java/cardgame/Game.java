package cardgame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Controls the game. Shuffles and deals cards, runs rounds, selects round winners,
 * and checks for eliminated players and the champion.
 * Main methods: start(), playRound(), findRoundWinner(), collectCards().
 */
public class Game {

    public static final int MIN_SHUFFLES = 1;
    public static final int MAX_SHUFFLES = 100;

    private final List<Player> players;
    private final Deck mainDeck;

    // 1. SETUP: Check the shuffle count, create the players and deck, then shuffle and deal.
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

    // 2. SHUFFLE: Shuffle the deck as many times as the user requested.
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

    // 3. DEAL: Give one card at a time to each player until the deck is empty.
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

    // 4. GAME LOOP: Main calls this after setup. Keep playing rounds until someone wins.
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

    // 5. CHECK CHAMPION: Before each round, check if a player has all 52 cards.
    private Player findChampion() {
        for (Player player : players) {
            if (player.getDeck().size() == 52) {
                return player;
            }
        }

        return null;
    }

    // 6. PLAY ONE ROUND: Follow the steps below, then go back to the game loop.
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

    // 6.1. Show the current round number.
    private static void displayRoundHeading(long round) {
        Console.heading("START ROUND " + round);
    }

    // 6.2. Take the top card from each player who still has cards.
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

    // 6.3. Show the card that each player played.
    private void displayPlayedCards(Map<Player, Card> playedCards) {
        for (Player player : playedCards.keySet()) {
            Card card = playedCards.get(player);
            System.out.println("Player " + player.getPlayerNumber() + " plays: " + card.toColoredString());
        }
    }

    // 6.4. Compare the played cards. The player with the highest card wins the round.
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

    // 6.5. Collect the winner's card first, then the others in player order. Add them to the winner's deck.
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

    // 6.6. Show the round winner, collected cards, and the winner's updated deck.
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

    // 6.7. Announce players who ran out of cards. Then the next round can begin.
    private void checkEliminations() {
        for (Player player : players) {
            if (!player.isActive() && !player.isEliminated()) {
                player.eliminate();
                System.out.println();
                System.out.println("Player " + player.getPlayerNumber() + " has been eliminated.");
            }
        }
    }

    // 7. END GAME: Once a champion is found, show the winner and total rounds played.
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
