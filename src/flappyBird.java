import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;

public class flappyBird extends JPanel implements ActionListener, KeyListener {
    private static Image topPipeImage;
    private static Image bottomPipeImage;
    // Constants for the game
    int boardHeight = 600;
    int boardWidth = 800;    
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 54;
    int birdHeight = 64;  // Corrected dimensions

    // Game logic variables
    Bird bird;
    int velocityX = -4; // pipes
    int velocityY = 0;
    int gravity = 1;  // Gravity should be positive for downward acceleration
    
    ArrayList<Pipe> pipes;
    
    Timer gameLoop;
    Timer placePipes;
    
    // PIPES
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    // Score
    int score = 0;
    JLabel scoreLabel;

    flappyBird() {
        // Load images
        Image birdImage = loadImage("flappybird.png", birdWidth, birdHeight);
        bottomPipeImage = loadImage("bottompipe.png", pipeWidth, pipeHeight); 
        topPipeImage = loadImage("toppipe.png", pipeWidth, pipeHeight); 
        
        // Initialize bird game logic
        bird = new Bird(birdImage);
        pipes = new ArrayList<>();
        
        placePipes = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        
        placePipes.start();

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true); // Ensures the panel can receive key events
        addKeyListener(this);
        
        setBackground(new Color(130, 207, 240));
        gameLoop = new Timer(1000 / 60, this); // 60 FPS game loop
        gameLoop.start();
        
        setVisible(true);

        // Initialize score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        this.add(scoreLabel);
    }

    private Image loadImage(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            return originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img; 

        Bird(Image img) {
            this.img = img;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }
    
    class Pipe {
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean passed = false;
        
        Pipe(Image img, int x, int y, int width, int height) {
            this.img = img;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (bird.img != null) {
            g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, this);
        }
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, this);
        }
        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        checkCollision();
        checkScore();
        repaint();
    }

    public void move() {
        // Apply gravity to bird's velocity
        velocityY += gravity;
        bird.y += velocityY;

        // Prevent the bird from moving out of the top of the screen
        bird.y = Math.max(bird.y, 0);

        // Prevent the bird from moving out of the bottom of the screen
        bird.y = Math.min(bird.y, boardHeight - birdHeight);
        
        // Move pipes
        for (Pipe pipe : pipes) {
            pipe.x += velocityX;
        }
    }

    public void placePipes() {
        int gap = 200; // Increase this value to increase the gap between pipes
        int topPipeHeight = (int)(Math.random() * (boardHeight - gap - 200)) + 100;
        int bottomPipeY = topPipeHeight + gap;

        Pipe topPipe = new Pipe(topPipeImage, boardWidth, topPipeHeight - pipeHeight, pipeWidth, pipeHeight);
        Pipe bottomPipe = new Pipe(bottomPipeImage, boardWidth, bottomPipeY, pipeWidth, pipeHeight);
        
        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }


    public void checkCollision() {
        for (Pipe pipe : pipes) {
            if (bird.getBounds().intersects(pipe.getBounds())) {
                // Handle collision
                gameOver();
            }
        }
    }

    public void checkScore() {
        for (Pipe pipe : pipes) {
            if (!pipe.passed && pipe.x + pipe.width < bird.x) {
                pipe.passed = true;
                score++;
                scoreLabel.setText("Score: " + score);
            }
        }
    }

    public void gameOver() {
        // Stop the game
        gameLoop.stop();
        placePipes.stop();
        JOptionPane.showMessageDialog(this, "you are done!. Your Score: " + score);
        new Home();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -10; // Make the bird jump up
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
}
