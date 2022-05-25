package Pacman;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Pacman {
    
    private int xPos = 14 * 16 - 8;
    private int yPos = 26 * 16 + 8;
    private int wallTempX = xPos;
    private int wallTempY = yPos;
    private Rectangle[] walls;
    Rectangle wallHitbox = new Rectangle(wallTempX, wallTempY, 16, 16);
    private final File[] animFiles = {new File("./resources/images/pacman_0.png"), new File("./resources/images/pacman_1.png"), new File("./resources/images/pacman_2.png"), new File("./resources/images/pacman_3.png"), new File("./resources/images/pacman_4.png")};
    private final BufferedImage[] animImages = new BufferedImage[animFiles.length];
    private int animState = 0;
    boolean isDead = false;

    Pacman(Map map) {
        this.walls = map.getWalls();
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
    }

    public int getX() {
        return xPos - 8;
    }

    public int getY() {
        return yPos - 8;
    }

    public boolean checkCollision(int direction) {
        switch (direction) {
            case 0: // right
                wallTempX = xPos + 16;
                wallTempY = yPos-8;
                wallHitbox.height = 32;
                wallHitbox.width = 16;
                break;
            case 1: // down
                wallTempY = yPos + 16;
                wallTempX = xPos-8;
                wallHitbox.width = 32;
                wallHitbox.height = 16;
                break;
            case 2: // left
                wallTempX = xPos - 16;
                wallTempY = yPos-8;
                wallHitbox.height = 32;
                wallHitbox.width = 16;
                break;
            case 3: // up
                wallTempY = yPos - 16;
                wallTempX = xPos-8;
                wallHitbox.width = 32;
                wallHitbox.height = 16;
                break;
        } 
        wallHitbox.x = wallTempX;
        wallHitbox.y = wallTempY;
        for (int i = 0; i<walls.length; i++) {
            if (wallHitbox.intersects(walls[i])) {
                return true;
            }
        }
        return false;
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

    public BufferedImage updateAnim(int direction) {
        BufferedImage anim = animImages[animState];
        // rotates the image based on direction
        BufferedImage rotatedImage = new BufferedImage(anim.getWidth(), anim.getHeight(), anim.getType()); 
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(90*direction), anim.getWidth()/2, anim.getHeight()/2);
        AffineTransformOp rotateOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage rotatedAnim = rotateOp.filter(anim, rotatedImage);
        animState++;
        if (animState > 4 ){
            animState = 0;
        }
        return rotatedAnim;
    } 
}
