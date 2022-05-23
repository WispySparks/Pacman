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
    private final File mapImage = new File("./resources/images/pacmap.png");
    private boolean started = false;
    private int direction = 0;
    private Map map = new Map();
    private Pacman pacman = new Pacman(map);
    private JLabel mapLabel = new JLabel();
    private BufferedImage bufferedImage;
    private AudioPlayer audioPlayer = new AudioPlayer();
    private Timer timer = new Timer(50, this);

    GamePanel() {
        audioPlayer.playStart();
        mapBG();
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.YELLOW);
        g.fillOval(pacman.getX(), pacman.getY(), tileSize, tileSize);
        g.setColor(Color.BLUE);
        //g.fillRect(pacman.tempac.x, pacman.tempac.y, pacman.tempac.width, pacman.tempac.height);
        g.setColor(Color.GREEN);
        for (int i = 0; i<map.walls.length; i++) {
            //g.fillRect(map.walls[i].x, map.walls[i].y, map.walls[i].width, map.walls[i].height);
        }
    }

    public void mapBG() {
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(gridWidth*tileSize, gridLength*tileSize));
        try {
            bufferedImage = ImageIO.read(mapImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon mapIcon = new ImageIcon(bufferedImage);
        mapLabel.setIcon(mapIcon);
        this.add(mapLabel);
    }

    public void move() {
        if (pacman.checkCollision(direction) == true) {
            
        }
        else {
            switch (direction) {
                case 0: 
                    pacman.up();
                    break;
                case 1:
                    pacman.right();
                    break;
                case 2:
                    pacman.down();
                    break;
                case 3:
                    pacman.left();
                    break;
            }
        }
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (started == true) {
            move();
        }
        else {
            started = audioPlayer.isFinished();
        }   
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            direction = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direction = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            direction = 2;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            direction = 3;
        }
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
}
