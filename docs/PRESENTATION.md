# Java War Card Game — Live Code Walkthrough and Panel Defense Guide

Based on the current seven Java source files and `resources/input.txt`. This guide follows the actual calls, not the order of every class declaration. No Java files, comments, game rules, or console messages were changed to prepare it.

## How to practice

1. Open `src/cardgame/Main.java` at `main()`.
2. Read the code first, then explain the idea in your own words. The Taglish passages are speaking suggestions.
3. Press **Ctrl + B** on the actual method name or constructor in the code, not on a comment.
4. After a nested explanation, press **Ctrl + Alt + Left** to return to the calling location. Repeat as needed when two or three calls are nested.
5. For `heading()`, `subheading()`, `prompt()`, `successMessage()`, `centeredHeading()`, and `centeredSuccessMessage()`, explain “display formatting lang” and stay in the game logic. Don't open Java library internals.
6. Same-file calls can also use Ctrl + B. These shortcuts assume your IntelliJ keymap matches the shortcuts you specified; if changed, use the named Go to Declaration and Navigate Back actions.

**Navigation is not execution.** Ctrl + B shows source code; Ctrl + Alt + Left changes the editor location. While presenting, you're tracing what would execute when Run is used. Editor history can include other clicks; use Navigate Back until you reach the stated caller.

## One-minute map of the project

| File | Purpose, introduced when the flow reaches it |
| --- | --- |
| Main.java | Starts the application, reads game settings, validates keyboard input, manages replay, and supplies shared output helpers. |
| Game.java | Creates the players/main deck, prepares and deals cards, runs rounds, awards cards, checks elimination/champion, and prints results. |
| Deck.java | Holds an ordered card queue; reads/validates the file, performs perfect shuffles, removes top cards, adds bottom cards, and displays card lists. |
| Player.java | Stores one player number, a personal Deck, and an elimination flag; displays that player's deck. |
| Card.java | Stores one Suit and Rank; compares cards, formats their text, and defines equality/hash behavior. |
| enums/Rank.java | Fixed ranks in highest-to-lowest order: Ace through Two. |
| enums/Suit.java | Fixed suits in tie-break order: Diamond, Heart, Spade, Club; also stores codes/symbols. |
| resources/input.txt | Starting data: 52 card codes, Diamond A–2, Heart A–2, Spade A–2, Club A–2. |

“So ang Main yung simula. Ang Game yung nagpapatakbo ng laban. Bawat Player may sariling Deck, at bawat Deck may Cards. Bawat Card may Suit at Rank. Makikita natin yung connection na iyon habang sinusundan natin yung calls.”

## Chronological walkthrough

### STEP 1: Program entry point: welcome muna

**Current file:** `Main.java`

**Code to highlight:**

```java
    public static void main(String[] args) {
        welcome();
```

**What I should say (Natural Taglish):**

“So dito sa Main, dito nagsisimula yung program natin. Yung main ang entry point na hinahanap ng Java launcher. First statement natin yung welcome(), so rules muna bago mag-load ng cards.”

**IntelliJ action:** Press Ctrl + B on `welcome` in `welcome();`. Same-file navigation ito.

**Destination:** Main.java → welcome()

**What I should explain there:**

`public` means accessible yung method. `static` means puwedeng tawagin ng launcher nang walang `new Main()`. `void` means walang returned value. `String[] args` para sa command-line arguments; hindi ginagamit yung args sa code natin.

**Navigation after explanation:** Stay at welcome() for Step 2; huwag munang bumalik.

**Next step:** Ipakita yung welcome implementation.

### STEP 2: Welcome at rules

**Current file:** `Main.java`

**Code to highlight:**

```java
        welcome();
        try (Scanner scanner = new Scanner(System.in)) {
            List<Card> cards = Deck.loadCards("resources/input.txt");
            do {
                Game game = createGame(cards, scanner);
                game.start();
                playAgainHeading();
            } while (askPlayAgain(scanner));
            goodbye();
        } catch (IOException | IllegalArgumentException exception) {
            System.out.println("ERROR: Unable to load resources/input.txt.");
            System.out.println(exception.getMessage());
        }
```

**What I should say (Natural Taglish):**

“So itong welcome, nagpapakita lang ng title at rules. Yung centeredHeading at subheading, pang-format lang ng output. Ang importanteng rule dito, bawat active player may isang top card, highest card ang panalo, at kukunin niya lahat ng played cards.”

**IntelliJ action:** Stay in Main.java. Huwag mag-Ctrl + B sa centeredHeading(), subheading(), o println().

**Destination:** Same method: Main.welcome()

**What I should explain there:**

Centered yung welcome; left-aligned yung GAME RULES at rule text. `println()` prints text and moves to a new line. Yung empty println() ay blank line. Walang game state na binabago dito.

**Navigation after explanation:** Press Ctrl + Alt + Left once to return to the welcome() call inside main().

**Next step:** Gagawa ng Scanner sa try block, tapos Deck.loadCards().

### STEP 3: Keyboard input at pag-load ng file

**Current file:** `Main.java`

**Code to highlight:**

```java
        try (Scanner scanner = new Scanner(System.in)) {
            List<Card> cards = Deck.loadCards("resources/input.txt");
```

**What I should say (Natural Taglish):**

“So after ng welcome, gumagawa tayo ng Scanner para mabasa yung keyboard input. Then kino-call natin yung loadCards sa Deck. Ipinapasa natin yung resources/input.txt na file path.”

**IntelliJ action:** Press Ctrl + B on `loadCards` in `Deck.loadCards(...)`.

**Destination:** Deck.java → loadCards(String filename)

**What I should explain there:**

`Scanner scanner = new Scanner(System.in)` creates an object for keyboard input. Yung try-with-resources ang automatic na magsasara sa Scanner kapag natapos yung block. Yung file path ang argument; `filename` ang parameter sa loadCards. `List<Card>` ang returned type: ordered list ng Card objects.

**Navigation after explanation:** Stay in Deck.loadCards() for Steps 4–9. Huwag munang bumalik sa Main.

**Next step:** I-explain ang reader at token loop bago sundan yung Suit call.

### STEP 4: Basahin at hatiin ang input.txt

**Current file:** `Deck.java`

**Code to highlight:**

```java
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
```

**What I should say (Natural Taglish):**

“So dito naman sa loadCards, may list tayo para sa cards at Set para sa unique cards. Binubuksan natin yung file, tapos line by line yung basa. Sa loob ng bawat line, kinukuha natin yung card codes isa-isa.”

**IntelliJ action:** Stay in loadCards(). Open resources/input.txt briefly from the Project tree, then return to Deck.java. Huwag mag-Ctrl + B papunta sa Java library readers/tokenizer.

**Destination:** resources/input.txt for data inspection; then Deck.loadCards()

**What I should explain there:**

Actual file order: Diamond A→2, Heart A→2, Spade A→2, Club A→2. First token is `D-A`. Tokenizer delimiters are comma, space, tab, carriage return, and newline. `split("-", -1)` keeps empty parts, so malformed codes can be rejected. `parts.length != 2` throws an error. `ArrayList` preserves input order. `HashSet` checks duplicates. `readLine()` returns null at end-of-file; nested while loops mean lines outside, tokens inside. `throws IOException` lets file-reading errors reach Main. The reader also closes automatically.

**Navigation after explanation:** Return from input.txt using Ctrl + Alt + Left if you navigated there; remain in loadCards().

**Next step:** Suit.fromCode(parts[0]) runs before Rank.fromCode(parts[1]).

### STEP 5: Convert suit code to enum

**Current file:** `Deck.java`

**Code to highlight:**

```java
                    Suit suit = Suit.fromCode(parts[0]);
```

**What I should say (Natural Taglish):**

“So kung D-A yung token, D yung parts[0]. Kino-call natin yung Suit.fromCode para gawing actual enum value yung code. Puntahan natin yung implementation.”

**IntelliJ action:** Press Ctrl + B on `fromCode` in `Suit.fromCode(parts[0])`.

**Destination:** enums/Suit.java → fromCode(String code)

**What I should explain there:**

Show this exact method:

```java
    public static Suit fromCode(String code) {
        for (Suit suit : values()) {
            if (suit.code.equalsIgnoreCase(code)) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Invalid card suit: " + code);
    }
```

“So dito, iniikot niya lahat ng Suit values. Kapag matching yung code, ibabalik niya yung suit. D becomes DIAMOND. Case-insensitive, kaya puwede rin lowercase. Kapag walang match, IllegalArgumentException.”

Enum = fixed choices, not arbitrary strings. Declaration order is DIAMOND, HEART, SPADE, CLUB. On first active use, Java initializes these enum values through `Suit(String code, String symbol)`, saving each code and symbol. Mention that constructor at the declaration; no need to navigate each enum value.

**Navigation after explanation:** Press Ctrl + Alt + Left to return to Deck.loadCards().

