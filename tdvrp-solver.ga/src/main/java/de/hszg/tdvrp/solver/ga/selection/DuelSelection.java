package de.hszg.tdvrp.solver.ga.selection;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.GASolver;
import de.hszg.tdvrp.solver.ga.Population;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class DuelSelection implements Selection {

    Random random = new Random(GASolver.RANDOM_SEED);

    @Override
    public Collection<Chromosome> select(Population population, int selectionSize) {

        Collection<Chromosome> result = new ArrayList<>();
        List<Chromosome> chromosomes = new ArrayList<>(population.getChromosomes());

        while (result.size() < selectionSize) {

            int iA = random.nextInt(chromosomes.size());
            int iB = random.nextInt(chromosomes.size());
            if (iA == iB) {
                iB = (iB + 1) % chromosomes.size();
            }
            Chromosome a = chromosomes.get(iA);
            Chromosome b = chromosomes.get(iB);

            if (a.fitness() > b.fitness()) {
                result.add(a);
            } else {
                result.add(b);
            }
        }

        return result;

    }

}
