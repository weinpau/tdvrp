package de.hszg.tdvrp.server.resources;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;

/**
 *
 * @author weinpau
 */
public class SolveRequest {

    private final Instance instance;
    private final TDFunction tdFunction;
    private final Integer expectedNumberOfVehicles;

    public SolveRequest(Instance instance, TDFunction tdFunction, Integer expectedNumberOfVehicles) {
        this.instance = instance;
        this.tdFunction = tdFunction;
        this.expectedNumberOfVehicles = expectedNumberOfVehicles;
    }

    public Instance getInstance() {
        return instance;
    }

    public TDFunction getTDFunction() {
        return tdFunction;
    }

    public Integer getExpectedNumberOfVehicles() {
        return expectedNumberOfVehicles;
    }

}
