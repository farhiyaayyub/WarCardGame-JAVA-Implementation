package cardgame;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// Stores a pile of cards and provides ways to take, add, show, and shuffle them.
public class Deck {

    private final Deque<Card> cards = new ArrayDeque<>();

    // Creates an empty deck, ready to receive cards.
    public Deck() {
    }

    // Creates a deck that starts with the cards in the given list.
    public Deck(List<Card> initialCards) {
        cards.addAll(initialCards);
    }

    // Says whether this deck has no cards.
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Tells how many cards are in this deck.
    public int size() {
        return cards.size();
    }

    // Takes and returns the card at the top of the deck.
    public Card removeTop() {
        return cards.removeFirst();
    }

    // Puts a card at the bottom of the deck.
    public void addBottom(Card card) {
        cards.addLast(card);
    }

    // Returns a list showing the cards without changing the deck.
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    // Splits the deck in half and alternates cards from both halves.
    public void perfectShuffle() {
        List<Card> original = getCards();

        if (original.size() % 2 != 0) {
            throw new IllegalStateException(
                "Perfect shuffle requires an even number of cards."
            );
        }

        int half = original.size() / 2;
        cards.clear();

        for (int i = 0; i < half; i++) {
            addBottom(original.get(i));
            addBottom(original.get(i + half));
        }
    }

    // Makes the deck readable when it is converted to text.
    @Override
    public String toString() {
        return cards.toString();
    }
}
