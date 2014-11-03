package de.hszg.tdvrp.scheduler.straight;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Numberable;
import de.hszg.tdvrp.core.scheduler.Schedule;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.scheduler.Task;
import de.hszg.tdvrp.core.scheduler.VehicleSchedule;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This scheduler creates a schedule for a solution. The resulting schedule is
 * directly and is determined without optimizing the travel time.
 *
 * @author weinpau
 */
public class StraightScheduler implements Scheduler {

    @Override
    public String getName() {
        return "STRAIGHT";
    }

    @Override
    public Optional<Schedule> schedule(Solution solution) {

        List<VehicleSchedule> vehicleSchedules = new ArrayList<>();
        for (Route route : solution.getRoutes()) {
            VehicleSchedule vSchedule = createVehicleSchedule(route, solution);
            if (vSchedule == null) {
                return Optional.empty();
            } else {
                vehicleSchedules.add(vSchedule);
            }
        }

        return Optional.of(new Schedule(solution.getInstance(), vehicleSchedules));

    }

    private VehicleSchedule createVehicleSchedule(Route route, Solution solution) {
        Depot depot = solution.getInstance().getDepot();
        TDFunction tdFunction = solution.getTDFunction();
        List<Customer> customers = route.getCustomers();

        double time = 0;
        Numberable position = depot;

        List<Task> tasks = new ArrayList<>();

        for (Customer customer : customers) {

            double arrivialTime = tdFunction.tavelTime(position, customer, time) + time;
            position = customer;
            if (arrivialTime > customer.getDueTime()) {
                return null;
            }
            double startTime = Math.max(arrivialTime, customer.getReadyTime());
            time = startTime + customer.getServiceTime();
            tasks.add(new Task(customer, arrivialTime, startTime, time));
        }
        if (!customers.isEmpty()) {
            time += tdFunction.tavelTime(customers.get(customers.size() - 1), depot, time);
        }
        return new VehicleSchedule(0, time, tasks);
    }

}
