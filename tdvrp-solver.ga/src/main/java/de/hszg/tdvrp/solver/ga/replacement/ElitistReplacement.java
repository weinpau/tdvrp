package de.hszg.tdvrp.solver.ga.replacement;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.Population;
import de.hszg.tdvrp.solver.ga.selection.ElitistSelection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class ElitistReplacement implements Replacement {

    ElitistSelection selection = new ElitistSelection();

    @Override
    public void replace(Population parentPopulation, Collection<Chromosome> children) {

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.addAll(parentPopulation.getChromosomes());
        chromosomes.addAll(children);

        parentPopulation.removeAll();
        parentPopulation.addAll(selection.select(new Population(chromosomes), parentPopulation.getChromosomes().size()));

    }

}
