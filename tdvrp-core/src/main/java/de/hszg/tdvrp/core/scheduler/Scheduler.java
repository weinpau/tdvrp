package de.hszg.tdvrp.core.scheduler;

import de.hszg.tdvrp.core.solver.Solution;
import java.util.Optional;

/**
 * This interface represents a scheduler for an TDRVP solution.
 *
 * @author weinpau
 */
public interface Scheduler {

    /**
     * Returns the name of the scheduler.
     *
     * @return name
     */
    String getName();

    /**
     * Returns the computed schedule for the given solution. The optional object
     * is empty if no schedule exists.
     *
     * @param solution the solution
     * @return the schedule
     */
    Optional<Schedule> schedule(Solution solution);

}
