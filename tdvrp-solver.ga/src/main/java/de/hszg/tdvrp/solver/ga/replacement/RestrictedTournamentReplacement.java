package de.hszg.tdvrp.solver.ga.replacement;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.Population;
import java.util.Collection;

/**
 *
 * @author weinpau
 */
public class RestrictedTournamentReplacement implements Replacement {

    @Override
    public void replace(Population population, Collection<Chromosome> children) {

        children.forEach(child -> {
            int distance = Integer.MAX_VALUE;
            Chromosome similarParent = null;
            for (Chromosome parent : population.getChromosomes()) {
                int d = child.hammingDistance(parent);
                if (d < distance) {
                    distance = d;
                    similarParent = parent;
                }
            }
            if (similarParent != null && similarParent.fitness() < child.fitness()) {
                if (population.remove(similarParent)) {
                    population.add(child);
                }
            }
        });

    }

}
