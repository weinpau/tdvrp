package de.hszg.tdvrp.solver.ga;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class ElitistReplacement implements Replacement {

    ElitistSelection selection = new ElitistSelection();

    @Override
    public Population replace(Population parentPopulation, Population childPopulation) {

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.addAll(parentPopulation.getChromosomes());
        chromosomes.addAll(childPopulation.getChromosomes());

        return new Population(selection.select(new Population(chromosomes), parentPopulation.getChromosomes().size()));
    }

}
