import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


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
                if(!stayButton.isEnabled()){
                    hiddenCardImage = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
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

            // Draw player's hand
            for(int i = 0; i < playerHand.size(); i++){
                Card card = playerHand.get(i);
                Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                g.drawImage(cardImage, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null);
            }
            
            // Win/lose disaply and conditions
            if(!stayButton.isEnabled()){
                dealerSum = reduceDealerAce();
                playerSum = reducePlayerAce();
                System.out.println("STAY: ");
                System.out.println(dealerSum);
                System.out.println(playerSum);

                String message = "";
                // TODO: LOGIC - UPDATE MESSAGE 
                if(playerSum > 21){
                    message = "YOU LOSE!";
                }
                else if(dealerSum > 21){
                    message = "YOU WIN!";
                }
                else if(playerSum == dealerSum){
                    message = "TIE";
                }
                else if(playerSum > dealerSum){
                    message = "YOU WIN!";
                }
                else if(playerSum < dealerSum){
                    message = "YOU LOSE!";
                }

                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.white);
                g.drawString(message, 220, 250);
                resetButton.setEnabled(true);
            }

        }
    };

    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton resetButton = new JButton("Reset");

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
        resetButton.setFocusable(false);
        resetButton.setEnabled(false);
        buttonPanel.add(resetButton);

        // Hit button
        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Card card = deck.remove(deck.size()-1);
                // Card card = new Card("A", "C");
                playerSum += card.getValue();
                if(card.isAce()){
                    playerAceCount += 1;
                }
                playerHand.add(card);
                if(reducePlayerAce() >= 21){     // disable hit button if sum already over 21
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                }
                gamePanel.repaint();
            }
        });

        // Stay Button
        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                // Dealer's turn to get a card
                while(dealerSum < 17){
                    Card card = deck.remove(deck.size()-1);
                    dealerSum += card.getValue();
                    if(card.isAce()){
                        dealerAceCount += 1;
                    }
                    dealerHand.add(card);
                }
                gamePanel.repaint();
            }
        });

        // Reset Button
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);
                resetButton.setEnabled(false);
                startGame();
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();

        // add a reset button to play again 

        

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

    public int reducePlayerAce(){   // return the new sum with the ace accounted as 1s
        while(playerSum > 21 && playerAceCount > 0){
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce(){
        while(dealerSum > 21 && dealerAceCount > 0){
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }
}
