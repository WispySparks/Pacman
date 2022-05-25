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
    private boolean startDone = false;
    private int direction = 2;
    private int nextDirection = 2;
    private int prevKey = 0;
    private Map map = new Map();
    private Pacman pacman = new Pacman(map);
    private Blinky blinky = new Blinky();
    private JLabel mapLabel = new JLabel();
    private BufferedImage mapImage;
    private AudioPlayer audioPlayer = new AudioPlayer();
    private Timer timer = new Timer(75, this);

    GamePanel() {
        audioPlayer.playStart();
        bgSetup();
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(pacman.updateAnim(direction), pacman.getX(), pacman.getY(), null);
        g.drawImage(blinky.updateAnim(), blinky.getX(), blinky.getY(), null);
        // g.setColor(Color.BLUE);
        // g.fillRect(pacman.wallHitbox.x, pacman.wallHitbox.y, pacman.wallHitbox.width, pacman.wallHitbox.height);
        // g.setColor(Color.GREEN);
        // for (int i = 0; i<map.walls.length; i++) {
        //     g.fillRect(map.walls[i].x, map.walls[i].y, map.walls[i].width, map.walls[i].height);
        // }
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

    public void move() {
        if (pacman.checkCollision(nextDirection) == false) {
            switch (nextDirection) {
                case 0: 
                    pacman.right();
                    direction = nextDirection;
                    break;
                case 1:
                    pacman.down();
                    direction = nextDirection;
                    break;
                case 2:
                    pacman.left();
                    direction = nextDirection;
                    break;
                case 3:
                    pacman.up();
                    direction = nextDirection;
                    break;
            }
        }
        else if (pacman.checkCollision(direction) == false) {
            switch (direction) {
                case 0: 
                    pacman.right();
                    break;
                case 1:
                    pacman.down();
                    break;
                case 2:
                    pacman.left();
                    break;
                case 3:
                    pacman.up();
                    break;
            }
        }
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        startDone = audioPlayer.isFinished("start");
        if (startDone == true) {
            move();
            /*if (audioPlayer.isFinished("waka")) {
                audioPlayer.playWaka();
            }*/
        }
    }

    public void keyPressed(KeyEvent e) {
        System.out.println(prevKey);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            prevKey = KeyEvent.VK_RIGHT;
            nextDirection = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            prevKey = KeyEvent.VK_DOWN;
            nextDirection = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            prevKey = KeyEvent.VK_LEFT;
            nextDirection = 2;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            prevKey = KeyEvent.VK_UP;
            nextDirection = 3;
        }
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
}
