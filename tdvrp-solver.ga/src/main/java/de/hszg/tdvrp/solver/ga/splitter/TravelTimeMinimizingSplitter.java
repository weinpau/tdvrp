package de.hszg.tdvrp.solver.ga.splitter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class TravelTimeMinimizingSplitter extends DPSplitter {

    @Override
    Collection<int[]> shortestPath(int[] route, List<List<Double>> travelTimes) {
        int size = travelTimes.size();
        int[] ancestors = new int[size];
        double[] piTravelTimes = initPiTravelTimes(size);

        for (int i = 0; i < size - 1; i++) {
            List<Double> edges = travelTimes.get(i);
            for (int j = 1; j <= edges.size(); j++) {
                if (piTravelTimes[i + j] > piTravelTimes[i] + edges.get(j - 1)) {
                    piTravelTimes[i + j] = piTravelTimes[i] + edges.get(j - 1);
                    ancestors[i + j] = i;
                }

            }
        }

        List<int[]> result = new LinkedList<>();
        int i = ancestors.length - 1;
        while (i >= 0) {
            int next = ancestors[i];
            result.add(0,Arrays.copyOfRange(route, next, i));
            i = next;
            if (i == 0 && next == 0) {
                break;
            }
        }
        return result;
    }

    double[] initPiTravelTimes(int size) {
        double[] piTravelTimes = new double[size];
        Arrays.fill(piTravelTimes, Double.POSITIVE_INFINITY);
        piTravelTimes[0] = 0;
        return piTravelTimes;
    }

}
