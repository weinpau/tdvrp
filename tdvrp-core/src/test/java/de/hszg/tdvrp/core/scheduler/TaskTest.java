package de.hszg.tdvrp.core.scheduler;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Position;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class TaskTest {

    Customer customer = new Customer(1, new Position(1, 1), 100, 10, 100, 20);

    @Test
    public void testIsValid_true() {
        Task task = new Task(customer, 10, 10, 30);
        assertTrue(task.isValid());
    }

    @Test
    public void testIsValid_false() {
        Task task = new Task(customer, 10, 10, 20);
        assertFalse(task.isValid());
    }

    @Test
    public void testIsValid_false_2() {
        Task task = new Task(customer, 110, 120, 150);
        assertFalse(task.isValid());
    }

}
