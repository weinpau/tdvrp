package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.solver.ga.splitter.Splitter;
import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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

    static Random random = new Random();

    public Chromosome(Instance instance, TDFunction tdFunction, GAOptions options, int[] route, int generation) {
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
            fitness = 1d / (subRoutes.size() + totalDuration / instance.getDepot().getClosingTime());
        }
        return fitness;
    }

    public int hammingDistance(Chromosome other) {
        int distance = 0;
        for (int i = 0; i < route.length; i++) {
            if (route[i] != other.route[i]) {
                distance++;
            }
        }
        return distance;

    }

    public Instance instance() {
        return instance;
    }

    public TDFunction tdFunction() {
        return tdFunction;
    }
        

    public int[] route() {
        return route;
    }

    public void route(int[] route) {
        this.route = route;
    }

    public Collection<Route> routes(Splitter splitter) {
        List<Customer> allCustomers = instance.getCustomers();
        Collection<Route> routes = new ArrayList<>();
        for (int[] r : splitter.split(this)) {
            List<Customer> customers = new ArrayList<>();
            for (int c : r) {
                customers.add(allCustomers.get(c - 1));
            }
            routes.add(new Route(customers));
        }
        return routes;
    }

    public Chromosome cross(Chromosome other) {
        int crossover = random.nextInt(options.crossovers().length);
        ChromosomePair pair = options.crossovers()[crossover].cross(this, other);
        if (pair.left().fitness() > pair.right().fitness()) {
            return pair.left();
        } else {
            return pair.right();
        }
    }

    public void mutate() {
        int mutation = random.nextInt(options.mutations().length);
        options.mutations()[mutation].mutate(this);
        fitness = -1;
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
