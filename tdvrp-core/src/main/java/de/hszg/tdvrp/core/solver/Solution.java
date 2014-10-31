package de.hszg.tdvrp.core.solver;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a solution for a TDVRP instance.
 *
 * @author weinpau
 */
public class Solution {

    private final Instance instance;
    private final TDFunction tdFunction;
    private final Collection<Route> routes;

    public Solution(Instance instance, TDFunction tdFunction, Collection<Route> routes) {
        this.instance = instance;
        this.tdFunction = tdFunction;
        this.routes = routes;
    }

    /**
     * Returns the instance of the problem solution.
     *
     * @return the instance
     */
    public Instance getInstance() {
        return instance;
    }

    /**
     * Returns the time-dependent function.
     *
     * @return time-dependent function
     */
    public TDFunction getTDFunction() {
        return tdFunction;
    }

    /**
     * Returns the routes of the solution.
     *
     * @return the routes
     */
    public Collection<Route> getRoutes() {
        return Collections.unmodifiableCollection(routes);
    }

    /**
     * Checks the validity of this solution.
     *
     * @return {@code true] if this solution is valid
     */
    public boolean isValid() {
        Set<Customer> customers = new HashSet<>(instance.getCustomers());

        for (Route route : getRoutes()) {
            if (route.getCustomers().stream().mapToDouble(c -> c.getDemand()).sum() > instance.getVehicleCapacity()) {
                return false;
            }
            if (!route.getCustomers().stream().allMatch(customers::contains)) {
                return false;
            }
            route.getCustomers().forEach(customers::remove);
        }

        return customers.isEmpty();
    }

}
