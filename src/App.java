import java.awt.Color;
import javax.swing.*;

public class App extends JFrame {
    public App() {
        // Set frame properties
        setTitle("Flappy Bird Game");
        setSize(900, 700); // Set frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel p1 = new JPanel();
        setPanel(p1, 600,900, 0, 0);
        p1.setBackground(new Color(0, 0, 0));
        p1.setLayout(null); // Use null layout for absolute positioning
        add(p1);

        // Heading panel
        JPanel heading = new JPanel();
        setPanel(heading, 900, 40, 0, 0);
        heading.setBackground(new Color(0, 0, 0));
        heading.setLayout(null);
        p1.add(heading);

        // Heading label
        JLabel usernameLabel = new JLabel("FLAPPY BIRDY");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(400, 5, 805, 30); 
        heading.add(usernameLabel);

        // Flappy Bird game panel
        flappyBird bird = new flappyBird();//took from another class
        bird.setBounds(50, 50, bird.boardWidth, bird.boardHeight); // Set bounds for positioning
        p1.add(bird);
        
        bird.requestFocus();

        setVisible(true);
    }

    public JPanel setPanel(JPanel panel, int width, int height, int x, int y) {
        panel.setSize(width, height);
        panel.setLocation(x, y);
        panel.setVisible(true);
        return panel;
    }

    public static void main(String[] args) {
    	
        new App();
    }
}
