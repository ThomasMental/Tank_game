
import java.awt.*;
import java.util.List;

public class Bullet {
    // tank client
    private TankClient tc;

    // speed
    private int x_speed = 15;
    private int y_speed = 15;

    // from me or enemy
    private boolean good;

    // exist
    private boolean live = true;

    // location
    private int x;
    private int y;

    // size of bullet
    private int WIDTH = 10;
    private int HEIGHT = 10;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public boolean isGood() {
        return good;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    private Tank.DIR dir;

    public Bullet(int x, int y, TankClient tc, Tank.DIR dir, boolean good) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.good = good;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        if(this.good)
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.GRAY);
        g.fillRect(x,y,WIDTH,HEIGHT);
        g.setColor(c);

        move();
    }

    private void move() {
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
        if(x<0||x>TankClient.GAME_WIDTH||y<0||y>TankClient.GAME_HEIGHT){
            live = false;
        }
    }

    public Rectangle getArea(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean isHit(Tank tank) {
        if(!tank.isLive()) {
            return false;
        }
        else if(this.getArea().intersects(tank.getArea()) && this.good != tank.isGood()) {
            this.live = false;
            if(!tank.isGood()) {
                tank.setLive(false);
                tc.setScore(tc.getScore() + 1);
            }
            else {
                tank.setLifes(tank.getLifes() - 1);
            }
                if(tank.getLifes()==0)
                tank.setLive(false);
                return true;
        }
        else {
            return false;
        }
    }

    public boolean HitTanks(List<Tank> tankList) {
        for (Tank a : tankList) {
            if (isHit(a)) {
                return true;
            }
        }
        return false;
    }

}
