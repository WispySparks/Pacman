package Pacman;

import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import Pacman.Ghosts.Ghost;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Pacman implements ActionListener {

    private final Map map;
    private final File[] animFiles = {new File("./resources/images/pacman_0.png"), new File("./resources/images/pacman_1.png"), new File("./resources/images/pacman_2.png"), new File("./resources/images/pacman_3.png"), new File("./resources/images/pacman_4.png"), new File("./resources/images/pacdeath_0.png"), new File("./resources/images/pacdeath_1.png"), new File("./resources/images/pacdeath_2.png"), new File("./resources/images/pacdeath_3.png"), new File("./resources/images/pacdeath_4.png"), new File("./resources/images/pacdeath_5.png"), new File("./resources/images/pacdeath_6.png"), new File("./resources/images/pacdeath_7.png"), new File("./resources/images/pacdeath_8.png"), new File("./resources/images/pacdeath_9.png"), new File("./resources/images/pacdeath_10.png"), new File("./resources/images/pacdeath_11.png"), new File("./resources/images/pacdeath_12.png")};
    private final BufferedImage[] animImages = new BufferedImage[animFiles.length];
    private final GameController controller;
    private final Timer animTimer = new Timer(100, this);
    private int animState = 0;
    private Ghost[] ghosts;
    private int xPos = 13 * 16;
    private int yPos = 51 * 8;
    private int direction = Constants.left;
    private int nextDirection = Constants.left;
    private int speed = Constants.baseSpeed;
    private Rectangle hitbox = new Rectangle(xPos+4, yPos+4, 24, 24);
    private boolean isDead = false;
    private int lives = 3;
    private boolean oneUp = false;
    private double eaten = 0;

    Pacman(GameController controller, Map map) {
        this.controller = controller;
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
        animTimer.start();
    }

    public void setGhosts(Ghost[] ghosts) {
        this.ghosts = ghosts;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getLives() {
        return lives;
    }

    public void extraLife() {
        if (oneUp == false) {
            lives++;
            controller.getAudio().playLife();
            oneUp = true;
        }
    }

    public void resetGhosts() {
        eaten = 0;
    }

    public void reset(boolean hard) {
        animState = 0;
        xPos = 13 * 16;
        yPos = 51 * 8;
        direction = Constants.left;
        nextDirection = Constants.left;
        isDead = false;
        hitbox.x = getX()+4;
        hitbox.y = getY()+4;
        eaten = 0;
        if (hard == true) {
            lives = 3;
        }
    }

    public void checkHitboxCollision() {
        for (int i = 0; i<ghosts.length; i++) {
            if (ghosts[i].getHitbox().intersects(hitbox) && ghosts[i].getState() < 2 && isDead == false) {
                controller.stopLoops();
                controller.getAudio().playDeath();
                isDead = true;
                lives--;
                animState = 5;
            }
            else if (ghosts[i].getHitbox().intersects(hitbox) && ghosts[i].getState() == Constants.frighten) {
                controller.getAudio().playGhost();
                ghosts[i].setState(Constants.eaten);
                ghosts[i].reAlign();
                eaten = Math.pow(2, eaten);
                if (eaten == 16) {eaten = eaten/2;}
                controller.setScore(200*eaten, 0);
            }
        }
    }

    public void setNextDir(int nextDirection) {
        this.nextDirection = nextDirection;
    }

    public void move() {
        map.eatDot(hitbox);
        if (map.checkTps(hitbox) == 1) {
            xPos = 28*16;
            nextDirection = direction = Constants.left;
        }
        else if (map.checkTps(hitbox) == 2) {
            xPos = -2*16;
            nextDirection = direction = Constants.right;
        }
        if (map.checkWallCollision(nextDirection, xPos, yPos, speed, true) == false) {
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
        else if (map.checkWallCollision(direction, xPos, yPos, speed, true) == false) {
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

    public BufferedImage staticImage() {
        return animImages[0];
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
