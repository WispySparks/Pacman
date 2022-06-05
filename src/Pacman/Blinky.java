package Pacman;

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.event.*;
import java.math.*;

public class Blinky implements ActionListener {

    private final Map map;
    private final Pacman pacman;
    private final File[] animFiles = {new File("resources/images/blinky_0.png"), new File("./resources/images/blinky_1.png")};
    private final BufferedImage[] animImages = new BufferedImage[animFiles.length];
    private final Timer timer= new Timer(75, this);
    private int animState = 0;
    private int xPos = 13 * 16;
    private int yPos = 14 * 16;
    private int direction = 2;
    private int nextDirection = 2;
    Rectangle hitbox = new Rectangle(xPos, yPos, 32, 32);
    
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
        return yPos-8;
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
        hitbox.x = getX();
        hitbox.y = getY();
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
    }

    public int getNextDirection() {
        int x1 = pacman.getX();
        int y1 = pacman.getY();
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
            rightdistance +=999;
        }
        if (map.checkWallCollision(1, xPos, yPos) || direction == 3) {
            downdistance +=999;
        }
        if (map.checkWallCollision(2, xPos, yPos) || direction == 0) {
            leftdistance +=999;
        }
        if (map.checkWallCollision(3, xPos, yPos) || direction == 1) {
            updistance +=999;
        }
        updistance += 1;
        leftdistance += 1;
        System.out.println(rightdistance + " " + downdistance + " " + leftdistance + " " + updistance);
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

}
