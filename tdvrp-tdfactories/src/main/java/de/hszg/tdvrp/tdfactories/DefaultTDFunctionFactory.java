package de.hszg.tdvrp.tdfactories;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;

/**
 *
 * @author weinpau
 */
public class DefaultTDFunctionFactory implements TDFunctionFactory {

    @Override
    public String getName() {
        return "DEFAULT";
    }

    @Override
    public TDFunction createTDFunction(Instance instance) {
        double[][] distanceMatrix = instance.distanceMatrix();
        double closingTime = instance.getDepot().getClosingTime();
        return (from, to, startTime) -> {

            if (startTime > closingTime || startTime < 0) {
                return Double.POSITIVE_INFINITY;
            }

            return distanceMatrix[from][to];
        };

    }

}
