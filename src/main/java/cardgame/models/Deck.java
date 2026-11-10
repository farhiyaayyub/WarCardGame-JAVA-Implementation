package cardgame.models;

import cardgame.enums.Rank;
import cardgame.enums.Suit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Manages the collection and order of cards. Reads and validates the input file,
 * shuffles the deck, and adds or removes cards.
 * Main methods: loadCards(), perfectShuffle(), removeTop(), addBottom().
 *
 * @author Farhiya Ayyub
 * @version October 30, 2026
 */
public class Deck {

    // Think of this as a pile: take cards from the top and add cards at the bottom.
    private final Deque<Card> cards = new ArrayDeque<>();

    /**
     * Creates an empty deck.
     */
    public Deck() {
    }

    /**
     * Creates a deck containing the supplied cards in their original order.
     *
     * @param initialCards cards to copy into the deck
     */
    public Deck(List<Card> initialCards) {
        cards.addAll(initialCards);
    }

    /**
     * Loads and validates exactly 52 distinct cards from a file or bundled resource.
     *
     * @param filename disk path or resource name to read
     * @return validated cards in input order
     * @throws IOException              if the file cannot be found or read
     * @throws IllegalArgumentException if a card is invalid, duplicated, or the count is not 52
     */
    public static List<Card> loadCards(String filename) throws IOException {
        List<Card> cards = new ArrayList<>();

        try (BufferedReader reader = openCardReader(filename)) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Separate card codes using commas, spaces, or tabs.
                StringTokenizer tokenizer = new StringTokenizer(line, ", \t\r\n");

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    // Example: D-A becomes suit D and rank A. Keep empty parts to detect missing codes.
                    String[] parts = token.split("-", -1);

                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid card format: " + token);
                    }

                    Suit suit = Suit.fromCode(parts[0]);
                    Rank rank = Rank.fromCode(parts[1]);
                    Card card = new Card(suit, rank);

                    // Do not allow the same card twice.
                    if (cards.contains(card)) {
                        throw new IllegalArgumentException("Duplicate card found: " + card);
                    }

                    cards.add(card);
                }
            }
        }

        if (cards.size() != 52) {
            throw new IllegalArgumentException("Invalid deck! Expected 52 cards, found " + cards.size());
        }

        return cards;
    }

    /**
     * Opens a UTF-8 reader, preferring a disk file over the bundled resource.
     *
     * @param filename disk path or resource name to open
     * @return an open reader that the caller must close
     * @throws IOException if the file cannot be found or opened
     */
    private static BufferedReader openCardReader(String filename) throws IOException {
        // Look for the file on disk first, then inside the packaged application.
        if (Files.exists(Paths.get(filename))) {
            return Files.newBufferedReader(Paths.get(filename));
        }

        String resourcePath = "/" + filename.replace('\\', '/');
        InputStream resource = Deck.class.getResourceAsStream(resourcePath);
        if (resource == null) {
            throw new IOException("Card file not found on disk or inside the JAR: " + filename);
        }
        return new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
    }

    /**
     * Interleaves the first and second halves, starting with the first half.
     *
     * @throws IllegalStateException if the deck contains an odd number of cards
     */
    public void perfectShuffle() {
        List<Card> original = getCards();

        if (original.size() % 2 != 0) {
            throw new IllegalStateException("Perfect shuffle requires an even number of cards.");
        }

        int half = original.size() / 2;
        cards.clear();

        for (int i = 0; i < half; i++) {
            addBottom(original.get(i));
            addBottom(original.get(i + half));
        }
    }

    /**
     * Removes the card at the top of the deck.
     *
     * @return the removed card
     * @throws java.util.NoSuchElementException if the deck is empty
     */
    public Card removeTop() {
        // Take out and return the first card.
        return cards.removeFirst();
    }

    /**
     * Appends a card to the bottom of the deck.
     *
     * @param card card to append
     * @throws NullPointerException if the card is null
     */
    public void addBottom(Card card) {
        // Place the card at the end of the pile.
        cards.addLast(card);
    }

    /**
     * Checks whether the deck has no cards.
     *
     * @return true if the deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Counts the cards currently in the deck.
     *
     * @return the number of cards
     */
    public int size() {
        return cards.size();
    }

    /**
     * Copies the deck's cards in top-to-bottom order.
     *
     * @return a new list whose modification does not change this deck
     */
    public List<Card> getCards() {
        // Return a copy so changing the list does not change this deck.
        return new ArrayList<>(cards);
    }

    /**
     * Prints colored cards with up to eight cards per row.
     *
     * @param cards cards to display in list order
     */
    public static void displayCards(List<Card> cards) {
        if (cards.isEmpty()) {
            System.out.println("(No cards)");
            return;
        }
        int count = 0;

        // Go through each card in the list.
        for (Card card : cards) {

            // Display the card in color, with spaces between cards.
            System.out.printf("%-5s  ", card.toColoredString());
            count++; // Keep track of how many cards have been displayed.

            // Move to a new line after every 8 displayed cards.
            if (count % 8 == 0) {
                System.out.println();
            }
        }

        // Move to a new line if the last row has fewer than 8 cards.
        if (count % 8 != 0) {
            System.out.println();
        }
    }

    /**
     * Formats the deck as a list of card codes.
     *
     * @return the textual representation of the cards
     */
    @Override
    public String toString() {
        return cards.toString();
    }
}
