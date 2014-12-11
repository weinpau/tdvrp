package de.hszg.tdvrp.solver.ga.mutation;

import de.hszg.tdvrp.solver.ga.Chromosome;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class RSM implements Mutation {

    Random random = new Random();

    @Override
    public void mutate(Chromosome chromosome) {
        int length = chromosome.route().length;

        int iA = random.nextInt(length);
        int iB = random.nextInt(length);
        if (iA == iB) {
            iB = (iB + 1) % length;
        }

        reverse(chromosome, iA, iB);

    }

    private void reverse(Chromosome chromosome,  int iA, int iB) {
        int[] array = chromosome.route();        
        int tmp;
        while (iB > iA) {
            tmp = array[iB];
            array[iB] = array[iA];
            array[iA] = tmp;
            iB--;
            iA++;
        }
    }

}
