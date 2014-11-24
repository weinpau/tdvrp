package de.hszg.tdvrp.solver.ga;

import java.util.Collection;

/**
 *
 * @author weinpau
 */
public interface Selection {
    
    Collection<Chromosome> select(Population population, int selectionSize);
}
