package de.hszg.tdvrp.benchmark;

/**
 *
 * @author weinpau
 */
public class ProblemClassResults {

    private final String problemClass;
    private final int customer;
    private final double avgNumberOfVehicles, avgDistance, avgTravelTime;

    public ProblemClassResults(String problemClass, int customer, double avgNumberOfVehicles, double avgDistance, double avgTravelTime) {
        this.problemClass = problemClass;
        this.customer = customer;
        this.avgNumberOfVehicles = avgNumberOfVehicles;
        this.avgDistance = avgDistance;
        this.avgTravelTime = avgTravelTime;
    }

    public double getAvgDistance() {
        return avgDistance;
    }

    public double getAvgNumberOfVehicles() {
        return avgNumberOfVehicles;
    }

    public double getAvgTravelTime() {
        return avgTravelTime;
    }

    public int getCustomer() {
        return customer;
    }

    public String getProblemClass() {
        return problemClass;
    }

    @Override
    public String toString() {
        return "ProblemClassResults{" + "problemClass=" + problemClass + ", customer=" + customer + ", avgNumberOfVehicles=" + avgNumberOfVehicles + ", avgDistance=" + avgDistance + ", avgTravelTime=" + avgTravelTime + '}';
    }

    
    
    
}
