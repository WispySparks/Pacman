package Pacman;

import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Blinky {

    private final Map map;
    private final Pacman pacman;
    private final Animator animator = new Animator("clyde");
    private int xPos = 13 * 16;
    private int yPos = 27 * 8;
    private int direction = Constants.left;
    private int nextDirection = Constants.left;
    private int speed = Constants.baseSpeed;
    private int count = 0;
    private Rectangle hitbox = new Rectangle(xPos+4, yPos+4, 24, 24);
    private int ghostState = Constants.scatter; // 0 = chase, 1 = scatter, 2 = frightened, 3 = eaten
    private Random rand = new Random();
    private boolean eaten = false;
    
    Blinky(Pacman pacman, Map map) {
        this.pacman = pacman;
        this.map = map;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public boolean isEaten() {
        return eaten;
    }

    public int getState() {
        return ghostState;
    }

    public void setState(int state) {
        if (state < 2) {
            speed = Constants.baseSpeed;
        }
        ghostState = state;
    }

    public BufferedImage updateAnim() {
        if (ghostState < 2) {
            return animator.normal();
        }
        else if (ghostState == Constants.frighten) {
            return animator.frighten();
        }
        else {
            return animator.eyes(direction);
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void getMove() {
        if (speed == Constants.baseSpeed) {
            move();
        }
        else if (speed == 4 && count % 2 == 0) {
            count++;
            move();
        }
        else if (speed == 4) {
            count++;
        }
        else {
            move();
            move();
        }
    }

    public void move() {
        nextDirection = getNextDirection();
        if (map.checkTps(hitbox) == 1) {
            xPos = 28*16;
            nextDirection = direction = Constants.left;
        }
        else if (map.checkTps(hitbox) == 2) {
            xPos = -2*16;
            nextDirection = direction = Constants.right;
        }
        if (map.checkWallCollision(nextDirection, xPos, yPos) == false) {
            switch (nextDirection) {
                case Constants.right: 
                    xPos += 8;
                    direction = nextDirection;
                    break;
                case Constants.down:
                    yPos += 8;
                    direction = nextDirection;
                    break;
                case Constants.left:
                    xPos -= 8;
                    direction = nextDirection;
                    break;
                case Constants.up:
                    yPos -= 8;
                    direction = nextDirection;
                    break;
            }
        }
        else if (map.checkWallCollision(direction, xPos, yPos) == false) {
            switch (direction) {
                case Constants.right: 
                    xPos += 8;
                    break;
                case Constants.down:
                    yPos += 8;
                    break;
                case Constants.left:
                    xPos -= 8;
                    break;
                case Constants.up:
                    yPos -= 8;
                    break;
            }
        }
        hitbox.x = getX()+4;
        hitbox.y = getY()+4;
    }

    public int getNextDirection() {   // get next direction based on ai and math to go to target tile
        int x1;
        int y1;
        if (ghostState == Constants.chase) {  // chase mode
            x1 = pacman.getX();
            y1 = pacman.getY();
        }
        else if (ghostState == Constants.scatter) {  // scatter mode
            x1 = 26*16;     // corner cordinates
            y1 = 4*16;
        }
        else if (ghostState == Constants.frighten) {  // frighten mode
            speed = Constants.baseSpeed/2;
            return randDirection();
        }
        else {
            speed = Constants.baseSpeed*2;
            x1 = 14*16;     // ghost house cordinates
            y1 = 16*16;
            eaten = true;
            ghostHouse();
        }
        int x2 = getX();
        int y2 = getY();
        int ablr = Math.abs(y2 - y1);
        int bcdu = Math.abs(x2 - x1);
        int bcright = Math.abs((x2+8) - x1);
        int abdown = Math.abs((y2+8) - y1);
        int bcleft = Math.abs((x2-8) - x1);
        int abup = Math.abs((y2-8) - y1);
        double rightdistance = Math.hypot(bcright, ablr);
        double downdistance = Math.hypot(bcdu, abdown);
        double leftdistance = Math.hypot(bcleft, ablr);
        double updistance = Math.hypot(bcdu, abup);
        if (map.checkWallCollision(0, xPos, yPos) || direction == 2) {
            rightdistance += 999;
        }
        if (map.checkWallCollision(1, xPos, yPos) || direction == 3) {
            downdistance += 999;
        }
        if (map.checkWallCollision(2, xPos, yPos) || direction == 0) {
            leftdistance += 999;
        }
        if (map.checkWallCollision(3, xPos, yPos) || direction == 1) {
            updistance += 999;
        }
        if (rightdistance == leftdistance) {
            leftdistance +=1;
        }
        if (updistance == downdistance) {
            updistance +=1;
        }
        updistance -= 1;
        downdistance -= 1;
        // System.out.println(rightdistance + " " + downdistance + " " + leftdistance + " " + updistance);
        return compare(rightdistance, downdistance, leftdistance, updistance);
    }

    public int compare(double right, double down, double left, double up) {     // compare all directions and find best one
        if (right < down && right < left && right < up) {
            return Constants.right;
        }
        else if (down < right && down < left && down < up) {
            return Constants.down;
        }
        else if (left < right && left < down && left < up) {
            return Constants.left;
        }
        else if (up < right && up < left && up < down) {
            return Constants.up;
        }
        return 5;
    }

    public int randDirection() {   // get a new random direction
        int x = rand.nextInt(4);
        if (x == Constants.right && direction == Constants.left) {
            return randDirection();
        }
        else if (x == Constants.down && direction == Constants.up) {
            return randDirection();
        }
        else if (x == Constants.left && direction == Constants.right) {
            return randDirection();
        }
        else if (x == Constants.up && direction == Constants.down) {
            return randDirection();
        }
        else if (map.checkWallCollision(x, xPos, yPos)) {
            return randDirection();
        }
        return x;
    }

    public void turnAround() {   // make ghost turn around 
        if (direction == Constants.right) {
            direction = Constants.left;
        }
        else if (direction == Constants.down) {
            direction = Constants.up;
        }
        else if (direction == Constants.left) {
            direction = Constants.right;
        }
        else {
            direction = Constants.down;
        }
    }

    public void ghostHouse() {   // travel to ghost house
        int x1 = 13*16;     // ghost house cordinates 224 256
        int y1 = 13*16 + 8;
        if (getX() == x1 && getY() == y1) {
            eaten = false;
            ghostState = Constants.scatter;
            speed = Constants.baseSpeed;
        }
    }

}
