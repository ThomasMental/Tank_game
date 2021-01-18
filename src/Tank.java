import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Tank {

    private TankClient tc;

    private String ImagePath = System.getProperty("user.dir");
    // enemy or me
    private boolean good;

    private boolean live = true;

    private int lifes;

    private int good_life = 5;

    // location
    private int x;
    private int y;

    // size of the tank
    private int WIDTH = 50;
    private int HEIGHT = 50;
    //moving direction
    private DIR dir = DIR.STOP;

    //tank direction
    private DIR tkdir = DIR.U;

    public enum DIR {
        L, R, U, D, STOP
    }

    private boolean BL = false;
    private boolean BU = false;
    private boolean BR = false;
    private boolean BD = false;

    private Random r = new Random();
    private int steps = r.nextInt(15)+5;

    public Tank(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public Tank(int x, int y, TankClient tc) {
        this(x,y);
        this.tc = tc;
    }

    public Tank(int x, int y, TankClient tc, boolean good) {
        this.x = x;
        this.y = y;
        this.tc = tc;
        this.good = good;

        if(good) this.lifes = good_life;
        else this.lifes = 1;
    }

    public Tank(int x, int y, TankClient tc, boolean good, DIR dir) {
        this.x = x;
        this.y = y;
        this.tc = tc;
        this.good = good;
        this.dir = dir;
        this.tkdir = dir;

        if(good) this.lifes = good_life;
        else this.lifes = 1;
    }

    public boolean isGood() {
        return good;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public DIR getTkdir() {
        return tkdir;
    }

    public void setDir(DIR dir) {
        this.dir = dir;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void PaintTank(Graphics g) {
        if(!this.isLive())
            return;

        if(!this.isGood())
            if(steps == 0) {
                steps = r.nextInt(9) + 3;
                DIR[] dirs = DIR.values();
                int rn = r.nextInt(dirs.length-1);
                this.dir = dirs[rn];
                this.tkdir = this.dir;
                tc.addBullet(fire());

            }

        BufferedImage image = null;
        try {
            switch (tkdir) {
                case U:
                    if(good)
                        image = ImageIO.read(new File(ImagePath+"/tank_good_U.jpg"));
                    else
                        image = ImageIO.read(new File(ImagePath+"/tank_bad_U.jpg"));
                    break;
                case D:
                    if(good)
                        image = ImageIO.read(new File(ImagePath+"/tank_good_D.jpg"));
                    else
                        image = ImageIO.read(new File(ImagePath+"/tank_bad_D.jpg"));
                    break;
                case L:
                    if(good)
                        image = ImageIO.read(new File(ImagePath+"/tank_good_L.jpg"));
                    else
                        image = ImageIO.read(new File(ImagePath+"/tank_bad_L.jpg"));
                    break;
                case R:
                    if(good)
                        image = ImageIO.read(new File(ImagePath+"/tank_good_R.jpg"));
                    else
                        image = ImageIO.read(new File(ImagePath+"/tank_bad_R.jpg"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(image,x,y,WIDTH,HEIGHT,null);
        move();

        steps--;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // key event
        switch (key) {
            case KeyEvent.VK_UP:
                BU = false;
                break;
            case KeyEvent.VK_DOWN:
                BD = false;
                break;
            case KeyEvent.VK_LEFT:
                BL = false;
                break;
            case KeyEvent.VK_RIGHT:
                BR = false;
                break;
            case 17:
                break;
        }
        movedir();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // key event
        switch (key) {
            case 17:
                if(this.isLive())
                tc.addBullet(fire());
                break;
            case KeyEvent.VK_UP:
                BU = true;
                break;
            case KeyEvent.VK_DOWN:
                BD = true;
                break;
            case KeyEvent.VK_LEFT:
                BL = true;
                break;
            case KeyEvent.VK_RIGHT:
                BR = true;
                break;
            case KeyEvent.VK_A:

                break;
        }
        movedir();
    }

    private void movedir() {
        if(BU && !BR && !BL && !BD) {
            dir = DIR.U;
        }
        else if(!BU && BR && !BL && !BD) {
            dir = DIR.R;
        }
        else if(!BU && !BR && BL && !BD) {
            dir = DIR.L;
        }
        else if(!BU && !BR && !BL && BD) {
            dir = DIR.D;
        }
        else {
            dir = DIR.STOP;
        }

        if(dir != DIR.STOP)
            tkdir = dir;
    }

    private void move() {
        // speed
        int x_speed = 10;
        int y_speed = 10;

        // good tank move
        switch(dir) {
            case U:
                y -= y_speed;
                break;
            case D:
                y += y_speed;
                break;
            case L:
                x -= x_speed;
                break;
            case R:
                x += x_speed;
                break;
        }
        dealTankBorder();
    }

    public Bullet fire(){
        int x = this.x +(this.WIDTH)/2 - 5;
        int y = this.y + (this.HEIGHT)/2 - 5;
        Bullet bullet = new Bullet(x, y, this.tc, this.tkdir, this.good);
        return bullet;
    }

    // deal the border
    private void dealTankBorder() {
        if(x<0){
            x = 0;
        }
        else if(x > TankClient.GAME_WIDTH - this.WIDTH){
            x = TankClient.GAME_WIDTH - this.WIDTH ;
        }
        if(y<0){
            y = 0;
        }
        else if(y > TankClient.GAME_WIDTH - this.HEIGHT){
            y = TankClient.GAME_WIDTH - this.HEIGHT;
        }
    }

    public Rectangle getArea(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isLive() {
        return live;
    }

}
