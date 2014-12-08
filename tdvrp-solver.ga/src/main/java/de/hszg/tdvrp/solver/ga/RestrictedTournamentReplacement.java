package de.hszg.tdvrp.solver.ga;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class RestrictedTournamentReplacement implements Replacement {

    @Override
    public Population replace(Population population, Collection<Chromosome> children) {

        List<Chromosome> newPopulation = new ArrayList<>(population.getChromosomes());
        
               
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
                if (newPopulation.remove(similarParent)) {
                 
                    newPopulation.add(child);
                }
            }
        });

        return new Population(newPopulation);

    }

}
