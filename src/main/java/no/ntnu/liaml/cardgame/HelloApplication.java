package no.ntnu.liaml.cardgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReferenceArray;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        Label welcomeText = new Label("Welcome to JavaFX Application!");
        Button helloButton = new Button("Deal 5 cards");
        DeckOfCards deck = new DeckOfCards();
        deck.shuffle();
        Cards[] hand = deck.dealHand(5);

        Label cardsDealt = new Label();
        updateText(hand, cardsDealt);

        helloButton.setOnAction(event -> {
            Cards[] newHand = deck.dealHand(5);
            updateHand(hand, newHand);
            updateText(hand, cardsDealt);
        });

        VBox vbox = new VBox(welcomeText, helloButton, cardsDealt);
        Scene scene = new Scene(vbox, 1000, 1200);

        stage.setTitle("Card Game Application");
        stage.setScene(scene);
        stage.show();
    }

    private void updateHand(Cards[] hand, Cards[] newHand) {
        System.arraycopy(newHand, 0, hand, 0, hand.length);
    }

    private void updateText(Cards[] hand, Label cardsDealt) {
        cardsDealt.setText("Cards dealt: ");
        for (Cards card : hand) {
            cardsDealt.setText(cardsDealt.getText() + card + ", ");
        }


    }

    public static void main(String[] args) {
        launch();
    }
}