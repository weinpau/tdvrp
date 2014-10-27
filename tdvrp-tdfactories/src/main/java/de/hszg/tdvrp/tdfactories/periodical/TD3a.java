package de.hszg.tdvrp.tdfactories.periodical;

/**
 *
 * @author weinpau
 */
public class TD3a extends PeriodicalTDFunctionFactory {

    @Override
    protected double[] getTravelSpeeds() {
        return new double[]{1, 2.5, 1.75, 2.5, 1};
    }

}