**Next step:** Save returned Suit in suit; next is Rank.fromCode().

### STEP 6: Convert rank code to enum

**Current file:** `Deck.java`

**Code to highlight:**

```java
                    Rank rank = Rank.fromCode(parts[1]);
```

**What I should say (Natural Taglish):**

“After ng suit, rank naman. Sa D-A, A yung parts[1]. Kino-call natin yung Rank.fromCode para makuha yung ACE na enum value.”

**IntelliJ action:** Press Ctrl + B on `fromCode` in `Rank.fromCode(parts[1])`.

**Destination:** enums/Rank.java → fromCode(String code)

**What I should explain there:**

Exact method:

```java
    public static Rank fromCode(String code) {
        for (Rank rank : values()) {
            if (rank.code.equalsIgnoreCase(code)) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid card rank: " + code);
    }
```

“Same approach: loop sa values, compare ng code, return kapag may match, error kapag wala.” Rank values are ACE, KING, QUEEN, JACK, TEN down to TWO. On first active use, enum initialization calls `Rank(String code)` for each declared value. Naka-save yung code sa private final field. `final` prevents reassignment; enum order later supplies card priority.

**Navigation after explanation:** Press Ctrl + Alt + Left to return to Deck.loadCards().

**Next step:** Gagawa ng Card gamit ang valid suit at rank.

### STEP 7: Create one Card object

**Current file:** `Deck.java`

**Code to highlight:**

```java
                    Card card = new Card(suit, rank);
```

**What I should say (Natural Taglish):**

“Ngayon, may valid suit at rank na. Gagawa tayo ng Card object gamit yung dalawang values. Yung class ang definition; yung object ang actual card na ginawa natin.”

**IntelliJ action:** Press Ctrl + B on `Card` in `new Card(suit, rank)`.

**Destination:** Card.java → Card(Suit suit, Rank rank) constructor

**What I should explain there:**

Exact constructor:

```java
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
```

“Dito sine-save yung suit at rank sa object. Yung this.suit, field ng current Card; yung suit sa kanan, parameter na ipinasa natin.” `new` allocates an object and calls its constructor. Constructor has the class name and no return type. Fields are private and final, so this card's suit/rank references cannot be reassigned after construction.

**Navigation after explanation:** Press Ctrl + Alt + Left to return to Deck.loadCards().

**Next step:** Duplicate checking sa uniqueCards.add(card).

### STEP 8: Check duplicate cards and preserve file order

**Current file:** `Deck.java`

**Code to highlight:**

```java
                    if (!uniqueCards.add(card)) {
                        throw new IllegalArgumentException("Duplicate card found: " + card);
                    }

                    cards.add(card);
```

**What I should say (Natural Taglish):**

“So bago natin idagdag sa final list, chine-check muna natin kung bago yung card. Kapag false yung add sa Set, ibig sabihin may kapareho na. Kapag unique, saka natin ilalagay sa cards list.”

**IntelliJ action:** Stay in Deck.loadCards(). Optional Ctrl + B on Card.equals()/hashCode() only if the panel asks how duplicate detection works. Huwag sundan ang HashSet library internals.

**Destination:** Deck.loadCards(); optional Card.equals(Object) and hashCode()

**What I should explain there:**

`HashSet.add` uses Card.hashCode() and equality checks internally; walang explicit `card.equals(...)` call sa loadCards. `equals` first checks same reference, rejects non-Card objects, casts to Card, and compares suit/rank. `hashCode` combines suit/rank hashes; equal cards have equal hashes. A hash collision alone does not mean duplicate. On a duplicate, building the error string invokes Card.toString() implicitly to show a code like D-A. These methods may be inspected as an optional short detour; their invocation is library-driven.

**Navigation after explanation:** If you inspect Card methods, press Ctrl + Alt + Left until back at uniqueCards.add(card) in loadCards().

**Next step:** Repeat remaining tokens/lines, then validate the total.

### STEP 9: Require exactly 52 valid unique cards

**Current file:** `Deck.java`

**Code to highlight:**

```java
        if (cards.size() != 52) {
            throw new IllegalArgumentException("Invalid deck! Expected 52 cards, found " + cards.size());
        }

        return cards;
    }
```

**What I should say (Natural Taglish):**

“Once na-complete na yung file, chine-check natin kung exactly 52 cards. Since valid suit at rank lahat at walang duplicates, complete deck na iyon. Kapag valid, return cards na tayo.”

**IntelliJ action:** Stay in loadCards(); no navigation for List.size().

**Destination:** Deck.java → end of loadCards()

**What I should explain there:**

`return cards` gives the list reference to Main. The reader has already closed when execution leaves its try-with-resources block. Invalid count throws IllegalArgumentException. File contents are not shuffled during loading; original order is preserved.

**Navigation after explanation:** Press Ctrl + Alt + Left to return to the Deck.loadCards(...) call in Main.main().

**Next step:** The returned list is assigned to Main's cards variable, then the do-while starts.

### STEP 10: Start one game's setup

**Current file:** `Main.java`

**Code to highlight:**

```java
            do {
                Game game = createGame(cards, scanner);
```

**What I should say (Natural Taglish):**

“So may starting cards na tayo. Dito nagsisimula yung do-while. At least once itong tatakbo, kaya magse-setup muna tayo bago magtanong ng replay. Kino-call natin yung createGame.”

**IntelliJ action:** Press Ctrl + B on `createGame` in `createGame(cards, scanner)`.

**Destination:** Main.java → createGame(List<Card> cards, Scanner scanner)

**What I should explain there:**

`createGame` is private because Main uses it internally. It returns a Game object. `cards` and `scanner` are passed as reference values: the method can use the same objects, but Java still passes arguments by value. It displays GAME SETUP and requests two validated numbers.

**Navigation after explanation:** Stay in createGame() for Steps 11–13.

**Next step:** First readNumber call: player count.

### STEP 11: Validate the number of players

**Current file:** `Main.java`

**Code to highlight:**

```java
        int numberOfPlayers = readNumber(
                scanner, "ENTER NUMBER OF PLAYERS (2-8): ", 2, 8);
```

**What I should say (Natural Taglish):**

“So unang tanong, ilang players. Ipinapasa natin yung minimum na 2 at maximum na 8 sa readNumber. Siya yung bahala sa validation.”

**IntelliJ action:** Press Ctrl + B on `readNumber` in the player-count call.

**Destination:** Main.java → readNumber(Scanner scanner, String message, int minimum, int maximum)

**What I should explain there:**

Show this exact method:

```java
    private static int readNumber(Scanner scanner, String message, int minimum, int maximum) {
        while (true) {
            prompt(message);
            String input = scanner.nextLine();

            if (!input.matches("[0-9]+")) {
                System.out.println("Invalid input! Enter numbers only.");
                System.out.println("Spaces, letters, and symbols are not allowed.");
                System.out.println();
                continue;
            }

            try {
                int number = Integer.parseInt(input);

                if (number >= minimum && number <= maximum) {
                    return number;
                }
            } catch (NumberFormatException exception) {
                System.out.println("The number is too large.");
                System.out.println();
                continue;
            }

            System.out.println(
                    "Invalid number! Please enter a value from " + minimum + " to " + maximum + ".");
            System.out.println();
        }
    }
```

“Babasa muna ng text. Yung [0-9]+, digits lang ang accepted. Kapag may space, letter, sign, o decimal point, message tapos continue para magtanong ulit. Kapag digits lang, Integer.parseInt yung nagko-convert sa whole number. Kapag sobrang laki para sa int, NumberFormatException yung sasalo. Kapag number na pero outside 2–8, ibang error message. Kapag valid, return number.”

`continue` restarts the next loop iteration; `return` ends the method. `&&` requires both range conditions. No need to visit prompt() or Integer.parseInt library code.

**Navigation after explanation:** Press Ctrl + Alt + Left after explaining a valid return, back to createGame().

**Next step:** Assign numberOfPlayers, then execute the second readNumber call.

### STEP 12: Validate the shuffle count

**Current file:** `Main.java`

**Code to highlight:**

```java
        int numberOfShuffles = readNumber(
                scanner, "ENTER NUMBER OF PERFECT SHUFFLES (1-100): ",
                Game.MIN_SHUFFLES, Game.MAX_SHUFFLES);
```

**What I should say (Natural Taglish):**

“Same method ulit para sa shuffle count. Pero ngayon, minimum na 1 at maximum na 100 yung arguments. Kaya reusable yung validation method natin.”

**IntelliJ action:** Stay in createGame(). Huwag nang buksan ulit ang readNumber unless asked. Optional Ctrl + B on Game.MIN_SHUFFLES to show constants.

**Destination:** Game.java constants only if asked; otherwise Main.createGame()

**What I should explain there:**

`Game.MIN_SHUFFLES = 1` and `MAX_SHUFFLES = 100` are public static final constants. `static` means class-level; `final` prevents reassignment. These are bounds, not random shuffle counts.

