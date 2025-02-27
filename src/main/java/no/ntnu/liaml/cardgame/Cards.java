package no.ntnu.liaml.cardgame;

public class Cards {
    private char suit;
    private int value;

    public Cards(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public char getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return value + " of " + suit;
    }
}
