package Pacman;

import java.awt.*;

public class Map {

    private final GamePanel panel;
    private final AudioPlayer audioPlayer = new AudioPlayer();
    private final Rectangle[] walls = {new Rectangle(5*8, 6*16, 48, 32), new Rectangle(5*8, 10*16, 48, 16), 
        new Rectangle(15*8, 6*16, 64, 32), new Rectangle(33*8, 6*16, 64, 32), new Rectangle(45*8, 6*16, 48, 32),
        new Rectangle(45*8, 10*16, 48, 16), new Rectangle(21*8, 10*16, 112, 16), new Rectangle(27*8, 4*16, 16, 64), 
        new Rectangle(0*8, 3*16, 448, 16), new Rectangle(0*8, 3*16, 8, 160), new Rectangle(0*8, 13*16, 88, 64), 
        new Rectangle(15*8, 10*16, 16, 112), new Rectangle(39*8, 10*16, 16, 112), new Rectangle(55*8, 3*16, 8, 160), 
        new Rectangle(45*8, 13*16, 96, 64), new Rectangle(15*8, 13*16, 64, 16), new Rectangle(33*8, 13*16, 64, 16),
        new Rectangle(27*8, 10*16, 16, 64), new Rectangle(21*8, 16*16, 112, 64), new Rectangle(0*8, 19*16, 88, 64), 
        new Rectangle(15*8, 19*16, 16, 64), new Rectangle(39*8, 19*16, 16, 64), new Rectangle(45*8, 19*16, 96, 64), 
        new Rectangle(27*8, 22*16, 16, 64), new Rectangle(21*8, 22*16, 112, 16), new Rectangle(0*8, 23*16, 8, 176), 
        new Rectangle(0*8, 34*16, 448, 16), new Rectangle(55*8, 23*16, 8, 176), new Rectangle(0*8, 28*16, 40, 16),
        new Rectangle(51*8, 28*16, 40, 16), new Rectangle(5*8, 25*16, 48, 16), new Rectangle(15*8, 25*16, 64, 16),
        new Rectangle(33*8, 25*16, 64, 16), new Rectangle(45*8, 25*16, 48, 16), new Rectangle(45*8, 25*16, 16, 64), 
        new Rectangle(9*8, 25*16, 16, 64), new Rectangle(21*8, 28*16, 112, 16), new Rectangle(5*8, 31*16, 144, 16), 
        new Rectangle(33*8, 31*16, 144, 16), new Rectangle(15*8, 28*16, 16, 48), new Rectangle(39*8, 28*16, 16, 48),
        new Rectangle(27*8, 29*16, 16, 48)};
    private final Rectangle[] tps = {new Rectangle(-5*8, 17*16, 16, 32), new Rectangle(59*8, 17*16, 16, 32)};
    private final Rectangle[] blanks = {new Rectangle(14*8, (12*16)+8, 216, 176), new Rectangle(0*8, 17*16, 96, 16), 
        new Rectangle(44*8, 17*16, 96, 16), new Rectangle(13*16, 26 * 16, 32, 32)};
    private Rectangle[] dots = new Rectangle[244];
    
    Map(GamePanel panel) {
        this.panel = panel;
        setDots();
    }

    public void setDots() {     // set up dots for the map
        int rollOver = 0;
        int yPos = 4;
        int xPos = 1;
        for (int i = 0; i < dots.length;) {
            Rectangle rect = new Rectangle((xPos*16)+4, (yPos*16)+12, 4, 4);  // create new dot to check
            if (!checkWallCollision(4, rect.x, rect.y) && !checkBlanks(rect)) { // check if dot will be in a wall or blank spaces
                dots[i] = rect;
                i++;
            }
            rollOver = (rollOver+1) % 27; // counter to know when to shift to next y level
            xPos++;
            if (rollOver == 26) {
                yPos++;
                xPos = 0;
            }
        }
    }

    public boolean checkBlanks(Rectangle rect) { // checks if a rectangle intersects with the blank parts of the map
        for (int i = 0; i < blanks.length; i++) {
            if (blanks[i].intersects(rect)) {
                return true;
            }
        }
        return false;
    }

    public Rectangle[] getDots() {
        return dots;
    }

    public void eatDot(Rectangle rect) {    // code for eating a dot
        Rectangle space = new Rectangle(0, 0, 0, 0);
        for (int i = 0; i<dots.length; i++) {
            if (dots[i].intersects(rect)) {
                audioPlayer.playWaka();
                panel.setScore(10);
                dots[i] = space;
            }
        }
    }

    public int checkTps(Rectangle rect) { // checks if a rectangle intersects with the teleports
        if (tps[0].intersects(rect)) {
            return 1;
        }
        if (tps[1].intersects(rect)) {
            return 2;
        }
        return 0;
    }

    public boolean checkWallCollision(int direction, int xPos, int yPos) {
        int wallTempX = xPos;
        int wallTempY = yPos;
        Rectangle wallHitbox = new Rectangle(wallTempX, wallTempY, 16, 16);
        switch (direction) {
            case 0: // right
                wallTempX = xPos + 24;
                wallTempY = yPos;
                wallHitbox.height = 32;
                wallHitbox.width = 16;
                break;
            case 1: // down
                wallTempY = yPos + 24;
                wallTempX = xPos;
                wallHitbox.width = 32;
                wallHitbox.height = 16;
                break;
            case 2: // left
                wallTempX = xPos - 8;
                wallTempY = yPos;
                wallHitbox.height = 32;
                wallHitbox.width = 16;
                break;
            case 3: // up
                wallTempY = yPos - 8;
                wallTempX = xPos;
                wallHitbox.width = 32;
                wallHitbox.height = 16;
                break;
            case 4: // by itself
                wallTempX = xPos;
                wallTempY = yPos;
                wallHitbox.width = 16;
                wallHitbox.height = 16;
        } 
        wallHitbox.x = wallTempX;
        wallHitbox.y = wallTempY;
        for (int i = 0; i<walls.length; i++) {
            if (wallHitbox.intersects(walls[i])) {
                return true;
            }
        }
        return false;
    }

}
