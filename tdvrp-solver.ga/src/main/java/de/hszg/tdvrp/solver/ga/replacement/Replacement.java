package de.hszg.tdvrp.solver.ga.replacement;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.Population;
import java.util.Collection;

/**
 *
 * @author weinpau
 */
public interface Replacement {
    
    void replace(Population population, Collection<Chromosome> children);
    
}
