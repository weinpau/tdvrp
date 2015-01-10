package de.hszg.tdvrp.solver.impact;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author weinpau
 */
public class ImpactHeuristic implements Solver {

    private final static double[][] PARAMETERS = new double[][]{
        {0, .7, .3, .3, .0, .7},
        {0, .8, .2, .1, .1, .8},
        {0, .8, .2, .1, .6, .3},
        {0, .8, .2, .5, .2, .3},
        {0, 1, .0, .1, .2, .7},
        {.1, .8, .1, .1, .6, .3},
        {.1, .8, .1, .3, .1, .6},
        {.2, .3, .5, 0, .8, .2},
        {.2, .6, .3, .4, .3, .3},
        {.2, .8, 0, 0, .3, .7},
        {.2, .8, 0, .2, .4, .4},
        {.3, .7, 0, .3, .2, .5},
        {.3, .7, 0, 0, .3, .7},
        {1d / 3d, 1d / 3d, 1d / 3d, .1, .2, .7},
        {1d / 3d, 1d / 3d, 1d / 3d, 1d / 3d, 1d / 3d, 1d / 3d},
        {.4, .5, .1, 0, .8, .2},
        {.4, .6, 0, 0, .1, .9},
        {.4, .6, 0, .4, .3, .3},
        {.6, .3, .1, 0, .3, .7},
        {.6, .3, .1, .2, .2, .6},
        {.6, .4, 0, .2, .3, .5},
        {.7, .2, .1, .2, .3, .5},
        {.7, .3, 0, 0, .1, .9},
        {.7, .3, 0, .4, 0, .6},
        {.8, .2, 0, 0, .1, .9},
        {.9, .1, 0, 0, .1, .9},
        {.9, .1, 0, .1, .2, .7},
        {1, 0, 0, 0, .7, .3},
        {1, 0, 0, .2, 0, .8},
        {1, 0, 0, 0, .4, .6}

    };

