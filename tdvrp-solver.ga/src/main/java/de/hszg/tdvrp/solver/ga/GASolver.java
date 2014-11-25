package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.Collection;
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

        Population population = createPopulation(instance, tdFunction, options.populationSize());

        int round = options.maxRounds();
        Chromosome bestEver = null;

        while (round-- > 0) {

            List<Chromosome> children = options.selection().
                    select(population, (int) (options.selectionRate() * population.getChromosomes().size())).
                    stream().
                    map(c -> c.child()).
                    collect(Collectors.toList());

            int length = children.size();

            for (int i = 0; i < length; i++) {

                if (random.nextDouble() <= options.crossoverProbability()) {
                    int other = random.nextInt(length);

                    if (other == i) {
                        other = (other + 1) % length;
                    }
                    ChromosomePair chromosomePair = children.get(i).cross(children.get(other));
                    children.add(chromosomePair.left());
                    children.add(chromosomePair.right());

                }
                if (random.nextDouble() <= options.mutationProbability()) {
                    children.get(i).mutate();
                }

            }

            population = options.replacement().replace(population, children);
            
            Chromosome best = population.getBestChromosome().orElse(null);
            if (best != null && (bestEver == null || bestEver.fitness() < best.fitness())) {
                System.out.println("updated best chromosome in round " + round + " to " + best.fitness);
                bestEver = best;
            }
        }

        if (bestEver == null) {
            return Optional.empty();
        } else {
            return Optional.of(new Solution(instance, tdFunction, bestEver.routes()));
        }

    }

    Population createPopulation(Instance instance, TDFunction tdFunction, int size) {
        Collection<Chromosome> chromosomes = new ArrayList<>();
        int length = instance.getCustomers().size();
        for (int i = 0; i < size; i++) {
            chromosomes.add(new Chromosome(instance, tdFunction, options, createRoute(length), 1));
        }
        return new Population(chromosomes);

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
