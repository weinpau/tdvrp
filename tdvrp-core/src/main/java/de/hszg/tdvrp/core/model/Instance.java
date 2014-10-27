package de.hszg.tdvrp.core.model;

import java.util.List;

/**
 *
 * This interface defines a general problem instance for a VRPTW.
 *
 * @author weinpau
 */
public interface Instance {

    /**
     * Returns the name of the instance,
     *
     * @return name of instance
     */
    String getName();

    /**
     * Returns the depot.
     *
     * @return the depot
     */
    Depot getDepot();

    /**
     * Returns a list of customers.
     *
     * @return list of customers
     */
    List<Customer> getCustomers();

    /**
     * Returns the number of available vehicles.
     *
     * @return the number of vehicles
     */
    int getAvailableVehicles();

    /**
     * Returns the capacity of a single vehicle.
     *
     * @return the capacity
     */
    int getVehicleCapacity();

    /**
     * Calculates a distance matrix of the instance.
     *
     * @return distance matrix
     */
    default double[][] distanceMatrix() {
        int n = getCustomers().size() + 1;
        double[][] matrix = new double[n][n];
        getCustomers().forEach(cA
                -> getCustomers().forEach(cB
                        -> matrix[cA.getNumber()][cB.getNumber()] = cA.getPosition().distance(cB.getPosition())
                )
        );
        matrix[getDepot().getNumber()][getDepot().getNumber()] = 0;
        getCustomers().forEach(c
                -> {
                    double distance = getDepot().getPosition().distance(c.getPosition());
                    matrix[getDepot().getNumber()][c.getNumber()] = distance;
                    matrix[c.getNumber()][getDepot().getNumber()] = distance;
                }
        );

        return matrix;
    }
}
