package de.hszg.tdvrp.benchmark.ga;

import de.hszg.tdvrp.benchmark.CSVOutput;
import de.hszg.tdvrp.benchmark.Benchmark;
import de.hszg.tdvrp.benchmark.ProblemClassResults;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import de.hszg.tdvrp.instances.Instances;
import de.hszg.tdvrp.scheduler.greedy.GreedyScheduler;
import de.hszg.tdvrp.solver.ga.GASolver;
import de.hszg.tdvrp.tdfactories.TDFunctionFactories;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author weinpau
 */
public class App {

    public static void main(String[] args) throws Exception {

        Instances.getInstances();
        
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Start executor service with " + availableProcessors + " threads.");
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        for (String configFile : args) {
            executor.execute(createRunnable(configFile));
        }
        executor.shutdown();
    }

    private static Runnable createRunnable(String configFile) {

        return () -> {
            try {
                Config config = ConfigReader.readConfig(configFile);
                TDFunctionFactory tdFunctionFactory = TDFunctionFactories.getFactoryByName(config.getTDFunction()).get();

                Solver solver = new GASolver(config.getGAOptions());
                Scheduler scheduler = new GreedyScheduler();

                Benchmark benchmark = new Benchmark(solver, scheduler, tdFunctionFactory, config.getAttempts());

                List<ProblemClassResults> results = new ArrayList<>();

                for (String problemClass : config.getProblemClasses()) {
                    results.add(benchmark.benchmark(100, problemClass));
                }

                CSVOutput.output(results, "results-" + configFile.split("\\.")[0] + ".csv");
            } catch (Exception exception) {

            }

        };
    }

}
