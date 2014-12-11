package de.hszg.tdvrp.solver.ga.splitter;

import gnu.trove.TDoubleArrayList;
import gnu.trove.TIntArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class VehicleMinimizingSplitter extends DPSplitter {

    @Override
    Collection<int[]> shortestPath(int[] route, List<TDoubleArrayList> travelTimes) {
        int size = travelTimes.size();

        List<TIntArrayList> leapAncestors = initLeapAncestors(size);
        int[] piLeaps = initPiLeaps(size);
        double[] piTravelTimes = initPiTravelTimes(size);
        for (int i = 0; i < size - 1; i++) {
            int edges = travelTimes.get(i).size();
            for (int j = 1; j <= edges; j++) {
                if (piLeaps[i + j] > piLeaps[i] + j) {
                    piLeaps[i + j] = piLeaps[i] + j;
                    leapAncestors.get(i + j).clear();
                }
                if (piLeaps[i + j] >= piLeaps[i] + j) {
                    leapAncestors.get(i + j).add(i);
                }
            }
        }
        int[] ancestors = new int[size];
        for (int i = size - 1; i >= 0; i--) {
            for (int j = 0; j < leapAncestors.get(i).size(); j++) {
                int ancestor = leapAncestors.get(i).get(j);
                double tt = travelTimes.get(ancestor).get(i - ancestor - 1);
                if (piTravelTimes[ancestor] > piTravelTimes[i] + tt) {
                    piTravelTimes[ancestor] = piTravelTimes[i] + tt;
                    ancestors[ancestor] = i;
                }
            }

        }
        Collection<int[]> result = new ArrayList<>();
        int i = 0;
        while (i < ancestors.length) {
            int next = ancestors[i];
            if (next != 0) {
                result.add(Arrays.copyOfRange(route, i, next));
            } else {
                break;
            }
            i = next;
        }
        return result;
    }

    int[] initPiLeaps(int size) {
        int[] piLeaps = new int[size];
        Arrays.fill(piLeaps, Integer.MAX_VALUE);
        piLeaps[0] = 0;

        return piLeaps;
    }

    double[] initPiTravelTimes(int size) {
        double[] piTravelTimes = new double[size];
        Arrays.fill(piTravelTimes, Double.POSITIVE_INFINITY);
        piTravelTimes[size - 1] = 0;
        return piTravelTimes;
    }

    List<TIntArrayList> initLeapAncestors(int size) {
        List<TIntArrayList> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(new TIntArrayList());
        }
        return result;
    }

}
