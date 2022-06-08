package Pacman.Math;

public class Vector2D {
    
    private double x = 0;
    private double y = 0;
    private double magnitude = 0;

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(double x, double y) {   // vector from x y
        this.x = x;
        this.y = y;
        calculateMag();
    }

    public Vector2D(double x1, double y1, double x2, double y2) {   // vector at origin
        this.x = x2 - x1;
        this.y = y2 - y1;
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

    public void normalize() {
        if (magnitude != 0) {
            x = x / magnitude;
            y = y / magnitude;
        }
    }

    public Vector2D reflection(int degrees) {
        switch (degrees) {
            case 90: return new Vector2D(-x, y);
            case -90: return new Vector2D(x, -y);
            case 180: return new Vector2D(-x, -y);
            default: return this;
        }
    }

    public static double dotProduct(Vector2D a, Vector2D b) {
        return ((a.getX() * b.getX()) + (a.getY() * b.getY()));
    }

    public static double angleBetween(Vector2D a, Vector2D b) {
        double angle = Math.acos((dotProduct(a, b)/(a.getMagnitude()*b.getMagnitude())));
        return Math.toDegrees(angle);
    }
}
