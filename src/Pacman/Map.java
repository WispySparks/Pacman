package Pacman;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Map {

    private final GameController controller;
    private final File cherryFile = new File("./resources/images/cherries.png");
    private BufferedImage cherryImage; 
    private int dotsate = 0;
    public final Rectangle[] walls = {new Rectangle(5*8, 6*16, 48, 32), new Rectangle(5*8, 10*16, 48, 16), 
        new Rectangle(15*8, 6*16, 64, 32), new Rectangle(33*8, 6*16, 64, 32), new Rectangle(45*8, 6*16, 48, 32),
        new Rectangle(45*8, 10*16, 48, 16), new Rectangle(21*8, 10*16, 112, 16), new Rectangle(27*8, 4*16, 16, 64), 
        new Rectangle(0*8, 3*16, 448, 16), new Rectangle(0*8, 3*16, 8, 160), new Rectangle(0*8, 13*16, 88, 64), 
        new Rectangle(15*8, 10*16, 16, 112), new Rectangle(39*8, 10*16, 16, 112), new Rectangle(55*8, 3*16, 8, 160), 
        new Rectangle(45*8, 13*16, 96, 64), new Rectangle(15*8, 13*16, 64, 16), new Rectangle(33*8, 13*16, 64, 16),
        new Rectangle(27*8, 10*16, 16, 64), new Rectangle(0*8, 19*16, 88, 64), new Rectangle(15*8, 19*16, 16, 64), 
        new Rectangle(39*8, 19*16, 16, 64), new Rectangle(45*8, 19*16, 96, 64), new Rectangle(27*8, 22*16, 16, 64), 
        new Rectangle(21*8, 22*16, 112, 16), new Rectangle(0*8, 23*16, 8, 176), new Rectangle(0*8, 34*16, 448, 16), 
        new Rectangle(55*8, 23*16, 8, 176), new Rectangle(0*8, 28*16, 40, 16),new Rectangle(51*8, 28*16, 40, 16), 
        new Rectangle(5*8, 25*16, 48, 16), new Rectangle(15*8, 25*16, 64, 16),new Rectangle(33*8, 25*16, 64, 16), 
        new Rectangle(45*8, 25*16, 48, 16), new Rectangle(45*8, 25*16, 16, 64), new Rectangle(9*8, 25*16, 16, 64), 
        new Rectangle(21*8, 28*16, 112, 16), new Rectangle(5*8, 31*16, 144, 16), new Rectangle(33*8, 31*16, 144, 16), 
        new Rectangle(15*8, 28*16, 16, 48), new Rectangle(39*8, 28*16, 16, 48),new Rectangle(27*8, 29*16, 16, 48), 
        new Rectangle(21*8, 16*16, 8, 64), new Rectangle(21*8, 19*16, 112, 16), new Rectangle(34*8, 16*16, 8, 64), /*this line and below is the ghost house walls*/
        new Rectangle(21*8, 16*16, 40, 16), new Rectangle(30*8, 16*16, 40, 16), new Rectangle(26*8, 16*16, 32, 16)}; // last rectangle is the door
    private final Rectangle[] tps = {new Rectangle(-5*8, 17*16, 16, 32), new Rectangle(59*8, 17*16, 16, 32)};
    private final Rectangle[] blanks = {new Rectangle(14*8, (12*16)+8, 216, 176), new Rectangle(0*8, 17*16, 96, 16), 
        new Rectangle(44*8, 17*16, 96, 16), new Rectangle(13*16, 26 * 16, 32, 32)};
    private Rectangle[] dots = new Rectangle[244];
    private Rectangle[] bigDots;
    private Rectangle cherry = new Rectangle(13*16+8, 19*16+16, 0, 0);
    private boolean ateCherry = false;
    
    Map(GameController controller) {
        this.controller = controller;
        try {
            cherryImage = ImageIO.read(cherryFile);
        } catch (Exception e) {
            System.out.println(e);
        }
        setDots();
    }

    public void setDots() {     // set up dots for the map
        dotsate = 0;
        cherry.width = 0;
        cherry.height = 0;
        ateCherry = false;
        bigDots = new Rectangle[]{new Rectangle(1*16-2, 6*16 + 6, 16, 16), new Rectangle(26*16-1, 6*16 + 6, 16, 16), new Rectangle(1*16-2, 26*16 + 4, 16, 16), new Rectangle(26*16-1, 26*16 + 4, 16, 16)};
        int rollOver = 0;
        int yPos = 4;
        int xPos = 1;
        for (int i = 0; i < dots.length;) {
            Rectangle rect = new Rectangle((xPos*16)+4, (yPos*16)+12, 4, 4);  // create new dot to check
            if (!checkWallCollision(4, rect.x, rect.y, Constants.baseSpeed, true) && !checkBlanks(rect)) { // check if dot will be in a wall or blank spaces
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

    public Rectangle[] getBigDots() {
        return bigDots;
    }

    public Boolean isCherry() {
        if (cherry.width != 0 && cherry.height != 0) {
            return true;
        }
        return false;
    }

    public BufferedImage cherryImage() {
        return cherryImage;
    }

    public void eatDot(Rectangle rect) {    // code for eating a dot
        Rectangle space = new Rectangle(0, 0, 0, 0);
        rect.y = rect.y+4;
        for (int i = 0; i<dots.length; i++) {
            if (dots[i].intersects(rect)) {
                dotsate++;
                controller.getAudio().playWaka();
                controller.setScore(10, dotsate);
                dots[i] = space;
            }
            if (i < 4 && bigDots[i].intersects(rect)) {
                dotsate++;
                controller.setScore(50, dotsate);
                bigDots[i] = space;
                controller.powerPellet();    // sets the ghosts to frightened mode
            }
            if (cherry.intersects(rect)) {
                cherry.width = 0;
                cherry.height = 0;
                ateCherry = true;
                controller.getAudio().playFruit();
                controller.setScore(400, 0);
            }
        }
        if (dotsate >= 124) {
            if (ateCherry == false) {
                cherry.width = 16;
                cherry.height = 16;
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

    public boolean checkWallCollision(int direction, int xPos, int yPos, int speed, boolean door) {  // check if something collides with the walls
        int wallTempX = xPos;
        int wallTempY = yPos;
        Rectangle wallHitbox = new Rectangle(wallTempX, wallTempY, 16, 16);
        switch (direction) {
            case Constants.right: // right
                wallTempX = xPos + 16 + speed; // 24 default
                wallTempY = yPos + 8;
                wallHitbox.height = 32;
                wallHitbox.width = 16;
                break;
            case Constants.down: // down
                wallTempY = yPos + 24 + speed; // 32 default
                wallTempX = xPos;
                wallHitbox.width = 32;
                wallHitbox.height = 16;
                break;
            case Constants.left: // left
                wallTempX = xPos - speed; // -8 default
                wallTempY = yPos + 8;
                wallHitbox.height = 32;
                wallHitbox.width = 16;
                break;
            case Constants.up: // up
                wallTempY = yPos + 8 - speed; // 0 default
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
                if (i == walls.length-1 && door == false) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

}
