package Pacman;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements KeyListener, ActionListener{

    private final int tileSize = 16;
    private final int gridWidth = 28;
    private final int gridLength = 36;
    private final File mapFile = new File("./resources/images/pacmap.png");
    private final Blinky blinky = new Blinky();
    private final Pacman pacman = new Pacman(blinky);
    private final JLabel mapLabel = new JLabel();
    private final AudioPlayer audioPlayer = new AudioPlayer();
    private BufferedImage mapImage;
    private boolean startDone = false;
    Map map = new Map();
    
    private Timer timer = new Timer(100, this);

    GamePanel() {
        audioPlayer.playStart();
        bgSetup();
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(pacman.updateAnim(), pacman.getX(), pacman.getY(), null);
        g.drawImage(blinky.updateAnim(), blinky.getX(), blinky.getY(), null);
        // g.setColor(Color.BLUE);
        // g.fillRect(pacman.wallHitbox.x, pacman.wallHitbox.y, pacman.wallHitbox.width, pacman.wallHitbox.height);
        // g.setColor(Color.PINK);
        // g.fillRect(blinky.hitbox.x, blinky.hitbox.y, blinky.hitbox.width, blinky.hitbox.height);
        // g.setColor(Color.CYAN);
        // g.fillRect(pacman.hitbox.x, pacman.hitbox.y, pacman.hitbox.width, pacman.hitbox.height);
        // g.setColor(Color.GREEN);
        g.setColor(Color.WHITE);
        for (int i = 0; i<map.dots.length; i++) {
            g.fillRect(map.dots[i].x, map.dots[i].y, map.dots[i].width, map.dots[i].height);
        }
    }

    public void bgSetup() {
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(gridWidth*tileSize, gridLength*tileSize));
        try {
            mapImage = ImageIO.read(mapFile);
        } catch (IOException e) {
            System.out.println(e);
        }
        ImageIcon mapIcon = new ImageIcon(mapImage);
        mapLabel.setIcon(mapIcon);
        this.add(mapLabel);
    }

    

    public void actionPerformed(ActionEvent e) {
        pacman.checkHitboxCollision();
        startDone = audioPlayer.isFinished("start");
        if (startDone == true && pacman.isDead() == false) {
            pacman.move();
            repaint();
        }
        else if (startDone == true && pacman.isDead() == true) {
            audioPlayer.playDeath();
            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.setNextDir(0);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.setNextDir(1);
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.setNextDir(2);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.setNextDir(3);
        }
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
}
