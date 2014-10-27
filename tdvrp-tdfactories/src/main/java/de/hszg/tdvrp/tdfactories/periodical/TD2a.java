package de.hszg.tdvrp.tdfactories.periodical;

/**
 *
 * @author weinpau
 */
public class TD2a extends PeriodicalTDFunctionFactory {

    @Override
    protected double[] getTravelSpeeds() {
        return new double[]{1, 2, 1.5, 2, 1};
    }

}
