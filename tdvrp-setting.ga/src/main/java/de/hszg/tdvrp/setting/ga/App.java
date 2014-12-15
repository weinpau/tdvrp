package de.hszg.tdvrp.setting.ga;

import de.hszg.tdvrp.benchmark.Benchmark;
import de.hszg.tdvrp.benchmark.ProblemClassResults;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import de.hszg.tdvrp.scheduler.straight.StraightScheduler;
import de.hszg.tdvrp.solver.ga.GASolver;
import de.hszg.tdvrp.tdfactories.TDFunctionFactories;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class App {

    private final static String PROBLEM_CLASSES[] = {"C1", "C2", "R1", "R2", "RC1", "RC2"};

    public static void main(String[] args) throws Exception {

        TDFunctionFactory tdFunctionFactory = TDFunctionFactories.getFactoryByName("DEFAULT").get();
        Solver solver = new GASolver(OptionsReader.readOptions(args[0]));
      
        Scheduler scheduler = new StraightScheduler();

        Benchmark benchmark = new Benchmark(solver, scheduler, tdFunctionFactory, 3);

        List<ProblemClassResults> results = new ArrayList<>();

        for (String problemClass : PROBLEM_CLASSES) {
            results.add(benchmark.benchmark(100, problemClass));
        }

        CSVOutput.output(results, "results.csv");

    }

}
