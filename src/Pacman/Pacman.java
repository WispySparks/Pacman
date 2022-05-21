package Pacman;

import java.awt.*;

public class Pacman {
    
    private int xPos = 21 * 16;
    private int yPos = 20 * 16;
    private int tempX = xPos;
    private int tempY = yPos;
    private Rectangle[] walls;
    Rectangle tempac = new Rectangle(tempX, tempY, 16, 16);
    boolean isDead = false;

    Pacman(Map map) {
        this.walls = map.getWalls();
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public boolean checkCollision(int direction) {
        switch (direction) {
                case 0:
                    tempY = yPos - 16;
                    tempX = xPos;
                    break;
                case 1:
                    tempX = xPos + 16;
                    tempY = yPos;
                    break;
                case 2:
                    tempY = yPos + 16;
                    tempX = xPos;
                    break;
                case 3:
                    tempX = xPos - 16;
                    tempY = yPos;
                    break;
            } 
        tempac.x = tempX;
        tempac.y = tempY;
        for (int i = 0; i<walls.length; i++) {
            if (tempac.intersects(walls[i])) {
                return true;
            }
        }
        return false;
    }

    public void up() {
        yPos -= 8;
    }

    public void down() {
        yPos += 8;
    }

    public void right() {
        xPos += 8;
    }

    public void left() {
        xPos -= 8;
    }

}
