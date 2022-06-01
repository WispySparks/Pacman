package Pacman;

import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Pacman implements ActionListener {

    private final Map map = new Map();
    private final File[] animFiles = {new File("./resources/images/pacman_0.png"), new File("./resources/images/pacman_1.png"), new File("./resources/images/pacman_2.png"), new File("./resources/images/pacman_3.png"), new File("./resources/images/pacman_4.png"), new File("./resources/images/pacdeath_0.png"), new File("./resources/images/pacdeath_1.png"), new File("./resources/images/pacdeath_2.png"), new File("./resources/images/pacdeath_3.png"), new File("./resources/images/pacdeath_4.png"), new File("./resources/images/pacdeath_5.png"), new File("./resources/images/pacdeath_6.png"), new File("./resources/images/pacdeath_7.png"), new File("./resources/images/pacdeath_8.png"), new File("./resources/images/pacdeath_9.png"), new File("./resources/images/pacdeath_10.png"), new File("./resources/images/pacdeath_11.png"), new File("./resources/images/pacdeath_12.png")};
    private final BufferedImage[] animImages = new BufferedImage[animFiles.length];
    private int animState = -1;
    private Timer animTimer = new Timer(75, this);
    private Blinky blinky;
    private int xPos = 13 * 16;
    private int yPos = 26 * 16;
    private int direction = 2;
    private int nextDirection = 2;
    Rectangle hitbox = new Rectangle(xPos, yPos, 32, 32);
    private boolean isDead = false;

    Pacman(Blinky blinky) {
        this.blinky = blinky;
        setupAnims();
    }

    public void setupAnims() {
        animTimer.start();
        for (int i = 0; i<animFiles.length; i++) {
            try {
                animImages[i] = ImageIO.read(animFiles[i]);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos-2;
    }

    public boolean isDead() {
        return isDead;
    }

    public void checkHitboxCollision() {
        hitbox.x = getX();
        hitbox.y = getY();
        if (blinky.getHitbox().intersects(hitbox)) {
            isDead = true;
        }
    }

    public void up() {
        yPos -= 8;
    }

    public void down() {
        yPos += 8;
    }

    public void right() {
        xPos += 8;
    }

    public void left() {
        xPos -= 8;
    }

    public void setNextDir(int nextDirection) {
        this.nextDirection = nextDirection;
    }

    public void move() {
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
                    right();
                    direction = nextDirection;
                    break;
                case 1:
                    down();
                    direction = nextDirection;
                    break;
                case 2:
                    left();
                    direction = nextDirection;
                    break;
                case 3:
                    up();
                    direction = nextDirection;
                    break;
            }
        }
        else if (map.checkWallCollision(direction, xPos, yPos) == false) {
            switch (direction) {
                case 0: 
                    right();
                    break;
                case 1:
                    down();
                    break;
                case 2:
                    left();
                    break;
                case 3:
                    up();
                    break;
            }
        }
    }

    public BufferedImage updateAnim() {
        BufferedImage anim = animImages[animState];
        // rotates the image based on direction
        BufferedImage rotatedImage = new BufferedImage(anim.getWidth(), anim.getHeight(), anim.getType()); 
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(90*direction), anim.getWidth()/2, anim.getHeight()/2);
        AffineTransformOp rotateOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage rotatedAnim = rotateOp.filter(anim, rotatedImage);
        return rotatedAnim;
    }
    
    public void actionPerformed(ActionEvent e) {
        animState++;
        if (animState > 4  && isDead == false){
            animState = 0;
        }
        else if (animState > 17 && isDead == true) {
            animState = 17;
        }
    }
}
