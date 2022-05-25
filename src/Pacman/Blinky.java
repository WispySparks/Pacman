package Pacman;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Blinky {

    private int xPos = 13 * 16;
    private int yPos = 55 * 4;
    private final File[] animFiles = {new File("resources/images/blinky_0.png"), new File("./resources/images/blinky_1.png")};
    private final BufferedImage[] animImages = new BufferedImage[animFiles.length];
    
    Blinky() {
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

    public BufferedImage updateAnim() {
        return animImages[0];
    }

}
