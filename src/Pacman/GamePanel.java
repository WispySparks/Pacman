package Pacman;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;

public class GamePanel extends JLayeredPane implements KeyListener {

    private final int tileSize = 16;
    private final int gridWidth = 28;
    private final int gridLength = 36;
    private final GameController controller = new GameController(this);
    private final Map map = new Map(controller);
    private final Pacman pacman = new Pacman(controller, map);
    private final Blinky blinky = new Blinky(pacman, map, controller);
    private final Pinky pinky = new Pinky(pacman, map, controller);
    private final Inky inky = new Inky(pacman, blinky, map, controller);
    private final Clyde clyde = new Clyde(pacman, map, controller);
    private final Ghost[] ghosts = {blinky, pinky, inky, clyde};
    private JLabel scoreLabel = new JLabel("HIGH SCORE " + Integer.toString(0));

    GamePanel() {
        mapSetup();
        controller.setPieces(ghosts, pacman);
        paint.start();
        // timer.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         System.out.println("gaming");
        //     }
        // });
        // timer.start();
        // ActionListener jam = new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         System.out.println("sheesh");
        //     }
        // };
        // timer2.addActionListener(jam);
    }

    public void paint(Graphics g) {//TODO: Display lives and display fruit, collect fruit and gain score for it, level restart when you win, 1up when you get enough score, game over screen, extra sounds, main menu, fix eyes sounds for all ghosts
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
        for (int i = 0; i<ghosts.length; i++) {
            g.drawImage(ghosts[i].updateAnim(), ghosts[i].getX(), ghosts[i].getY(), null);
        }
        g.drawImage(pacman.updateAnim(), pacman.getX(), pacman.getY(), null);
        g.setColor(Color.RED);
        g.fillRect(blinky.x1, blinky.y1, 16, 16);
        g.setColor(Color.PINK);
        g.fillRect(pinky.x1, pinky.y1, 16, 16);
        g.setColor(Color.BLUE);
        g.fillRect(inky.x1, inky.y1, 16, 16);
        g.setColor(Color.ORANGE);
        g.fillRect(clyde.x1, clyde.y1, 16, 16);
        // g.setColor(Color.GREEN);
        // for (int i = 0; i<map.walls.length; i++) {
        //     g.fillRect(map.walls[i].x, map.walls[i].y, map.walls[i].width, map.walls[i].height);
        // }
    }

    Thread paint = new Thread() {
        public void run() {
            repaint();
            try {
                Thread.sleep(75);
            } catch (Exception e) {
                System.out.println(e);
            }
            run();
        }
    };

    public void mapSetup() {
        BufferedImage mapImage = null;
        final File mapFile = new File("./resources/images/pacmap.png");
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
        this.add(mapLabel, Integer.valueOf(1));
        this.add(scoreLabel, Integer.valueOf(2));
    }

    public void gameOver() {
        System.out.println("show game over ui");
    }

    public void setScore(int score) {
        scoreLabel.setText("HIGH SCORE " + Integer.toString(score));
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
        if (e.getKeyCode() == KeyEvent.VK_R) {
            controller.stopLoops();
            controller.setLost(true);
            setScore(0);
            try {
                Thread.sleep(76);
            } catch (Exception e1) {
                System.out.println(e1);
            }
            map.setDots();
            
            controller.gameSetup();
        }
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }

}