**Navigation after explanation:** If you inspected constants, Ctrl + Alt + Left back to createGame().

**Next step:** Construct Game using the original cards and both validated numbers.

### STEP 13: Create the Game and validate its shuffle setting

**Current file:** `Main.java`

**Code to highlight:**

```java
        return new Game(cards, numberOfPlayers, numberOfShuffles);
```

**What I should say (Natural Taglish):**

“So kumpleto na yung settings. Dito, new Game yung ginagawa natin. Automatic na tatakbo yung constructor bago ibalik yung Game object sa main.”

**IntelliJ action:** Press Ctrl + B on `Game` in `new Game(...)`.

**Destination:** Game.java → Game(List<Card> originalCards, int numberOfPlayers, int numberOfShuffles)

**What I should explain there:**

Focus first on constructor validation and initialization:

```java
    public Game(List<Card> originalCards, int numberOfPlayers, int numberOfShuffles) {
        if (numberOfShuffles < MIN_SHUFFLES || numberOfShuffles > MAX_SHUFFLES) {
            throw new IllegalArgumentException(
                    "Number of shuffles must be between " + MIN_SHUFFLES + " and " + MAX_SHUFFLES + ".");
        }

        players = new ArrayList<>();
        mainDeck = new Deck(originalCards);
```

“Chine-check ulit yung shuffle range para protected din yung constructor. Gumagawa tayo ng players list at main deck.” `List<Player>` is an ordered collection; ArrayList is its concrete implementation. `players` and `mainDeck` are final references; their contents can still change. Player count is validated by Main, not by this constructor.

**Navigation after explanation:** Stay in Game constructor. Follow new Deck(originalCards) before the player loop.

**Next step:** Construct the main Deck.

### STEP 14: Main deck constructor and card copy

**Current file:** `Game.java`

**Code to highlight:**

```java
        mainDeck = new Deck(originalCards);
```

**What I should say (Natural Taglish):**

“Puntahan natin yung Deck constructor. Kailangan natin ng sariling pile ng cards sa game para hindi maubos yung original list na gagamitin ulit sa replay.”

**IntelliJ action:** Press Ctrl + B on `Deck` in `new Deck(originalCards)`.

**Destination:** Deck.java → Deck(List<Card> initialCards)

**What I should explain there:**

The field initializer is `private final Deque<Card> cards = new ArrayDeque<>();`. Highlight the List constructor, not the empty overload:

```java
    public Deck(List<Card> initialCards) {
        cards.addAll(initialCards);
    }
```

“The field initializer makes an ArrayDeque before the constructor body. addAll copies the list entries in order. New collection ito, pero same Card objects ang references; okay iyon dahil hindi pinapalitan yung suit/rank nila.” Deque supports both ends; here the front is top and the back is bottom.

**Navigation after explanation:** Press Ctrl + Alt + Left to return to Game constructor at mainDeck assignment.

**Next step:** Execute the loop that creates players.

### STEP 15: Create each Player

**Current file:** `Game.java`

**Code to highlight:**

```java
        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player(i));
        }
```

**What I should say (Natural Taglish):**

“So next, gagawa tayo ng players, numbered 1 hanggang sa number na pinili. Bawat ikot, new Player yung idinadagdag sa list.”

**IntelliJ action:** Press Ctrl + B on `Player` in `new Player(i)`.

**Destination:** Player.java → Player(int playerNumber), also show fields above it

**What I should explain there:**

Exact fields and constructor:

```java
    private final int playerNumber;
    private final Deck deck = new Deck();
    private boolean eliminated;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }
```

“Bawat Player may number, sariling Deck, at eliminated flag. Yung boolean field, false na by default. Yung this.playerNumber, sine-save yung number na ipinasa.” Important order: `private final Deck deck = new Deck();` initializes the personal deck before the Player constructor body saves playerNumber.

**Navigation after explanation:** Before returning to Game, follow the field initializer's new Deck() in Step 16.

**Next step:** Construct this player's empty Deck.

### STEP 16: Player's empty Deck is initialized before its constructor body finishes

**Current file:** `Player.java`

**Code to highlight:**

```java
    private final Deck deck = new Deck();
```

**What I should say (Natural Taglish):**

“So itong sariling Deck ng player, empty muna. Gagawin ito bago matapos yung Player constructor. Lalagyan natin siya ng cards sa dealing step mamaya.”

**IntelliJ action:** Press Ctrl + B on `Deck` in the field initializer `new Deck()` above the Player constructor.

**Destination:** Deck.java → Deck()

**What I should explain there:**

Exact empty constructor:

```java
    public Deck() {
    }
```

“Empty yung body, pero tumakbo na yung field initializer na new ArrayDeque. Kaya may valid na empty card queue yung bagong Deck.” No need to enter ArrayDeque's Java library constructor.

**Navigation after explanation:** Ctrl + Alt + Left back to Player's field initializer; show this.playerNumber assignment, then Ctrl + Alt + Left back to new Player(i) in Game constructor.

**Next step:** Repeat for all players, then prepareDeck(numberOfShuffles).

### STEP 17: Prepare the deck: repeat perfect shuffles

**Current file:** `Game.java`

**Code to highlight:**

```java
        prepareDeck(numberOfShuffles);
```

**What I should say (Natural Taglish):**

“So kumpleto na yung players. Next, kino-call yung prepareDeck. Ipapakita niya kung ilang perfect shuffles ang gagawin, tapos tatawagin yung shuffle method sa Deck.”

**IntelliJ action:** Press Ctrl + B on `prepareDeck` in the constructor.

**Destination:** Game.java → prepareDeck(int numberOfShuffles)

**What I should explain there:**

Exact implementation:

```java
        prepareDeck(numberOfShuffles);
        distributeCards();
    }

    private void prepareDeck(int numberOfShuffles) {
```

“The for loop calls mainDeck.perfectShuffle() exactly numberOfShuffles times. Sa demo natin, dalawang calls iyon. Pagkatapos lang ng lahat ng shuffles ipapakita yung final shuffled deck, hindi after each shuffle.” Do not visit centered/display labels.

**Navigation after explanation:** Stay in prepareDeck and follow perfectShuffle().

**Next step:** Deck.perfectShuffle() performs one deterministic interleave.

### STEP 18: Perfect shuffle implementation

**Current file:** `Game.java`

**Code to highlight:**

```java
            mainDeck.perfectShuffle();
```

**What I should say (Natural Taglish):**

“So puntahan natin yung actual shuffle. Hindi random yung ginagawa nito. Hinahati niya yung current deck sa dalawa, tapos salit-salitang kinukuha yung cards mula sa parehong halves.”

**IntelliJ action:** Press Ctrl + B on `perfectShuffle` in mainDeck.perfectShuffle().

**Destination:** Deck.java → perfectShuffle()

**What I should explain there:**

Exact method:

```java
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
```

“First, getCards() para may list copy. Kapag odd yung size, hindi puwedeng hatiin nang pantay kaya error. Sa 52, half is 26. Nililinis yung queue, tapos first-half card, second-half card, paulit-ulit.” The original list copy survives cards.clear(). `getCards()` returns `new ArrayList<>(cards)`; getter is small, so explain inline without navigating. Example `[A,B,C,D] → [A,C,B,D]` is illustrative, not a separate rule.

**Navigation after explanation:** Stay in perfectShuffle for addBottom() in Step 19.

**Next step:** First addBottom(original.get(i)), then addBottom(original.get(i + half)).

### STEP 19: Nested shuffle call: addBottom()

**Current file:** `Deck.java`

**Code to highlight:**

```java
            addBottom(original.get(i));
            addBottom(original.get(i + half));
```

**What I should say (Natural Taglish):**

“So dalawang addBottom calls bawat ikot. Una galing first half, kasunod galing second half. Ito yung bumubuo ng bagong shuffled order.”

**IntelliJ action:** Press Ctrl + B on the first `addBottom` call.

**Destination:** Deck.java → addBottom(Card card)

**What I should explain there:**

Exact method:

```java
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
```

“addLast yung gamit, kaya sa likod ng queue napupunta yung card. Same helper din ito yung gagamitin sa dealing at pag-award ng won cards.” `void` means it changes the queue but does not return a value. No need to visit ArrayDeque.addLast.

**Navigation after explanation:** Ctrl + Alt + Left back to perfectShuffle(). Explain the second call and loop; then Ctrl + Alt + Left back to prepareDeck().

**Next step:** Repeat shuffle if needed; after the last iteration, display the shuffled deck.

### STEP 20: Display the shuffled deck; evaluate getCards() before displayCards()

**Current file:** `Game.java`

**Code to highlight:**

```java
        Main.subheading("SHUFFLED DECK:");
        Deck.displayCards(mainDeck.getCards());
    }
```

**What I should say (Natural Taglish):**

