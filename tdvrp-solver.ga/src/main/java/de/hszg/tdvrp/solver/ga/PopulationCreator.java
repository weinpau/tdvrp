package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Numberable;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author weinpau
 */
public final class PopulationCreator {

    private static final Random random = new Random();

    public static Population createPopulation(Instance instance, TDFunction tdFunction, GAOptions options) {

        int assumedVehicleCount = assumedVehicleCount(instance, tdFunction);
        List<Chromosome> chromosomes = new ArrayList<>(options.populationSize());

        for (int i = 0; i < options.populationSize(); i++) {
            Chromosome chromosome = createChromosome(instance, tdFunction, options, assumedVehicleCount);
            chromosome.mutate();
            chromosomes.add(chromosome);
        }
        return new Population(chromosomes);

    }

    private static Chromosome createChromosome(Instance instance, TDFunction tdFunction, GAOptions options, int assumedVehicleCount) {
     
        PriorityQueue<Customer> unallocated = new PriorityQueue<>((a, b)
                -> Double.compare(a.getDueTime(), b.getDueTime()));
        unallocated.addAll(instance.getCustomers());
        List<Stack<Customer>> routes = initRoutes(assumedVehicleCount, instance, unallocated);
        while (!unallocated.isEmpty()) {

            Customer next = unallocated.poll();
            Stack<Customer> route = findRoute(routes, next, instance, tdFunction, options.initPopulationVariance());
            if (route != null) {
                route.push(next);
            } else {
                Stack<Customer> newRoute = new Stack<>();
                newRoute.add(next);
                routes.add(newRoute);
            }
        }
        int[] route = new int[instance.getCustomers().size()];
        int i = 0;
        while (!routes.isEmpty()) {
            int pos = routes.size() > 1 ? random.nextInt(routes.size() - 1) : 0;
            Stack<Customer> stack = routes.remove(pos);
            for (Customer c : stack) {              
                    route[i] = c.getNumber();
                    i++;             
            }
        }
        return new Chromosome(instance, tdFunction, options, route, 1);
    }

    public static int assumedVehicleCount(Instance instance, TDFunction tdFunction) {

        int assumedVehicleCount = instance.getAvailableVehicles();
        boolean valid = true;
        while (assumedVehicleCount > 0 && valid) {
            PriorityQueue<Customer> unallocated = new PriorityQueue<>((a, b) -> Double.compare(a.getDueTime(), b.getDueTime()));
            unallocated.addAll(instance.getCustomers());

            List<Stack<Customer>> routes = initRoutes(assumedVehicleCount - 1, instance, unallocated);

            while (!unallocated.isEmpty()) {

                Customer next = unallocated.poll();
                Stack<Customer> route = findRoute(routes, next, instance, tdFunction, 1d);
                if (route != null) {
                    route.push(next);
                } else {
                    valid = false;
                    break;

                }
            }
            if (valid) {
                assumedVehicleCount--;
            }
        }
        return assumedVehicleCount;

    }

    private static Stack<Customer> findRoute(List<Stack<Customer>> routes, Customer next, Instance instance, TDFunction tdFunction, double threshold) {

        double distance = Double.POSITIVE_INFINITY;

        for (Stack<Customer> candidate : routes) {
            double d = candidate.peek().getPosition().distance(next.getPosition());
            if (isValid(instance, tdFunction, candidate, next) && d < distance) {

                distance = d;
            }
        }
        List<Stack<Customer>> results = new ArrayList<>();

        for (Stack<Customer> candidate : routes) {
            double d = candidate.peek().getPosition().distance(next.getPosition());
            if (isValid(instance, tdFunction, candidate, next) && d <= distance * threshold) {
                results.add(candidate);
            }
        }

        if (results.isEmpty()) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            return results.get(random.nextInt(results.size() - 1));
        }
    }

    private static List<Stack<Customer>> initRoutes(int expectedNumberOfVehicles, Instance instance, PriorityQueue<Customer> unallocated) {
        List<Stack<Customer>> routes = new ArrayList<>();
        for (int i = 0; i < Math.min(expectedNumberOfVehicles, instance.getCustomers().size()); i++) {
            Stack<Customer> route = new Stack<>();
            route.push(unallocated.poll());
            routes.add(route);
        }
        return routes;
    }

    private static boolean isValid(Instance instance, TDFunction tdFunction, Stack<Customer> route, Customer candidate) {

        double time = 0;
        int remainingCapacity = instance.getVehicleCapacity();
        Numberable position = instance.getDepot();
        for (Customer c : route) {
            time += tdFunction.travelTime(position, c, time);
            position = c;
            time = Math.max(c.getReadyTime(), time) + c.getServiceTime();
            remainingCapacity -= c.getDemand();
        }

        if (remainingCapacity - candidate.getDemand() < 0) {
            return false;
        }

        time += tdFunction.travelTime(position, candidate, time);
        time = Math.max(candidate.getReadyTime(), time);

        if (time > candidate.getDueTime()) {
            return false;
        }
        time += candidate.getServiceTime();
        time += tdFunction.travelTime(candidate, instance.getDepot(), time);
        return time <= instance.getDepot().getClosingTime();
    }

}
