package main.java.Pacman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import main.java.Pacman.Ghosts.Ghost;

public class GameController implements ActionListener {
    
    private final AudioPlayer audioPlayer = new AudioPlayer();
    private final Timer ghostTimer = new Timer(75, this);
    private final GamePanel panel;
    private final double[] modeTimes = {9, 27, 38, 54, 63, 79, 88};  // times for switching from chase to scatter 
    private Map map;
    private Pacman pacman;
    private Ghost[] ghosts;
    private float modeTime = 0;
    private float frightenTime = 0;     // timer for when frighten runs out
    private boolean power = false;  // whether a power pellet are currently active
    private int state = 1;  // level wide state that the ghosts should be at when not eaten or frightened
    private double ghostTime = 0; // time for releasing ghosts
    private int score = 0;
    private boolean won = false;
    private boolean lost = false;
    private int level = 1;

    GameController(GamePanel panel) {
        this.panel = panel;
    }
    
    public void setPieces(Ghost[] ghosts, Pacman pacman, Map map) {
        this.ghosts = ghosts;
        this.pacman = pacman;
        this.map = map;
        pacman.setGhosts(ghosts);
        gameSetup();
    }

    public void gameSetup() {   // starts main thread and resets all values to what they should be
        audioPlayer.playStart();
        modeTime = 0;
        frightenTime = 0;
        power = false;
        state = 1;
        ghostTime = 0;
        lost = false;
        won = false;
        score = 0;
        level = 1;
        for (int i = 0; i<ghosts.length; i++) {
            ghosts[i].start(false);
        }
        pacman.reset(true);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        Thread main = new Thread() {
        public void run() {
            gameLoop();
            try {
                Thread.sleep(75);
            } catch (InterruptedException e1) {
                System.out.println(e1);
            }
            if (!lost) run();
        }};
        ghostTimer.start();
        main.start();
    }

    public void actionPerformed(ActionEvent e) {    // ghost movements and freeing
        if (audioPlayer.isFinished("start") == true && pacman.isDead() == false) {
            ghostTime += 1;
            for (int i = 0; i<ghosts.length; i++) {
                ghosts[i].getMove();
            }
            if (ghostTime/10 == 5) {     // this section lets the ghosts out of the ghost house
                ghosts[1].start(true);
            }
            if (ghostTime/10 == 10) {
                ghosts[2].start(true);
            }
            if (ghostTime/10 == 15) { 
                ghosts[3].start(true);
            }
        }
    }

    public void gameLoop() {    // main game loop
        pacman.checkHitboxCollision();
        if (audioPlayer.isFinished("start") == true && pacman.isDead() == false && won == false) {  // normal game loop stuff
            modes();
            if (modeTime == 1) {
                audioPlayer.loopSiren(true);
            }
            pacman.move();
        }
        else if (pacman.isDead() == true) {  // when pacman is dead 
            ghostTimer.stop();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e1) {     // sleep so the audio works
                System.out.println(e1);
            }
            if (pacman.getLives() > 0) {    // restart game for your new life
                frightenTime = 0;
                power = false;
                ghostTime = 0;
                if (audioPlayer.isFinished("death") == true) {  // reset pacman and ghosts to continue playing
                    for (int i = 0; i<ghosts.length; i++) {
                        ghosts[i].start(false);
                    }
                    pacman.reset(false);
                    try {
                        Thread.sleep(1250);
                    } catch (InterruptedException e2) {
                        System.out.println(e2);
                    }
                    ghostTimer.restart();
                    audioPlayer.loopSiren(true);
                }
            }
            else {  // game over man
                if (audioPlayer.isFinished("death") == true) {
                    lost = true;
                    panel.gameOver();
                }
            }
        }
        else if (won == true) {  // when you win
            level++;
            ghostTimer.stop();
            stopLoops();
            modeTime = 0;
            frightenTime = 0;
            power = false;
            state = 1;
            ghostTime = 0;
            lost = false;
            map.setDots();
            for (int i = 0; i<ghosts.length; i++) { // reset pacman and ghosts to continue playing
                ghosts[i].start(false);
            }
            pacman.reset(false);
            try {
                Thread.sleep(1600);
            } catch (InterruptedException e2) {
                System.out.println(e2);
            }
            ghostTimer.restart();
            audioPlayer.loopSiren(true);
            won = false;
        }
        panel.setScore(score);
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
            stopLoops();
            audioPlayer.loopSiren(true);
            for (int i = 0; i<ghosts.length; i++) {
                ghosts[i].reAlign();
                if (ghosts[i].getState() != Constants.eaten && ghosts[i].isEaten() == false) {
                    ghosts[i].setState(Constants.chase);
                }
            }
            frightenTime = 0;
            power = false;
            pacman.resetGhosts();
        }
    }

    public void powerPellet() { // set states of ghosts to frightened
        audioPlayer.loopSiren(false);
        audioPlayer.loopPowerPellet(true);
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

    public boolean power() {
        return power;
    }

    public void setScore(double amount, int dots) {    // increment score
        score += amount;
        if (dots == 248) {
            won = true;
        }
        if (score >= 10000) {
            pacman.extraLife();
        }
    }

    public AudioPlayer getAudio() {
        return audioPlayer;
    }

    public void setLost(boolean bool) {
        lost = bool;
    }

    public boolean isLost() {
        return lost;
    }

    public int level() {
        return level;
    }

    public boolean won() {
        return won;
    }

    public void stopLoops() {
        audioPlayer.loopSiren(false);
        audioPlayer.loopPowerPellet(false);
        audioPlayer.loopEyes(false);
    }

}
