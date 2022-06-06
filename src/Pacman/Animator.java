package Pacman;

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class Animator implements ActionListener{

    private final File[] blinkyFiles = {new File("resources/images/blinky_0.png"), new File("./resources/images/blinky_1.png")};
    private final File[] pinkyFiles = {new File("resources/images/pinky_0.png"), new File("./resources/images/pinky_1.png")};
    private final File[] inkyFiles = {new File("resources/images/inky_0.png"), new File("./resources/images/inky_1.png")};
    private final File[] clydeFiles = {new File("resources/images/clyde_0.png"), new File("./resources/images/clyde_1.png")};
    private final File[] eyeFiles = {new File("./resources/images/eyes_0.png"), new File("./resources/images/eyes_1.png"), new File("./resources/images/eyes_2.png"), new File("./resources/images/eyes_3.png")};
    private final File[] scaredFiles = {};
    private final BufferedImage[] normalImages = new BufferedImage[blinkyFiles.length];
    private final BufferedImage[] eyeImages = new BufferedImage[eyeFiles.length];
    private final BufferedImage[] scaredImages = new BufferedImage[scaredFiles.length];
    private final Timer timer = new Timer(75, this);
    private int animState = 0;
    private String ghost = "null";
    
    Animator(String ghost) {
        this.ghost = ghost;
        setupAnims();
        timer.start();
    }

    public void setupAnims() {
        for (int i = 0; i<blinkyFiles.length; i++) {
            try {
                switch (ghost) {
                    case "blinky": normalImages[i] = ImageIO.read(blinkyFiles[i]); break;
                    case "pinky": normalImages[i] = ImageIO.read(pinkyFiles[i]); break;
                    case "inky": normalImages[i] = ImageIO.read(inkyFiles[i]); break;
                    case "clyde": normalImages[i] = ImageIO.read(clydeFiles[i]); break;
                }
                scaredImages[i] = ImageIO.read(scaredFiles[i]);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        for (int i = 0; i<eyeFiles.length; i++) {
            try {
                eyeImages[i] = ImageIO.read(eyeFiles[i]);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public BufferedImage normal() {
        if (animState > 1) {
            animState = 0;
        }
        return normalImages[animState];
    }

    public BufferedImage frighten() {
        if (animState > 1) {
            animState = 0;
        }
        return scaredImages[animState];
    }

    public BufferedImage eyes(int direction) {
        return eyeImages[direction];
    }

    public void actionPerformed(ActionEvent e) {
        animState++;
    }

}
