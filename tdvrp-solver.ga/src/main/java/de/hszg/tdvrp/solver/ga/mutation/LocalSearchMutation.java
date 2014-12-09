package de.hszg.tdvrp.solver.ga.mutation;

import de.hszg.tdvrp.solver.ga.Chromosome;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class LocalSearchMutation implements Mutation {

    Random random = new Random();

    @Override
    public void mutate(Chromosome chromosome) {
        Chromosome mutated;
        Chromosome best = chromosome;
        int length = best.route().length;

        int sA = random.nextInt(length);
        int sB = random.nextInt(length);

        int rangeA = Math.min(random.nextInt(10), length);
        int rangeB = Math.min(random.nextInt(10), length);

        for (int i = 0; i < rangeA; i++) {
            int iA = (sA + i) % length;
            for (int j = 0; j < rangeB; j++) {
                int iB = (sB + j) % length;

                mutated = best.copy(best.route());
                int[] mutatedRoute = mutated.route();

                int tmp = mutatedRoute[iA];
                mutatedRoute[iA] = mutatedRoute[iB];
                mutatedRoute[iB] = tmp;

                if (mutated.fitness() > best.fitness()) {
                    best = mutated;
                    break;
                }
            }
        }
        if (chromosome.fitness() < best.fitness()) {
            chromosome.route(best.route());
        }

    }

}
