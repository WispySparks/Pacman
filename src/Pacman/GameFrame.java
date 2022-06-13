package Pacman;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
    GameFrame() {
        GamePanel panel = new GamePanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pacman");
        this.add(panel);
        this.addKeyListener(panel);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
