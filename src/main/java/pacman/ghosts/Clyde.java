package pacman.ghosts;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import pacman.Animator;
import pacman.Constants;
import pacman.GameController;
import pacman.Map;
import pacman.Pacman;

public class Clyde implements Ghost {

    private final Map map;
    private final Pacman pacman;
    private final Animator animator = new Animator("clyde");
    private final GameController controller;
    private final Random rand = new Random();
    private int xPos = 15*16;
    private int yPos = 33 * 8;
    private int direction = Constants.left;
    private int nextDirection = Constants.left;
    private int speed = Constants.baseSpeed;
    private Rectangle hitbox = new Rectangle(xPos+4, yPos+4, 24, 24);
    private int ghostState = Constants.eaten;
    private boolean eaten = false;
    private boolean house = true;    // used for beginning of game to leave ghost house
    private boolean enter = false;   // whether ghost is entering or exiting ghost house
    private boolean scaredout = true;
    private int x1;  // target x
    private int y1;  // target y
    
    public Clyde(Pacman pacman, Map map, GameController controller) {
        this.pacman = pacman;
        this.map = map;
        this.controller = controller;
    }

    @Override
    public void start(boolean start) {
        if (start == true) {
            house = false;
            eaten = true;
        }
        else {
            xPos = 15*16;
            yPos = 33 * 8;
            direction = Constants.left;
            nextDirection = Constants.left;
            speed = Constants.baseSpeed;
            hitbox.x = getX()+4;
            hitbox.y = getY()+4;
            ghostState = Constants.eaten;
            eaten = false;
            house = true;
            enter = false;
            scaredout = true;
        }
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    @Override
    public boolean isEaten() {
        return eaten;
    }

    @Override
    public int getState() {
        return ghostState;
    }

    @Override
    public void setState(int state) {
        if (state < 2) {
            speed = Constants.baseSpeed;
        }
        if (state != ghostState && state != Constants.eaten) {
            turnAround();
        }
        ghostState = state;
    }

    @Override
    public BufferedImage updateAnim() {
        return animator.getAnim(direction, ghostState, enter);
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
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
        double dist;
        x1 = pacman.getX();
        y1 = pacman.getY();
        int x2 = getX();
        int y2 = getY();
        int ablr = Math.abs(y2 - y1);
        int bcdu = Math.abs(x2 - x1);
        dist = Math.hypot(bcdu, ablr);
        if (dist <= 128) {
            x1 = 1*16;
            y1 = 33*16;
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
            x1 = 1*16;     // corner cordinates
            y1 = 33*16;
        }
        else if (ghostState == Constants.frighten) {  // frighten mode
            speed = Constants.baseSpeed/2;
            return randDirection();
        }
        else if (enter == true) {
            controller.getAudio().loopPowerPellet(false);
            controller.getAudio().loopEyes(true);
            speed = Constants.baseSpeed/2;
            x1 = 13*16;     // ghost house cordinates
            y1 = 16*16+8;
            eaten = true;
            enterHouse();
        }
        else {
            x1 = 13*16;     // outside ghost house
            y1 = 13*16+8;
            speed = Constants.baseSpeed/2;
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

    @Override
    public void turnAround() {   
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

    @Override
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
            controller.getAudio().loopEyes(false);
            if (controller.power() == true) {
                controller.getAudio().loopPowerPellet(true);
            }
        }
    }

    public void exitHouse() {    // exit ghost house
        int x1 = 13*16;     
        int y1 = 13*16+8;
        if (getX() == x1 && getY() == y1) {
            eaten = false;
            if (scaredout == true && controller.power() == true) {
                ghostState = Constants.frighten;
                scaredout = false;
            }
            else {
                scaredout = false;
                ghostState = controller.gameState();
            }
            enter = true;
            speed = Constants.baseSpeed;
        }
    }

}