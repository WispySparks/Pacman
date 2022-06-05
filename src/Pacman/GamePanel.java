package Pacman;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JLayeredPane implements KeyListener, ActionListener{

    private final int tileSize = 16;
    private final int gridWidth = 28;
    private final int gridLength = 36;
    private final File mapFile = new File("./resources/images/pacmap.png");
    private final Map map = new Map(this);
    private final Pacman pacman = new Pacman(this, map);
    private final Blinky blinky = new Blinky(pacman, map);
    private final JLabel mapLabel = new JLabel();
    private final AudioPlayer audioPlayer = new AudioPlayer();
    private int score = 0;
    private BufferedImage mapImage;
    private JLabel scoreLabel = new JLabel("HIGH SCORE " + Integer.toString(score));
    private boolean startDone = false;
    private Rectangle[] dots = map.getDots();
    private Timer timer = new Timer(100, this);

    GamePanel() {
        audioPlayer.playStart();
        gameSetup();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        for (int i = 0; i<dots.length; i++) {
            g.fillRect(dots[i].x, dots[i].y, dots[i].width, dots[i].height);
        }
        g.drawImage(pacman.updateAnim(), pacman.getX(), pacman.getY(), null);
        g.drawImage(blinky.updateAnim(), blinky.getX(), blinky.getY(), null);
        // g.setColor(Color.BLUE);
        // g.fillRect(pacman.hitbox.x, pacman.hitbox.y, pacman.hitbox.width, pacman.hitbox.height);
        // g.setColor(Color.PINK);
        // g.fillRect(blinky.hitbox.x, blinky.hitbox.y, blinky.hitbox.width, blinky.hitbox.height);
        // g.setColor(Color.CYAN);
        // g.fillRect(map.test.x, map.test.y, map.test.width, map.test.height);
        // g.setColor(Color.GREEN);
        // for (int i = 0; i<map.walls.length; i++) {
        //     g.fillRect(map.walls[i].x, map.walls[i].y, map.walls[i].width, map.walls[i].height);
        // }
    }

    public void gameSetup() {
        this.setOpaque(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(gridWidth*tileSize, gridLength*tileSize));
        try {
            mapImage = ImageIO.read(mapFile);
        } catch (IOException e) {
            System.out.println(e);
        }
        ImageIcon mapIcon = new ImageIcon(mapImage);
        mapLabel.setIcon(mapIcon);
        mapLabel.setBounds(0, 0, gridWidth*tileSize, gridLength*tileSize);
        scoreLabel.setBounds(gridWidth*tileSize/2-75, 0, 200, 50);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        this.add(mapLabel, Integer.valueOf(1));
        this.add(scoreLabel, Integer.valueOf(2));
        pacman.setGhosts(blinky);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        startDone = audioPlayer.isFinished("start");
        pacman.checkHitboxCollision();
        if (startDone == true && pacman.isDead() == false) {
            pacman.move();
            blinky.move();
        }
        repaint();
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

    public boolean isStartDone() {
        return startDone;
    }

    public void setScore(int amount) {
        score += amount;
        scoreLabel.setText("HIGH SCORE " + Integer.toString(score));
    }
}
