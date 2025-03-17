package no.ntnu.liaml.cardgame;

public class Cards {
    private char suit;
    private char value;

    public Cards(char suit, char value) {
        this.suit = suit;
        this.value = value;
    }

    public char getSuit() {
        return suit;
    }

    public char getValue() {
        return value;
    }

    public String toString() {
        String suitName;
        switch (suit) {
            case 'S': suitName = "Spades"; break;
            case 'H': suitName = "Hearts"; break;
            case 'D': suitName = "Diamonds"; break;
            case 'C': suitName = "Clubs"; break;
            default: suitName = String.valueOf(suit);
        }

        String valueName;
        switch (value) {
            case 'T': valueName = "10"; break;
            case 'J': valueName = "Jack"; break;
            case 'Q': valueName = "Queen"; break;
            case 'K': valueName = "King"; break;
            case 'A': valueName = "Ace"; break;
            default: valueName = String.valueOf(value);
        }

        return valueName + " of " + suitName;
    }
}