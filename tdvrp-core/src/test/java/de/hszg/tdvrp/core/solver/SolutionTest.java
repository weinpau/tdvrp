package de.hszg.tdvrp.core.solver;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Position;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class SolutionTest {

    Customer customer1 = new Customer(1, new Position(15, 15), 10, 0, 50, 5);
    Customer customer2 = new Customer(2, new Position(20, 20), 10, 70, 80, 5);
    Customer other = new Customer(3, new Position(15, 20), 10, 60, 70, 5);

    @Test
    public void testIsValid_true() {
        Solution solution = new Solution(instance, tdFunction, Arrays.asList(new Route(Arrays.asList(customer1, customer2))));
        assertTrue(solution.isValid());
    }

    @Test
    public void testIsValid_false() {
        Solution solution = new Solution(instance, tdFunction, Arrays.asList(new Route(Arrays.asList(customer1))));
        assertFalse(solution.isValid());
    }

    @Test
    public void testIsValid_false_2() {
        Solution solution = new Solution(instance, tdFunction, Arrays.asList(new Route(Arrays.asList(customer1, other))));
        assertFalse(solution.isValid());
    }

    Instance instance = new Instance() {

        Depot depot;
        List<Customer> customers = new ArrayList<>();

        {
            depot = new Depot(new Position(10, 10), 100);
            customers.add(customer1);
            customers.add(customer2);
        }

        @Override
        public String getName() {
            return "Test Instance";
        }

        @Override
        public Depot getDepot() {
            return depot;
        }

        @Override
        public List<Customer> getCustomers() {
            return customers;

        }

        @Override
        public int getAvailableVehicles() {
            return 1;
        }

        @Override
        public int getVehicleCapacity() {
            return 100;
        }
    };

    TDFunction tdFunction = (from, to, startTime) -> instance.distanceMatrix()[from.getNumber()][to.getNumber()];

}
