package de.hszg.tdvrp.core.model;

/**
 * This class represents the depot.
 *
 * @author weinpau
 */
public class Depot implements Numberable {

    private final Position position;
    private final double closingTime;

    public Depot(Position position, double closingTime) {
        this.position = position;
        this.closingTime = closingTime;
    }

    /**
     * Returns the position of the depot.
     *
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the closing time of the depot.
     *
     * @return the closing time
     */
    public double getClosingTime() {
        return closingTime;
    }

    @Override
    public int getNumber() {
        return 0;
    }

}
