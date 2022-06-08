package Pacman;

import javax.swing.Timer;
import java.awt.event.*;

public class GhostController implements ActionListener{
    
    private final Timer timer = new Timer(76, this);
    private final Ghost[] ghosts;
    private final GamePanel panel;
    private final Pacman pacman;
    private double time = 0;

    GhostController(Ghost[] ghosts, GamePanel panel, Pacman pacman) {
        this.panel = panel;
        this.ghosts = ghosts;
        this.pacman = pacman;
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        if (panel.isStartDone() == true && pacman.isDead() == false) {
            time += 1;
            //System.out.println((int) time);
            for (int i = 0; i<ghosts.length; i++) {
                ghosts[i].getMove();
            }
            if (time/10 == 5) {
                //ghosts[1].start();
            }
            if (time/10 == 10) {
                ghosts[2].start();
            }
        }
    }

}
