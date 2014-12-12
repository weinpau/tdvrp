package de.hszg.tdvrp.solver.ga.selection;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.GASolver;
import de.hszg.tdvrp.solver.ga.Population;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class RandomSelection implements Selection {

    Random random = new Random(GASolver.RANDOM_SEED);
    
    @Override
    public Collection<Chromosome> select(Population population, int selectionSize) {

        Collection<Chromosome> result = new ArrayList<>();
        List<Chromosome> chromosomes = new ArrayList<>(population.getChromosomes());
        Collections.shuffle(chromosomes, random);

        while (result.size() < selectionSize) {
            int size = result.size();
            for (int i = 0; i < Math.max(selectionSize, chromosomes.size()) - size; i++) {
                result.add(chromosomes.get(i));
            }
        }

        return result;
    }

}
