package de.hszg.tdvrp.solver.random;

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
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * @author weinpau
 */
public class RandomSolver implements Solver {

    Random random = new Random();

    @Override
    public String getName() {
        return "RANDOM_SOLVER";
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

        List<Customer> unallocated = new ArrayList<>(instance.getCustomers());
        List<Stack<Customer>> routes = initRoutes(expectedNumberOfVehicles, unallocated);

        for (int i = 0; i < unallocated.size(); i++) {
            Customer customer = unallocated.get(random.nextInt(unallocated.size()));

            int shift = random.nextInt(expectedNumberOfVehicles);
            for (int j = 0; j < expectedNumberOfVehicles; j++) {

                Stack<Customer> route = routes.get((shift + j) % expectedNumberOfVehicles);

                if (isValid(instance, tdFunction, route, customer)) {
                    route.push(customer);
                    unallocated.remove(customer);
                    break;

                }

            }

        }
        if (unallocated.isEmpty()) {
            return Optional.of(
                    new Solution(instance, tdFunction, routes.stream().
                            map(ArrayList::new).
                            map(Route::new).
                            collect(Collectors.toList())));
        } else {
            return Optional.empty();
        }
    }

    private List<Stack<Customer>> initRoutes(int expectedNumberOfVehicles, List<Customer> unallocated) {
        List<Stack<Customer>> routes = new ArrayList<>(expectedNumberOfVehicles);
        for (int i = 0; i < expectedNumberOfVehicles; i++) {
            Stack<Customer> route = new Stack<>();
            route.push(unallocated.remove(random.nextInt(unallocated.size())));
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
