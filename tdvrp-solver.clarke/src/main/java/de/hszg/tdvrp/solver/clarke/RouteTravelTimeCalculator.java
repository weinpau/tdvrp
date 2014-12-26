package de.hszg.tdvrp.solver.clarke;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class RouteTravelTimeCalculator {

    Instance instance;
    TDFunction tdFunction;

    public RouteTravelTimeCalculator(Instance instance, TDFunction tdFunction) {
        this.instance = instance;
        this.tdFunction = tdFunction;
    }

    public double travelTime(Route route) {
        return travelTime(route.getCustomers().stream().mapToInt(c -> c.getNumber()).toArray());
    }

    public double travelTime(int[] route) {

        double time = 0;
        List<Customer> customers = instance.getCustomers();
        int position = 0;
        int remainingCapacity = instance.getVehicleCapacity();

        for (int r : route) {

            double arrivialTime = tdFunction.travelTime(position, r, time) + time;
            position = r;
            Customer customer = customers.get(position - 1);

            if (arrivialTime > customer.getDueTime()) {
                return Double.NaN;
            }
            double startTime = Math.max(arrivialTime, customer.getReadyTime());
            time = startTime + customer.getServiceTime();

            remainingCapacity -= customer.getDemand();
            if (remainingCapacity < 0) {
                return Double.NaN;
            }
        }
        if (route.length != 0) {
            time += tdFunction.travelTime(route[route.length - 1], 0, time);
        }
        if (time <= instance.getDepot().getClosingTime()) {
            return time;
        } else {
            return Double.NaN;
        }
    }

}
