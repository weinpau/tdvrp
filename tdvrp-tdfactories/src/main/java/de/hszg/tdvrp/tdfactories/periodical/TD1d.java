package de.hszg.tdvrp.tdfactories.periodical;

/**
 *
 * @author weinpau
 */
public class TD1d extends PeriodicalTDFunctionFactory {

    @Override
    protected double[] getTravelSpeeds() {
        return new double[]{1, 1, 1.05, 1.6, 1.6};
    }

}
