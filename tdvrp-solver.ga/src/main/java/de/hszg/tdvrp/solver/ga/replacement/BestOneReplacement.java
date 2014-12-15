package de.hszg.tdvrp.solver.ga.replacement;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.Population;
import java.util.Collection;

/**
 *
 * @author weinpau
 */
public class BestOneReplacement implements Replacement {

    @Override
    public void replace(Population population, Collection<Chromosome> children) {

        Chromosome bestChild = getBestChild(children);

        population.getWorstChromosome().ifPresent(worst -> {

            if (population.remove(worst)) {
                population.add(bestChild);
            }
        });
    }

    private Chromosome getBestChild(Collection<Chromosome> children) {
        Chromosome bestChild = null;
        for (Chromosome c : children) {
            if (bestChild == null || c.fitness() > bestChild.fitness()) {
                bestChild = c;
            }
        }
        return bestChild;
    }

}
