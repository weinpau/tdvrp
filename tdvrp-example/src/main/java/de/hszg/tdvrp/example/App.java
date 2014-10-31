package de.hszg.tdvrp.example;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.scheduler.Schedule;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import de.hszg.tdvrp.instances.Instances;
import de.hszg.tdvrp.scheduler.straight.StraightScheduler;
import de.hszg.tdvrp.solver.dummy.DummySolver;
import de.hszg.tdvrp.tdfactories.TDFunctionFactories;

/**
 *
 * @author weinpau
 */
public class App {

    public static void main(String[] args) {

        // load the instance
        Instance instance = Instances.getInstanceByName("025_C101").get();

        // create the time-dependent function
        TDFunctionFactory tdFunctionFactory = TDFunctionFactories.getFactoryByName("TD1a").get();
        TDFunction tdFunction = tdFunctionFactory.createTDFunction(instance);

        // instantiate the solver
        Solver solver = new DummySolver();

        // search for a solution
        Solution solution = solver.solve(instance, tdFunction).orElse(null);

        if (solution == null || !solution.isValid()) {
            System.out.println("No solution found");
            System.exit(0);
        }

        // instantiate the scheduler
        Scheduler scheduler = new StraightScheduler();

        // create a schedule for the solution 
        Schedule schedule = scheduler.schedule(solution).orElse(null);

        if (schedule == null || !schedule.isValid()) {
            System.out.println("No schedule found");
            System.exit(0);
        }

        System.out.printf("selected solver: %s%n", solver.getName());
        System.out.printf("selected scheduler: %s%n", scheduler.getName());
        System.out.printf("number of vehicles: %d%n", schedule.getVehicleSchedules().size());
        System.out.printf("traveling time: %f%n", schedule.getTravelingTime());
        System.out.printf("total distance: %f%n", schedule.getTotalDistance());

    }

}
