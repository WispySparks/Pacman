package Pacman;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;
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
    private final Blinky blinky = new Blinky(pacman, map, this);
    private final Pinky pinky = new Pinky(pacman, map, this);
    private final Inky inky = new Inky(pacman, blinky, map, this);
    private final Clyde clyde = new Clyde(pacman, map, this);
    private final Ghost[] ghosts = {blinky, pinky, inky, clyde};
    private final JLabel mapLabel = new JLabel();
    private final AudioPlayer audioPlayer = new AudioPlayer();
    private final double[] modeTimes = {9, 27, 38, 54, 63, 79, 88};  // times for switching from chase to scatter 
    private float currentTime = 0;
    private float frightenTime = 0;     // timer for when frighten runs out
    private boolean power = false;  // whether a power pellet are currently active
    private int score = 0;
    private JLabel scoreLabel = new JLabel("HIGH SCORE " + Integer.toString(score));
    private boolean startDone = false;  // whether start sound is done or not
    private Rectangle[] dots = map.getDots();
    private Rectangle[] bigDots = map.getBigDots();  // power pellets
    private Timer timer = new Timer(75, this);
    private int state = 1;  // level wide state that the ghosts should be at when not eaten or frightened

    GamePanel() {
        audioPlayer.playStart();
        new GhostController(ghosts, this, pacman);
        gameSetup();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(255, 184, 151));
        for (int i = 0; i<dots.length; i++) {
            g.fillRect(dots[i].x, dots[i].y, dots[i].width, dots[i].height);
        }
        for (int i = 0; i<bigDots.length; i++) {
            g.fillOval(bigDots[i].x, bigDots[i].y, bigDots[i].width, bigDots[i].height);
        }
        for (int i = 0; i<ghosts.length; i++) {
            g.drawImage(ghosts[i].updateAnim(), ghosts[i].getX(), ghosts[i].getY(), null);
        }
        g.drawImage(pacman.updateAnim(), pacman.getX(), pacman.getY(), null);
        g.setColor(Color.RED);
        g.fillRect(blinky.x1, blinky.y1, 8, 8);
        g.setColor(Color.PINK);
        g.fillRect(pinky.x1, pinky.y1, 8, 8);
        g.setColor(Color.BLUE);
        g.fillRect(inky.x1, inky.y1, 8, 8);
        g.setColor(Color.ORANGE);
        g.fillRect(clyde.x1, clyde.y1, 8, 8);
        // g.setColor(Color.GREEN);
        // for (int i = 0; i<map.walls.length; i++) {
        //     g.fillRect(map.walls[i].x, map.walls[i].y, map.walls[i].width, map.walls[i].height);
        // }
    }

    public void gameSetup() {
        BufferedImage mapImage = null;
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
        pacman.setGhosts(ghosts);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        startDone = audioPlayer.isFinished("start");
        pacman.checkHitboxCollision();
        if (startDone == true && pacman.isDead() == false) {
            modes();
            pacman.move();
        }
        repaint();
    }

    public void modes() {   // sets the states of the ghosts based on the current time or if the power pellets have run out
        if (power) {
            frightenTime += .075;
        }
        else {
            currentTime += 1;
        }
        for (int i = 0; i<modeTimes.length; i++) {
            if ((currentTime/10) == modeTimes[i]) {
                for (int j = 0; j<ghosts.length; j++) {
                    if (ghosts[j].getState() < 2) {
                        state = i%2;
                        ghosts[j].setState(state);
                    }
                }
            }
        }
        if ((int) frightenTime == 7) {
            for (int i = 0; i<ghosts.length; i++) {
                ghosts[i].reAlign();
                if (ghosts[i].isEaten() == false) {
                    ghosts[i].setState(Constants.chase);
                }
            }
            frightenTime = 0;
            power = false;
        }
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

    public void powerPellet() { // set states of ghosts to frightened
        for (int i = 0; i<ghosts.length; i++) {
            if (ghosts[i].getState() != Constants.eaten) {
                ghosts[i].setState(Constants.frighten);
            }
        }
        power = true;
    }

    public int gameState() {
        return state;
    } 
}
