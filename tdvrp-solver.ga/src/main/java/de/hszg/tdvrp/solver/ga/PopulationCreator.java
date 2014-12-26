package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.solver.clarke.ClarkeWrightSolver;
import de.hszg.tdvrp.solver.ga.mutation.TWORS;
import de.hszg.tdvrp.solver.greedy.GreedySolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public final class PopulationCreator {

    private static final Random random = new Random(GASolver.RANDOM_SEED);
    private static final TWORS twors = new TWORS();

    public static Population createPopulation(Instance instance, TDFunction tdFunction, GAOptions options) {

        List<Chromosome> chromosomes = new ArrayList<>(options.populationSize());

        addSolution(new GreedySolver(), instance, tdFunction, options, chromosomes);
        addSolution(new ClarkeWrightSolver(), instance, tdFunction, options, chromosomes);
        expandPopulation(chromosomes, options);

        return new Population(chromosomes);

    }

    private static void addSolution(Solver greedySolver, Instance instance, TDFunction tdFunction, GAOptions options, List<Chromosome> chromosomes) {
        greedySolver.solve(instance, tdFunction).
                map(s -> transfromToChromosome(s, options)).
                ifPresent(chromosomes::add);
    }

    private static Chromosome transfromToChromosome(Solution solution, GAOptions options) {
        List<Customer> customers = new ArrayList<>();
        Collection<Route> routes = solution.getRoutes();

        routes.forEach(r -> customers.addAll(r.getCustomers()));
        int[] route = customers.stream().mapToInt(c -> c.getNumber()).toArray();

        return new Chromosome(solution.getInstance(), solution.getTDFunction(), options, route, 1);

    }

    private static void expandPopulation(List<Chromosome> chromosomes, GAOptions options) {

        while (chromosomes.size() < options.populationSize()) {
            Chromosome parent = chromosomes.get(random.nextInt(chromosomes.size()));
            Chromosome child = parent.copy(Arrays.copyOf(parent.route(), parent.route().length));
            int mutations = random.nextInt(10);
            for (int i = 0; i < mutations; i++) {
                twors.mutate(child);
            }
            chromosomes.add(child);
        }
    }

}
