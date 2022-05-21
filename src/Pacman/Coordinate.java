package Pacman;

public class Coordinate {

    private int x;
    private int y;
    
    Coordinate(int x, int y) {
        this.x = x * 8;
        this.y = y * 8;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
