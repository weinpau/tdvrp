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

    public SolveRequest(Instance instance, TDFunction tdFunction) {
        this.instance = instance;
        this.tdFunction = tdFunction;
    }

    public Instance getInstance() {
        return instance;
    }

    public TDFunction getTDFunction() {
        return tdFunction;
    }

}
