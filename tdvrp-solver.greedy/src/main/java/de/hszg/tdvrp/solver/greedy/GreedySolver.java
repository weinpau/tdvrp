package de.hszg.tdvrp.solver.greedy;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Numberable;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * A simple solver, which distributes tasks without optimization on the
 * available vehicles.
 *
 * @author weinpau
 */
public class GreedySolver implements Solver {

    @Override
    public String getName() {
        return "GREEDY_SOLVER";
    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction) {

        int v = instance.getAvailableVehicles();

        Optional<Solution> result = solve(instance, tdFunction, v);

        Optional<Solution> next = result;
        while (next.isPresent()) {
            next = solve(instance, tdFunction, v);

            if (next.isPresent()) {
                result = next;
                v--;
            } else {
                break;
            }
        }
        return result;

    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction, int expectedNumberOfVehicles) {

        if (expectedNumberOfVehicles < 0) {
            throw new IllegalArgumentException("The number of vehicles must be greater than zero.");
        }

        PriorityQueue<Customer> unallocated = new PriorityQueue<>((a, b) -> Double.compare(a.getDueTime(), b.getDueTime()));
        unallocated.addAll(instance.getCustomers());

        List<Stack<Customer>> routes = initRoutes(expectedNumberOfVehicles, instance, unallocated);

        while (!unallocated.isEmpty()) {

            Customer next = unallocated.poll();
            Stack<Customer> route = findRoute(routes, next, instance, tdFunction);

            if (route == null) {
                return Optional.empty();
            } else {
                route.push(next);
            }
        }

        return Optional.of(
                new Solution(instance, tdFunction, routes.stream().
                        map(ArrayList::new).
                        map(Route::new).
                        collect(Collectors.toList())));

    }

    private Stack<Customer> findRoute(List<Stack<Customer>> routes, Customer next, Instance instance, TDFunction tdFunction) {
        Stack<Customer> result = null;
        double distance = Double.POSITIVE_INFINITY;

        for (Stack<Customer> candidate : routes) {
            double d = candidate.peek().getPosition().distance(next.getPosition());
            if (isValid(instance, tdFunction, candidate, next) && d < distance) {
                result = candidate;
                distance = d;
            }
        }
        return result;
    }

    private List<Stack<Customer>> initRoutes(int expectedNumberOfVehicles, Instance instance, PriorityQueue<Customer> unallocated) {
        List<Stack<Customer>> routes = new ArrayList<>();
        for (int i = 0; i < Math.min(expectedNumberOfVehicles, instance.getCustomers().size()); i++) {
            Stack<Customer> route = new Stack<>();
            route.push(unallocated.poll());
            routes.add(route);
        }
        return routes;
    }

    private boolean isValid(Instance instance, TDFunction tdFunction, Stack<Customer> route, Customer candidate) {

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
