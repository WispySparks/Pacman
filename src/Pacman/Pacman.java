package Pacman;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Pacman {
    
    private int xPos = 21 * 16;
    private int yPos = 20 * 16;
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
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public boolean checkCollision(int direction) {
        switch (direction) {
            case 0:
                wallTempX = xPos + 16;
                wallTempY = yPos;
                break;
            case 1:
                wallTempY = yPos + 16;
                wallTempX = xPos;
                break;
            case 2:
                wallTempX = xPos - 16;
                wallTempY = yPos;
                break;
            case 3:
                wallTempY = yPos - 16;
                wallTempX = xPos;
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
