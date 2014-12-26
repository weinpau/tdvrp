package de.hszg.tdvrp.solver.clarke;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.solver.clarke.HobQueue.Hob;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 *
 * @author weinpau
 */
public class HobQueue extends PriorityQueue<Hob> {

    Instance instance;
    TDFunction tdFunction;

    RouteTravelTimeCalculator travelTimeCalculator;

    public HobQueue(Instance instance, TDFunction tdFunction, int[] route) {
        this.instance = instance;
        this.tdFunction = tdFunction;
        travelTimeCalculator = new RouteTravelTimeCalculator(instance, tdFunction);

        for (int i = 0; i < route.length; i++) {
            for (int j = i + 1; j < route.length; j++) {
                double c = travelTimeCalculator.travelTime(new int[]{route[i]});
                c += travelTimeCalculator.travelTime(new int[]{route[j]});

                Hob hob = createHob(new int[]{route[i], route[j]}, new int[]{route[j], route[i]}, c);

                if (hob != null) {
                    offer(hob);
                }
            }

        }
    }

    public final class Hob implements Comparable<Hob> {

        private final int[] route;
        private final double costs, saving;

        private Hob(int[] route) {
            this.route = route;
            costs = travelTimeCalculator.travelTime(route);
            double c = 0;
            for (int r : route) {
                c += travelTimeCalculator.travelTime(new int[]{r});
            }
            saving = c - costs;

        }

        public int[] route() {
            return route;
        }

        public double costs() {
            return costs;
        }

        public double saving() {
            return saving;
        }

        public Hob connect(Hob other) {

            int[] r0 = connect(route, other.route);
            if (r0 == null || !disjoint(r0)) {
                return null;
            }
            int[] r1 = reverse(r0);
            double d = costs() + other.costs();
            return createHob(r0, r1, d);

        }

        int[] connect(int[] a1, int[] a2) {

            int[] result = new int[a1.length + a2.length - 1];

            if (a1[0] == a2[0]) {
                a1 = reverse(a1);
            } else if (a1[0] == a2[a2.length - 1]) {
                a1 = reverse(a1);
                a2 = reverse(a2);
            } else if (a1[a1.length - 1] == a2[a2.length - 1]) {
                a2 = reverse(a2);
            } else {
                return null;
            }

            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 1, result, a1.length, a2.length - 1);
            return result;
        }

        @Override
        public int compareTo(Hob o) {
            return Double.compare(saving, o.saving) * -1;
        }

    }

    private Hob createHob(int[] r0, int[] r1, double d) {
        double c0 = travelTimeCalculator.travelTime(r0);
        double c1 = travelTimeCalculator.travelTime(r1);

        if (Double.isFinite(c0) && (Double.isNaN(c1) || c0 < c1)) {
            return new Hob(r0);
        } else if (Double.isFinite(c1)) {
            return new Hob(r1);
        } else {
            return null;
        }
    }

    private boolean disjoint(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (i != j && array[i] == array[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[] reverse(int[] array) {
        array = Arrays.copyOf(array, array.length);
        int iA = 0, iB = array.length - 1, tmp;
        while (iB > iA) {
            tmp = array[iB];
            array[iB] = array[iA];
            array[iA] = tmp;
            iB--;
            iA++;
        }
        return array;
    }

}
