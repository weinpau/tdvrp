package de.hszg.tdvrp.core.scheduler;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Instance;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a valid schedule for an instance.
 *
 * @author weinpau
 */
public class Schedule {

    private final List<VehicleSchedule> vehicleSchedules = new ArrayList<>();
    private final Instance instance;

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
     * Returns the total travel time of this schedule.
     *
     * @return the traveling time
     */
    public double getTravelTime() {
        double travelTime = 0;
        for (VehicleSchedule vSchedule : getVehicleSchedules()) {
            double lastDepartureTime = vSchedule.getDepartureTime();
            for (Task task : vSchedule.getTasks()) {
                travelTime += task.getArrivalTime() - lastDepartureTime;
                lastDepartureTime = task.getDepartureTime();
            }
            travelTime += vSchedule.getArrivalTime() - lastDepartureTime;
        }
        return travelTime;
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

    /**
     * Checks the validity of this schedule.
     *
     * @return {@code true] if this schedule is valid
     */
    public boolean isValid() {
        Set<Customer> customers = new HashSet<>(instance.getCustomers());

        for (VehicleSchedule vSchedule : getVehicleSchedules()) {

            if (vSchedule.getDepartureTime() < 0 || vSchedule.getArrivalTime() > instance.getDepot().getClosingTime()) {
                return false;
            }

            if (vSchedule.getTasks().stream().
                    map(t -> t.getCustomer()).
                    mapToDouble(c -> c.getDemand()).
                    sum() > instance.getVehicleCapacity()) {
                return false;
            }

            for (Task task : vSchedule.getTasks()) {
                Customer c = task.getCustomer();
                if (!customers.remove(c) || !task.isValid()) {
                    return false;
                }
            }
        }

        return customers.isEmpty();
    }

}
