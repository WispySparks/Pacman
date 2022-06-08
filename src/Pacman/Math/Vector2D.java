package Pacman.Math;
import java.lang.Math;

public class Vector2D {
    
    private double x = 0;
    private double y = 0;
    private double magnitude = 0;
    private double directionAngle = 0;
    private double slope = 0;

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(double x, double y) {   // vector from x y
        this.x = x;
        this.y = y;
        calculateMag(this);
        directionAngle = directionAngle(this);
        if (this.x != 0) {
            slope = this.y/this.x;
        }
    }

    public Vector2D(double x1, double y1, double x2, double y2) {   // vector at origin
        this.x = x2 - x1;
        this.y = y2 - y1;
        calculateMag(this);
        directionAngle = directionAngle(this);
        if (this.x != 0) {
            slope = this.y/this.x;
        }
    }

    public void calculateMag(Vector2D vector) {
        magnitude = Math.sqrt(((vector.x*vector.x)+(vector.y*vector.y)));
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

    public double getDirectionAngle() {
        return directionAngle;
    }

    public double getSlope() {
        return slope;
    }

    public void normalize() {   // normalize a vector into a unit vector
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

    public static double directionAngle(Vector2D vector) {   // find direction angle of a vector
        double angle = Math.atan((vector.y/vector.x));
        return Math.toDegrees(angle);
    }

    public static Vector2D addition(Vector2D a, Vector2D b) {
        return new Vector2D((a.x+b.x), (a.y+b.y));
    }

    public static Vector2D scaleVector(Vector2D vector, double scale) {
        return new Vector2D((vector.x*scale), (vector.y*scale));
    }

    public static double dotProduct(Vector2D a, Vector2D b) {   // find dot product of two vectors
        return ((a.getX() * b.getX()) + (a.getY() * b.getY()));
    }

    public static double angleBetween(Vector2D a, Vector2D b) {     // find angle between two vectors
        double angle = Math.acos((dotProduct(a, b)/(a.getMagnitude()*b.getMagnitude())));
        return Math.toDegrees(angle);
    }
}