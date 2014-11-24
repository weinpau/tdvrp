package de.hszg.tdvrp.tdfactories.periodical;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import static java.lang.Math.abs;

/**
 *
 * @author weinpau
 */
abstract class PeriodicalTDFunctionFactory implements TDFunctionFactory {

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public TDFunction createTDFunction(Instance instance) {

        double[][] distanceMatrix = instance.distanceMatrix();
        double[] travelSpeeds = getTravelSpeeds();
        double closingTime = instance.getDepot().getClosingTime();

        return (from, to, startTime) -> {

            if (startTime > closingTime || startTime < 0) {
                return Double.POSITIVE_INFINITY;
            }
            double periodLength = closingTime / travelSpeeds.length;

            double residualDistance = distanceMatrix[from][to];
            double travelTime = 0d;

            while (residualDistance > 0d) {

                int currentPeriod = (int) (travelSpeeds.length * (travelTime + startTime) / closingTime);

                double balanceTime = (currentPeriod + 1d) * periodLength - (startTime + travelTime);

                if (balanceTime == 0d) {
                    currentPeriod++;
                    balanceTime = (currentPeriod + 1d) * periodLength - (startTime + travelTime);
                }
                if (currentPeriod >= travelSpeeds.length) {
                    return Double.POSITIVE_INFINITY;
                }
                double range = abs(travelSpeeds[currentPeriod] * balanceTime);

                if (range >= residualDistance) {
                    travelTime += residualDistance / travelSpeeds[currentPeriod];
                    residualDistance = 0d;
                } else {
                    travelTime += range / travelSpeeds[currentPeriod];
                    residualDistance -= range;
                }
            }

            return travelTime;
        };
    }

    protected abstract double[] getTravelSpeeds();

}
