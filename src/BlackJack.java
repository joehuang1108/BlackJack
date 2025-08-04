import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class BlackJack {

    private class Card{
        String value;
        String type;

        Card(String value, String type){
            this.value = value;
            this.type = type;
        }

        public String toString(){
            return value + "-" + type;
        }

        public int getValue(){
            if("AJQK".contains(value)){ // checking A J Q K
                if(value == "A"){
                    return 11;
                }
                else{
                    return 10;
                }
            }
            else{
                return Integer.parseInt(value);
            }
        }

        public boolean isAce(){
            if(value == "A"){
                return true;
            } 
            else{
                return false;
            }
        }

        public String getImagePath(){   // returning the path to a specific card
            return "./cards/" + toString() + ".png";
        }

    }

    ArrayList<Card> deck;
    Random random = new Random(); 

    // Dealer Hand
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount; 

    // Player Hand
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    // GUI Window
    int boardWidth = 600;
    int boardHeight = 600;

    int cardWidth = 110;     // ratio -- 1/1.4
    int cardHeight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            try {
                Image hiddenCardImage = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                g.drawImage(hiddenCardImage, 20, 20, cardWidth, cardHeight, null);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Draw dealer's hand
            for(int i = 0; i < dealerHand.size(); i++){
                Card card = dealerHand.get(i);
                Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                g.drawImage(cardImage, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHeight, null);
            }

            
        }
    };

    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");

    public BlackJack() {
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);





        gamePanel.repaint();



    }


    public void startGame(){
        buildDeck();
        shuffleDeck();

        // LOGIC

        // Dealer Hand
        dealerHand = new ArrayList<Card>(); // create new ArrayList
        dealerSum = 0;
        dealerAceCount = 0; 

        // removes a card from deck, variable hiddenCard holds onto that card
        hiddenCard = deck.remove(deck.size()-1);
        dealerSum += hiddenCard.getValue();
        if(hiddenCard.isAce()){
            dealerAceCount += 1;
        }

        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        if(card.isAce()){
            dealerAceCount += 1;
        }
        dealerHand.add(card);

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);


        // Player Hand
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i = 0; i < 2; i++){
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            if(card.isAce()){
                dealerAceCount += 1;
            }
            playerHand.add(card);
        }

        System.out.println("PLAYER: ");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);
        

    }

    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"}; // Clubs, Diamonds, Hearts, Spades

        // Create a deck with the given values and types
        for(int i = 0; i < types.length; i++){
            for(int j = 0; j < values.length; j++){
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILT DECK");
        System.out.println(deck);
    }

    public void shuffleDeck(){
        for(int i = 0; i < deck.size(); i++){
            int j = random.nextInt(deck.size()); // generate random number from 0-51
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }
    
}
