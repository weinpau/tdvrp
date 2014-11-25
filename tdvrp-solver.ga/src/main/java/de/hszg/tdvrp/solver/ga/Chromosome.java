package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class Chromosome {

    Instance instance;
    TDFunction tdFunction;
    GAOptions options;
    int[] route;
    int generation;
    double fitness = -1;

    Chromosome(Instance instance, TDFunction tdFunction, GAOptions options, int[] route, int generation) {
        this.instance = instance;
        this.tdFunction = tdFunction;
        this.options = options;
        this.route = route;
        this.generation = generation;
    }

    public double fitness() {
        if (fitness < 0) {
            double totalDuration = 0;
            Collection<int[]> subRoutes = options.splitter().split(this);
            for (int[] r : subRoutes) {
                totalDuration += assumedTravelTime(r);
            }
            fitness = 1d / ((subRoutes.size() - 1) * instance.getDepot().getClosingTime() + totalDuration);
        }
        return fitness;
    }

    public Collection<Route> routes() {
        List<Customer> allCustomers = instance.getCustomers();
        Collection<Route> routes = new ArrayList<>();
        for (int[] r : options.splitter().split(this)) {
            List<Customer> customers = new ArrayList<>();
            for (int c : r) {
                customers.add(allCustomers.get(c - 1));
            }
            routes.add(new Route(customers));
        }
        return routes;
    }

    public ChromosomePair cross(Chromosome other) {
        return options.crossover().cross(this, other);
    }

    public void mutate() {
        fitness = -1;
        options.mutation().mutate(this);
    }

    public int generation() {
        return generation;
    }

    public Chromosome child() {
        return new Chromosome(instance, tdFunction, options, route, generation + 1);
    }

    public Chromosome copy(int[] route) {
        return new Chromosome(instance, tdFunction, options, route, generation);
    }

    double assumedTravelTime(int[] route) {
        double departureTime = 0;
        double totalTravelTime = 0;
        List<Customer> customers = instance.getCustomers();
        int position = 0;
        for (int customer : route) {
            double travelTime = tdFunction.travelTime(position, customer, departureTime);
            totalTravelTime += travelTime;
            double arrivialTime = departureTime + travelTime;
            position = customer;
            departureTime = Math.max(arrivialTime, customers.get(position - 1).getReadyTime()) + customers.get(position - 1).getServiceTime();
        }
        if (route.length != 0) {
            totalTravelTime += tdFunction.travelTime(route[route.length - 1], 0, departureTime);
        }
        return totalTravelTime;
    }
}