“After ng shuffles, dito ipinapakita yung deck. Sa Deck.displayCards(mainDeck.getCards()), mauuna munang makuha yung list copy, saka ipapasa sa displayCards.”

**IntelliJ action:** Skip subheading(). Press Ctrl + B on `displayCards` after briefly explaining getCards().

**Destination:** Deck.java → displayCards(List<Card> cards)

**What I should explain there:**

Exact method:

```java
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
```

“Kung empty, may message. Otherwise, iniikot yung list at pini-print yung colored text ng bawat card. Kada walong cards, newline. Kung incomplete yung last row, tatapusin din ng newline.” `%s` is string output, `%-5s` sets a minimum left-aligned width, and two literal spaces separate cards. printf has no automatic newline; println supplies it. The field width counts ANSI escape characters too, so it is not a true visible-width layout guarantee.

**Navigation after explanation:** Stay at displayCards() for its toColoredString() call.

**Next step:** Follow card text generation once; reuse that explanation in later rounds.

### STEP 21: Nested card formatting and enum getters

**Current file:** `Deck.java`

**Code to highlight:**

```java
            System.out.printf("%-5s  ", card.toColoredString());
```

**What I should say (Natural Taglish):**

“So para sa isang card, kino-call yung toColoredString. Dito nanggagaling yung suit symbol at code na nakikita natin sa console.”

**IntelliJ action:** Press Ctrl + B on `toColoredString` in the printf argument.

**Destination:** Card.java → toColoredString()

**What I should explain there:**

Exact methods:

```java
    public String toColoredString() {
        String color = suit.isRed() ? RED : WHITE;
        return color + suit.getSymbol() + " " + toString() + RESET;
    }

    @Override
    public String toString() {
        return suit.getCode() + "-" + rank.getCode();
    }
```

“isRed() muna para pumili ng RED o WHITE. Yung ternary na question mark at colon, RED kapag true, otherwise WHITE. Next, suit symbol, space, tapos toString() para sa plain code. Sa toString, suit.getCode() then rank.getCode().” Getters are tiny: Suit.isRed is Diamond or Heart, getSymbol returns symbol, both getCode methods return code. Explain them without opening each one. Optional Ctrl + B on toString() here, then Ctrl + Alt + Left back to toColoredString(). RESET restores console color. Plain H-A remains readable when Unicode suit support differs.

**Navigation after explanation:** Ctrl + Alt + Left back to Deck.displayCards(); after finishing the list, Ctrl + Alt + Left back to prepareDeck(); then Ctrl + Alt + Left back to the Game constructor.

**Next step:** Constructor's next call is distributeCards().

### STEP 22: Deal cards to players in rotation

**Current file:** `Game.java`

**Code to highlight:**

```java
        distributeCards();
```

**What I should say (Natural Taglish):**

“So tapos na yung shuffle. Ngayon, distributeCards naman. One card at a time, paikot sa players, hanggang maubos yung main deck.”

**IntelliJ action:** Press Ctrl + B on `distributeCards` in the constructor.

**Destination:** Game.java → distributeCards()

**What I should explain there:**

Show the logic:

```java
        int currentPlayer = 0;

        while (!mainDeck.isEmpty()) {
            Player player = players.get(currentPlayer);
            Card card = mainDeck.removeTop();
            player.getDeck().addBottom(card);

            currentPlayer = (currentPlayer + 1) % players.size();
        }
```

“Zero yung first list index, pero Player 1 yung number. isEmpty() ang check kung may cards pa. Kinukuha yung Player, removeTop sa main Deck, tapos addBottom sa personal Deck. Yung percent operator, modulo, ang nagpapabalik sa zero pagkatapos ng last index.” Tiny isEmpty() delegates to cards.isEmpty(); no navigation needed. In the 8-player demo: players 1–4 receive 7 cards, players 5–8 receive 6. All 52 are dealt, not 52/8 rounded equally.

**Navigation after explanation:** Stay in distributeCards for the nested removal call.

**Next step:** Visit removeTop(), then explain the player's deck chain.

### STEP 23: Nested dealing calls: removeTop(), getDeck(), addBottom()

**Current file:** `Game.java`

**Code to highlight:**

```java
            Player player = players.get(currentPlayer);
            Card card = mainDeck.removeTop();
            player.getDeck().addBottom(card);
```

**What I should say (Natural Taglish):**

“So yung removeTop ang actual na nag-aalis sa unahan. Tapos sa player.getDeck().addBottom(card), kukunin muna yung Deck object ng player, saka doon tatawag ng addBottom.”

**IntelliJ action:** Press Ctrl + B on `removeTop`; skip the tiny getDeck getter unless asked. addBottom was already explained, so don't revisit it.

**Destination:** Deck.java → removeTop()

**What I should explain there:**

Exact helpers:

```java
    public Card removeTop() {
        return cards.removeFirst();
    }
```

In Player.java:

```java
    public Deck getDeck() {
        return deck;
    }
```

“removeFirst removes and returns the top Card; getDeck returns this player's actual Deck, not a copy. addBottom uses addLast, so cards retain dealing order.” Because top removal happens before addBottom, the source main deck loses the card before the player receives it. No new Card objects are created during dealing.

**Navigation after explanation:** Ctrl + Alt + Left back to distributeCards(). Finish its loop and player-count output; Ctrl + Alt + Left back to Game constructor; Ctrl + Alt + Left back to Main.createGame(); Ctrl + Alt + Left back to Main.main().

**Next step:** Assign the fully initialized Game to game, then call game.start().

### STEP 24: Start the battle and champion-check loop

**Current file:** `Main.java`

**Code to highlight:**

```java
                game.start();
```

**What I should say (Natural Taglish):**

“So ready na yung game, dahil tapos na yung constructor at dealing. Dito naman tatawagin yung start para sa actual rounds.”

**IntelliJ action:** Press Ctrl + B on `start` in game.start().

**Destination:** Game.java → start()

**What I should explain there:**

Exact method:

```java
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
```

“Battle announcement muna. Round number starts at 1. Bawat ikot, champion check muna. Kapag meron, finalResult tapos return. Kapag wala, playRound tapos roundNumber++.” `long` is the round counter type. `while(true)` repeats until this method returns; no built-in round cap. Do not navigate to the display helper.

**Navigation after explanation:** Stay in start(); follow findChampion() before playRound().

**Next step:** findChampion() runs even before Round 1.

### STEP 25: Champion check before each round

**Current file:** `Game.java`

**Code to highlight:**

```java
            Player champion = findChampion();
```

**What I should say (Natural Taglish):**

“So bago tayo mag-draw, titingnan muna kung may player nang may 52 cards. Sa umpisa, wala pa, kaya tuloy tayo sa round.”

**IntelliJ action:** Press Ctrl + B on `findChampion`.

**Destination:** Game.java → findChampion()

**What I should explain there:**

Exact method:

```java
    private Player findChampion() {
        for (Player player : players) {
            if (player.getDeck().size() == 52) {
                return player;
            }
        }

        return null;
    }
```

“Each player, kuha ng Deck, kuha ng size. Kapag exactly 52, return player. Kapag walang match sa buong loop, return null.” `Player.getDeck()` and `Deck.size()` are simple accessors; explain without navigation. Null means no champion object found, not an eliminated player.

**Navigation after explanation:** Press Ctrl + Alt + Left to return to start(). For the first round, champion != null is false.

**Next step:** Call playRound(roundNumber).

### STEP 26: Round controller: show the steps in order

**Current file:** `Game.java`

**Code to highlight:**

```java
            playRound(roundNumber);
            roundNumber++;
        }
    }

    private Player findChampion() {
        for (Player player : players) {
```

**What I should say (Natural Taglish):**

“So ito yung isang round. Sunod-sunod lang yung calls: heading, draw, reveal, compare, collect, display ng result, tapos check ng eliminations.”

**IntelliJ action:** From start(), press Ctrl + B on `playRound`. Skip navigation into startRound(), which only calls Main.heading().

**Destination:** Game.java → playRound(long roundNumber)

**What I should explain there:**

`startRound(roundNumber)` prints START ROUND with a divider. It is a formatting helper, not a game rule. Each remaining call has actual game logic, so visit them in sequence. Don't jump to finalResult yet. `playedCards` is the current round's player-card information; `collected` is the ordered list awarded to the winner.

**Navigation after explanation:** Stay in playRound(); after every nested visit, return here before the next call.

**Next step:** drawCards() executes first.

### STEP 27: Draw top cards from active players

**Current file:** `Game.java`

**Code to highlight:**

```java
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
```

**What I should say (Natural Taglish):**

“So dito naman sa drawCards, iniikot natin yung list of players. Chine-check muna kung active pa. Kapag may cards, kukuha ng isang top card sa Deck niya. Then ise-save yung player together with yung card na nilabas niya.”

**IntelliJ action:** Press Ctrl + B on `drawCards` from playRound(). Before explaining removeTop, follow Player.isActive in Step 28.

