package Pacman;

import java.util.Random;
import Pacman.Math.Vector2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Inky implements Ghost {

    private final Map map;
    private final Pacman pacman;
    private final Animator animator = new Animator("inky");
    private final GameController controller;
    private final Blinky blinky;
    private int xPos = 11*16;
    private int yPos = 33 * 8;
    private int direction = Constants.right;
    private int nextDirection = Constants.right;
    private int speed = Constants.baseSpeed;
    private Rectangle hitbox = new Rectangle(xPos+4, yPos+4, 24, 24);
    private int ghostState = Constants.eaten;
    private Random rand = new Random();
    private boolean eaten = false;
    private boolean house = true;    // used for beginning of game to leave ghost house
    private boolean enter = false;   // whether ghost is entering or exiting ghost house
    public int x1;  // target x
    public int y1;  // target y
    
    Inky(Pacman pacman, Blinky blinky, Map map, GameController controller) {
        this.pacman = pacman;
        this.blinky = blinky;
        this.map = map;
        this.controller = controller;
    }

    public void start(boolean start) {
        if (start == true) {
            house = false;
            eaten = true;
        }
        else {
            xPos = 11*16;
            yPos = 33 * 8;
            direction = Constants.right;
            nextDirection = Constants.right;
            speed = Constants.baseSpeed;
            hitbox.x = getX()+4;
            hitbox.y = getY()+4;
            ghostState = Constants.eaten;
            eaten = false;
            house = true;
            enter = false;
        }
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
        if (state != ghostState && state != Constants.eaten) {
            turnAround();
        }
        ghostState = state;
    }

    public BufferedImage updateAnim() {
        return animator.getAnim(direction, ghostState, enter);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void getMove() {
        if (house == false) {
            if (eaten == true && enter == true) {
                move();
                move();
                move();
            }
            else {
                move();
            }
        }
    }

    public void targetTile() {
        int dir;
        int x3 = blinky.getX();
        int y3 = blinky.getY();
        x1 = pacman.getX();
        y1 = pacman.getY();
        dir = pacman.getDirection();
        switch (dir) {
            case Constants.right: x1 += 40; break;
            case Constants.down: y1 += 40; break;
            case Constants.left: x1 -= 24; break;
            case Constants.up: y1 -= 24; break;
        }
        Vector2D vector = new Vector2D(x1, y1, x3, y3);
        vector = vector.reflection(180);
        x1 += vector.getX();
        y1 += vector.getY();
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
        if (map.checkWallCollision(nextDirection, xPos, yPos, speed, !eaten) == false) {
            switch (nextDirection) {
                case Constants.right: 
                    xPos += speed;
                    direction = nextDirection;
                    break;
                case Constants.down:
                    yPos += speed;
                    direction = nextDirection;
                    break;
                case Constants.left:
                    xPos -= speed;
                    direction = nextDirection;
                    break;
                case Constants.up:
                    yPos -= speed;
                    direction = nextDirection;
                    break;
            }
        }
        else if (map.checkWallCollision(direction, xPos, yPos, speed, !eaten) == false) {
            switch (direction) {
                case Constants.right: 
                    xPos += speed;
                    break;
                case Constants.down:
                    yPos += speed;
                    break;
                case Constants.left:
                    xPos -= speed;
                    break;
                case Constants.up:
                    yPos -= speed;
                    break;
            }
        }
        hitbox.x = getX()+4;
        hitbox.y = getY()+4;
    }

    public int getNextDirection() {   // get next direction based on ai and math to go to target tile
        if (ghostState == Constants.chase) {  // chase mode
            targetTile();
        }
        else if (ghostState == Constants.scatter) {  // scatter mode
            x1 = 27*16;     // corner cordinates
            y1 = 33*16;
        }
        else if (ghostState == Constants.frighten) {  // frighten mode
            speed = Constants.baseSpeed/2;
            return randDirection();
        }
        else if (enter == true) {
            speed = Constants.baseSpeed/2;
            x1 = 13*16;     // ghost house cordinates
            y1 = 16*16+8;
            eaten = true;
            enterHouse();
        }
        else {
            x1 = 13*16;     // outside ghost house
            y1 = 13*16+8;
            exitHouse();
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
        if (map.checkWallCollision(0, xPos, yPos, speed, !eaten) || direction == 2) {
            rightdistance += 999;
        }
        if (map.checkWallCollision(1, xPos, yPos, speed, !eaten) || direction == 3) {
            downdistance += 999;
        }
        if (map.checkWallCollision(2, xPos, yPos, speed, !eaten) || direction == 0) {
            leftdistance += 999;
        }
        if (map.checkWallCollision(3, xPos, yPos, speed, !eaten) || direction == 1) {
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
        return compare(rightdistance, downdistance, leftdistance, updistance);
    }

    public int compare(double right, double down, double left, double up) {     // compare all directions and find best one
        double lrmin = Math.min(right, left);
        double udmin = Math.min(up, down);
        double min = Math.min(lrmin, udmin);
        if (min == right) {
            return Constants.right;
        }
        else if (min == down) {
            return Constants.down;
        }
        else if (min == left) {
            return Constants.left;
        }
        else {
            return Constants.up;
        }
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
        else if (map.checkWallCollision(x, xPos, yPos, speed, true)) {
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

    public void reAlign() {     // realign ghost after frightened mode
        if (xPos % 8 != 0) {
            if (!map.checkWallCollision(Constants.right, xPos, yPos, 4, true)) {
                xPos += 4;
            }
            else {
                xPos -= 4;
            }
        }
        if (yPos % 8 != 0) {
            if (!map.checkWallCollision(Constants.down, xPos, yPos, 4, true)) {
                yPos += 4;
            }
            else {
                yPos -= 4;
            }
        }
    }

    public void enterHouse() {   // travel to ghost house
        int x1 = 13*16;     // ghost house cordinates 224 256
        int y1 = 16*16+8;
        if (getX() == x1 && getY() == y1) {
            nextDirection = direction = Constants.up;
            enter = false;
            speed = Constants.baseSpeed;
        }
    }

    public void exitHouse() {    // exit ghost house
        int x1 = 13*16;     
        int y1 = 13*16+8;
        if (getX() == x1 && getY() == y1) {
            eaten = false;
            ghostState = controller.gameState();;
            enter = true;
        }
    }

}