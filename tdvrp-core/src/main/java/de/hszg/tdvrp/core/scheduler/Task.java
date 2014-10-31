package de.hszg.tdvrp.core.scheduler;

import de.hszg.tdvrp.core.model.Customer;
import java.io.Serializable;

/**
 * Represents a single task for a schedule.
 *
 * @author weinpau
 */
public class Task {

    private final Customer customer;
    private final double arrivalTime, startTime, departureTime;

    public Task(Customer customer, double arrivalTime, double startTime, double departureTime) {
        this.customer = customer;
        this.arrivalTime = arrivalTime;
        this.startTime = startTime;
        this.departureTime = departureTime;
    }

    /**
     * Returns the customer of this task.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the arrival time of this task.
     *
     * @return the arrival time
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns the start time of this task.
     *
     * @return the start time
     */
    public double getStartTime() {
        return startTime;
    }

    /**
     * Returns the departure time of this task.
     *
     * @return the departure time
     */
    public double getDepartureTime() {
        return departureTime;
    }

}
