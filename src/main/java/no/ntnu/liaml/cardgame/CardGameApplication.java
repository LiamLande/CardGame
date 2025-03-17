package no.ntnu.liaml.cardgame;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class CardGameApplication extends Application {
    private Cards[] hand;
    private DeckOfCards deck;
    private Pane cardDisplayArea;
    private TextField sumOfFacesField;
    private TextField cardsOfHeartsField;
    private TextField flushField;
    private TextField queenOfSpadesField;
    private List<Pane> cardPanes = new ArrayList<>();
    private boolean handDealt = false;

    // CSS styles
    private final String BUTTON_STYLE = "-fx-background-color: #4a6ea9; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 8 15 8 15;";

    private final String BUTTON_HOVER_STYLE = "-fx-background-color: #385a96; " +
            "-fx-cursor: hand;";

    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: #2c4c7c; " +
            "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);";

    private final String TEXT_FIELD_STYLE = "-fx-background-color: #f8f8f8; " +
            "-fx-border-color: #bbbbbb; " +
            "-fx-border-radius: 3; " +
            "-fx-background-radius: 3; " +
            "-fx-padding: 5;";

    private final String CARD_AREA_STYLE = "-fx-background-color: #e8f4f8; " +
            "-fx-border-color: #c0c0c0; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5;";

    @Override
    public void start(Stage stage) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f0f5fa);");

        // Create title
        Label titleLabel = new Label("Card Game");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 15, 0));
        mainLayout.setTop(titleLabel);

        // Create the card display area
        cardDisplayArea = new Pane();
        cardDisplayArea.setMinSize(500, 400);
        cardDisplayArea.setStyle(CARD_AREA_STYLE);

        // Add drop shadow effect to card area
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.15));
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(2);
        dropShadow.setRadius(5);
        cardDisplayArea.setEffect(dropShadow);

        // Add text to display area
        Label displayText = new Label("No cards dealt yet.\nClick 'Deal hand' to start.");
        displayText.setLayoutX(180);
        displayText.setLayoutY(190);
        displayText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        displayText.setStyle("-fx-text-fill: #7f8c8d;");
        cardDisplayArea.getChildren().add(displayText);

        // Create buttons
        VBox buttonsBox = new VBox(15);
        buttonsBox.setPadding(new Insets(0, 0, 0, 20));
        buttonsBox.setAlignment(Pos.TOP_CENTER);

        Button dealHandButton = createStyledButton("Deal hand");
        Button checkHandButton = createStyledButton("Check hand");
        Button dealUntilFlushButton = createStyledButton("Deal until flush");

        buttonsBox.getChildren().addAll(dealHandButton, checkHandButton, dealUntilFlushButton);

        // Create information fields
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(15);
        infoGrid.setVgap(15);
        infoGrid.setPadding(new Insets(20, 0, 0, 0));
        infoGrid.setStyle("-fx-background-color: #f5f7fa; -fx-background-radius: 5; -fx-border-color: #dde4e9; -fx-border-radius: 5; -fx-padding: 15;");

        // Style for info labels
        String labelStyle = "-fx-font-weight: bold; -fx-text-fill: #34495e;";

        Label sumLabel = new Label("Sum of the faces:");
        sumLabel.setStyle(labelStyle);
        infoGrid.add(sumLabel, 0, 0);

        sumOfFacesField = createStyledTextField(60);
        infoGrid.add(sumOfFacesField, 1, 0);

        Label heartsLabel = new Label("Cards of hearts:");
        heartsLabel.setStyle(labelStyle);
        infoGrid.add(heartsLabel, 2, 0);

        cardsOfHeartsField = createStyledTextField(200);
        infoGrid.add(cardsOfHeartsField, 3, 0);

        Label flushLabel = new Label("Flush:");
        flushLabel.setStyle(labelStyle);
        infoGrid.add(flushLabel, 0, 1);

        flushField = createStyledTextField(60);
        infoGrid.add(flushField, 1, 1);

        Label queenLabel = new Label("Queen of spades:");
        queenLabel.setStyle(labelStyle);
        infoGrid.add(queenLabel, 2, 1);

        queenOfSpadesField = createStyledTextField(60);
        infoGrid.add(queenOfSpadesField, 3, 1);

        // Initialize deck and hand
        deck = new DeckOfCards();
        deck.shuffle();

        // Set up button actions
        dealHandButton.setOnAction(event -> {
            dealNewHandWithAnimation();
        });

        checkHandButton.setOnAction(event -> {
            if (!handDealt) {
                showAlert("No Cards", "Please deal a hand first before checking.");
                return;
            }
            checkHandWithAnimation();
        });

        dealUntilFlushButton.setOnAction(event -> {
            dealUntilFlush();
        });

        // Add components to the main layout
        mainLayout.setCenter(cardDisplayArea);
        mainLayout.setRight(buttonsBox);
        mainLayout.setBottom(infoGrid);

        // Create the scene
        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setTitle("Card Game");
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(120);
        button.setStyle(BUTTON_STYLE);

        // Add hover, press, and release effects
        button.setOnMouseEntered(e -> button.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(BUTTON_STYLE));
        button.setOnMousePressed(e -> button.setStyle(BUTTON_STYLE + BUTTON_PRESSED_STYLE));
        button.setOnMouseReleased(e -> button.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE));

        // Add subtle scale animation on press
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(0.95);
            st.setToY(0.95);
            st.play();
        });

        button.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        return button;
    }

    private TextField createStyledTextField(int width) {
        TextField textField = new TextField();
        textField.setEditable(false);
        textField.setPrefWidth(width);
        textField.setStyle(TEXT_FIELD_STYLE);
        return textField;
    }

    private void dealNewHandWithAnimation() {
        // Clear any existing cards and info
        cardDisplayArea.getChildren().clear();
        cardPanes.clear();
        clearInfoFields();

        // Deal new hand
        hand = deck.dealHand(5);
        handDealt = true;

        // Create and animate each card
        for (int i = 0; i < hand.length; i++) {
            Pane cardPane = createCardPane(hand[i], i);
            cardPanes.add(cardPane);
            cardDisplayArea.getChildren().add(cardPane);

            // Initial state: transparent and scaled down
            cardPane.setOpacity(0);
            cardPane.setScaleX(0.1);
            cardPane.setScaleY(0.1);

            // Create animations
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), cardPane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), cardPane);
            scaleUp.setFromX(0.1);
            scaleUp.setFromY(0.1);
            scaleUp.setToX(1.0);
            scaleUp.setToY(1.0);

            RotateTransition rotateIn = new RotateTransition(Duration.millis(300), cardPane);
            rotateIn.setFromAngle(90);
            rotateIn.setToAngle(0);

            // Combine animations
            ParallelTransition pt = new ParallelTransition(fadeIn, scaleUp, rotateIn);
            pt.setDelay(Duration.millis(i * 150)); // Stagger the animations
            pt.play();

            // Make cards interactive
            setupCardInteractivity(cardPane);
        }
    }

    private void checkHandWithAnimation() {
        // Calculate values
        int sum = calculateSumOfFaces();
        String heartsCards = findHeartsCards();
        boolean hasFlush = checkFlush();
        boolean hasQueenOfSpades = checkQueenOfSpades();

        // Animate highlighting of relevant cards
        highlightRelevantCards(heartsCards, hasQueenOfSpades);

        // Animate results appearing
        animateTextField(sumOfFacesField, String.valueOf(sum));
        animateTextField(cardsOfHeartsField, heartsCards.isEmpty() ? "None" : heartsCards);
        animateTextField(flushField, hasFlush ? "Yes" : "No");
        animateTextField(queenOfSpadesField, hasQueenOfSpades ? "Yes" : "No");

        // Highlight result fields based on values
        highlightResultFields(hasFlush, hasQueenOfSpades);
    }

    private void highlightRelevantCards(String heartsCards, boolean hasQueenOfSpades) {
        for (int i = 0; i < hand.length; i++) {
            Cards card = hand[i];
            Pane cardPane = cardPanes.get(i);

            boolean isHeart = card.getSuit() == 'H';
            boolean isQueenOfSpades = card.getSuit() == 'S' && card.getValue() == 'Q';

            if (isHeart || isQueenOfSpades) {
                // Create glow effect
                DropShadow glow = new DropShadow();
                glow.setColor(isHeart ? Color.RED : Color.BLUE);
                glow.setRadius(20);

                // Apply glow with animation
                FadeTransition glowFade = new FadeTransition(Duration.millis(500), cardPane);
                glowFade.setAutoReverse(true);
                glowFade.setCycleCount(4);
                glowFade.setFromValue(1.0);
                glowFade.setToValue(0.7);

                // Slight bounce animation
                TranslateTransition bounce = new TranslateTransition(Duration.millis(250), cardPane);
                bounce.setAutoReverse(true);
                bounce.setCycleCount(4);
                bounce.setFromY(0);
                bounce.setToY(-10);

                cardPane.setEffect(glow);

                ParallelTransition pt = new ParallelTransition(glowFade, bounce);
                pt.play();
            }
        }
    }

    private void animateTextField(TextField field, String value) {
        field.clear();

        // Type-in animation for text fields
        String finalText = value;
        new Thread(() -> {
            try {
                for (int i = 0; i <= finalText.length(); i++) {
                    final String text = finalText.substring(0, i);
                    Thread.sleep(50);
                    javafx.application.Platform.runLater(() -> field.setText(text));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void highlightResultFields(boolean hasFlush, boolean hasQueenOfSpades) {
        // Flash background for special results
        if (hasFlush) {
            flashTextField(flushField, Color.GREEN);
        }

        if (hasQueenOfSpades) {
            flashTextField(queenOfSpadesField, Color.ORANGE);
        }
    }

    private void flashTextField(TextField field, Color color) {
        String originalStyle = field.getStyle();
        String highlightStyle = "-fx-background-color: " + toRGBCode(color) + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold;";

        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    javafx.application.Platform.runLater(() -> field.setStyle(highlightStyle));
                    Thread.sleep(200);
                    javafx.application.Platform.runLater(() -> field.setStyle(originalStyle));
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private Pane createCardPane(Cards card, int index) {
        Pane cardPane = new Pane();

        int cardWidth = 100;
        int cardHeight = 150;
        int spacing = 20;
        int startX = 50;
        int startY = 125;

        cardPane.setLayoutX(startX + index * (cardWidth + spacing));
        cardPane.setLayoutY(startY);

        // Card background
        Rectangle cardRect = new Rectangle(0, 0, cardWidth, cardHeight);
        cardRect.setArcWidth(10);
        cardRect.setArcHeight(10);
        cardRect.setFill(Color.WHITE);
        cardRect.setStroke(Color.GRAY);

        // Add inner shadow for a more 3D look
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(1.0);
        innerShadow.setOffsetY(1.0);
        innerShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        cardRect.setEffect(innerShadow);

        // Card value and suit
        Text valueText = new Text(getDisplayValue(card.getValue()));
        valueText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        valueText.setX(10);
        valueText.setY(30);

        // Corner suit
        Text cornerSuit = new Text(getSuitSymbol(card.getSuit()));
        cornerSuit.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        cornerSuit.setX(10);
        cornerSuit.setY(50);

        // Set color based on suit
        Color suitColor = (card.getSuit() == 'H' || card.getSuit() == 'D') ? Color.RED : Color.BLACK;
        valueText.setFill(suitColor);
        cornerSuit.setFill(suitColor);

        // Large center suit
        Text suitText = new Text(getSuitSymbol(card.getSuit()));
        suitText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        suitText.setX(cardWidth/2 - 20);
        suitText.setY(cardHeight/2 + 20);
        suitText.setFill(suitColor);

        // Bottom corner (inverted)
        Text bottomValue = new Text(getDisplayValue(card.getValue()));
        bottomValue.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        bottomValue.setX(cardWidth - 30);
        bottomValue.setY(cardHeight - 10);
        bottomValue.setFill(suitColor);
        bottomValue.setRotate(180);

        Text bottomSuit = new Text(getSuitSymbol(card.getSuit()));
        bottomSuit.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        bottomSuit.setX(cardWidth - 30);
        bottomSuit.setY(cardHeight - 30);
        bottomSuit.setFill(suitColor);
        bottomSuit.setRotate(180);

        cardPane.getChildren().addAll(cardRect, valueText, cornerSuit, suitText, bottomValue, bottomSuit);
        return cardPane;
    }

    private void setupCardInteractivity(Pane cardPane) {
        // Add hover effect
        cardPane.setOnMouseEntered(e -> {
            TranslateTransition rise = new TranslateTransition(Duration.millis(150), cardPane);
            rise.setToY(-20);
            rise.play();

            // Add shadow
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.rgb(0, 0, 0, 0.5));
            shadow.setRadius(10);
            shadow.setOffsetY(5);
            cardPane.setEffect(shadow);
        });

        cardPane.setOnMouseExited(e -> {
            TranslateTransition fall = new TranslateTransition(Duration.millis(150), cardPane);
            fall.setToY(0);
            fall.play();

            // Remove shadow
            cardPane.setEffect(null);
        });

        // Add click effect
        cardPane.setOnMousePressed(e -> {
            ScaleTransition shrink = new ScaleTransition(Duration.millis(100), cardPane);
            shrink.setToX(0.95);
            shrink.setToY(0.95);
            shrink.play();
        });

        cardPane.setOnMouseReleased(e -> {
            ScaleTransition restore = new ScaleTransition(Duration.millis(100), cardPane);
            restore.setToX(1.0);
            restore.setToY(1.0);
            restore.play();
        });
    }

    private String getDisplayValue(char value) {
        switch (value) {
            case 'T': return "10";
            case 'J': return "J";
            case 'Q': return "Q";
            case 'K': return "K";
            case 'A': return "A";
            default: return String.valueOf(value);
        }
    }

    private String getSuitSymbol(char suit) {
        switch (suit) {
            case 'H': return "♥";
            case 'D': return "♦";
            case 'C': return "♣";
            case 'S': return "♠";
            default: return String.valueOf(suit);
        }
    }

    private void clearInfoFields() {
        sumOfFacesField.clear();
        cardsOfHeartsField.clear();
        flushField.clear();
        queenOfSpadesField.clear();
    }

    private int calculateSumOfFaces() {
        int sum = 0;
        for (Cards card : hand) {
            char value = card.getValue();
            if (value >= '2' && value <= '9') {
                sum += (value - '0');
            } else if (value == 'T') {
                sum += 10;
            } else if (value == 'J') {
                sum += 11;
            } else if (value == 'Q') {
                sum += 12;
            } else if (value == 'K') {
                sum += 13;
            } else if (value == 'A') {
                sum += 14;
            }
        }
        return sum;
    }

    private String findHeartsCards() {
        StringBuilder hearts = new StringBuilder();
        for (Cards card : hand) {
            if (card.getSuit() == 'H') {
                hearts.append("H").append(card.getValue()).append(" ");
            }
        }
        return hearts.toString().trim();
    }

    private boolean checkFlush() {
        if (hand == null || hand.length == 0) return false;

        char suit = hand[0].getSuit();
        for (int i = 1; i < hand.length; i++) {
            if (hand[i].getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    private boolean checkQueenOfSpades() {
        for (Cards card : hand) {
            if (card.getSuit() == 'S' && card.getValue() == 'Q') {
                return true;
            }
        }
        return false;
    }

    private void dealUntilFlush() {
        // Create a progress indicator and counters
        cardDisplayArea.getChildren().clear();
        cardPanes.clear();
        clearInfoFields();

        Label searchingLabel = new Label("Searching for a flush...");
        searchingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        searchingLabel.setStyle("-fx-text-fill: #3498db;");
        searchingLabel.setLayoutX(180);
        searchingLabel.setLayoutY(150);

        Label attemptsLabel = new Label("Attempts: 0");
        attemptsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        attemptsLabel.setLayoutX(200);
        attemptsLabel.setLayoutY(180);

        cardDisplayArea.getChildren().addAll(searchingLabel, attemptsLabel);

        // Create a background task to search for a flush
        new Thread(() -> {
            int attempts = 0;
            boolean foundFlush = false;

            while (!foundFlush) {
                attempts++;
                final int currentAttempts = attempts;

                // Update the UI on the JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    attemptsLabel.setText("Attempts: " + currentAttempts);
                });

                // Deal a new hand and check for flush
                Cards[] newHand = deck.dealHand(5);

                // Determine if it's a flush
                if (newHand.length > 0) {
                    char suit = newHand[0].getSuit();
                    foundFlush = true;

                    for (int i = 1; i < newHand.length; i++) {
                        if (newHand[i].getSuit() != suit) {
                            foundFlush = false;
                            break;
                        }
                    }
                }

                // If found a flush, update the UI
                if (foundFlush) {
                    final Cards[] finalHand = newHand;
                    javafx.application.Platform.runLater(() -> {
                        // Update the main application state
                        hand = finalHand;
                        handDealt = true;

                        // Show success message
                        cardDisplayArea.getChildren().clear();
                        Label successLabel = new Label("Flush found after " + currentAttempts + " attempts!");
                        successLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                        successLabel.setStyle("-fx-text-fill: #27ae60;");
                        successLabel.setLayoutX(160);
                        successLabel.setLayoutY(80);
                        cardDisplayArea.getChildren().add(successLabel);

                        // Show the hand with a delay and animation
                        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.millis(1000));
                        pause.setOnFinished(e -> {
                            cardDisplayArea.getChildren().clear();
                            displayHand();
                            checkHandWithAnimation();
                        });
                        pause.play();
                    });
                }

                // Add a small delay to not freeze the UI
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void displayHand() {
        // Display the current hand
        for (int i = 0; i < hand.length; i++) {
            Pane cardPane = createCardPane(hand[i], i);
            cardPanes.add(cardPane);
            cardDisplayArea.getChildren().add(cardPane);
            setupCardInteractivity(cardPane);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}