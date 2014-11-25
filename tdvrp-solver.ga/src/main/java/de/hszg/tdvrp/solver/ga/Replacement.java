package de.hszg.tdvrp.solver.ga;

import java.util.Collection;

/**
 *
 * @author weinpau
 */
public interface Replacement {
    
    Population replace(Population population, Collection<Chromosome> children);
    
}