**Destination:** Game.java → drawCards()

**What I should explain there:**

`Map<Player, Card>` pairs a player key with the card value played this round. `new LinkedHashMap<>()` preserves insertion order, so later output follows player order. `continue` skips empty players. All active players draw before any winner is selected or cards are awarded. `playedCards.put(player, card)` stores the reference; it doesn't clone or return cards to a deck.

**Navigation after explanation:** Stay in drawCards for isActive().

**Next step:** Check a player's current deck before drawing.

### STEP 28: Nested active check, then return to drawCards

**Current file:** `Game.java`

**Code to highlight:**

```java
            if (!player.isActive()) {
                continue;
            }
```

**What I should say (Natural Taglish):**

“So ang active dito, may cards pa. Hindi lang natin binabase sa eliminated flag; mismong laman ng deck yung chine-check.”

**IntelliJ action:** Press Ctrl + B on `isActive` in player.isActive().

**Destination:** Player.java → isActive()

**What I should explain there:**

Exact method:

```java
    public boolean isActive() {
        return !deck.isEmpty();
    }
```

“deck.isEmpty() returns true kapag walang cards. Yung exclamation mark, binabaligtad iyon. So true ang isActive kapag may cards.” Deck.isEmpty is a one-line helper; no need to open it. After returning, a valid player goes through getDeck().removeTop(), already explained during dealing.

**Navigation after explanation:** Ctrl + Alt + Left back to drawCards(). Explain the loop and return playedCards; then Ctrl + Alt + Left back to playRound().

**Next step:** Assign the returned Map, then displayPlayedCards(playedCards).

### STEP 29: Reveal every player's played card before selecting a winner

**Current file:** `Game.java`

**Code to highlight:**

```java
        displayPlayedCards(playedCards);
        Player winner = findRoundWinner(playedCards);
        List<Card> collected = collectCards(winner, playedCards);

        roundWinner(winner, roundNumber, collected);

        checkEliminations();
    }

    public static void startRound(long round) {
```

**What I should say (Natural Taglish):**

“So ngayon, may cards na tayo sa Map. Ipinapakita muna natin yung nilapag ng bawat active player para makita ng user kung bakit may nanalo. Kasama dito yung magiging winner.”

**IntelliJ action:** Press Ctrl + B on `displayPlayedCards` from playRound().

**Destination:** Game.java → displayPlayedCards(Map<Player, Card> playedCards)

**What I should explain there:**

`keySet()` supplies the players in LinkedHashMap insertion order. `get(player)` retrieves that player's card. The output is exactly `Player N plays: ...`. `getPlayerNumber()` is a getter, and toColoredString() was covered at the shuffled-deck display; skip repeat navigation. For the demo's first round: P1 D-A, P2 H-A, P3 S-A, P4 C-A, P5 D-K, P6 H-K, P7 S-K, P8 C-K.

**Navigation after explanation:** Press Ctrl + Alt + Left back to playRound().

**Next step:** findRoundWinner(playedCards) selects the winner.

### STEP 30: Find the highest played card

**Current file:** `Game.java`

**Code to highlight:**

```java
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
```

**What I should say (Natural Taglish):**

“So dito naman, hahanapin natin yung highest card. Wala muna tayong winner. Yung first card ang magiging initial highest, tapos iko-compare yung mga kasunod.”

**IntelliJ action:** Press Ctrl + B on `findRoundWinner` in playRound(), then follow compareTo in Step 31.

**Destination:** Game.java → findRoundWinner(Map<Player, Card> playedCards)

**What I should explain there:**

`winner` and `highestCard` start as null. The first iteration succeeds through `highestCard == null`; because `||` short-circuits, compareTo is not called against null. Later cards call compareTo(highestCard). Positive result replaces both winner and highestCard. It returns a Player, not the winning Card. With a validated unique deck, identical rank-and-suit cards cannot appear as separate draws.

**Navigation after explanation:** Stay in findRoundWinner(); inspect compareTo next.

**Next step:** Card.compareTo defines the rank and suit rule.

### STEP 31: Card comparison: rank first, suit second

**Current file:** `Game.java`

**Code to highlight:**

```java
            if (highestCard == null || playedCard.compareTo(highestCard) > 0) {
                winner = player;
                highestCard = playedCard;
            }
```

**What I should say (Natural Taglish):**

“Dito naman papasok yung Card class. Siya yung may rule ng comparison. Kaya ang Game, kino-call na lang yung compareTo.”

**IntelliJ action:** Press Ctrl + B on `compareTo` in playedCard.compareTo(highestCard).

**Destination:** Card.java → compareTo(Card otherCard)

**What I should explain there:**

Exact implementation:

```java
    public int compareTo(Card otherCard) {
        int rankComparison = Integer.compare(otherCard.rank.ordinal(), this.rank.ordinal());

        if (rankComparison != 0) {
            return rankComparison;
        }

        return Integer.compare(otherCard.suit.ordinal(), this.suit.ordinal());
    }
```

“Rank muna. Dahil ACE yung first value sa Rank enum, ordinal 0 siya, then KING 1, hanggang TWO 12. Reversed yung Integer.compare arguments para positive kapag mas mataas yung current card. Kapag hindi equal ang ranks, return agad. Kapag equal, suit naman: DIAMOND 0, HEART 1, SPADE 2, CLUB 3.”

Example: D-A vs H-A → Integer.compare(other suit 1, current suit 0) = positive, so D-A wins. Positive = current card higher, negative = lower, zero = equal. `implements Comparable<Card>` supplies this comparison contract; `@Override` marks its implementation. This project uses suit tie-breaking, not a face-down 'war' subround. Enums were already visited; no need to navigate again.

**Navigation after explanation:** Ctrl + Alt + Left back to findRoundWinner(); show winner/highestCard assignment and return. Then Ctrl + Alt + Left back to playRound().

**Next step:** collectCards(winner, playedCards) awards the round.

### STEP 32: Collect in winner-first circular order, then append to bottom

**Current file:** `Game.java`

**Code to highlight:**

```java
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
```

**What I should say (Natural Taglish):**

“So may winner na tayo. Dito naman, kinokolekta yung cards sa tamang order: sariling card muna ng winner, tapos paikot sa player numbers.”

**IntelliJ action:** Press Ctrl + B on `collectCards` from playRound(). Skip reopening addBottom(), since we already visited its implementation.

**Destination:** Game.java → collectCards(Player winner, Map<Player, Card> playedCards)

**What I should explain there:**

`winnerIndex = winner number - 1` converts from a 1-based number to a 0-based list index. For each offset, modulo wraps the player index. With four players and winner P2: P2, P3, P4, P1. With inactive players, missing Map entries are null and skipped. First it builds collected, then loops through that list and calls winner.getDeck().addBottom(card).

“Yung natirang cards ng winner, hindi gagalawin. Idadagdag sa likod yung sariling winning card, kasunod yung cards ng ibang players.” Example: remaining `[C-5,S-7]`, winner's played D-A, opponent H-2 → `[C-5,S-7,D-A,H-2]`. All 52 cards are conserved across decks and temporary played cards. It returns collected for display. No ranks are sorted here; order is circular player order.

**Navigation after explanation:** Ctrl + Alt + Left back to playRound() after the return.

**Next step:** roundWinner(winner, roundNumber, collected) displays the result.

### STEP 33: Round result and collected card list

**Current file:** `Game.java`

**Code to highlight:**

```java
        roundWinner(winner, roundNumber, collected);

        checkEliminations();
    }

    public static void startRound(long round) {
```

**What I should say (Natural Taglish):**

“So itong roundWinner method, display na ito ng result na napili natin. Hindi na siya yung nagko-compare. Ipinapakita niya kung sino yung winner at anong cards yung nakuha.”

**IntelliJ action:** Press Ctrl + B on `roundWinner` from playRound(). Skip Main.successMessage() and Main.subheading().

**Destination:** Game.java → roundWinner(Player winner, long round, List<Card> collected)

**What I should explain there:**

`StringJoiner(", ")` builds a comma-separated line. For each collected card, it adds toColoredString(); reuse the earlier formatting explanation. println(collectedCards) prints the joiner's text. Important: `Player winner` is the result object; `roundWinner(...)` is a method name. This method does not change collection order or add cards itself—the awarding already happened in collectCards.

**Navigation after explanation:** Stay in roundWinner() and follow winner.displayDeck().

**Next step:** Player.displayDeck shows the full updated winner deck.

### STEP 34: Updated winner deck: inspect the actual bottom order

**Current file:** `Game.java`

**Code to highlight:**

```java
        winner.displayDeck("UPDATED DECK");
```

**What I should say (Natural Taglish):**

“So para ma-check kung nailagay sa bottom yung cards, kino-call natin yung displayDeck sa winner na Player. Ipinapasa yung label na UPDATED DECK.”

