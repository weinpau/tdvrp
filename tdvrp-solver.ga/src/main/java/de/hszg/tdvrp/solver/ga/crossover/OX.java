package de.hszg.tdvrp.solver.ga.crossover;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.ChromosomePair;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class OX implements Crossover {

    Random random = new Random();

    @Override
    public ChromosomePair cross(Chromosome p1, Chromosome p2) {
        int length = p1.route().length;
        int rA = random.nextInt(length);
        int rB = random.nextInt(length);
        if (rA == rB) {
            rB = (rB + 1) % length;
        }
        return cross(p1, p2, rA, rB);
    }

    ChromosomePair cross(Chromosome p1, Chromosome p2, int rA, int rB) {

        int length = p1.route().length;

        int csA = Math.min(rA, rB);
        int csB = Math.max(rA, rB);

        int[] c1 = new int[length];
        int[] c2 = new int[length];

        for (int i = csA; i <= csB; i++) {
            c1[i] = p1.route()[i];
            c2[i] = p2.route()[i];
        }

        int[] t1 = Arrays.copyOfRange(p1.route(), csA, csB + 1);
        int[] t2 = Arrays.copyOfRange(p2.route(), csA, csB + 1);
        Arrays.sort(t1);
        Arrays.sort(t2);

        int iA = csB;
        int iB = csB;

        for (int i = csB + 1; i < length; i++) {
            if (Arrays.binarySearch(t2, p1.route()[i]) < 0) {
                iB = (iB + 1) % length;
                c2[iB] = p1.route()[i];
            }
            if (Arrays.binarySearch(t1, p2.route()[i]) < 0) {
                iA = (iA + 1) % length;
                c1[iA] = p2.route()[i];
            }
        }

        for (int i = 0; i <= csB; i++) {
            if (Arrays.binarySearch(t2, p1.route()[i]) < 0) {
                iB = (iB + 1) % length;
                c2[iB] = p1.route()[i];
            }
            if (Arrays.binarySearch(t1, p2.route()[i]) < 0) {
                iA = (iA + 1) % length;
                c1[iA] = p2.route()[i];
            }
        }

        return new ChromosomePair(p1.copy(c1), p2.copy(c2));
    }

}
