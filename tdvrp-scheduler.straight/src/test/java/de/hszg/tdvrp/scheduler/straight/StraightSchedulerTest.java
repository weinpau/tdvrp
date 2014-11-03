package de.hszg.tdvrp.scheduler.straight;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Position;
import de.hszg.tdvrp.core.scheduler.Schedule;
import de.hszg.tdvrp.core.scheduler.Task;
import de.hszg.tdvrp.core.scheduler.VehicleSchedule;
import de.hszg.tdvrp.core.solver.Route;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class StraightSchedulerTest {

    Depot depot;
    List<Customer> customers = new ArrayList<>();

    public StraightSchedulerTest() {

        depot = new Depot(new Position(10, 10), 100);

        customers.add(new Customer(1, new Position(15, 15), 10, 0, 50, 5));
        customers.add(new Customer(2, new Position(20, 20), 10, 70, 80, 5));
    }

    /**
     * Test of schedule method, of class StraightScheduler.
     */
    @Test
    public void testSchedule() {
        Instance instance = createTestInstance();
        double[][] distanceMatrix = instance.distanceMatrix();
        for (int i = 0; i < distanceMatrix.length; i++) {
            System.out.println(Arrays.toString(distanceMatrix[i]));
        }

        TDFunction tdFunction = (from, to, time) -> instance.distanceMatrix()[from.getNumber()][to.getNumber()];

        Solution solution = new Solution(instance, tdFunction, Collections.singletonList(new Route(customers)));

        StraightScheduler scheduler = new StraightScheduler();

        Schedule result = scheduler.schedule(solution).get();
        VehicleSchedule vSchedule = result.getVehicleSchedules().get(0);
        Task task0 = vSchedule.getTasks().get(0);
        Task task1 = vSchedule.getTasks().get(1);

        assertEquals(2 * sqrt(50) + sqrt(200), result.getTotalDistance(), 0.0001d);
        assertEquals(2 * sqrt(50) + sqrt(200), result.getTravelTime(), 0.0001d);

        assertEquals(0, vSchedule.getDepartureTime(), 0.0001d);
        assertEquals(75 + sqrt(200), vSchedule.getArrivalTime(), 0.0001d);

        assertEquals(sqrt(50), task0.getArrivalTime(), 0.0001d);
        assertEquals(sqrt(50), task0.getStartTime(), 0.0001d);
        assertEquals(sqrt(50) + 5, task0.getDepartureTime(), 0.0001d);

        assertEquals(2 * sqrt(50) + 5, task1.getArrivalTime(), 0.0001d);
        assertEquals(70, task1.getStartTime(), 0.0001d);
        assertEquals(75, task1.getDepartureTime(), 0.0001d);

    }

    private Instance createTestInstance() {
        return new Instance() {

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
    }

}
