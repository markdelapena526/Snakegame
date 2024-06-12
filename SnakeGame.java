import java.awt.*; // Imports AWT classes for GUI components and graphics
import java.awt.event.*; // Imports event handling classes
import java.util.ArrayList; // Imports ArrayList class for dynamic arrays
import java.util.Random; // Imports Random class for random number generation
import javax.swing.*; // Imports Swing classes for creating GUI


public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    // This inner class represents a tile on the board
    private class Tile {
        int x; // X-coordinate of the tile
        int y; // Y-coordinate of the tile

        Tile(int x, int y) {
            this.x = x; // Initialize the x-coordinate
            this.y = y; // Initialize the y-coordinate
        }
    }

    // Instance variables for the game
    int boardWidth; // Width of the game board
    int boardHeight; // Height of the game board
    int tileSize = 25; // Size of each tile in pixels

    // Snake-related variables
    Tile snakeHead; // Represents the head of the snake
    ArrayList<Tile> snakeBody; // Represents the body of the snake

    // Food-related variables
    Tile food; // Represents the food tile
    Random random; // Random object to place food randomly

    // Game logic variables
    int velocityX; // Snake's movement in the x direction
    int velocityY; // Snake's movement in the y direction
    Timer gameLoop; // Timer to control game updates

    boolean gameOver = false; // Flag to indicate if the game is over

    // Constructor to initialize the game
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth; // Set the board width
        this.boardHeight = boardHeight; // Set the board height
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight)); // Set the preferred size of the panel
        setBackground(Color.black); // Set the background color to black
        addKeyListener(this); // Add the class as a key listener
        setFocusable(true); // Make the panel focusable to receive key events

        snakeHead = new Tile(5, 5); // Initialize the snake head at position (5, 5)
        snakeBody = new ArrayList<Tile>(); // Initialize the snake body as an empty list

        food = new Tile(10, 10); // Initialize the food tile
        random = new Random(); // Create a random object
        placeFood(); // Place the food at a random position

        velocityX = 1; // Set the initial movement direction to right
        velocityY = 0; // No initial vertical movement

        gameLoop = new Timer(100, this); // Initialize the game timer with a 100ms delay
        gameLoop.start(); // Start the game timer
    }

    // Overridden method to handle custom painting
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass's method
        draw(g); // Draw the game elements
    }

    // Method to draw the game elements
    public void draw(Graphics g) {
        // Draw the grid lines
        for(int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight); // Vertical grid lines
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); // Horizontal grid lines
        }

        // Draw the food
        g.setColor(Color.red); // Set color to red
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true); // Draw the food tile

        // Draw the snake head
        g.setColor(Color.green); // Set color to green
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true); // Draw the snake head

        // Draw the snake body
        for (Tile snakePart : snakeBody) {
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true); // Draw each part of the snake body
        }

        // Draw the score or game over message
        g.setFont(new Font("Arial", Font.PLAIN, 16)); // Set the font for text
        if (gameOver) {
            g.setColor(Color.red); // Set color to red for game over message
            g.drawString("Game Over: " + snakeBody.size(), tileSize - 16, tileSize); // Draw game over message
        } else {
            g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize); // Draw the score
        }
    }

    // Method to place the food at a random position
    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize); // Random x-coordinate within the board
        food.y = random.nextInt(boardHeight/tileSize); // Random y-coordinate within the board
    }

    // Method to move the snake
    public void move() {
        // Check if the snake head collides with the food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y)); // Add a new tile to the snake body
            placeFood(); // Place new food
        }

        // Move the snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x; // Move the first body part to the previous head position
                snakePart.y = snakeHead.y; // Move the first body part to the previous head position
            } else {
                Tile prevSnakePart = snakeBody.get(i-1); // Get the previous body part
                snakePart.x = prevSnakePart.x; // Move current body part to the previous body part's position
                snakePart.y = prevSnakePart.y; // Move current body part to the previous body part's position
            }
        }

        // Move the snake head
        snakeHead.x += velocityX; // Update the head's x-coordinate
        snakeHead.y += velocityY; // Update the head's y-coordinate

        // Check for collision with the snake's body
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true; // Set game over flag if collision occurs
            }
        }

        // Check for collision with the board boundaries
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth/tileSize || // Left or right boundary
            snakeHead.y < 0 || snakeHead.y >= boardHeight/tileSize) { // Top or bottom boundary
            gameOver = true; // Set game over flag if collision occurs
        }
    }

    // Method to check for collision between two tiles
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y; // Return true if both tiles have the same coordinates
    }

    // Method called by the timer every 100 milliseconds
    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // Update the game state
        repaint(); // Repaint the panel
        if (gameOver) {
            gameLoop.stop(); // Stop the game loop if the game is over
        }
    }

    // Method to handle key presses
    @Override
    public void keyPressed(KeyEvent e) {
        // Change the snake's direction based on the key pressed
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0; // No horizontal movement
            velocityY = -1; // Move up
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0; // No horizontal movement
            velocityY = 1; // Move down
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1; // Move left
            velocityY = 0; // No vertical movement
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1; // Move right
            velocityY = 0; // No vertical movement
        }
    }

    // Required by KeyListener but not used
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
