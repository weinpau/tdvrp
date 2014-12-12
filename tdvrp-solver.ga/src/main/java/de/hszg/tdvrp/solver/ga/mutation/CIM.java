package de.hszg.tdvrp.solver.ga.mutation;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.GASolver;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class CIM implements Mutation {

    Random random = new Random(GASolver.RANDOM_SEED);

    @Override
    public void mutate(Chromosome chromosome) {

        int length = chromosome.route().length;
        int c = random.nextInt(length - 1);

        reverse(chromosome, 0, c);
        reverse(chromosome, c + 1, length - 1);

    }

    private void reverse(Chromosome chromosome, int iA, int iB) {
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
