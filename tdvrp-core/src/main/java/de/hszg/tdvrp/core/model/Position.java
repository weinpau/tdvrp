package de.hszg.tdvrp.core.model;

import java.io.Serializable;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * This class represents a position with XY coordinates.
 *
 * @author weinpau
 */
public final class Position implements Serializable {

    private double x, y;

    private Position() {
    }

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate.
     *
     * @return x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     *
     * @return y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Calculates the distance between the given position.
     *
     * @param other the position to which the distance is to be determined
     * @return the distance
     */
    public double distance(Position other) {
        return sqrt(pow(x - other.x, 2) + pow(y - other.y, 2));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        return Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y);
    }

    @Override
    public String toString() {
        return "Position{" + "x=" + x + ", y=" + y + '}';
    }
    
    

}
