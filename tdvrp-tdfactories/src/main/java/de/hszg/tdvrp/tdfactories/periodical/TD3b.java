package de.hszg.tdvrp.tdfactories.periodical;

/**
 *
 * @author weinpau
 */
public class TD3b extends PeriodicalTDFunctionFactory {

    @Override
    protected double[] getTravelSpeeds() {
        return new double[]{2.5, 1, 1.75, 1, 2.5};
    }

}
