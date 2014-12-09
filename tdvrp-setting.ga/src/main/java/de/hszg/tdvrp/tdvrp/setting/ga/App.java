package de.hszg.tdvrp.tdvrp.setting.ga;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.scheduler.Schedule;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import de.hszg.tdvrp.instances.Instances;
import de.hszg.tdvrp.scheduler.straight.StraightScheduler;
import de.hszg.tdvrp.solver.ga.GASolver;
import de.hszg.tdvrp.tdfactories.TDFunctionFactories;

/**
 *
 * @author weinpau
 */
public class App {

    public static void main(String[] args) {

        Instance instance = Instances.getInstanceByName("025_C101").get();
        
        TDFunctionFactory tdFunctionFactory = TDFunctionFactories.getFactoryByName("DEFAULT").get();
        TDFunction tdFunction = tdFunctionFactory.createTDFunction(instance);

        Solver solver = new GASolver();

        Solution solution = solver.solve(instance, tdFunction).orElse(null);
   

     
        Scheduler scheduler = new StraightScheduler();

        Schedule schedule = scheduler.schedule(solution).orElse(null);

    }

}
