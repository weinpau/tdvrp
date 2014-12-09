package de.hszg.tdvrp.solver.ga.mutation;

import de.hszg.tdvrp.solver.ga.Chromosome;

/**
 *
 * @author weinpau
 */
public interface Mutation {
    
    
    void mutate(Chromosome chromosome);

}
