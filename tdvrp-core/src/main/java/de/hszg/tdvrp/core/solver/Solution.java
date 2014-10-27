package de.hszg.tdvrp.core.solver;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a solution for a TDVRP instance.
 *
 * @author weinpau
 */
public class Solution {

    private final Instance instance;
    private final TDFunction tdFunction;
    private final List<Route> routes;

    public Solution(Instance instance, TDFunction tdFunction, List<Route> routes) {
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
    public List<Route> getRoutes() {
        return Collections.unmodifiableList(routes);
    }

  
}
