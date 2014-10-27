package de.hszg.tdvrp.instances.solomon;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Position;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author weinpau
 */
public class SolomonReaderTest {

    @Test
    public void testReadInstances() {
        SolomonReader reader = new SolomonReader();

        List<Instance> instances = reader.readInstances();

        Instance instance = instances.stream().filter(i -> i.getName().equals("025_C101")).findFirst().orElse(null);

        assertNotNull(instance);

        assertEquals(25, instance.getAvailableVehicles());
        assertEquals(200, instance.getVehicleCapacity());

        Depot depot = instance.getDepot();
        assertEquals(0, depot.getNumber());
        assertEquals(new Position(40, 50), depot.getPosition());
        assertEquals(1236, depot.getClosingTime(), 0);

        assertEquals(25, instance.getCustomers().size());

        Customer customer = instance.getCustomers().get(0);
        assertEquals(1, customer.getNumber());
        assertEquals(new Position(45, 68), customer.getPosition());
        assertEquals(10, customer.getDemand(), 0);
        assertEquals(912, customer.getReadyTime(), 0);
        assertEquals(967, customer.getDueTime(), 0);
        assertEquals(90, customer.getServiceTime(), 0);

    }

}
