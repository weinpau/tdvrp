package de.hszg.tdvrp.core.tdfunction;

import de.hszg.tdvrp.core.model.Instance;

/**
 * This interface represents a factory for time-dependent functions.
 *
 * @author weinpau
 */
public interface TDFunctionFactory {

    /**
     * Returns the name of the factoy.
     *
     * @return name
     */
    String getName();

    /**
     * Creates a time-dependent function for the specified instance.
     *
     * @param instance
     * @return time-dependent function
     */
    TDFunction createTDFunction(Instance instance);

}
