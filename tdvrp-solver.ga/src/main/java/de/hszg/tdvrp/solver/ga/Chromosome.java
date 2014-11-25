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
    Splitter splitter;
    int[] route;
    int generation;

    Chromosome(Instance instance, TDFunction tdFunction, Splitter splitter, int[] route, int generation) {
        this.instance = instance;
        this.tdFunction = tdFunction;
        this.splitter = splitter;
        this.route = route;
        this.generation = generation;
    }

    public double fitness() {
        double totalDuration = 0;
        for (int[] r : splitter.split(this)) {
            totalDuration += assumedTravelTime(r);
        }
        return 1d / totalDuration;
    }

    public Collection<Route> routes() {
        List<Customer> allCustomers = instance.getCustomers();
        Collection<Route> routes = new ArrayList<>();
        for (int[] r : splitter.split(this)) {
            List<Customer> customers = new ArrayList<>();
            for (int c : r) {
                customers.add(allCustomers.get(c));
            }
            routes.add(new Route(customers));
        }
        return routes;
    }
    
    

    public int getGeneration() {
        return generation;
    }
    
    public Chromosome copy(int[] route) {
        return new Chromosome(instance, tdFunction, splitter, route, generation);
    }

    double assumedTravelTime(int[] route) {
        double departureTime = 0;
        double totalTravelTime = 0;
        List<Customer> customers = instance.getCustomers();
        int position = 0;
        for (int customer : route) {
            double travelTime = tdFunction.travelTime(0, customer, departureTime);
            totalTravelTime += travelTime;
            double arrivialTime = departureTime + travelTime;
            position = customer;
            departureTime = Math.max(arrivialTime, customers.get(position).getReadyTime()) + customers.get(position).getServiceTime();
        }
        if (route.length != 0) {
            totalTravelTime += tdFunction.travelTime(route[route.length - 1], 0, departureTime);
        }
        return totalTravelTime;
    }
}
