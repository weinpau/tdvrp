package de.hszg.tdvrp.tdfactories.periodical;

/**
 *
 * @author weinpau
 */
public class TD2b extends PeriodicalTDFunctionFactory {

    @Override
    protected double[] getTravelSpeeds() {
        return new double[]{2, 1, 1.5, 1, 2};
    }

}
