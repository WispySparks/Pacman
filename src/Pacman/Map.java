package Pacman;

import java.awt.*;

public class Map {

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

    public Rectangle[] getWalls() {
        return walls;
    }

    public int checkTps(Rectangle hitbox) {
        if (tps[0].intersects(hitbox)) {
            return 1;
        }
        if (tps[1].intersects(hitbox)) {
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
