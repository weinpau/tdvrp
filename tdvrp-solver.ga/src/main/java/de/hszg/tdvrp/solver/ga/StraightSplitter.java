package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Customer;
import java.util.ArrayList;
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

        int from = 1;
        while (from < customers.size()) {
            
            int to = from;
            int capacity = chromosome.instance.getVehicleCapacity();
            double time = 0;

            for (int i = from; i < customers.size(); i++) {

                time = chromosome.tdFunction.travelTime(i - 1, i, time);

                Customer c = customers.get(i);
                double startTime = Math.max(c.getReadyTime(), time);
                double departureTime = startTime + c.getServiceTime();
                double depotTravelTime = chromosome.tdFunction.travelTime(i, 0, departureTime);

                if (capacity - c.getDemand() >= 0 && startTime < c.getDueTime() && departureTime + depotTravelTime <= chromosome.instance.getDepot().getClosingTime()) {
                    time = departureTime;
                    capacity -= c.getDemand();
                    to = i;
                } else {
                    break;
                }
            }
            result.add(createSubRoute(from, to, customers));
            from = to + 1;
        }
        return result;
    }

    private int[] createSubRoute(int from, int to, List<Customer> customers) {
        int[] route = new int[(to - from) + 1];
        for (int i = from; i <= to; i++) {
            route[i - from] = customers.get(i).getNumber();
        }
        return route;
    }

}
