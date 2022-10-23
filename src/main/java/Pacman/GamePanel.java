package main.java.Pacman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import main.java.Pacman.Ghosts.Blinky;
import main.java.Pacman.Ghosts.Clyde;
import main.java.Pacman.Ghosts.Ghost;
import main.java.Pacman.Ghosts.Inky;
import main.java.Pacman.Ghosts.Pinky;

public class GamePanel extends JLayeredPane implements KeyListener {

    private final int tileSize = 16;
    private final int gridWidth = 28;
    private final int gridLength = 36;
    private final GameController controller = new GameController(this);
    private final Map map = new Map(controller);
    private final Pacman pacman = new Pacman(controller, map);
    private final Blinky blinky = new Blinky(pacman, map, controller);
    private final Ghost[] ghosts = {blinky, new Pinky(pacman, map, controller), new Inky(pacman, blinky, map, controller), new Clyde(pacman, map, controller)};
    private JLabel scoreLabel = new JLabel("HIGH SCORE " + Integer.toString(0));
    private JLabel levelLabel = new JLabel("LEVEL " + Integer.toString(1));
    private JLabel lossLabel = new JLabel("GAME OVER");
    private JLabel lossLabel2 = new JLabel("PRESS R TO RESTART");
    private JLabel startLabel = new JLabel("PRESS ENTER TO START");

    GamePanel() {
        mapSetup();
        paint.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle[] dots = map.getDots();
        Rectangle[] bigDots = map.getBigDots();  // power pellets
        
        g.setColor(new Color(255, 184, 151)); 
        for (int i = 0; i<dots.length; i++) {
            g.fillRect(dots[i].x, dots[i].y, dots[i].width, dots[i].height);
        }
        for (int i = 0; i<bigDots.length; i++) {
            g.fillOval(bigDots[i].x, bigDots[i].y, bigDots[i].width, bigDots[i].height);
        }
        for (int i = 1; i<pacman.getLives(); i++) {     // draw pacman lives
            g.drawImage(pacman.staticImage(), (2*i)*16, 545, null);
        }
        if (controller.isLost() == false) {
            if (map.isCherry() == true) {
                g.drawImage(map.cherryImage(), 13*16, 19*16+8, null);
            }
            for (int i = 0; i<ghosts.length; i++) {
                g.drawImage(ghosts[i].updateAnim(), ghosts[i].getX(), ghosts[i].getY(), null);
            }
            g.drawImage(pacman.updateAnim(), pacman.getX(), pacman.getY(), null);
        }
        // g.setColor(Color.GREEN);
        // for (int i = 0; i<map.walls.length; i++) {
        //     g.fillRect(map.walls[i].x, map.walls[i].y, map.walls[i].width, map.walls[i].height);
        // }
    }

    Thread paint = new Thread() {   // thread to repaint the screen on its own
        public void run() {
            repaint();
            try {
                Thread.sleep(16);   // roughly 60 frames per second
            } catch (Exception e) {
                System.out.println(e);
            }
            run();
        }
    };

    public void mapSetup() {    // set up the ui and background images
        BufferedImage mapImage = null;
        final URL mapFile = this.getClass().getResource("/main/resources/images/pacmap.png");
        final JLabel mapLabel = new JLabel();
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
        levelLabel.setBounds(gridWidth*tileSize/2-75+190, 0, 200, 50);
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        lossLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        lossLabel.setForeground(Color.RED);
        lossLabel.setBounds(170, 305, 200, 50);
        lossLabel.setVisible(false);
        lossLabel2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        lossLabel2.setForeground(Color.RED);
        lossLabel2.setBounds(145, 205, 200, 50);
        lossLabel2.setVisible(false);
        startLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        startLabel.setForeground(Color.RED);
        startLabel.setBounds(145, 305, 200, 50);
        startLabel.setVisible(true);
        this.add(mapLabel, Integer.valueOf(1));
        this.add(scoreLabel, Integer.valueOf(2));
        this.add(lossLabel, Integer.valueOf(2));
        this.add(lossLabel2, Integer.valueOf(2));
        this.add(startLabel, Integer.valueOf(2));
        this.add(levelLabel, Integer.valueOf(2));
    }

    public void gameOver() {    // show game over ui
        lossLabel.setVisible(true);
        lossLabel2.setVisible(true);
    }

    public void setScore(int score) {
        scoreLabel.setText("HIGH SCORE " + Integer.toString(score));
        levelLabel.setText("LEVEL " + Integer.toString(controller.level()));
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            pacman.setNextDir(Constants.right);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            pacman.setNextDir(Constants.down);
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            pacman.setNextDir(Constants.left);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            pacman.setNextDir(Constants.up);
        }
        if (e.getKeyCode() == KeyEvent.VK_R && startLabel.isVisible() == false && controller.won() == false) {  // restarts the game by stopping music and calling game setup
            controller.stopLoops();
            controller.setLost(true);   // stops current main thread
            setScore(0);
            lossLabel.setVisible(false);
            lossLabel2.setVisible(false);
            try {
                Thread.sleep(76);   // waits for main thread to finish
            } catch (Exception e1) {
                System.out.println(e1);
            }
            map.setDots();
            controller.gameSetup();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && startLabel.isVisible() == true) {
            controller.setPieces(ghosts, pacman, map);
            startLabel.setVisible(false);
        }
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }

}
