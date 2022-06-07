package Pacman;

import java.awt.Rectangle;

public interface Ghost {
    
    public void setState(int state);

    public int getState();

    public Rectangle getHitbox();

    public void reAlign();

    public void turnAround();

    public void getMove();

    public boolean isEaten();

}
