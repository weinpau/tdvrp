package de.hszg.tdvrp.solver.ga;

import java.util.Collection;

/**
 *
 * @author weinpau
 */
public interface Splitter {
        
    Collection<int[]> split(Chromosome chromosome);
    
}
