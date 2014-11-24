package de.hszg.tdvrp.solver.ga;

/**
 *
 * @author weinpau
 */
public interface Replacement {
    
    Population replace(Population parentPopulation, Population childPopulation);
    
}
