package de.hszg.tdvrp.core.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class InstanceTest {

    /**
     * Test of distanceMatrix method, of class Instance.
     */
    @Test
    public void testDistanceMatrix() {
        Instance instance = new SimpleInstance(Arrays.asList(
                new Customer(1, new Position(8, 9), 10, 0, 50, 5),
                new Customer(2, new Position(2, 1), 10, 70, 80, 5)));

        double[][] expResult = new double[][]{{0, 5, 5}, {5, 0, 10}, {5, 10, 0}};
        double[][] result = instance.distanceMatrix();
        assertArrayEquals(expResult, result);

    }
    
    /**
     * Test of distanceMatrix method, of class Instance.
     */
    @Test
    public void testDistanceMatrix_empty() {
        Instance instance = new SimpleInstance(Collections.emptyList());

        double[][] expResult = new double[][]{{0}};
        double[][] result = instance.distanceMatrix();
        assertArrayEquals(expResult, result);

    }

    public class SimpleInstance implements Instance {

        final List<Customer> customers;

        public SimpleInstance(List<Customer> customers) {
            this.customers = customers;
        }

        @Override
        public String getName() {
            return "Test instance";
        }

        @Override
        public Depot getDepot() {
            return new Depot(new Position(5, 5), 100);
        }

        @Override
        public List<Customer> getCustomers() {

            return customers;
        }

        @Override
        public int getAvailableVehicles() {
            return 1;
        }

        public int getVehicleCapacity() {
            return 100;
        }
    }

}
