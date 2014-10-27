package de.hszg.tdvrp.core.solver;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.Optional;

/**
 * This interface represents a solution algorithm for an TDRVP.
 *
 * @author weinpau
 */
public interface Solver {

    /**
     * Returns the name of the solution algorithm.
     *
     * @return name
     */
    String getName();

    /**
     * Returns the computed solution for the given instance and the
     * time-dependent function. The optional object is empty if no solution
     * exists. The number of vehicles used for the solution will be optimized.
     *
     * @param instance the instance
     * @param tdFunction the time-dependent function
     * @return the solution
     */
    Optional<Solution> solve(Instance instance, TDFunction tdFunction);

    /**
     * Returns the computed solution for the given instance and the
     * time-dependent function. Furthermore, the number of vehicles used for the
     * solution is set with the parameter {@code expectedNumberOfVehicles}. The optional
     * object is empty if no solution exists.
     *
     * @param instance the instance
     * @param tdFunction the time-dependent function
     * @param expectedNumberOfVehicles the number of expected vehicles
     * @return the solution
     */
    Optional<Solution> solve(Instance instance, TDFunction tdFunction, int expectedNumberOfVehicles);

}
