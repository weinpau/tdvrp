package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.solver.ga.splitter.VehicleMinimizingSplitter;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author weinpau
 */
public class GASolver implements Solver {

    Random random = new Random();

    GAOptions options = new GAOptions();

    public GASolver() {
    }

    public GASolver(GAOptions options) {
        this.options = options;
    }

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

        Population population = PopulationCreator.createPopulation(instance, tdFunction, options);
        Chromosome bestEver = null;
        int round = options.maxRounds();
        int roundsSinceLastImprovement = 0;
        double bestPopulationFitness = Double.NEGATIVE_INFINITY;

        while (round-- > 0 && roundsSinceLastImprovement < options.maxRoundsWithoutImproving()) {
            List<Chromosome> children = new ArrayList<>();
            List<Chromosome> selection = options.selection().
                    select(population, (int) (options.selectionRate() * population.getChromosomes().size())).
                    stream().
                    map(c -> c.child()).
                    collect(Collectors.toList());

            int length = selection.size();

            for (int i = 0; i < length; i++) {

                int other = random.nextInt(length);

                if (other == i) {
                    other = (other + 1) % length;
                }
                Chromosome child = selection.get(i).cross(selection.get(other));

                if (random.nextDouble() <= options.mutationProbability()) {
                    child.mutate();
                }

                children.add(child);

            }

            population = options.replacement().replace(population, children);

            Chromosome best = population.getBestChromosome().orElse(null);

            double populationFitness = population.getChromosomes().stream().mapToDouble(c -> c.fitness()).sum();

            if (best != null && (bestEver == null || bestEver.fitness() < best.fitness())) {
                System.out.println("updated best chromosome in round " + (options.maxRounds() - round) + " to " + best.fitness + " " + population.getChromosomes().size());
                bestEver = best;
            }

            if (populationFitness > bestPopulationFitness) {
                bestPopulationFitness = populationFitness;
                roundsSinceLastImprovement = 0;
                System.out.println("updated population fitness in round " + (options.maxRounds() - round) + " to " + bestPopulationFitness);

            } else {
                roundsSinceLastImprovement++;
            }

        }

        if (bestEver == null) {
            return Optional.empty();
        } else {
            return Optional.of(new Solution(instance, tdFunction, bestEver.routes(new VehicleMinimizingSplitter())));
        }

    }

    private int[] createRoute(int length) {
        int[] route = new int[length];
        for (int i = 1; i <= length; i++) {
            route[i - 1] = i;
        }
        shuffleArray(route);
        return route;
    }

    void shuffleArray(int[] array) {
        int index;

        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }

}
