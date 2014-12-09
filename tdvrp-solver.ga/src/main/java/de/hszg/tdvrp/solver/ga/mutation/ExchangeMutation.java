package de.hszg.tdvrp.solver.ga.mutation;

import de.hszg.tdvrp.solver.ga.Chromosome;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class ExchangeMutation implements Mutation {

    Random random = new Random();

    @Override
    public void mutate(Chromosome chromosome) {
        int length = chromosome.route().length;

        int iA = random.nextInt(length);
        int iB = random.nextInt(length);
        int[] route = chromosome.route();

        int tmp = route[iA];
        route[iA] = route[iB];
        route[iB] = tmp;
    }

}
