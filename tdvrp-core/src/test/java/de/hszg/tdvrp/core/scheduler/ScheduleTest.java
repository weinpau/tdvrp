package de.hszg.tdvrp.core.scheduler;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Position;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class ScheduleTest {
    
    Depot depot;
    List<Customer> customers = new ArrayList<>();
    Customer customer1 = new Customer(1, new Position(15, 15), 10, 0, 50, 5);
    Customer customer2 = new Customer(2, new Position(20, 20), 10, 70, 80, 5);
    
    public ScheduleTest() {
        depot = new Depot(new Position(10, 10), 100);
        customers.add(customer1);
        customers.add(customer2);
    }

    /**
     * Test of getTravelingTime method, of class Schedule.
     */
    @Test
    public void testGetTravelingTime() {
        Schedule schedule = new Schedule(createTestInstance(), createVehicleSchedules());
        assertEquals(40, schedule.getTravelTime(), 0.0);
        
    }

    /**
     * Test of getTravelingTime method, of class Schedule.
     */
    @Test
    public void testGetTravelingTime_empty() {
        Schedule schedule = new Schedule(createTestInstance(), Collections.emptyList());
        assertEquals(0, schedule.getTravelTime(), 0.0);
        
    }

    /**
     * Test of getTotalDistance method, of class Schedule.
     */
    @Test
    public void testGetTotalDistance() {
        Schedule schedule = new Schedule(createTestInstance(), createVehicleSchedules());
        assertEquals(2 * sqrt(50) + sqrt(200), schedule.getTotalDistance(), 0.0001d);
        
    }

    /**
     * Test of getTotalDistance method, of class Schedule.
     */
    @Test
    public void testGetTotalDistance_empty() {
        Schedule schedule = new Schedule(createTestInstance(), Collections.emptyList());
        assertEquals(0, schedule.getTotalDistance(), 0.0);
    }
    
    @Test
    public void testIsValid() {
        Schedule schedule = new Schedule(createTestInstance(), createVehicleSchedules());
        assertTrue(schedule.isValid());
        
    }
    
    private List<VehicleSchedule> createVehicleSchedules() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(customer1, 10, 10, 15));
        tasks.add(new Task(customer2, 30, 70, 75));
        return Collections.singletonList(new VehicleSchedule(0, 90, tasks));
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
