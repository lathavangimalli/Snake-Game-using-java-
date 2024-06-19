import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    public class Tile {
        int x;
        int y;

        Tile(int x, int y){
        this.x = x;
        this.y = y;
        }

        
        
    }
    int boardwidth;
    int boardheight;
    int tilesize = 25;
    //snake
    Tile snakehead;
    ArrayList<Tile> snakeBody;
    //food
    Tile food;
    Random random;
    //game loop
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameover = false;

    SnakeGame(int boardwidth, int boardheight){
        this.boardwidth = boardwidth;
        this.boardheight = boardheight;
        setPreferredSize(new Dimension(this.boardwidth, this.boardheight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakehead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();
        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this );
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //grid
       // for(int i = 0;i < boardwidth/tilesize; i++){
        //    g.drawLine(i*tilesize,0,i*tilesize, boardheight);
         //   g.drawLine(0,i*tilesize,boardwidth,i*tilesize);
       // }

        //food
        g.setColor(Color.red);
       // g.fillRect(food.x*tilesize, food.y*tilesize, tilesize, tilesize);
        g.fill3DRect(food.x*tilesize, food.y*tilesize, tilesize, tilesize,true);

        //snake head
        g.setColor(Color.green);
       // g.fillRect(snakehead.x*tilesize, snakehead.y*tilesize, tilesize, tilesize);
        g.fill3DRect(snakehead.x*tilesize, snakehead.y*tilesize, tilesize, tilesize,true);


        //snakebody
        for(int i = 0; i < snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x*tilesize, snakePart.y*tilesize, tilesize, tilesize);
            g.fill3DRect(snakePart.x*tilesize, snakePart.y*tilesize, tilesize, tilesize,true);

        }

        //score
        g.setFont(new Font("Arial",Font.PLAIN, 16));
        if(gameover){
            g.setColor(Color.red);
            g.drawString("Game Over:"+ String.valueOf(snakeBody.size()),tilesize - 16, tilesize);
        }
        else {
            g.drawString("Score:" + String.valueOf(snakeBody.size()), tilesize - 16, tilesize);
        }

    }

    public void placeFood(){
        food.x = random.nextInt(boardwidth/tilesize);
        food.y = random.nextInt(boardheight/tilesize);
    }
    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move(){
        //eat food
        if(collision(snakehead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for(int i = snakeBody.size()-1; i>=0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakehead.x;
                snakePart.y = snakehead.y;

            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
            
        }


        //snakehead
        snakehead.x += velocityX;
        snakehead.y += velocityY;

        //gameover condition
        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //collide with snakehead
            if(collision(snakehead, snakePart)){
                gameover = true;
            }
        }
        if(snakehead.x*tilesize < 0 || snakehead.x*tilesize > boardwidth ||
        snakehead.y*tilesize < 0 || snakehead.y*tilesize > boardheight){
            gameover = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();  
        if(gameover){
            gameLoop.stop();
        } 
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;

        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX !=1){
            velocityX = -1;
            velocityY = 0;

        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT  && velocityX !=-1){
            velocityX = 1;
            velocityY = 0;
        }
    }

//donot need
    @Override
    public void keyTyped(KeyEvent e) {}

   
    @Override
    public void keyReleased(KeyEvent e) {} 
}
