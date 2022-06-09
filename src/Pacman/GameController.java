package Pacman;

import javax.swing.Timer;
import java.awt.event.*;

public class GameController implements ActionListener{
    
    private final AudioPlayer audioPlayer = new AudioPlayer();
    private final Timer ghostTimer = new Timer(75, null);
    private final Timer mainTimer = new Timer(75, this);
    private final GamePanel panel;
    private Pacman pacman;
    private Ghost[] ghosts;
    private final double[] modeTimes = {9, 27, 38, 54, 63, 79, 88};  // times for switching from chase to scatter 
    private float modeTime = 0;
    private float frightenTime = 0;     // timer for when frighten runs out
    private boolean power = false;  // whether a power pellet are currently active
    private int state = 1;  // level wide state that the ghosts should be at when not eaten or frightened
    private double time = 0;
    private int score = 0;
    private boolean won = false;

    GameController(GamePanel panel) {
        this.panel = panel;
        audioPlayer.playStart();
    }

    public void setPieces(Ghost[] ghosts, Pacman pacman) {
        this.ghosts = ghosts;
        this.pacman = pacman;
        gameSetup();
    }

    public void gameSetup() {
        pacman.setGhosts(ghosts);
        ghostTimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (audioPlayer.isFinished("start") == true && pacman.isDead() == false) {
                    time += 1;
                    for (int i = 0; i<ghosts.length; i++) {
                        ghosts[i].getMove();
                    }
                    if (time/10 == 5) {     // this section lets the ghosts out of the ghost house
                        ghosts[1].start();
                    }
                    if (time/10 == 10) {
                        ghosts[2].start();
                    }
                    if (time/10 == 15) { 
                        ghosts[3].start();
                    }
                }
            }
        });
        ghostTimer.start();
        mainTimer.start();
    }

    public void actionPerformed(ActionEvent e) {
        pacman.checkHitboxCollision();
        if (audioPlayer.isFinished("start") == true && pacman.isDead() == false && won == false) {
            modes();
            pacman.move();
        }
        else if (pacman.isDead() == true) {
            if (pacman.getLives() > 0) {
                System.out.println("not dead yet!" + pacman.getLives());
                ghostTimer.stop();
                pacman.reset();
            }
        }
        panel.setScore(score);
        panel.repaint();
    }

    public void modes() {   // sets the states of the ghosts based on the current time or if the power pellets have run out
        if (power) {
            frightenTime += .075;
        }
        else {
            modeTime += 1;
        }
        for (int i = 0; i<modeTimes.length; i++) {
            if ((modeTime/10) == modeTimes[i]) {
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

    public void powerPellet() { // set states of ghosts to frightened
        for (int i = 0; i<ghosts.length; i++) {
            if (ghosts[i].getState() != Constants.eaten && ghosts[i].isEaten() == false) {
                ghosts[i].setState(Constants.frighten);
            }
        }
        power = true;
        frightenTime = 0;
    }

    public int gameState() {
        return state;
    }

    public void setScore(int amount, int dots) {
        score += amount;
        if (dots == 248) {
            System.out.println("You Win!");
        }
    }

}
