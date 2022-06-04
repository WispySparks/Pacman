package Pacman;

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.event.*;

public class Blinky implements ActionListener {

    private final Map map;
    private final File[] animFiles = {new File("resources/images/blinky_0.png"), new File("./resources/images/blinky_1.png")};
    private final BufferedImage[] animImages = new BufferedImage[animFiles.length];
    private final Timer timer= new Timer(75, this);
    private int animState = 0;
    private int xPos = 13 * 16;
    private int yPos = 55 * 4;
    private int direction = 2;
    private int nextDirection = 2;
    Rectangle hitbox = new Rectangle(xPos, yPos, 32, 32);
    
    Blinky(Map map) {
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
        return yPos-6;
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
        //getNextDirection();
        if (map.checkTps(hitbox) == 1) {
            xPos = 28*16;
            nextDirection = direction = 2;
        }
        else if (map.checkTps(hitbox) == 2) {
            xPos = -2*16;
            nextDirection = direction = 0;
        }
        if (map.checkWallCollision(nextDirection, xPos, getY()) == false) {
            System.out.println("go want");
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
        else if (map.checkWallCollision(direction, xPos, getY()) == false) {
            System.out.println("go have to");
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
        // mathhhh
        return 0;
    }
}
