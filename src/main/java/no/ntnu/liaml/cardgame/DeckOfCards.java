package no.ntnu.liaml.cardgame;

public class DeckOfCards {
    private Cards[] deck;
    private int cardsUsed;
    private final char[] suit = { 'S', 'H', 'D', 'C' };
    private final char[] value = { '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A' };

    public DeckOfCards() {
        deck = new Cards[52];
        int cardCount = 0;
        for (char c : suit) {
            for (char card : value) {
                deck[cardCount] = new Cards(c, card);
                cardCount++;
            }
        }
        cardsUsed = 0;
    }

    public void shuffle() {
        for (int i = 51; i > 0; i--) {
            int rand = (int) (Math.random() * (i + 1));
            Cards temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }

    public int cardsLeft() {
        return 52 - cardsUsed;
    }

    public Cards dealCard() {
        if (cardsUsed == 52)
            shuffle();
        cardsUsed++;
        return deck[cardsUsed - 1];
    }

    public Cards[] dealHand(int numCards) {
        Cards[] hand = new Cards[numCards];
        for (int i = 0; i < numCards; i++) {
            hand[i] = dealCard();
        }
        return hand;
    }
}
