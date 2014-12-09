package de.hszg.tdvrp.solver.ga.splitter;

import de.hszg.tdvrp.solver.ga.Chromosome;
import java.util.Collection;

/**
 *
 * @author weinpau
 */
public interface Splitter {
        
    Collection<int[]> split(Chromosome chromosome);
    
}
