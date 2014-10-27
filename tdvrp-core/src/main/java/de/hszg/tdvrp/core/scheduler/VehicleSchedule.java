package de.hszg.tdvrp.core.scheduler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an individual schedule for a vehicle.
 *
 * @author weinpau
 */
public final class VehicleSchedule implements Serializable {

    private final List<Task> tasks = new ArrayList<>();

    private double departureTime;
    private double arrivalTime;

    private VehicleSchedule() {
    }

    public VehicleSchedule(double departureTime, double arrivalTime, List<Task> tasks) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.tasks.addAll(tasks);
    }

    /**
     * Returns the tasks of this schedule.
     *
     * @return the tasks
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns the time of arrival at the depot.
     *
     * @return the arrival time
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns the departure time at the depot.
     *
     * @return the departure time
     */
    public double getDepartureTime() {
        return departureTime;
    }

}
