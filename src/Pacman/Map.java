package Pacman;

import java.awt.*;

public class Map {

    public final Rectangle[] walls = {new Rectangle(5*8, 6*16, 48, 32), new Rectangle(5*8, 10*16, 48, 16), 
        new Rectangle(15*8, 6*16, 64, 32), new Rectangle(33*8, 6*16, 64, 32), new Rectangle(45*8, 6*16, 48, 32),
        new Rectangle(45*8, 10*16, 48, 16)};

    public Rectangle[] getWalls() {
        return walls;
    }

}
