package de.hszg.tdvrp.core.tdfunction;

import de.hszg.tdvrp.core.model.Numberable;

/**
 * This interface represents a time-dependent function for the calculation of
 * traveling time between two vertices.
 *
 * @author weinpau
 */
@FunctionalInterface
public interface TDFunction {

    /**
     * Calculates the traveling time between two vertices.
     *
     * @param from
     * @param to
     * @param startTime
     * @return traveling time between two vertices
     */
    double tavelingTime(Numberable from, Numberable to, double startTime);

}
