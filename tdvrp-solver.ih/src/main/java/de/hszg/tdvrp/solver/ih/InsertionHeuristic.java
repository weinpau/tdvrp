package de.hszg.tdvrp.solver.ih;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/**
 * This class provides a insertion heuristic for a VRP. The algorithm
 * corresponds to the Solomon I3 method in 1986.
 *
 * @author weinpau
 */
public class InsertionHeuristic implements Solver {

    private final static double[][] PARAMETERS = new double[][]{
        {1, 1, 1, 0},
        {1, 2, 1, 0},
        {1, 1, 0, 1},
        {1, 2, 0, 1},
        {1, 1, .5, .5},
        {1, 2, .5, .5}};

    @Override
    public String getName() {
        return "INSERTION_HEURISTIC";
    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction) {

        List<int[]> bestRoutes = null;
        double lowestCost = Double.POSITIVE_INFINITY;

        for (double[] param : PARAMETERS) {

            Set<Customer> customers = new HashSet<>(instance.getCustomers());
            List<int[]> routes = new ArrayList<>();
            while (!customers.isEmpty()) {
                RouteInsertion routeInsertion = new RouteInsertion(instance, tdFunction, customers, param[0], param[1], param[2], param[3]);
                routeInsertion.solve();
                routes.add(routeInsertion.getRoute());
            }
            double cost = cost(instance, tdFunction, routes);

            if (cost < lowestCost) {
                lowestCost = cost;
                bestRoutes = routes;
            }
        }
        return Optional.of(toSolution(bestRoutes, instance, tdFunction));
    }

    private Solution toSolution(Collection<int[]> routes, Instance instance, TDFunction tdFuntion) {

        List<Route> rls = new ArrayList<>();
        List<Customer> allCustomers = instance.getCustomers();

        for (int[] r : routes) {
            List<Customer> cls = new ArrayList<>();
            for (int i = 1; i < r.length - 1; i++) {
                cls.add(allCustomers.get(r[i] - 1));
            }
            rls.add(new Route(cls));
        }

        return new Solution(instance, tdFuntion, rls);
    }

    private double cost(Instance instance, TDFunction tdFunction, Collection<int[]> routes) {
        double totalDuration = 0;
        for (int[] r : routes) {
            totalDuration += assumedTravelTime(instance, tdFunction, r);
        }
        return routes.size() + totalDuration / (instance.getDepot().getClosingTime() * routes.size());
    }

    private double assumedTravelTime(Instance instance, TDFunction tdFunction, int[] route) {
        double departureTime = 0;
        double totalTravelTime = 0;
        List<Customer> customers = instance.getCustomers();
        int position = 0;
        for (int i = 1; i < route.length - 1; i++) {
            int customer = route[i];
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

    private class RouteInsertion {

        private final double my, lambda, alpha1, alpha2;
        private final Instance instance;
        private final TDFunction tdFunction;
        private final double d[][];
        private final Set<Customer> customers;
        private int[] route = new int[]{0, 0};

        public RouteInsertion(Instance instance, TDFunction tdFunction, Set<Customer> customers, double my, double lambda, double alpha1, double alpha2) {
            this.instance = instance;
            this.tdFunction = tdFunction;
            this.customers = customers;
            this.my = my;
            this.lambda = lambda;
            this.alpha1 = alpha1;
            this.alpha2 = alpha2;
            d = instance.distanceMatrix();
        }

        public Set<Customer> getCustomers() {
            return customers;
        }

        public int[] getRoute() {
            return route;
        }

        public void solve() {
            while (!customers.isEmpty()) {
                Map<Customer, Integer> positions = new HashMap<>();
                customers.forEach(c -> {
                    double leastC1 = Double.POSITIVE_INFINITY;
                    for (int p = 1; p < route.length; p++) {
                        double c1 = c1(route[p - 1], c.getNumber(), route[p]);
                        if (feasible(insert(route, p, c.getNumber())) && leastC1 > c1) {
                            positions.put(c, p);
                            leastC1 = c1;
                        }
                    }
                });

                double optimum = Double.NEGATIVE_INFINITY;
                Customer ci = null;
                for (Entry<Customer, Integer> entry : positions.entrySet()) {
                    int p = entry.getValue();
                    int u = entry.getKey().getNumber();
                    double c2 = c2(route[p - 1], u, route[p]);
                    if (c2 > optimum) {
                        ci = entry.getKey();
                        optimum = c2;
                    }
                }

                if (ci == null) {
                    break;
                } else {
                    route = insert(route, positions.get(ci), ci.getNumber());
                    customers.remove(ci);
                }
            }
        }

        private double c1(int i, int u, int j) {
            return alpha1 * c11(i, u, j) + alpha2 * c12(i, u, j);
        }

        private double c2(int i, int u, int j) {
            return lambda * d[0][u] - c1(i, u, j);
        }

        private double c11(int i, int u, int j) {
            return d[i][u] + d[u][j] - my * d[i][j];
        }

        private double c12(int i, int u, int j) {
            int p = p(j);
            return b(insert(route, p, u), p + 1) - b(route, p);
        }

        private int[] insert(int[] r, int p, int c) {
            int[] result = Arrays.copyOf(r, r.length + 1);
            result[p] = c;
            for (int i = p; i < r.length; i++) {
                result[i + 1] = r[i];
            }
            return result;
        }

        private double b(int[] r, int p) {
            if (r.length == 2) {
                return 0;
            }

            List<Customer> allCustomers = instance.getCustomers();

            double time = 0;
            int position = 0;
            for (int i = 1; i <= p; i++) {
                time += tdFunction.travelTime(position, r[i], time);
                position = r[i];
                if (position == 0) {
                    return time;
                }
                Customer c = allCustomers.get(position - 1);
                time = Math.max(c.getReadyTime(), time);
                if (i == p) {
                    return time;
                }
                time += c.getServiceTime();
            }
            return -1;
        }

        private int p(int c) {
            if (c == 0) {
                return route.length - 1;
            }
            for (int i = 0; i < route.length; i++) {
                if (route[i] == c) {
                    return i;
                }
            }
            return -1;
        }

        private boolean feasible(int[] r) {
            List<Customer> allCustomers = instance.getCustomers();
            int remainingCapacity = instance.getVehicleCapacity();
            double time = 0;

            int position = 0;
            for (int i = 1; i < r.length - 1; i++) {
                time += tdFunction.travelTime(position, r[i], time);
                position = r[i];
                Customer c = allCustomers.get(position - 1);
                time = Math.max(c.getReadyTime(), time);
                if (time > c.getDueTime()) {
                    return false;
                }

                time += c.getServiceTime();
                remainingCapacity -= c.getDemand();
                if (remainingCapacity < 0) {
                    return false;
                }
            }

            time += tdFunction.travelTime(r[r.length - 2], 0, time);
            return time <= instance.getDepot().getClosingTime();
        }
    }
}
