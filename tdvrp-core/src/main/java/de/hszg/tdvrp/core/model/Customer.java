package de.hszg.tdvrp.core.model;

import java.io.Serializable;

/**
 * This class represents a customer with a specific location and details on
 * demand and time window of his order.
 *
 * @author weinpau
 */
public class Customer implements Numberable {

    private final int number;
    private final Position position;

    private final double demand;
    private final double readyTime;
    private final double dueTime;
    private final double serviceTime;
    
    public Customer(int number, Position position, double demand, double readyTime, double dueTime, double serviceTime) {
        this.number = number;
        this.position = position;
        this.demand = demand;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
        this.serviceTime = serviceTime;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public Position getPosition() {
        return position;
    }

    public double getDemand() {
        return demand;
    }

    public double getReadyTime() {
        return readyTime;
    }

    public double getDueTime() {
        return dueTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.number;
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
        final Customer other = (Customer) obj;
        return this.number == other.number;
    }

}
