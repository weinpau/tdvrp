package de.hszg.tdvrp.solver.dummy;

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
 * This solver distributed to customers anywhere on the available or specified
 * vehicles, without performing any optimization.
 *
 * @author weinpau
 */
public class DummySolver implements Solver {

    @Override
    public String getName() {
        return "DUMMY_SOLVER";
    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction) {
        return solve(instance, tdFunction, instance.getAvailableVehicles());

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
        Numberable position = instance.getDepot();
        for (Customer c : route) {
            time += tdFunction.tavelingTime(position, c, time);
            position = c;
            time = Math.max(c.getReadyTime(), time) + c.getServiceTime();

        }
        time += tdFunction.tavelingTime(position, candidate, time);
        time = Math.max(candidate.getReadyTime(), time);

        if (time > candidate.getDueTime()) {
            return false;
        }
        time += candidate.getServiceTime();
        time += tdFunction.tavelingTime(candidate, instance.getDepot(), time);
        return time <= instance.getDepot().getClosingTime();
    }

}