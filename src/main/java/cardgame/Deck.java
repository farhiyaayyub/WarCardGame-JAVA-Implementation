package cardgame;

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

public class Deck {

    // Think of this as a pile: take cards from the top and add cards at the bottom.
    private final Deque<Card> cards = new ArrayDeque<>();

    public Deck() {
    }

    public Deck(List<Card> initialCards) {
        cards.addAll(initialCards);
    }

    // Read the file -> check each card -> return 52 different cards.
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

    // Split the pile in half, then take one card from each half in turn.
    // Example: [A, B, C, D] -> [A, C, B, D].
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

    public Card removeTop() {
        // Take out and return the first card.
        return cards.removeFirst();
    }

    public void addBottom(Card card) {
        // Place the card at the end of the pile.
        cards.addLast(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public List<Card> getCards() {
        // Return a copy so changing the list does not change this deck.
        return new ArrayList<>(cards);
    }

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

    // Return the deck's list of cards as text.
    @Override
    public String toString() {
        return cards.toString();
    }
}
