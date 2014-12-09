package de.hszg.tdvrp.solver.ga.selection;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.Population;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author weinpau
 */
public class ElitistSelection implements Selection {

    @Override
    public Collection<Chromosome> select(Population population, int selectionSize) {

        SortedMap<Double, Chromosome> map = new TreeMap<>();
        population.getChromosomes().forEach(c -> map.put(1d / c.fitness(), c));
        List<Chromosome> selectedPopulation = new ArrayList<>(selectionSize);
        for (Chromosome chromosome : map.values()) {
            selectedPopulation.add(chromosome);
            if (--selectionSize == 0) {
                break;
            }
        }
        return selectedPopulation;
    }
}
