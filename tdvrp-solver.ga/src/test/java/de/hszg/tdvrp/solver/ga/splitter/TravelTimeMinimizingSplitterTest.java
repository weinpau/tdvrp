package de.hszg.tdvrp.solver.ga.splitter;

import gnu.trove.TDoubleArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class TravelTimeMinimizingSplitterTest {

    /**
     * Test of shortestPath method, of class TravelTimeMinimizingSplitter.
     */
    @Test
    public void testShortestPath() {
        int[] route = new int[]{1, 2, 3, 4};
        List<TDoubleArrayList> travelTimes = new ArrayList<>();
        travelTimes.add(new TDoubleArrayList(new double[] {10d, 200d}));
        travelTimes.add(new TDoubleArrayList(new double[] {100d}));
        travelTimes.add(new TDoubleArrayList(new double[] {50d, 10d}));
        travelTimes.add(new TDoubleArrayList(new double[] {70d}));
        travelTimes.add(new TDoubleArrayList());

        TravelTimeMinimizingSplitter instance = new TravelTimeMinimizingSplitter();

        Collection<int[]> shortestPath = instance.shortestPath(route, travelTimes);

        assertEquals(3, shortestPath.size());

        assertTrue(shortestPath.stream().anyMatch(r -> Arrays.equals(r, new int[]{1})));
        assertTrue(shortestPath.stream().anyMatch(r -> Arrays.equals(r, new int[]{2})));
        assertTrue(shortestPath.stream().anyMatch(r -> Arrays.equals(r, new int[]{3, 4})));

    }

}
