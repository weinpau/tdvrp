package de.hszg.tdvrp.solver.ga.selection;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.Population;
import java.util.Collection;

/**
 *
 * @author weinpau
 */
public interface Selection {
    
    Collection<Chromosome> select(Population population, int selectionSize);
}
