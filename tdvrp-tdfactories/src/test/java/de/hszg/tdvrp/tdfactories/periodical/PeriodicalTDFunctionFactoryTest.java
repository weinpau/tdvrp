package de.hszg.tdvrp.tdfactories.periodical;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Position;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import static java.lang.Math.*;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class PeriodicalTDFunctionFactoryTest {

    @Test
    public void testCreateTDFunction_Constant() {

        PeriodicalTDFunctionFactory factory = new SamplePeriodicalTDFunctionFactory(new double[]{1});
        TDFunction tdFunction = factory.createTDFunction(sampleInstance);

        assertEquals(sqrt(200), tdFunction.tavelingTime(sampleInstance.getDepot(), customer, 0), 0.001);

    }

    @Test
    public void testCreateTDFunction_Constant_0() {

        PeriodicalTDFunctionFactory factory = new SamplePeriodicalTDFunctionFactory(new double[]{1});
        TDFunction tdFunction = factory.createTDFunction(sampleInstance);

        assertEquals(0, tdFunction.tavelingTime(customer, customer, 0), 0.001);

    }

    @Test
    public void testCreateTDFunction_Overlap() {

        PeriodicalTDFunctionFactory factory = new SamplePeriodicalTDFunctionFactory(new double[]{1, 2, 2, 1});
        TDFunction tdFunction = factory.createTDFunction(sampleInstance);

        assertEquals(5 + (sqrt(200) - 5) / 2, tdFunction.tavelingTime(sampleInstance.getDepot(), customer, 0), 0.001);

    }

    @Test
    public void testCreateTDFunction_Overlap_0() {

        PeriodicalTDFunctionFactory factory = new SamplePeriodicalTDFunctionFactory(new double[]{1, 2, 2, 1});
        TDFunction tdFunction = factory.createTDFunction(sampleInstance);

        assertEquals(0, tdFunction.tavelingTime(customer, customer, 0), 0.001);

    }

    Customer customer = new Customer(1, new Position(10, 10), 0, 10, 50, 5);
    Instance sampleInstance = new Instance() {

        @Override
        public String getName() {
            return "test";
        }

        @Override
        public Depot getDepot() {
            return new Depot(new Position(0, 0), 20);
        }

        @Override
        public List<Customer> getCustomers() {
            return Collections.singletonList(customer);
        }

        @Override
        public int getAvailableVehicles() {
            return 1;
        }

        @Override
        public int getVehicleCapacity() {
            return 10;
        }
    };

    class SamplePeriodicalTDFunctionFactory extends PeriodicalTDFunctionFactory {

        private final double[] travelSpeeds;

        public SamplePeriodicalTDFunctionFactory(double[] travelSpeeds) {
            this.travelSpeeds = travelSpeeds;
        }

        @Override
        public double[] getTravelSpeeds() {
            return travelSpeeds;
        }
    }

}