    @Override
    public String getName() {
        return "IMPACT_SOLVER";
    }

//    @Override
//    public Optional<Solution> solve(Instance instance, TDFunction tdFunction) {
//
//        List<int[]> bestRoutes = null;
//        double lowestCost = Double.POSITIVE_INFINITY;
//
//        double bestB1 = 0, bestB2 = 0, bestB3 = 0, bestBS = 0, bestBE = 0, bestBR = 0;
//
//        for (double b1 = 0.0; b1 <= 1; b1 += .1) {
//            for (double b2 = 0.0; b2 <= 1 - b1; b2 += .1) {
//                for (double bs = 0.0; bs <= 1; bs += .1) {
//                    for (double be = 0.0; be <= 1 - bs; be += .1) {
//                        double b3 = 1d - b1 - b2;
//                        double br = 1d - bs - be;
//                        if (b3 < 0 || br < 0) {
//                            continue;
//                        }
//
//                        System.out.println("impact " + b1 + ", " + b2 + ", " + b3 + ", " + bs + ", " + be + ", " + br);
//
//                        Set<Customer> customers = new HashSet<>(instance.getCustomers());
//                        List<int[]> routes = new ArrayList<>();
//                        while (!customers.isEmpty()) {
//                            ImpactRoute impactRoute = new ImpactRoute(instance, tdFunction, customers,
//                                    b1, b2, b3, bs, be, br);
//                            impactRoute.route();
//                            routes.add(impactRoute.getRoute());
//                        }
//                        double cost = cost(instance, tdFunction, routes);
//
//                        if (cost < lowestCost) {
//                            lowestCost = cost;
//                            bestRoutes = routes;
//
//                            bestB1 = b1;
//                            bestB2 = b2;
//                            bestB3 = b3;
//                            bestBE = be;
//                            bestBR = br;
//                            bestBS = bs;
//                        }
//                    }
//                }
//            }
//        }
//
//        System.out.println("impact " + bestB1 + ", " + bestB2 + ", " + bestB3 + ", "
//                + bestBS + ", " + bestBE + ", " + bestBR);
//
//        return Optional.of(toSolution(bestRoutes, instance, tdFunction));
//    }
    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction) {

        List<int[]> bestRoutes = null;
        double lowestCost = Double.POSITIVE_INFINITY;

        for (double[] param : PARAMETERS) {

            Set<Customer> customers = new HashSet<>(instance.getCustomers());
            List<int[]> routes = new ArrayList<>();
            while (!customers.isEmpty()) {
                ImpactRoute impactRoute = new ImpactRoute(instance, tdFunction, customers,
                        param[0], param[1], param[2], param[3], param[4], param[5]);
                impactRoute.route();
                routes.add(impactRoute.getRoute());
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

    private class ImpactRoute {

        private final Instance instance;
        private final TDFunction tdFunction;
        private final double d[][];
        private final double b1, b2, b3, bs, be, br;
        private int[] route = new int[]{0, 0};
        private final Set<Customer> customers;

        public ImpactRoute(Instance instance, TDFunction tdFunction, Set<Customer> customers, double b1, double b2, double b3, double bs, double be, double br) {
            this.instance = instance;
            this.tdFunction = tdFunction;
            this.d = instance.distanceMatrix();
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.bs = bs;
            this.be = be;
            this.br = br;
            this.customers = customers;
        }

        public Set<Customer> getCustomers() {
            return customers;
        }

        public int[] getRoute() {
            return route;
        }

        public void route() {
            List<Customer> allCustomers = instance.getCustomers();
            int seed = seed();
            if (seed > -1 && feasible(insert(route, 1, seed))) {
                route = insert(route, 1, seed);
                customers.remove(allCustomers.get(seed - 1));
            }

            while (!customers.isEmpty()) {
                int iu = -1;
                int ip = -1;
                double minImpact = Double.POSITIVE_INFINITY;

                for (Customer c : customers) {
                    int u = c.getNumber();
                    for (int p = 1; p < route.length; p++) {
                        if (feasible(insert(route, p, u))) {
                            double impact = IMPACT(u, p);
                            if (impact < minImpact) {
                                minImpact = impact;
                                iu = u;
                                ip = p;
                            }
                        }
                    }
                }
                if (iu != -1) {
                    route = insert(route, ip, iu);
                    customers.remove(allCustomers.get(iu - 1));
                } else {
                    break;
                }

            }
        }

        private int seed() {
            int seed = -1;
            double furthestDistance = Double.NEGATIVE_INFINITY;

            for (Customer c : customers) {
                int j = c.getNumber();
                if (furthestDistance < d[0][j]) {
                    furthestDistance = d[0][j];
                    seed = j;
                }
            }
            return seed;
        }

        private double IMPACT(int u, int p) {
            return bs * IS(u, p) + be * IU(u) + br * IR(u);
        }

        private double IS(int u, int p) {
            return a(insert(route, p, u), p) - instance.getCustomers().get(u - 1).getReadyTime();
        }

        private double IU(int u) {
            double iu = 0d;
            for (Customer c : customers) {
                int j = c.getNumber();
                if (j != u) {
                    iu += (1d / ((double) customers.size() - 1d)) * max(l(j) - e(u) - d[u][j], l(u) - e(j) - d[u][j]);
                }
            }
            return iu;

        }

        private double IR(int u) {
            int n = 0;
            double sumLD = 0;
            for (int p = 1; p < route.length; p++) {
                int[] r = insert(route, p, u);
                if (feasible(r)) {
                    n++;
                    sumLD += LD(route[p - 1], u, route[p]);
                }
            }
            return sumLD / (double) n;
        }

        private double LD(int i, int u, int j) {
            return b1 * c1(i, u, j) + b2 * c2(i, u, j) + b3 * c3(i, u, j);
        }

        private double c1(int i, int u, int j) {
            return d[i][u] + d[u][j] - d[i][j];
        }

        private double c2(int i, int u, int j) {

            int p = p(j);
            int[] r = insert(route, p, u);
            double ai = a(r, p - 1);
            double au = a(r, p);

            return (l(j) - (ai + s(i) + d[i][j])) - (l(j) - (au + s(u) + d[u][j]));
        }

        private double c3(int i, int u, int j) {
            double ai = a(route, p(i));
            return l(u) - (ai + s(i) + d[i][u]);
        }

        private double s(int c) {
            if (c == 0) {
                return 0;
            } else {
                return instance.getCustomers().get(c - 1).getServiceTime();
            }

        }

        private double e(int c) {
            if (c == 0) {
                return 0;
            } else {
                return instance.getCustomers().get(c - 1).getReadyTime();
            }

        }

        private double l(int c) {

            if (c == 0) {
                return instance.getDepot().getClosingTime();
            } else {
                return instance.getCustomers().get(c - 1).getDueTime();
            }

        }

        private double a(int[] r, int p) {
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

        private int[] insert(int[] r, int p, int c) {
            int[] result = Arrays.copyOf(r, r.length + 1);
            result[p] = c;
            for (int i = p; i < r.length; i++) {
                result[i + 1] = r[i];
            }
            return result;
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
