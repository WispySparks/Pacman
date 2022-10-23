package main.java.Pacman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Animator implements ActionListener{

    private final InputStream[] blinkyFiles = {this.getClass().getResourceAsStream("/resources/images/blinky_0.png"), this.getClass().getResourceAsStream("/resources/images/blinky_1.png"), this.getClass().getResourceAsStream("/resources/images/blinky_2.png"), this.getClass().getResourceAsStream("/resources/images/blinky_3.png"), this.getClass().getResourceAsStream("/resources/images/blinky_4.png"), this.getClass().getResourceAsStream("/resources/images/blinky_5.png"), this.getClass().getResourceAsStream("/resources/images/blinky_6.png"), this.getClass().getResourceAsStream("/resources/images/blinky_7.png")};
    private final InputStream[] pinkyFiles = {this.getClass().getResourceAsStream("/resources/images/pinky_0.png"), this.getClass().getResourceAsStream("/resources/images/pinky_1.png"), this.getClass().getResourceAsStream("/resources/images/pinky_2.png"), this.getClass().getResourceAsStream("/resources/images/pinky_3.png"), this.getClass().getResourceAsStream("/resources/images/pinky_4.png"), this.getClass().getResourceAsStream("/resources/images/pinky_5.png"), this.getClass().getResourceAsStream("/resources/images/pinky_6.png"), this.getClass().getResourceAsStream("/resources/images/pinky_7.png")};
    private final InputStream[] inkyFiles = {this.getClass().getResourceAsStream("/resources/images/inky_0.png"), this.getClass().getResourceAsStream("/resources/images/inky_1.png"), this.getClass().getResourceAsStream("/resources/images/inky_2.png"), this.getClass().getResourceAsStream("/resources/images/inky_3.png"), this.getClass().getResourceAsStream("/resources/images/inky_4.png"), this.getClass().getResourceAsStream("/resources/images/inky_5.png"), this.getClass().getResourceAsStream("/resources/images/inky_6.png"), this.getClass().getResourceAsStream("/resources/images/inky_7.png")};
    private final InputStream[] clydeFiles = {this.getClass().getResourceAsStream("/resources/images/clyde_0.png"), this.getClass().getResourceAsStream("/resources/images/clyde_1.png"), this.getClass().getResourceAsStream("/resources/images/clyde_2.png"), this.getClass().getResourceAsStream("/resources/images/clyde_3.png"), this.getClass().getResourceAsStream("/resources/images/clyde_4.png"), this.getClass().getResourceAsStream("/resources/images/clyde_5.png"), this.getClass().getResourceAsStream("/resources/images/clyde_6.png"), this.getClass().getResourceAsStream("/resources/images/clyde_7.png")};
    private final InputStream[] eyeFiles = {this.getClass().getResourceAsStream("/resources/images/eyes_0.png"), this.getClass().getResourceAsStream("/resources/images/eyes_1.png"), this.getClass().getResourceAsStream("/resources/images/eyes_2.png"), this.getClass().getResourceAsStream("/resources/images/eyes_3.png")};
    private final InputStream[] scaredFiles = {this.getClass().getResourceAsStream("/resources/images/scared_0.png"), this.getClass().getResourceAsStream("/resources/images/scared_1.png")};
    private final BufferedImage[] normalImages = new BufferedImage[blinkyFiles.length];
    private final BufferedImage[] eyeImages = new BufferedImage[eyeFiles.length];
    private final BufferedImage[] scaredImages = new BufferedImage[scaredFiles.length];
    private final Timer timer = new Timer(100, this);
    private int animState = 0;
    private String ghost = "null";
    
    public Animator(String ghost) {
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
                if (i < 2) {
                    scaredImages[i] = ImageIO.read(scaredFiles[i]);
                }
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

    public BufferedImage normal(int direction) {
        switch (direction) {
            case Constants.down: direction = 2; break;
            case Constants.left: direction = 4; break;
            case Constants.up: direction = 6; break;
        }
        if (animState > 1) {
            animState = 0;
        }
        return normalImages[animState + direction];
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

    public BufferedImage getAnim(int direction, int ghostState, boolean enter) {
        if (ghostState < 2) {
            return normal(direction);
        }
        else if (ghostState == Constants.frighten) {
            return frighten();
        }
        else if (ghostState == Constants.eaten && enter == false) {
            return normal(direction);
        }
        else {
            return eyes(direction);
        }
    }

    public void actionPerformed(ActionEvent e) {
        animState++;
    }

}