**IntelliJ action:** Press Ctrl + B on `displayDeck`.

**Destination:** Player.java → displayDeck(String label)

**What I should explain there:**

Exact method:

```java
    public void displayDeck(String label) {
        System.out.println();
        Main.subheading("PLAYER " + playerNumber + "'S " + label);
        System.out.println("Total Cards: " + deck.size());
        Deck.displayCards(deck.getCards());
    }
```

“Title at Total Cards muna, tapos list ng cards. Sa Deck.displayCards(deck.getCards()), getCards muna para sa snapshot ng queue order. Pagkatapos, displayCards na.” `Deck.getCards()` copies in front-to-back order, so displayed first card is the next top card; last card is the bottom. It doesn't shuffle or sort. Tiny size/getCards getters need no navigation. Optional Ctrl + B into Deck.displayCards if the panel asks; its row-of-eight logic was shown during shuffle display.

**Navigation after explanation:** If you visited Deck.displayCards, Ctrl + Alt + Left to Player.displayDeck. Then Ctrl + Alt + Left to Game.roundWinner(), and again to Game.playRound().

**Next step:** checkEliminations() runs after result display and collection.

### STEP 35: Elimination happens after cards have been awarded

**Current file:** `Game.java`

**Code to highlight:**

```java
        checkEliminations();
    }

    public static void startRound(long round) {
```

**What I should say (Natural Taglish):**

“After ng result, saka natin chine-check yung eliminated. Kapag wala nang cards at hindi pa marked, doon lang tayo mag-a-announce.”

**IntelliJ action:** Press Ctrl + B on `checkEliminations` in playRound(). Follow Player.eliminate only when explaining a true elimination condition.

**Destination:** Game.java → checkEliminations()

**What I should explain there:**

`!player.isActive()` means empty personal deck. `!player.isEliminated()` prevents repeated announcements. Both must be true. It prints a blank line before `Player N has been eliminated.` The Player object remains in the players list; future rounds skip it because its deck is empty. Crucial: drawing your last card is not enough to be eliminated; you may win the round and regain cards before this check.

**Navigation after explanation:** Before returning, inspect eliminate() in Step 36 when the condition is true. In early demo rounds without eliminations, explain that the branch is simply skipped.

**Next step:** The flag is updated only for a newly eliminated player.

### STEP 36: Nested elimination flag update; complete the round

**Current file:** `Game.java`

**Code to highlight:**

```java
                player.eliminate();
                System.out.println();
```

**What I should say (Natural Taglish):**

“So pag empty na talaga after collection, tatawagin yung eliminate sa Player. Flag lang yung binabago nito para hindi paulit-ulit yung announcement.”

**IntelliJ action:** Press Ctrl + B on `eliminate` in player.eliminate(). For rounds with no elimination, describe the conditional branch without pretending it executes.

**Destination:** Player.java → eliminate()

**What I should explain there:**

Exact method:

```java
    public void eliminate() {
        eliminated = true;
    }
```

“eliminated becomes true. Yung isEliminated getter, ibabalik lang yung flag.” Explain these tiny methods in one visit. There is no player removal or card destruction. In the verified demo, P5–P8 become eliminated after round 6; P2–P4 after round 7.

**Navigation after explanation:** Ctrl + Alt + Left back to checkEliminations(). Finish its loop, Ctrl + Alt + Left to playRound(), then Ctrl + Alt + Left to start().

**Next step:** start increments roundNumber and repeats findChampion().

### STEP 37: Repeat rounds, then enter the final-result branch

**Current file:** `Game.java`

**Code to highlight:**

```java
            if (champion != null) {
                finalResult(champion, roundNumber - 1, players);
                return;
            }

            playRound(roundNumber);
            roundNumber++;
        }
    }
```

**What I should say (Natural Taglish):**

“So tapos na yung round. roundNumber++ muna, then balik sa champion check. Hindi tayo babalik sa Main pagkatapos ng bawat round; nasa start loop pa rin tayo.”

**IntelliJ action:** Stay in Game.start(). Don't re-open methods for every round. When findChampion returns a Player, press Ctrl + B on finalResult().

**Destination:** Game.java → finalResult(Player champion, long totalRounds, List<Player> players)

**What I should explain there:**

Once a player owns 52 cards, the branch becomes true. `roundNumber - 1` counts completed rounds, since the counter was incremented after the last round. For the tested 8/2 demo, rounds 1–7 finish and the counter is 8 at champion check; finalResult receives 7. finalResult starts by calling displayPlayerStatus—visit it before explaining congratulations.

**Navigation after explanation:** Stay in finalResult() for the next nested call.

**Next step:** displayPlayerStatus(players) executes first.

### STEP 38: Final player statuses before the congratulations

**Current file:** `Game.java`

**Code to highlight:**

```java
        displayPlayerStatus(players);
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

    public static void displayPlayerStatus(List<Player> players) {
```

**What I should say (Natural Taglish):**

“So bago i-declare yung final result, ipinapakita muna yung status ng bawat player. Dito makikita yung card count at kung active o eliminated.”

**IntelliJ action:** From finalResult(), press Ctrl + B on `displayPlayerStatus`. Skip the getters and heading().

**Destination:** Game.java → displayPlayerStatus(List<Player> players)

**What I should explain there:**

The method starts with PLAYER STATUS heading, loops players in list order, starts status as ACTIVE, replaces it with ELIMINATED if the flag is true, then uses printf. `%d` prints integers; `%-2d` and `%-12s` set minimum left-aligned widths; `%n` adds a platform newline. This display method does not decide who won. In the tested finished game, P1 has 52, all others 0.

**Navigation after explanation:** Press Ctrl + Alt + Left to return to finalResult().

**Next step:** Print GAME RESULT, winner announcement, congratulations, total rounds, and final deck.

### STEP 39: Final declaration, final deck, and return to Main

**Current file:** `Game.java`

**Code to highlight:**

```java
                finalResult(champion, roundNumber - 1, players);
                return;
            }

            playRound(roundNumber);
            roundNumber++;
        }
    }

    private Player findChampion() {
        for (Player player : players) {
            if (player.getDeck().size() == 52) {
```

**What I should say (Natural Taglish):**

“So dito na yung congratulations sa champion. Yung Total Rounds Played, bilang ng completed rounds. Then displayDeck ulit, pero FINAL DECK na yung label. Pagkatapos, may thank-you message.”

**IntelliJ action:** Stay in finalResult(). Do not revisit displayDeck() or centeredSuccessMessage(), since they were already explained.

**Destination:** Game.finalResult(), then return through Game.start()

**What I should explain there:**

Actual order: statuses → GAME RESULT → PLAYER N WINS → one-line congratulations → blank line → total rounds → Player.displayDeck("FINAL DECK") → blank line → thank-you. There is no Reason line. The Player deck display still delegates to Deck.displayCards and Card.toColoredString. `centeredSuccessMessage` adds spaces only if the message fits the 52-character width; the long congratulations can exceed it and receives no negative padding.

**Navigation after explanation:** Ctrl + Alt + Left back to finalResult(...) in start(); show the next return statement. Ctrl + Alt + Left back to game.start() in Main.main().

**Next step:** Main.playAgainHeading() executes next; the replay decision has not happened yet.

### STEP 40: Replay heading and YES/NO decision

**Current file:** `Main.java`

**Code to highlight:**

```java
                playAgainHeading();
            } while (askPlayAgain(scanner));
```

**What I should say (Natural Taglish):**

“So balik tayo sa Main. Ipapakita yung replay heading, tapos tatawagin yung askPlayAgain sa condition ng do-while.”

**IntelliJ action:** Skip playAgainHeading(): it only calls heading("START A NEW GAME?"). Press Ctrl + B on `askPlayAgain` in the while condition.

**Destination:** Main.java → askPlayAgain(Scanner scanner)

**What I should explain there:**

Exact method:

```java
    private static boolean askPlayAgain(Scanner scanner) {
        while (true) {
            prompt("Do you want to play again? (YES/NO): ");

            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("NO")) {
                return answer.equalsIgnoreCase("YES");
            }

            System.out.println("Invalid input! Please enter YES or NO.");
        }
    }
```

“Babasa ng answer. Kapag YES o NO, valid na. Ibinabalik niya yung answer.equalsIgnoreCase("YES"), kaya true sa YES at false sa NO. Kapag ibang sagot, loop ulit.” It is case-insensitive but not trimmed: `yes` works; ` YES ` is invalid. `||` means either allowed answer. The Scanner is the same one created at program start.

**Navigation after explanation:** Ctrl + Alt + Left back to Main.main() at the do-while condition.

**Next step:** YES jumps back to createGame; NO exits the loop and calls goodbye().

### STEP 41: Replay branch or goodbye, then resource cleanup

**Current file:** `Main.java`

**Code to highlight:**

```java
            goodbye();
```

**What I should say (Natural Taglish):**

