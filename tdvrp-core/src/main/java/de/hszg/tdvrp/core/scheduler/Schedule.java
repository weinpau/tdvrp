package de.hszg.tdvrp.core.scheduler;

import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Instance;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a valid schedule for an instance.
 * 
 * @author weinpau
 */
public class Schedule implements Serializable {

    private final List<VehicleSchedule> vehicleSchedules = new ArrayList<>();
    private Instance instance;

    private Schedule() {
    }

    public Schedule(Instance instance, List<VehicleSchedule> vehicleSchedules) {
        this.instance = instance;
        this.vehicleSchedules.addAll(vehicleSchedules);
    }

    /**
     * Returns the individual schedules per used vehicle.
     * 
     * @return schedules per used vehicle
     */
    public List<VehicleSchedule> getVehicleSchedules() {
        return vehicleSchedules;
    }

    /**
     * Returns the associated instance.
     * 
     * @return the associated instance
     */
    public Instance getInstance() {
        return instance;
    }

    /**
     * Returns the total traveling time of this schedule.
     *
     * @return the traveling time
     */
    public double getTravelingTime() {
        double travelingTime = 0;
        for (VehicleSchedule vSchedule : getVehicleSchedules()) {
            double lastDepartureTime = vSchedule.getDepartureTime();
            for (Task task : vSchedule.getTasks()) {
                travelingTime += task.getArrivalTime() - lastDepartureTime;
                lastDepartureTime = task.getDepartureTime();
            }
            travelingTime += vSchedule.getArrivalTime() - lastDepartureTime;
        }
        return travelingTime;
    }

    /**
     * Returns the total distance.
     *
     * @return the total distance
     */
    public double getTotalDistance() {
        Depot depot = instance.getDepot();
        double distance = 0;
        for (VehicleSchedule vSchedule : getVehicleSchedules()) {
            List<Task> task = vSchedule.getTasks();
            if (!task.isEmpty()) {
                distance += depot.getPosition().distance(task.get(0).getCustomer().getPosition());
                for (int i = 1; i < task.size(); i++) {
                    distance += task.get(i - 1).getCustomer().getPosition().distance(task.get(i).getCustomer().getPosition());
                }
                distance += task.get(task.size() - 1).getCustomer().getPosition().distance(depot.getPosition());
            }
        }
        return distance;
    }

}
