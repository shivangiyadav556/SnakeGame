import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
public class SnakeGame extends JPanel implements ActionListener,KeyListener{
    private class Tile{
        int x;
        int y;

        Tile(int x,int y){
            this.x=x;
            this.y=y;

        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize=25;
    
    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver=false;


    

    SnakeGame(int boardWidth,int boardHeight){
           this.boardWidth=boardWidth;
           this.boardHeight=boardHeight;
           setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
           setBackground(Color.black);
           addKeyListener(this);
           setFocusable(true);

           snakeHead=new Tile(5,5);
           snakeBody=new ArrayList<Tile>();

           food=new Tile(10,10);
           random=new Random();
           placeFood();

           velocityX=0;
           velocityY=0;

           gameLoop =new Timer(100, this);
           gameLoop.start();


    }
    public void paintComponent(Graphics g){
      super.paintComponent(g);
      draw(g);
    }

    public void draw(Graphics g){
      //grid
    //   for(int i=0;i<boardWidth/tileSize;i++){
    //     //(x1,y1,x2,y2)
    //   g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
    //   g.drawLine(0,i*tileSize, boardWidth,i*tileSize);
    // }
        
      //food
      g.setColor(Color.red);
      g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);
      g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);

      //snake Head
      g.setColor(Color.GREEN);
      int headX = snakeHead.x * tileSize;
      int headY = snakeHead.y * tileSize;
      int headWidth = tileSize;
      int headHeight = tileSize;

        // Draw snake head shape
        g.fillOval(headX, headY, headWidth, headHeight); // body of the head
        fillTriangle(g, headX + headWidth / 2, headY, headX + headWidth, headY + headHeight / 2, headX + headWidth / 2, headY + headHeight); // top of the head
        fillTriangle(g, headX, headY + headHeight / 2, headX + headWidth / 2, headY + headHeight, headX + headWidth, headY + headHeight / 2); // bottom of the head
         
        g.setColor(Color.GREEN);
    int earWidth = tileSize / 5;
    int earHeight = tileSize / 10;
    int earX = headX - earWidth;
    int earY = headY - earHeight;
    g.fillOval(earX, earY, earWidth, earHeight); // left ear
    g.fillOval(headX + headWidth - earWidth, earY, earWidth, earHeight); // right ear

    // Draw eyes
    g.setColor(Color.BLACK);
    int eyeSize = tileSize / 10;
    int eyeSpacing = tileSize / 10;
    int centerX = headX + headWidth / 2;
    int centerY = headY + headHeight / 2;
    g.fillOval(centerX - eyeSpacing, centerY - eyeSize / 2, eyeSize, eyeSize); // left eye
    g.fillOval(centerX + eyeSpacing, centerY - eyeSize / 2, eyeSize, eyeSize); // right eye

    // Draw tongue
    g.setColor(Color.RED);
    int tongueWidth = tileSize / 5;
    int tongueHeight = tileSize / 10;
    int tongueX = headX + headWidth / 2 - tongueWidth / 2;
    int tongueY = headY + headHeight - tongueHeight;
    g.fillRect(tongueX, tongueY, tongueWidth, tongueHeight);

    
      
    //snake Body
    g.setColor(Color.GREEN);
    for(int i=0;i<snakeBody.size();i++){
      Tile snakePart=snakeBody.get(i);
      g.fillOval(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize);
    }
    //Score
    g.setFont(new Font("Arial",Font.PLAIN,16));
    if(gameOver){
        g.setColor(Color.red);
        g.drawString("Game Over: " +  String.valueOf(snakeBody.size()),tileSize-16,tileSize);
    }else{
        g.drawString("Score: " + String.valueOf(snakeBody.size()),tileSize-16,tileSize);
    }

    }
    private void fillTriangle(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3) {
        int[] xPoints = {x1, x2, x3};
        int[] yPoints = {y1, y2, y3};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public void placeFood(){
        food.x=random.nextInt(boardWidth/tileSize);
        food.y=random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        // here we are cecking if two tiles are colliding
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }

    public void move(){
       // eat the food
       if(collision(snakeHead,food)){
        snakeBody.add(new Tile(food.x,food.y));
        placeFood();
       }

       //Move snake body
       for(int i=snakeBody.size()-1;i>=0;i--){
         Tile snakePart=snakeBody.get(i);
         //first member of snake body i==0
         if(i==0){
            snakePart.x=snakeHead.x;
            snakePart.y=snakeHead.y;
         }else{
            Tile prevSnakePart=snakeBody.get(i-1);
            snakePart.x=prevSnakePart.x;
            snakePart.y=prevSnakePart.y;
         }
       }

       //To move sanke
       snakeHead.x+=velocityX;
       snakeHead.y+=velocityY;

       //game over conditions
       for(int i=0;i<snakeBody.size();i++){
         Tile snakePart=snakeBody.get(i);
         //collide with the snake head
         if(collision(snakeHead, snakePart)){
           gameOver=true;
         }
       }
      if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize>boardWidth ||
       snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHeight){
         gameOver=true;
      }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
      //this method will call draw over and over again
      //it will draw frame over and over in every 100 mls
      move(); 
      repaint();
      //if snake body touches then game over
      if(gameOver){
         gameLoop.stop();
      }
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //setting the key presses
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1){
            velocityX=0;
            velocityY=-1;
        }else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1){
            velocityX=0;
            velocityY=1;
        }else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
        }
    
}