“So kapag YES, babalik sa setup at gagawa ng bagong Game. Original cards ulit yung gamit, pero bagong Deck at Player objects. Kapag NO, goodbye na.”

**IntelliJ action:** On the NO path, press Ctrl + B on `goodbye`. Do not visit the formatting helper.

**Destination:** Main.java → goodbye()

**What I should explain there:**

Exact method:

```java
            goodbye();
        } catch (IOException | IllegalArgumentException exception) {
```

“Centered yung THANK YOU FOR PLAYING at SEE YOU IN THE NEXT CARD BATTLE. Wala nang game logic dito.” On YES, loadCards does not run again: the original list was loaded before the do-while. The file is not reread if changed between games.

**Navigation after explanation:** Ctrl + Alt + Left back to goodbye() call in main().

**Next step:** Exit try-with-resources, close Scanner/System.in, skip catch on a normal run, then main returns and the program ends.

## Error branches: explain when the condition occurs

These are alternate paths, not extra steps that execute after every successful game.

### Invalid or unreadable input.txt

**Current file:** Main.java → catch block.

**Code to highlight:**

```java
        } catch (IOException | IllegalArgumentException exception) {
            System.out.println("ERROR: Unable to load resources/input.txt.");
            System.out.println(exception.getMessage());
        }
```

**What to say:**

“Kapag missing o unreadable yung file, IOException. Kapag malformed, unknown suit/rank, duplicate, o hindi 52 yung cards, IllegalArgumentException. Babalik yung error sa Main at ipapakita yung message.”

**Navigation:** Stay here; optionally Ctrl + B to Deck.loadCards() to point to the specific validation already explained, then Navigate Back.

**What happens next:** The loop is never reached if initial loading fails. The Scanner closes; the catch prints the messages, then main ends. goodbye() is inside the try, so it doesn't run on this failure path. The catch surrounds the whole try block, so its “Unable to load” wording is broader than the single load operation.

### Invalid player count or shuffle count

**Current file:** Main.readNumber().

**What to say:**

“Kapag invalid yung number, hindi nagse-start yung Game. Nasa loop lang muna tayo hanggang valid yung input.”

- `abc`, `2.5`, `-2`, ` 2 `, or an empty line: fails the digits-only pattern.
- `99999999999999999`: digits, but parseInt throws NumberFormatException.
- `1` for player count or `101` for shuffle count: valid integer but outside the bounds.
- `02`: accepted as integer 2, because leading zeroes are not prohibited.
- No trim() is used; spaces are invalid.

**Navigation:** No extra file; explain readNumber's branches and return to createGame when a valid number is returned.

### Invalid replay answer

**Current file:** Main.askPlayAgain().

“Kapag MAYBE yung answer, may error message tapos same replay question ulit. Lowercase yes or no works, pero leading/trailing spaces aren't removed.”

## Live console demo: 8 players and 2 perfect shuffles

### What was verified

I compiled the current Java files into a temporary directory and ran the real application with inputs `8`, `2`, and `NO`. The source and input file were left unchanged.

**Observed with the current input.txt:**

| Item | Verified result |
| --- | --- |
| Starting deck | 52 validated unique cards |
| Perfect shuffles | 2 |
| Dealing counts | Players 1–4: 7 cards each; Players 5–8: 6 each |
| Round winners | Player 1 in rounds 1 through 7 |
| Eliminations | Players 5–8 after round 6; Players 2–4 after round 7 |
| Final champion | Player 1, with 52 cards |
| Completed rounds | 7 |
| Replay | Asked after the final result; NO prints both closing messages |

This is the verified result for this exact input order and these settings, not a promise that Player 1 always wins for every possible deck/settings. The program is deterministic: same starting cards and settings give the same sequence. It does not guarantee different round winners.

### Before pressing Run

“Ngayon, i-run natin yung game para makita kung tugma yung output sa code na sinundan natin. Gagamit tayo ng 8 players at 2 perfect shuffles.”

Run `cardgame.Main` in IntelliJ with the project folder as the working directory, so `resources/input.txt` resolves correctly.

### At welcome and input

“Welcome at rules muna, katulad ng first call sa Main. Next, 8 yung players at 2 yung shuffles. Hindi pa nagsisimula yung laban habang kinukuha yung settings.”

Type:

```text
8
2
```

Interactive console input is echoed by the terminal; a captured noninteractive test may show prompts on one line because input echo is not produced by the application itself.

### At the shuffled deck

“Dito yung final shuffled deck after two perfect shuffles. Hindi random ito. Makikita natin yung result ng salit-salitang pagsama ng dalawang halves.”

For this current file, its first eight cards after two shuffles are:

```text
D-A, H-A, S-A, C-A, D-K, H-K, S-K, C-K
```

These plain codes match the data. Actual output also includes suit symbols and ANSI color codes. Symbol/color rendering depends on terminal support; don't promise a particular font or color on every PC.

### At dealing

“Naipamigay na lahat ng 52 cards. Since hindi divisible by 8 yung 52, hindi pare-pareho yung counts. First four players may seven cards, tapos yung remaining four may six. Walang nawawalang cards.”

Actual count lines:

```text
PLAYER 1 - 7 CARDS
PLAYER 2 - 7 CARDS
PLAYER 3 - 7 CARDS
PLAYER 4 - 7 CARDS
PLAYER 5 - 6 CARDS
PLAYER 6 - 6 CARDS
PLAYER 7 - 6 CARDS
PLAYER 8 - 6 CARDS
```

### At Round 1's reveals

“Before i-announce yung winner, kita muna yung nilapag ng lahat. Player 1 Diamond Ace, then Heart Ace, Spade Ace, Club Ace, then yung Kings. Among the Aces, Diamond yung highest suit, kaya Player 1 yung winner.”

**Plain-code transcription of the verified output** (symbols/colors omitted only in this guide):

```text
Player 1 plays: D-A
Player 2 plays: H-A
Player 3 plays: S-A
Player 4 plays: C-A
Player 5 plays: D-K
Player 6 plays: H-K
Player 7 plays: S-K
Player 8 plays: C-K

ROUND 1 WINNER: PLAYER 1

COLLECTED CARDS:
D-A, H-A, S-A, C-A, D-K, H-K, S-K, C-K
```

Don't say every round's cards are this same set. These are the first round's verified cards.

### At Player 1's updated deck

“Bago mag-draw, seven cards si Player 1. Naglabas siya ng isa, so six yung natira. Nakuha niya yung eight played cards, including yung sarili niyang Ace, kaya fourteen na yung updated count.”

“Makikita rin natin yung bottom order. Natira muna yung old cards niya, tapos yung sariling D-A, tapos yung other players' cards.”

Verified Round 1 deck, top to bottom:

```text
PLAYER 1'S UPDATED DECK
Total Cards: 14
D-Q, D-10, D-8, D-6, D-4, D-2,
D-A, H-A, S-A, C-A, D-K, H-K, S-K, C-K
```

This is a **comma-separated transcription** for explanation. The actual Deck.displayCards output uses spaced cells and rows of eight.

### While rounds continue

“Umuulit lang yung draw, reveal, compare, collect, result, at elimination check. Sa particular na setup na ito, si Player 1 ang nananalo sa bawat round. Result iyon ng fixed starting order at perfect shuffles, hindi hard-coded na winner.”

The full game may finish quickly; scroll back through output to explain a round instead of claiming there is a pause or manual next-round feature. There is no per-round user confirmation in the code.

### At eliminations

“Pagkatapos ng Round 6, ubos na yung cards ng Players 5 hanggang 8, kaya marked na silang eliminated. Hindi na sila magdo-draw sa next round. Players 2 hanggang 4 naman pagkatapos ng Round 7.”

“Yung check, after collection pa rin, kaya chance muna manalo yung player kahit last card na niya.”

### At final result and replay

“After seven completed rounds, nasa Player 1 na lahat ng 52 cards. Sa next champion check, magre-return yung Player na iyon, kaya final result na.”

“Then balik tayo sa Main. Tatanungin kung maglalaro ulit. Kapag YES, setup ulit with a fresh Game. Sa demo natin, NO, kaya closing messages at tapos na.”

Verified winner text:

```text
PLAYER 1 WINS!
Congratulations, Player 1! You collected all 52 cards!

Total Rounds Played: 7
```

Actual spacing/color depends on the formatting helpers. The final deck is displayed before the replay prompt.

If asked for a setup with alternating winners, the previously checked `5 players / 3 shuffles` on this same input gives P1/P2 alternating in its first ten rounds and finishes in 36 rounds with P1 champion. That's a separate optional demo, not what happens in the required 8/2 demo.

## Panel defense: short natural answers

| Possible question | Answer you can say |
| --- | --- |
| Why did you use multiple classes? | “Para malinaw yung responsibilities. Main para sa program flow at input, Game para sa laban, Player para sa participant, Deck para sa pile operations, Card para sa comparison, at enums para sa valid ranks/suits.” |
| Why does the program start in Main.java? | “Dahil doon nakalagay yung public static void main(String[] args) na pinili nating run configuration. Hindi special yung filename na Main mismo; yung entry-point method at launched class yung mahalaga.” |
| What happens when a method is called? | “Ipinapasa yung arguments, tatakbo yung method body, tapos babalik sa caller. Kapag may return value, puwede nating i-save o gamitin yung result.” |
| Is Go to Declaration executing the method? | “Hindi po. Navigation lang iyon para makita yung definition. Run ang nagpapatakbo; ginagamit ko lang yung navigation para sundan yung flow habang nagpapaliwanag.” |
| Why Map<Player, Card>? | “Para alam natin kung aling player yung naglabas ng aling card sa current round. Hindi ko na kailangan ng separate parallel lists.” |
| Why LinkedHashMap? | “Para may player-card pairs na preserved yung insertion order, kaya maayos yung reveal order. Hindi iyon ang nagso-sort ng cards.” |
| How does compareTo determine the winner? | “Rank muna, suit kapag same rank. Positive kapag mas mataas yung current card. Ace ang highest rank, Diamond ang highest tie-break suit.” |
| What if two players play the same rank? | “Suit yung tie-breaker. Sa implementation na ito, wala pong face-down war subround.” |
| Can two identical cards tie? | “Sa valid loaded deck, hindi, kasi rejected yung duplicate suit-and-rank cards. compareTo can return zero for equal cards, pero hindi dapat mangyari iyon as separate draws sa valid game.” |
| How does perfect shuffle work? | “Split sa equal halves, then first-half card, second-half card, alternate hanggang matapos. Same inputs, same order; hindi random.” |
| Why shuffle 1–100 times? | “Iyon yung implemented allowed range. Validated sa input method at Game constructor. Hindi ibig sabihin ng mas maraming shuffles ay mas random.” |
| How are cards distributed? | “Top card, next player, paikot gamit modulo, hanggang empty yung main deck. Kaya puwedeng may one-card difference sa player counts.” |
| Why do cards go to the bottom? | “Iyon yung queue rule ng game natin: top yung next draw, bottom yung won cards. Hindi agad marereplay yung bagong nakuha kung may old cards pa.” |
| Which collected card goes first? | “Sariling played card ng winner, then other players in circular player-number order. Inactive players have no entry, so skipped.” |
| Why does Player 1 keep winning in the 8/2 demo? | “Deterministic yung shuffle at ordered yung input. Sa specific setup, highest yung cards niya each round. Walang if na automatic Player 1 ang winner.” |
| How does elimination work? | “After collection, empty deck at not yet eliminated yung condition. Mark flag true and announce once. Hindi nirere-move yung Player object.” |
| What's the difference between active and eliminated? | “isActive checks current card availability. isEliminated is the flag na nagtatanda kung na-mark/announce na. Kaya after awarding pa yung elimination check.” |
| How does the game know when to stop? | “findChampion checks exactly 52 cards sa isang player. Then finalResult and return from start. Program exits only after NO sa replay.” |
| Can the game run indefinitely? | “Possible in principle kapag may repeated state at walang champion. Walang cycle detection o round cap sa code. Yung tested 8/2 demo terminates in seven rounds; hindi iyon proof para sa lahat ng arrangements.” |
| What happens if the file is invalid? | “Specific validation error ang itatapon, sasaluin sa Main, tapos error message at program end. Hindi tutuloy sa setup.” |
| What happens if keyboard input is invalid? | “Loop ulit sa same question. Numbers need digits and valid range; replay needs YES or NO.” |
| Why parseInt? | “Text yung result ng Scanner.nextLine. Kailangan natin siyang gawing int para magamit sa player count at shuffle loops.” |
| println versus printf? | “println has automatic newline. printf follows a format, gaya ng %d, %s, at %n; kailangan ng %n kung gusto ng newline.” |
| Why enums instead of free text? | “Para fixed yung valid choices at may clear codes at order. Hindi scattered yung arbitrary suit/rank strings sa game logic.” |
| Why final on Card fields? | “Para hindi mapalitan yung suit at rank after construction. Stable yung card identity at HashSet behavior.” |
| Does final make a Deck immutable? | “Hindi. Final yung reference, pero puwede pa rin add/remove yung cards sa queue. Hindi lang puwedeng ituro yung field sa ibang Deck.” |
| Does getCards expose the queue? | “Hindi, list copy ang ibinabalik. getDeck is different: actual Deck object ng Player yung returned reference.” |
| Is the original deck copied deeply? | “Hindi. New collection yung main Deck, same Card objects yung references. Safe sa current code dahil final yung card identity fields.” |
| Why equals and hashCode? | “Para makilala yung equal suit/rank values sa HashSet. Same object reference lang kasi yung default equality kung hindi natin i-override.” |
| What OOP concepts are applied? | “Encapsulation sa private fields at methods, composition sa Player na may Deck at Card na may enums, at abstraction sa removeTop/addBottom operations. May interface implementation at overriding sa Comparable/Card.compareTo.” |
| Did you use inheritance? | “Walang custom extends hierarchy sa project. Card implements Comparable, at like other Java classes, inherited Object methods yung equals/hashCode/toString na ino-override natin.” |
| Where is polymorphism? | “Sa Comparable contract ng Card at overridden methods gaya ng equals/hashCode na ginagamit ng HashSet. List/Deque/Map fields also refer to specific implementations like ArrayList/ArrayDeque/LinkedHashMap. Hindi natin kailangan mag-claim ng malaking custom inheritance design.” |
| Why public/private/static? | “Public kapag callable outside its class; private para internal helper. Static sa Main/display helpers at constants, instance methods para sa specific Game, Deck, o Player state.” |
| Does replay read input.txt again? | “Hindi, loaded once before the do-while. New Game uses the original in-memory cards. Kailangan rerun kung gusto basahin ulit yung edited file.” |
| Why not explain every class from top to bottom? | “Sinusundan ko yung execution para makita yung actual connection. Binubuksan ko yung class kapag may relevant call, then bumabalik sa caller.” |

## Limitations and possible issues — separate from the walkthrough

These are observations, not changes made to the project.

1. **No cycle detection or maximum-round safeguard.** Deterministic rules can revisit a state for some arrangements; the code would keep running if no champion is reached. No need to claim an actual infinite run was observed. The 8/2 scenario was tested and terminates.
2. **This is a War variant with suit tie-breaking.** Equal ranks use Diamond > Heart > Spade > Club. There is no face-down tie-war sequence. Present the implemented rules honestly.
3. **EOF is not handled as a user answer.** Scanner.nextLine() has no hasNextLine guard. If the input stream closes or runs out unexpectedly, NoSuchElementException can escape; the outer catch only handles IOException and IllegalArgumentException.
4. **The file path is relative to the working directory.** A run from a different folder can fail to locate resources/input.txt. This is not a classpath-resource loader.
5. **Number-of-players checking lives in Main.** Main restricts 2–8. Game's constructor validates shuffle count but does not independently validate numberOfPlayers, so it assumes the normal setup path.
6. **The outer error label covers more than file loading.** Main catches IOException/IllegalArgumentException around the entire game loop, but its text always says unable to load resources/input.txt. If an IllegalArgumentException came from another operation in that try, the label could be misleading.
7. **Console rendering varies.** Suit glyphs and ANSI colors depend on terminal support. ASCII suit/rank codes remain included, but raw ANSI escapes can be visible in unsupported terminals. printf minimum width includes escape characters; it does not guarantee perfectly aligned visible card columns.
8. **Long centered messages are not wrapped.** Math.max clamps padding to zero for strings longer than 52, so the congratulations may extend beyond that width. It stays on one line.
9. **Full output can be long.** Every round prints revealed cards, collected cards, and the winner's full updated deck. There is no pause, pagination, round-limit UI, or manual next-round control.
10. **The closed Scanner closes System.in.** Fine for this standalone run; not designed to leave shared console input open for another application in the same JVM.

If asked for an improvement, explain it as future work: “Puwede pong magdagdag ng cycle detection, EOF handling, or optional compact output. Hindi pa iyon implemented dito.”

## Quick rehearsal checklist

- Start at Main.main(), not at a standalone class tour.
- Know the difference between the method doing work and the display helper.
- On nested calls, explain the inner method before returning.
- On tiny getters, say the returned value and stay in the caller.
- Don't repeatedly revisit the same helper after its first explanation.
- Read the actual played cards before naming the winner.
- Show that the winner's updated deck ends with collected cards in the preserved order.
- Finish Game.start() before returning to Main's replay prompt.
- Treat error branches as conditional paths, not events in every game.
- State the 8/2 result as observed for this exact file, not universal behavior.

**Memory line:** “Sundan yung call, explain yung ginagawa, balik sa caller, then next statement.”

