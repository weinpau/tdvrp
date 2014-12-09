package de.hszg.tdvrp.setting.ga;

import de.hszg.tdvrp.benchmark.Benchmark;
import de.hszg.tdvrp.benchmark.ProblemClassResults;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import de.hszg.tdvrp.scheduler.straight.StraightScheduler;
import de.hszg.tdvrp.solver.dummy.DummySolver;
import de.hszg.tdvrp.solver.ga.GASolver;
import de.hszg.tdvrp.tdfactories.TDFunctionFactories;

/**
 *
 * @author weinpau
 */
public class App {

    public static void main(String[] args) {
 
        
        TDFunctionFactory tdFunctionFactory = TDFunctionFactories.getFactoryByName("DEFAULT").get();
        Solver solver = new GASolver();
         
        Scheduler scheduler = new StraightScheduler();
        
        Benchmark benchmark = new Benchmark(solver, scheduler, tdFunctionFactory, 2);
        
        
        ProblemClassResults results = benchmark.benchmark(100, "C1");
        
        System.out.println(results);

    }
    
    
   

}
