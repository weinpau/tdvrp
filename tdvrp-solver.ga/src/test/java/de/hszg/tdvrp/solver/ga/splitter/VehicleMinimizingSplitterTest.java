package de.hszg.tdvrp.solver.ga.splitter;

import de.hszg.tdvrp.solver.ga.splitter.VehicleMinimizingSplitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class VehicleMinimizingSplitterTest {

    /**
     * Test of shortestPath method, of class ExactSplitter.
     */
    @Test
    public void testShortestPath() {
        int[] route = new int[]{1, 2, 3, 4};
        List<List<Double>> travelTimes = new ArrayList<>();
        travelTimes.add(Arrays.asList(10d, 20d));
        travelTimes.add(Arrays.asList(100d));
        travelTimes.add(Arrays.asList(50d, 10d));
        travelTimes.add(Arrays.asList(70d));
        travelTimes.add(Collections.emptyList());

        VehicleMinimizingSplitter instance = new VehicleMinimizingSplitter();

        Collection<int[]> shortestPath = instance.shortestPath(route, travelTimes);

        assertEquals(2, shortestPath.size());

        assertTrue(shortestPath.stream().anyMatch(r -> Arrays.equals(r, new int[]{1, 2})));
        assertTrue(shortestPath.stream().anyMatch(r -> Arrays.equals(r, new int[]{3, 4})));

    }

}
