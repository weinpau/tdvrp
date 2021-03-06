package de.hszg.tdvrp.solver.ga.splitter;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.solver.ga.Chromosome;
import gnu.trove.TDoubleArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author weinpau
 */
abstract class DPSplitter implements Splitter {

    @Override
    public Collection<int[]> split(Chromosome chromosome) {
        List<TDoubleArrayList> travelTimes = calculateTravelTimes(chromosome);
        return shortestPath(chromosome.route(), travelTimes);
    }

    abstract Collection<int[]> shortestPath(int[] route, List<TDoubleArrayList> travelTimes);

    List<TDoubleArrayList> calculateTravelTimes(Chromosome chromosome) {
        List<Customer> customers = chromosome.instance().getCustomers();
        List<TDoubleArrayList> result = new ArrayList<>(customers.size());
        for (int i = 0; i < customers.size(); i++) {
            result.add(calculateSubroute(chromosome, i));
        }
        result.add(new TDoubleArrayList());
        return result;
    }

    TDoubleArrayList calculateSubroute(Chromosome chromosome, int from) {
        TDoubleArrayList result = new TDoubleArrayList();
        Instance instance = chromosome.instance();

        List<Customer> customers = instance.getCustomers();
        int[] r = chromosome.route();
        double closingTime = instance.getDepot().getClosingTime();

        int capacity = instance.getVehicleCapacity();
        double time = 0;
        TDFunction tdFunction = chromosome.tdFunction();

        for (int i = from; i < customers.size(); i++) {

            time += tdFunction.travelTime(i - from == 0 ? 0 : r[i - 1], r[i], time);

            Customer c = customers.get(r[i] - 1);
            double startTime = Math.max(c.getReadyTime(), time);
            double departureTime = startTime + c.getServiceTime();
            double depotTravelTime = tdFunction.travelTime(r[i], 0, departureTime);

            if (capacity - c.getDemand() >= 0 && startTime < c.getDueTime() && departureTime + depotTravelTime <= closingTime) {
                time = departureTime;
                capacity -= c.getDemand();
                result.add(time + depotTravelTime);
            } else {
                break;
            }
        }
        return result;
    }

}
