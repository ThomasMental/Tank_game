import com.sun.javafx.scene.traversal.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;

public class TankClient extends JFrame {

    private Random r = new Random();

    // score
    private int score = 0;

    public static final int GAME_HEIGHT = 600;
    public static final int GAME_WIDTH = 600;

    // good tank
    private Tank tank = new Tank(500,500, this,true);

    private List<Tank> me = new ArrayList<Tank>();

    // lots of enemies
    private List<Tank> enemies = new ArrayList<Tank>();

    private List<Bullet> bullets = new ArrayList<Bullet>();

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public static void main(String[] args) {
        new TankClient().LaunchGame();
    }

    // set the window
    private void LaunchGame() {
        this.setTitle("Tank Game"); // set the title
        this.setLocation(200,200); // set the windows location
        this.setSize(GAME_WIDTH,GAME_HEIGHT); // set the size of the window
        this.setBackground(Color.WHITE); // set the background white

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setResizable(false); // not allow set the size of the window
        this.setVisible(true);

        new Thread(new MyRepaint()).start();
        this.addKeyListener(new KeyMonitor());

    }

    // update to prevent the flickering
    private Image offScreenImage =  null;
    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics goffScreen = offScreenImage.getGraphics();
        Color c = goffScreen.getColor();
        goffScreen.setColor(Color.WHITE);
        goffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        goffScreen.setColor(c);

        paintComponent(goffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    // draw all the objects
    private void paintComponent(Graphics g) {

        g.drawString("Score: " + score, 30, 50);
        g.drawString("Lives: " + tank.getLifes(), 30,70);

        // draw good tank
        tank.PaintTank(g);

        // add new tanks
        if(enemies.size()==0)
            produceTank();

        // draw bad tanks
        for(int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if(enemy.isLive()) {
                enemy.PaintTank(g);
            }
            else {
                enemies.remove(enemy);
            }
        }

        // draw bullets
        for(int i = 0; i < bullets.size(); i++) {
            if(!bullets.get(i).isLive()) {
                bullets.remove(bullets.get(i));
            }
            else{
                bullets.get(i).HitTanks(enemies);
                bullets.get(i).isHit(tank);
                bullets.get(i).draw(g);
            }
        }

    }


    // repaint every 50 ms
    private class MyRepaint implements Runnable {
        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // use keys to move
    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            tank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            tank.keyReleased(e);
        }
    }

    private void produceTank(){

        int totalNum =r.nextInt(5) + 2;

        for(int i=0;i<totalNum;i++){
            // random position
            int x = (r.nextInt(10)+1)*40;
            int y = (r.nextInt(10)+1)*30;

            // random direction
            Tank.DIR[] dirs = Tank.DIR.values();
            int rn = r.nextInt(dirs.length-1);
            Tank.DIR dir = dirs[rn];
            Tank enemy = new Tank(x,y,this,false,dir);

            enemies.add(enemy);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
