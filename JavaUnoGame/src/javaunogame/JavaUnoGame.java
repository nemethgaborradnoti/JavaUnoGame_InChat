package javaunogame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

public class JavaUnoGame {

    ////// Player move
    public static String playCardPlayer(List<String> playerHand, List<String> usedCards, String currentCard, List<String> drawingDeck, Scanner scanner) {
        String chosenCard = "";
        String playerName = "You";
        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("Choose a card to play from your hand (or draw):\t");
                chosenCard = scanner.nextLine().toLowerCase();

            if (chosenCard.equals("draw")) { // Draw a card and ask if wants to play it
                dealCards(drawingDeck, playerHand, 1);
                String newDrawCard = playerHand.get(playerHand.size() - 1);
                //chosenCard = newDrawCard;
                System.out.println("* You drew a [" + newDrawCard + "] *");
                String[] currentCardTokens = currentCard.split(" ");

                if (currentCardTokens[0].equals(newDrawCard.split(" ")[0]) ||
                        currentCardTokens[1].equals(newDrawCard.split(" ")[1]) || newDrawCard.contains("wild")) {
                    System.out.print("Your cards:\t");
                    printCards(playerHand);
                    System.out.print("Play new card? ([ " + newDrawCard + " ])\t");
                    chosenCard = scanner.nextLine().toLowerCase();
                    switch(chosenCard) {
                        case "y", "ye", "yes" -> { 
                            chosenCard = newDrawCard;
                            if (chosenCard.contains("wild")) {
                                String chosenColor = promptForColor(scanner);
                                currentCard = chosenColor + " wild";
                            }
                            validChoice = true;
                            break;
                        }
                        default -> {
                            return currentCard;
                        }
                    }
                } else{
                    return currentCard;
                }
            }
            if(!validChoice){
                if (playerHand.contains(chosenCard)) {
                    if (chosenCard.contains("wild")) {
                        String chosenColor = promptForColor(scanner);
                        currentCard = chosenColor + " wild";
                        validChoice = true;
                    } else {
                        String[] currentCardTokens = currentCard.split(" ");
                        String[] chosenCardTokens = chosenCard.split(" ");

                        if (currentCardTokens[0].equals(chosenCardTokens[0]) || currentCardTokens[1].equals(chosenCardTokens[1])) {
                            validChoice = true;
                        } else {
                            System.out.println("You cannot play this card. Please choose another one.");
                        }
                    }
                } else {
                    System.out.println("You don't have this card in your hand!");
                }
            }
        }
        usedCards.add(chosenCard);
        playerHand.remove(chosenCard);
        currentCard = chosenCard;
        System.out.println("You put [ "+ currentCard +" ]!");
        checkPlayerHand(playerHand, playerName);
        return currentCard;
    }
    
    ////// Shuffle function
    public static void shuffleDeck(List<String> deck) {
        Random rand = new Random();
        int n = deck.size();
        for (int i = 0; i < n; i++) {
            int j = i + rand.nextInt(n - i);
            String temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
           }
    }
    
    ////// Game state 1
    public static void printCards(List<String> cards) { // Print cards with "[]" around them
        for (String card : cards) {
            System.out.print("[" + card + "] ");
        }
        System.out.println();
    }
    ////// Game state 2
    public static void printGameState(List<String> player1Cards, List<String> player2Cards, List<String> drawingDeck, String currentCard, List<String> usedCards, int round) {
        System.out.println("\t\t\t\t\t* * * Round #" + round + " * * *\n");
        System.out.print("Drawing deck (" + drawingDeck.size() + ")\t\t");
        //printCards(drawingDeck); // Prints out the deck that you can draw from
        
        System.out.print("Current card: ( ( ( [" + currentCard + "] ) ) )\t\t"); // Prints out the current cart
        
        System.out.println("Used cards (" + usedCards.size() +"): " + usedCards); // Prints out used cards
        
        System.out.print("Your hand (" + player1Cards.size() + "):\t\t"); // Prints out Player's hand
        printCards(player1Cards); 
        System.out.print("\nComputer's hand (" + player2Cards.size() + "):\t"); // Prints out Computer's hand
        printCards(player2Cards);
    }
    
    ////// Deal cards
    public static void dealCards(List<String> deck, List<String> playerHand, int numCards) {
        for (int i = 0; i < numCards; i++) {
            String card = deck.remove(0);
            playerHand.add(card);
        }
    }
    
    ////// Check for number of cards in the Player's hand
    public static void checkPlayerHand(List<String> playerHand, String playerName) {
        if(playerHand.isEmpty()){
            System.out.println(playerName + " won the game!");
            System.exit(0);
        }
        else if(playerHand.size() == 1){
            System.out.println("UNO!");
        }
    }
    
    ////// Wild card color choose
    private static String promptForColor(Scanner scanner) {
        System.out.print("Choose a color: (1)red, (2)green, (3)yellow, (4)blue:\t");
        int colorChoice = scanner.nextInt();
        return switch (colorChoice) {
            case 2 -> "green";
            case 3 -> "yellow";
            case 4 -> "blue";
            default -> "red";
        };
    }
    
    ////// Wild card color choose for the AI
    private static String promptForColorAI() {
        int colorChoice = ThreadLocalRandom.current().nextInt(1, 5); 
        return switch (colorChoice) {
            case 2 -> "green";
            case 3 -> "yellow";
            case 4 -> "blue";
            default -> "red";
        };
    }

    ////// AI move
    public static String playCardAI(List<String> player2Cards, List<String> usedCards, String currentCard, List<String> drawingDeck) {
        boolean played = false;
        String chosenCard = "";
        String playerName = "Computer";

        // Iterate through player's cards and check for matching cards
        for (String card : player2Cards) {
            String[] currentCardTokens = currentCard.split(" ");
            String[] chosenCardTokens = card.split(" ");

            if (currentCardTokens[0].equals(chosenCardTokens[0]) || currentCardTokens[1].equals(chosenCardTokens[1]) || card.contains("wild")) {
                chosenCard = card;
                played = true;
                break;
            }
        }

        // If no matching cards are found, draw a card and check it
        if (!played) {
            System.out.println("- Computer draws a card.");
            dealCards(drawingDeck, player2Cards, 1);
            String card = player2Cards.get(player2Cards.size() - 1);
            String[] currentCardTokens = currentCard.split(" ");
            String[] chosenCardTokens = card.split(" ");

            if (currentCardTokens[0].equals(chosenCardTokens[0]) || currentCardTokens[1].equals(chosenCardTokens[1]) || card.contains("wild")) {
                chosenCard = card;
                played = true;
            }
        }

        // If a matching card is found, remove it from player's hand and update the current card
        if (played) {
            usedCards.add(chosenCard);
            player2Cards.remove(chosenCard);
            if(chosenCard.contains("wild")){
                chosenCard = promptForColorAI() + " wild";
            }
            currentCard = chosenCard;
            System.out.println("- Computer put [" + currentCard + "]!");
        } else {
            System.out.println("- Computer cannot play any cards.");
        }
        checkPlayerHand(player2Cards, playerName);
        return currentCard;
    }
    
    ////// MAIN
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        // Initialization and declaration of cards
        String[] colors = {"red", "green", "yellow", "blue"};
        String[] types = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "skip", "reverse", "draw_two"};
        int[] cardCounts = {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

        // Card list
        List<String> pureDeck = new ArrayList<>();
        
        // Player hands
        List<String> player1Cards = new ArrayList<>();
        List<String> player2Cards = new ArrayList<>();

        // Generating cards
        for (String nxtcolor : colors) {
            for (int j = 0; j < types.length; j++) {
                int count = cardCounts[j];
                for (int k = 0; k < count; k++) {
                    String card = nxtcolor + " " + types[j];
                    pureDeck.add(card);
                }
            }
        }
        
        // Making wildcard and wilddraw_4
        final int WILDCARD_COUNT = 40;
        final int WILD_DRAW_4_COUNT = 4;
        for (int i = 0; i < WILDCARD_COUNT; i++) {
            pureDeck.add("wild card");
        }
        for (int i = 0; i < WILD_DRAW_4_COUNT; i++) {
            pureDeck.add("wild draw_four");
        }

        // Copying the pure deck to drawing deck
        List<String> drawingDeck = new ArrayList<>(pureDeck.size());
        drawingDeck.addAll(pureDeck);
        
        // Shuffle the deck
        shuffleDeck(drawingDeck);
        
        // Dealing cards to players
        dealCards(drawingDeck, player1Cards, 2);
        dealCards(drawingDeck, player2Cards, 5);
        
        // Declaring the Played Cards list and flipping first card for the table
        List<String> usedCards = new ArrayList<>();
        usedCards.add(drawingDeck.get(0));
        String currentCard = drawingDeck.remove(0);
        if(currentCard.contains("wild")){
            currentCard = promptForColorAI() + " wild";
        }
        
        //Print out game state
        int round = 1;
        printGameState(player1Cards, player2Cards, drawingDeck, currentCard, usedCards, round);

        for(int i = 0; i < 6; i++){
            // Player 1 move
            currentCard = playCardPlayer(player1Cards, usedCards, currentCard, drawingDeck, scanner);
            // Player 2 AI move
            currentCard = playCardAI(player2Cards, usedCards, currentCard, drawingDeck);
            round++;
            printGameState(player1Cards, player2Cards, drawingDeck, currentCard, usedCards, round);
        }
        scanner.close();
    }
}