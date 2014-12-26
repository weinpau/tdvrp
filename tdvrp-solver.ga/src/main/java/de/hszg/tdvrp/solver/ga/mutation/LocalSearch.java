package de.hszg.tdvrp.solver.ga.mutation;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.GASolver;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class LocalSearch implements Mutation {

    Random random = new Random(GASolver.RANDOM_SEED);

    @Override
    public void mutate(Chromosome chromosome) {
        Chromosome mutated = chromosome.copy();
        int length = chromosome.route().length;

        int sA = random.nextInt(length);
        int sB = random.nextInt(length);

        int[] mutatedRoute = mutated.route();

        for (int i = 0; i < length; i++) {
            int iA = (sA + i) % length;
            for (int j = 0; j < length; j++) {
                int iB = (sB + j) % length;

                int tmp = mutatedRoute[iA];
                mutatedRoute[iA] = mutatedRoute[iB];
                mutatedRoute[iB] = tmp;
                mutated.route(mutatedRoute);

                if (mutated.fitness() > chromosome.fitness()) {
                    chromosome.route(mutated.route());
                    return;
                } else {
                    mutatedRoute[iB] = mutatedRoute[iA];
                    mutatedRoute[iA] = tmp;
                }
            }
        }

    }

}
