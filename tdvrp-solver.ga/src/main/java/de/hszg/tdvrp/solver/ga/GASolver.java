package de.hszg.tdvrp.solver.ga;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Numberable;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.Optional;
import java.util.Stack;

/**
 *
 * @author weinpau
 */
public class GASolver implements Solver {

    public static final int POPULATION_SIZE = 50;
    public static final int MAX_ROUNDS = 500;
    
    
    @Override
    public String getName() {
        return "GA_SOLVER";
    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction) {
        return solve(instance, tdFunction, instance.getAvailableVehicles());

    }

    @Override
    public Optional<Solution> solve(Instance instance, TDFunction tdFunction, int expectedNumberOfVehicles) {
        if (expectedNumberOfVehicles < 0) {
            throw new IllegalArgumentException("The number of vehicles must be greater than zero.");
        }

                
        return Optional.empty();
    }

    private boolean isValid(Instance instance, TDFunction tdFunction, Stack<Customer> route, Customer candidate) {

        double time = 0;
        int remainingCapacity = instance.getVehicleCapacity();
        Numberable position = instance.getDepot();
        for (Customer c : route) {
            time += tdFunction.travelTime(position, c, time);
            position = c;
            time = Math.max(c.getReadyTime(), time) + c.getServiceTime();
            remainingCapacity -= c.getDemand();
        }

        if (remainingCapacity - candidate.getDemand() < 0) {
            return false;
        }

        time += tdFunction.travelTime(position, candidate, time);
        time = Math.max(candidate.getReadyTime(), time);

        if (time > candidate.getDueTime()) {
            return false;
        }
        time += candidate.getServiceTime();
        time += tdFunction.travelTime(candidate, instance.getDepot(), time);
        return time <= instance.getDepot().getClosingTime();
    }

}
