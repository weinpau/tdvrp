package de.hszg.tdvrp.core.tdfunction;

import de.hszg.tdvrp.core.model.Numberable;

/**
 * This interface represents a time-dependent function for the calculation of
 * travel time between two vertices.
 *
 * @author weinpau
 */
@FunctionalInterface
public interface TDFunction {

    /**
     * Calculates the travel time between two nodes.
     *
     * @param from
     * @param to
     * @param startTime
     * @return travel time between two vertices
     */
    double travelTime(Numberable from, Numberable to, double startTime);

}
