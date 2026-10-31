package cardgame;

import cardgame.enums.Rank;
import cardgame.enums.Suit;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Deck {

    private final Deque<Card> cards = new ArrayDeque<>();

    public Deck() {
    }

    public Deck(List<Card> initialCards) {
        cards.addAll(initialCards);
    }

    // Reads card codes in file order, validates format, suit, and rank, and rejects duplicates.
    // Returns the cards only when exactly 52 valid cards have been loaded.
    public static List<Card> loadCards(String filename) throws IOException {
        List<Card> cards = new ArrayList<>();
        Set<Card> uniqueCards = new HashSet<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ", \t\r\n");

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    String[] parts = token.split("-", -1);

                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid card format: " + token);
                    }

                    Suit suit = Suit.fromCode(parts[0]);
                    Rank rank = Rank.fromCode(parts[1]);
                    Card card = new Card(suit, rank);

                    if (!uniqueCards.add(card)) {
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

    // 1. Copies the current order and rejects decks with an odd number of cards.
    // 2. Splits the copy into equal halves and clears the deck.
    // 3. Rebuilds the deck by alternating cards from the first and second halves.
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
        return cards.removeFirst();
    }

    public void addBottom(Card card) {
        cards.addLast(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public static void displayCards(List<Card> cards) {
        if (cards.isEmpty()) {
            System.out.println("(No cards)");
            return;
        }

        int count = 0;

        for (Card card : cards) {
            System.out.printf("%-5s  ", card.toColoredString());
            count++;

            if (count % 8 == 0) {
                System.out.println();
            }
        }

        if (count % 8 != 0) {
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
