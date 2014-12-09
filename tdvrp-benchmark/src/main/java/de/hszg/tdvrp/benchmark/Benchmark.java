package de.hszg.tdvrp.benchmark;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.scheduler.Schedule;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import de.hszg.tdvrp.instances.Instances;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author weinpau
 */
public class Benchmark {

    Solver solver;
    Scheduler scheduler;
    TDFunctionFactory tdFunctionFactory;
    int attempts;

    public Benchmark(Solver solver, Scheduler scheduler, TDFunctionFactory tDFunctionFactory, int attempts) {
        this.solver = solver;
        this.scheduler = scheduler;
        this.tdFunctionFactory = tDFunctionFactory;
        this.attempts = attempts;
    }

    public ProblemClassResults benchmark(int customer, String problemClass) {
        String regex = "0?" + customer + "_" + problemClass + "\\d+";

        List<Instance> instances = Instances.getInstances().
                stream().
                filter(i -> i.getName().matches(regex)).
                collect(Collectors.toList());

        double sumOfTravelTimes = 0;
        double sumOfDistance = 0;
        int sumOfVehicles = 0;

        for (Instance instance : instances) {
            TDFunction tdFunction = tdFunctionFactory.createTDFunction(instance);
            System.out.println("benchmark " + instance.getName());
            int bestNûmberOfVehicles = Integer.MAX_VALUE;
            double bestTravelTime = Double.POSITIVE_INFINITY;
            double bestDistance = Double.POSITIVE_INFINITY;

            for (int attempt = 0; attempt < attempts; attempt++) {
                Solution solution = solver.solve(instance, tdFunction).orElse(null);

                Schedule schedule = scheduler.schedule(solution).orElse(null);

                int vehicles = schedule.getVehicleSchedules().size();
                double travelTime = schedule.getTravelTime();
                double distance = schedule.getTotalDistance();

                if (bestNûmberOfVehicles > vehicles
                        || (bestNûmberOfVehicles == vehicles && (bestTravelTime > travelTime
                        || (bestTravelTime == travelTime && bestDistance > distance)))) {
                    bestNûmberOfVehicles = vehicles;
                    bestTravelTime = schedule.getTravelTime();
                    bestDistance = distance;
                }

            }

            System.out.println("instance results");
            System.out.println("vehicles: " + bestNûmberOfVehicles + ", travel time: " + bestTravelTime + ", distance: " + bestDistance);

            sumOfVehicles += bestNûmberOfVehicles;
            sumOfTravelTimes += bestTravelTime;
            sumOfDistance += bestDistance;
        }
        double n = instances.size();

        return new ProblemClassResults(problemClass, customer,
                ((double) sumOfVehicles) / n,
                sumOfDistance / n,
                sumOfTravelTimes / n);

    }

}
