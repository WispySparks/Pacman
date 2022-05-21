package Pacman;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
    GameFrame() {
        GamePanel panel = new GamePanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.addKeyListener(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
