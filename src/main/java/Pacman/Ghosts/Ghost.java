package main.java.Pacman.Ghosts;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface Ghost {
    
    public void setState(int state);

    public int getState();

    public Rectangle getHitbox();

    public void reAlign();

    public void turnAround();

    public void getMove();

    public boolean isEaten();

    public BufferedImage updateAnim();

    public int getX();
    
    public int getY();

    public void start(boolean start);
    
}
