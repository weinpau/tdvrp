package de.hszg.tdvrp.solver.clarke;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.solver.clarke.HobQueue.Hob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
public class ClarkeWrightSolver implements Solver {

    @Override
    public String getName() {
        return "CLARKE_WRIGHT_SOLVER";
    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction) {

        List<Hob> hobs = new ArrayList<>();
        int[] route = instance.getCustomers().stream().mapToInt(c -> c.getNumber()).toArray();

        while (route.length > 0) {

            HobQueue queue = new HobQueue(instance, tdFunction, route);
            Hob bestHob = null;

            if (queue.isEmpty()) {
                break;
            }
            while (!queue.isEmpty()) {
                Hob hob = queue.poll();
                for (Hob h : queue) {
                    Hob connected = hob.connect(h);
                    if (connected != null) {
                        queue.remove(h);
                        queue.offer(connected);
                        break;
                    }
                }

                if (bestHob == null || bestHob.route().length < hob.route().length || (bestHob.route().length == hob.route().length && bestHob.saving() < hob.saving())) {
                    bestHob = hob;
                }
            }
            if (bestHob != null) {
                hobs.add(bestHob);
                route = reduceRoute(route, bestHob);
            }
        }
        Collection<Route> routes = createRoutes(instance, hobs, route);

        System.out.println("improve routes: " + improveRoutes(routes, new RouteTravelTimeCalculator(instance, tdFunction)));

        Solution solution = new Solution(instance, tdFunction, routes);
        return Optional.of(solution);
    }

    private Collection<Route> createRoutes(Instance instance, List<Hob> hobs, int[] route) {
        Collection<Route> result = new ArrayList<>();
        List<Customer> customers = instance.getCustomers();
        hobs.forEach(h -> {
            List<Customer> r = new ArrayList<>();
            for (int i : h.route()) {
                r.add(customers.get(i - 1));

            }
            result.add(new Route(r));
        });

        for (int i : route) {
            result.add(new Route(Collections.singletonList(customers.get(i - 1))));
        }
        return result;
    }

    private int[] reduceRoute(int[] route, Hob bestHob) {
        int[] tmp = new int[route.length - bestHob.route().length];
        int j = 0;
        int[] t = Arrays.copyOf(bestHob.route(), bestHob.route().length);
        Arrays.sort(t);
        for (int i = 0; i < route.length; i++) {
            if (Arrays.binarySearch(t, route[i]) < 0) {
                tmp[j++] = route[i];
            }
        }
        return tmp;
    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction, int expectedNumberOfVehicles) {
        return solve(instance, tdFunction);
    }

    private boolean improveRoutes(Collection<Route> routes, RouteTravelTimeCalculator calculator) {
        Route minRoute = null;

        List<Route> newRoutes = new ArrayList<>();
        routes.forEach(r -> newRoutes.add(new Route(r.getCustomers())));

        for (Route r : newRoutes) {
            if (minRoute == null || r.getCustomers().size() < minRoute.getCustomers().size()) {
                minRoute = r;
            }
        }
        if (minRoute == null) {
            return false;
        }
        newRoutes.remove(minRoute);
        int allocated = 0;
        mainLoop:
        for (Customer customer : minRoute.getCustomers()) {
            for (Route route : newRoutes) {

                for (int i = 0; i < route.getCustomers().size(); i++) {
                    List<Customer> r = new ArrayList<>(route.getCustomers());
                    r.add(i, customer);
                    Route testRoute = new Route(r);
                    if (Double.isFinite(calculator.travelTime(testRoute))) {
                        newRoutes.remove(route);
                        newRoutes.add(testRoute);
                        allocated++;
                        continue mainLoop;
                    }
                }

            }
        }

        if (allocated == minRoute.getCustomers().size()) {
            routes.clear();
            routes.addAll(newRoutes);
            improveRoutes(routes, calculator);
            return true;
        } else {
            return false;
        }

    }

}
