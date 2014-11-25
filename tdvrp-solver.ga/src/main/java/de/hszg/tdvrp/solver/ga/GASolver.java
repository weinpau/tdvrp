package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
public class GASolver implements Solver {

    public static final int POPULATION_SIZE = 50;
    public static final int MAX_ROUNDS = 500;
    public static final double MUTATION_PROBABILITY = 0.05;
    public static final double CROSSOVER_PROBABILITY = 0.10;

    public static final Selection SELECTION = new ElitistSelection();
    public static final Replacement REPLACEMENT = new ElitistReplacement();
    public static final Splitter SPLITTER = new StraightSplitter();

    public static final Mutation MUTATION = new ExchangeMutation();
    public static final Crossover CROSSOVER = new OX();

    @Override
    public String getName() {
        return "GA_SOLVER";
    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction) {
        return solve(instance, tdFunction, instance.getAvailableVehicles());

    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction, int expectedNumberOfVehicles) {
        if (expectedNumberOfVehicles < 0) {
            throw new IllegalArgumentException("The number of vehicles must be greater than zero.");
        }

        return Optional.empty();
    }

}
