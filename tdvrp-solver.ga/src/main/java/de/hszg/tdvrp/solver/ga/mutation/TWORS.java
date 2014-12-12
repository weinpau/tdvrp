package de.hszg.tdvrp.solver.ga.mutation;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.GASolver;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class TWORS implements Mutation {

    Random random = new Random(GASolver.RANDOM_SEED);

    @Override
    public void mutate(Chromosome chromosome) {
        int length = chromosome.route().length;

        int iA = random.nextInt(length);
        int iB = random.nextInt(length);
        if (iA == iB) {
            iB = (iB + 1) % length;
        }

        int[] route = chromosome.route();

        int tmp = route[iA];
        route[iA] = route[iB];
        route[iB] = tmp;
    }

}
