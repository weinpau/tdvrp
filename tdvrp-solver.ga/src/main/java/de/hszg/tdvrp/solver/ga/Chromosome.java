package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Numberable;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.List;
import java.util.Set;

/**
 *
 * @author weinpau
 */
public class Chromosome {

    Instance instance;
    TDFunction tdFunction;
    int[] route;
    int generation;

    Chromosome(Instance instance, TDFunction tdFunction, int[] route, int generation) {
        this.instance = instance;
        this.tdFunction = tdFunction;
        this.route = route;
        this.generation = generation;
    }

    public ChromosomePair cross(Chromosome other) {
        return new ChromosomePair(this, other);
    }

    public Chromosome mutate() {
        return this;
    }

    public double fitness() {
        double totalDuration = 0;
        for (Route r : routes()) {
            totalDuration += assumedTravelTime(r);
        }
        return 1d / totalDuration;
    }

    public Set<Route> routes() {
        return null;
    }

    public int getGeneration() {
        return generation;
    }
    
    
    
    
    double assumedTravelTime(Route route) {
        double departureTime = 0;
        double totalTravelTime = 0;
        Numberable position = instance.getDepot();
        List<Customer> customers = route.getCustomers();
        for (Customer customer : customers) {
            double travelTime = tdFunction.travelTime(position, customer, departureTime);
            totalTravelTime += travelTime;
            double arrivialTime = departureTime + travelTime;
            position = customer;
            departureTime = Math.max(arrivialTime, customer.getReadyTime()) + customer.getServiceTime();
        }
        if (!customers.isEmpty()) {
            totalTravelTime += tdFunction.travelTime(customers.get(customers.size() - 1), instance.getDepot(), departureTime);
        }
        return totalTravelTime;
    }
}
