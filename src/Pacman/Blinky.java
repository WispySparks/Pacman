package Pacman;

import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.event.*;

public class Blinky implements ActionListener {

    private final Map map;
    private final Pacman pacman;
    private final File[] animFiles = {new File("resources/images/blinky_0.png"), new File("./resources/images/blinky_1.png")};
    private final BufferedImage[] animImages = new BufferedImage[animFiles.length];
    private final Timer timer= new Timer(75, this);
    private int animState = 0;
    private int xPos = 13 * 16;
    private int yPos = 27 * 8;
    private int direction = 2;
    private int nextDirection = 2;
    private Rectangle hitbox = new Rectangle(xPos+4, yPos+4, 24, 24);
    private int ghostState = 1; // 0 = chase, 1 = scatter, 2 = frightened, 3 = eaten
    private Random rand = new Random();
    
    Blinky(Pacman pacman, Map map) {
        this.pacman = pacman;
        this.map = map;
        setupAnims();
    }

    public void setupAnims() {
        for (int i = 0; i<animFiles.length; i++) {
            try {
                animImages[i] = ImageIO.read(animFiles[i]);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        timer.start();
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getState() {
        return ghostState;
    }

    public void setState(int state) {
        ghostState = state;
    }

    public BufferedImage updateAnim() {
        return animImages[animState];
    }
    
    public void actionPerformed(ActionEvent e) {
        animState++;
        if (animState > 1){
            animState = 0;
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void move() {
        nextDirection = getNextDirection();
        if (map.checkTps(hitbox) == 1) {
            xPos = 28*16;
            nextDirection = direction = 2;
        }
        else if (map.checkTps(hitbox) == 2) {
            xPos = -2*16;
            nextDirection = direction = 0;
        }
        if (map.checkWallCollision(nextDirection, xPos, yPos) == false) {
            switch (nextDirection) {
                case 0: 
                    xPos += 8;
                    direction = nextDirection;
                    break;
                case 1:
                    yPos += 8;
                    direction = nextDirection;
                    break;
                case 2:
                    xPos -= 8;
                    direction = nextDirection;
                    break;
                case 3:
                    yPos -= 8;
                    direction = nextDirection;
                    break;
            }
        }
        else if (map.checkWallCollision(direction, xPos, yPos) == false) {
            switch (direction) {
                case 0: 
                    xPos += 8;
                    break;
                case 1:
                    yPos += 8;
                    break;
                case 2:
                    xPos -= 8;
                    break;
                case 3:
                    yPos -= 8;
                    break;
            }
        }
        hitbox.x = getX()+4;
        hitbox.y = getY()+4;
    }

    public int getNextDirection() {
        int x1 = 0;
        int y1 = 0;
        if (ghostState == 0) {  // chase mode
            x1 = pacman.getX();
            y1 = pacman.getY();
        }
        else if (ghostState == 1) {  // scatter mode
            x1 = 26*16;
            y1 = 4*16;
        }
        else if (ghostState == 2) {  // frighten mode
            return randDirection();
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

    public int compare(double right, double down, double left, double up) {
        if (right < down && right < left && right < up) {
            return 0;
        }
        else if (down < right && down < left && down < up) {
            return 1;
        }
        else if (left < right && left < down && left < up) {
            return 2;
        }
        else if (up < right && up < left && up < down) {
            return 3;
        }
        return 5;
    }

    public int randDirection() {    // get a new random direction
        int x = rand.nextInt(4);
        if (x == 0 && direction == 2) {
            return randDirection();
        }
        else if (x == 1 && direction == 3) {
            return randDirection();
        }
        else if (x == 2 && direction == 0) {
            return randDirection();
        }
        else if (x == 3 && direction == 1) {
            return randDirection();
        }
        else if (map.checkWallCollision(x, xPos, yPos)) {
            return randDirection();
        }
        return x;
    }

    public void turnAround() {
        if (direction == 0) {
            direction = 2;
        }
        else if (direction == 1) {
            direction = 3;
        }
        else if (direction == 2) {
            direction = 0;
        }
        else {
            direction = 1;
        }
    }

}
