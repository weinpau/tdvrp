package de.hszg.tdvrp.solver.ga.crossover;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.ChromosomePair;

/**
 *
 * @author weinpau
 */
public interface Crossover {
    
    ChromosomePair cross(Chromosome p1, Chromosome p2); 
    
    
}
