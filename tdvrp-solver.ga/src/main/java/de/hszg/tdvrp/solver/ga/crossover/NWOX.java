package de.hszg.tdvrp.solver.ga.crossover;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.ChromosomePair;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class NWOX implements Crossover {

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
        int[] c1 = new int[length];
        int[] c2 = new int[length];
        int csA = Math.min(rA, rB);
        int csB = Math.max(rA, rB);

        for (int i = csA; i <= csB; i++) {
            c1[i] = p2.route()[i];
            c2[i] = p1.route()[i];
        }

        int[] t1 = Arrays.copyOfRange(p1.route(), csA, csB + 1);
        int[] t2 = Arrays.copyOfRange(p2.route(), csA, csB + 1);
        Arrays.sort(t1);
        Arrays.sort(t2);

        int iA = 0;
        int iB = 0;

        for (int i = 0; i < csA; i++) {
            if (Arrays.binarySearch(t2, p1.route()[i]) < 0) {
                c1[iA++] = p1.route()[i];
            }
            if (Arrays.binarySearch(t1, p2.route()[i]) < 0) {
                c2[iB++] = p2.route()[i];
            }
        }
        int j = csA;
        for (int i = iA; i < csA; i++) {
            while (Arrays.binarySearch(t2, p1.route()[j]) >= 0) {
                j++;
            }
            c1[i] = p1.route()[j++];
        }
        j = csA;
        for (int i = iB; i < csA; i++) {
            while (Arrays.binarySearch(t1, p2.route()[j]) >= 0) {
                j++;
            }
            c2[i] = p2.route()[j++];
        }

        iA = length - 1;
        iB = length - 1;

        for (int i = length - 1; i > csB; i--) {
            if (Arrays.binarySearch(t2, p1.route()[i]) < 0) {
                c1[iA--] = p1.route()[i];
            }
            if (Arrays.binarySearch(t1, p2.route()[i]) < 0) {
                c2[iB--] = p2.route()[i];
            }
        }

        j = csB;
        for (int i = iA; i > csB; i--) {
            while (Arrays.binarySearch(t2, p1.route()[j]) >= 0) {
                j--;
            }

            c1[i] = p1.route()[j--];
        }
        j = csB;
        for (int i = iB; i > csB; i--) {
            while (Arrays.binarySearch(t1, p2.route()[j]) >= 0) {
                j--;
            }

            c2[i] = p2.route()[j--];
        }

        return new ChromosomePair(p1.copy(c1), p2.copy(c2));
    }

}
