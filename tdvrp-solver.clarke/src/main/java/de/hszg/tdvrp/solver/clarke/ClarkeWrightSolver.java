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
 * This class provides a sequential savings algorithm by Clarke and Wright (1964).
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
        int[] C = instance.getCustomers().stream().mapToInt(c -> c.getNumber()).toArray();

        while (C.length > 0) {

            HobQueue queue = new HobQueue(instance, tdFunction, C);
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
                C = sub(C, bestHob.route());
            }
        }
        Collection<Route> routes = createRoutes(instance, hobs, C);

        improveRoutes(routes, new RouteTravelTimeCalculator(instance, tdFunction));

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

    private int[] sub(int[] a, int[] b) {
        int[] tmp = new int[a.length - b.length];
        int j = 0;
        int[] t = Arrays.copyOf(b, b.length);
        Arrays.sort(t);
        for (int i = 0; i < a.length; i++) {
            if (Arrays.binarySearch(t, a[i]) < 0) {
                tmp[j++] = a[i];
            }
        }
        return tmp;
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
