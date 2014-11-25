package de.hszg.tdvrp.solver.ga;

/**
 *
 * @author weinpau
 */
public interface Crossover {
    
    ChromosomePair cross(Chromosome p1, Chromosome p2); 
    
    
}
