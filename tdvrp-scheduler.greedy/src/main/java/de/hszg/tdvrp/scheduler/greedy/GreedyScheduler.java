package de.hszg.tdvrp.scheduler.greedy;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Numberable;
import de.hszg.tdvrp.core.scheduler.Schedule;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.scheduler.Task;
import de.hszg.tdvrp.core.scheduler.VehicleSchedule;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This scheduler creates a schedule for a solution.
 *
 * @author weinpau
 */
public class GreedyScheduler implements Scheduler {

    @Override
    public String getName() {
        return "GREEDY-HEURISTIC";
    }

    @Override
    public Optional<Schedule> schedule(Solution solution) {

        List<VehicleSchedule> vehicleSchedules = new ArrayList<>();
        for (Route route : solution.getRoutes()) {
            VehicleSchedule vSchedule = createVehicleSchedule(route, solution);
            if (vSchedule == null) {
                return Optional.empty();
            } else {
                vehicleSchedules.add(vSchedule);
            }
        }

        return Optional.of(new Schedule(solution.getInstance(), vehicleSchedules));

    }

    private VehicleSchedule createVehicleSchedule(Route route, Solution solution) {

        double[][] distanceMatrix = solution.getInstance().distanceMatrix();

        Queue<RoutePair> queue = new PriorityQueue<>(
                (a, b) -> -1 * Double.compare(
                        distanceMatrix[a.getStartingPosition().getNumber()][a.getTargetPosition().getNumber()],
                        distanceMatrix[b.getStartingPosition().getNumber()][b.getTargetPosition().getNumber()]));

        List<RoutePair> routePairs = createRoutePairs(solution, route);
        queue.addAll(routePairs);
        improveDepartureTimes(queue, routePairs, solution);
        
        return toVehicleSchedule(routePairs, solution);

    }

    private void improveDepartureTimes(Queue<RoutePair> queue, List<RoutePair> routePairs, Solution solution) {
        while (!queue.isEmpty()) {
            
            RoutePair currentPair = queue.poll();
            List<RoutePair> firstTail = firstTail(routePairs, currentPair);
            List<RoutePair> lastTail = lastTail(routePairs, currentPair);

            double minDepartureTime = minDepartureTime(firstTail, solution);
            double minTravelTime = Double.POSITIVE_INFINITY;
            double optDepartureTime = minDepartureTime;
            double time = minDepartureTime;

            while (isAchievable(lastTail, solution, time)) {
                double tt = solution.getTDFunction().tavelTime(currentPair.getStartingPosition(), currentPair.getTargetPosition(), time);
                if (tt < minTravelTime) {
                    minTravelTime = tt;
                    optDepartureTime = time;
                }
                time++;
            }
            currentPair.setDepartureTime(optDepartureTime);
        }
    }

    private List<RoutePair> createRoutePairs(Solution solution, Route route) {
        List<RoutePair> routePairs = new ArrayList<>();
        Numberable currentPosition = solution.getInstance().getDepot();
        for (Customer customer : route.getCustomers()) {
            routePairs.add(new RoutePair(currentPosition, customer));
            currentPosition = customer;
        }
        routePairs.add(new RoutePair(currentPosition, solution.getInstance().getDepot()));
        return routePairs;
    }

    private List<RoutePair> firstTail(List<RoutePair> routePairs, RoutePair currentPair) {
        List<RoutePair> firstTail = new ArrayList<>();
        for (RoutePair p : routePairs) {
            if (p == currentPair) {
                break;
            } else {
                firstTail.add(p);
            }
        }
        return firstTail;
    }

    private List<RoutePair> lastTail(List<RoutePair> routePairs, RoutePair currentPair) {
        List<RoutePair> lastTail = new ArrayList<>();
        lastTail.add(currentPair);
        boolean fillable = false;
        for (RoutePair p : routePairs) {
            if (p == currentPair) {
                fillable = true;
            } else if (fillable) {
                lastTail.add(p);
            }
        }
        return lastTail;
    }

    private double minDepartureTime(List<RoutePair> routePairs, Solution solution) {
        TDFunction tdFunction = solution.getTDFunction();
        double time = 0;
        for (RoutePair p : routePairs) {
            if (p.getTargetPosition() instanceof Customer) {
                if (p.getDepartureTime().isPresent()) {
                    time = p.getDepartureTime().getAsDouble();
                }
                time += tdFunction.tavelTime(p.getStartingPosition(), p.getTargetPosition(), time);
                Customer targetCustomer = (Customer) p.getTargetPosition();
                time = Math.max(targetCustomer.getReadyTime(), time);
                time += targetCustomer.getServiceTime();
            }
        }

        return time;
    }

    private boolean isAchievable(List<RoutePair> routePairs, Solution solution, double departureTime) {
        if (departureTime > solution.getInstance().getDepot().getClosingTime()) {
            return false;
        }
        TDFunction tdFunction = solution.getTDFunction();

        for (RoutePair p : routePairs) {
            departureTime = tdFunction.tavelTime(p.getStartingPosition(), p.getTargetPosition(), departureTime);
            if (p.getDepartureTime().isPresent()) {
                return departureTime <= p.getDepartureTime().getAsDouble();
            } else if (p.getTargetPosition() instanceof Customer) {
                Customer targetCustomer = (Customer) p.getTargetPosition();
                departureTime = Math.max(targetCustomer.getReadyTime(), departureTime);

                if (departureTime < targetCustomer.getDueTime()) {
                    return false;
                }

                departureTime += targetCustomer.getServiceTime();
            }
        }
        return departureTime <= solution.getInstance().getDepot().getClosingTime();
    }

    private VehicleSchedule toVehicleSchedule(List<RoutePair> routePairs, Solution solution) {
        TDFunction tdFunction = solution.getTDFunction();

        double time = 0;
        double departureTime = 0;

        List<Task> tasks = new ArrayList<>();

        for (RoutePair p : routePairs) {

            if (p.getStartingPosition() instanceof Depot) {
                departureTime = p.getDepartureTime().getAsDouble();
                time = departureTime + tdFunction.tavelTime(p.getStartingPosition(), p.getTargetPosition(), departureTime);
            } else {
                Customer customer = (Customer) p.getStartingPosition();
                double startTime = Math.max(customer.getReadyTime(), time);
                double dTime = p.getDepartureTime().getAsDouble();
                tasks.add(new Task(customer, time, startTime, dTime));
                time = dTime + tdFunction.tavelTime(p.getStartingPosition(), p.getTargetPosition(), dTime);

            }

        }

        return new VehicleSchedule(departureTime, time, tasks);
    }

    private class RoutePair {

        private final Numberable startingPosition, targetPosition;
        private Double departureTime;

        public RoutePair(Numberable startingPosition, Numberable targetPosition) {
            this.startingPosition = startingPosition;
            this.targetPosition = targetPosition;
        }

        public Numberable getStartingPosition() {
            return startingPosition;
        }

        public Numberable getTargetPosition() {
            return targetPosition;
        }

        public OptionalDouble getDepartureTime() {
            if (departureTime == null) {
                return OptionalDouble.empty();
            } else {
                return OptionalDouble.of(departureTime);
            }
        }

        public void setDepartureTime(double departureTime) {
            this.departureTime = departureTime;
        }
    }

}
