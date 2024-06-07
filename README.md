Welcome to the Snake Game project! This is a classic Snake game implemented in Java using the Swing framework. Players control a snake to collect food, growing in length with each piece of food collected. The game ends if the snake runs into the walls or itself.

Table of Contents
Project Overview
Getting Started
How to Play
Project Structure
Code Overview
Configuration
Contributing
License
Project Overview
Project Name: Snake Game

Description: A classic Snake game built with Java and Swing. Players control the snake using keyboard arrows, guiding it to collect food while avoiding collisions with the walls or the snake's own body.

Getting Started
Prerequisites
Java Development Kit (JDK) installed.
Visual Studio Code with Java extensions or any other Java IDE.
Installation
Clone the repository:
sh
Copy code
git clone <repository-url>
Open the project in Visual Studio Code:
sh
Copy code
code Snake
Compile the project:
Open the terminal in Visual Studio Code and run:
sh
Copy code
javac -d bin src/*.java
Running the Project
Run the game:
In the terminal, navigate to the bin directory and run:
sh
Copy code
java App
How to Play
Use the arrow keys to control the direction of the snake.
The goal is to eat the red food squares, which causes the snake to grow.
Avoid colliding with the walls or the snake's own body.
The game ends when the snake collides with the wall or itself, displaying the score.
Project Structure
Workspace Folders:

src: Contains the Java source files.
bin: Contains the compiled class files.
.vscode: Contains configuration files for Visual Studio Code.
Key Files:

src/App.java: The main entry point for the application.
src/SnakeGame.java: Contains the logic for the Snake game.
.vscode/settings.json: Configuration for the project in Visual Studio Code.
Code Overview
App.java
java
Copy code
import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
SnakeGame.java
java
Copy code
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth, boardHeight, tileSize = 25;
    Tile snakeHead, food;
    ArrayList<Tile> snakeBody;
    Random random;
    Timer gameLoop;
    int velocityX, velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        g.setColor(Color.GREEN);
        g.fillOval(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        for (Tile snakePart : snakeBody) {
            g.fillOval(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + snakeBody.size(), tileSize - 16, tileSize);
        } else {
            g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (key == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (key == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (key == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }




