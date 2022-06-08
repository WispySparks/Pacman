package Pacman.Math;

public class Vector2D {
    
    private double x = 0;
    private double y = 0;
    private double magnitude = 0;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
        calculateMag();
    }

    private void calculateMag() {
        magnitude = Math.sqrt(((x*x)+(y*y)));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public static double dotProduct(Vector2D a, Vector2D b) {
        return ((a.getX() * b.getX()) + (a.getY() * b.getY()));
    }

    public static double angleBetween(Vector2D a, Vector2D b) {
        double angle = Math.acos((dotProduct(a, b)/(a.getMagnitude()*b.getMagnitude())));
        return Math.toDegrees(angle);
    }
}
