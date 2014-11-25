package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Customer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class StraightSplitter implements Splitter {

    @Override
    public Collection<int[]> split(Chromosome chromosome) {
        Collection<int[]> result = new ArrayList<>(chromosome.instance.getVehicleCapacity());
        List<Customer> customers = chromosome.instance.getCustomers();
        int[] r = chromosome.route;
        double closingTime = chromosome.instance.getDepot().getClosingTime();

        int from = 0;
        while (from < customers.size()) {

            int to = from;
            int capacity = chromosome.instance.getVehicleCapacity();
            double time = 0;

            for (int i = from; i < customers.size(); i++) {

                time += chromosome.tdFunction.travelTime(i - from == 0 ? 0 : r[i - 1], r[i], time);

                Customer c = customers.get(r[i] - 1);
                double startTime = Math.max(c.getReadyTime(), time);
                double departureTime = startTime + c.getServiceTime();
                double depotTravelTime = chromosome.tdFunction.travelTime(r[i], 0, departureTime);

                if (capacity - c.getDemand() >= 0 && startTime < c.getDueTime() && departureTime + depotTravelTime <= closingTime) {
                    time = departureTime;
                    capacity -= c.getDemand();
                    to = i;
                } else {
                    break;
                }
            }
            result.add(Arrays.copyOfRange(r, from, to + 1));
            from = to + 1;
        }
        return result;
    }

}
