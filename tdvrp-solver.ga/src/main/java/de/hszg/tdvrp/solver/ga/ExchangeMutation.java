package de.hszg.tdvrp.solver.ga;

import java.util.Random;

/**
 *
 * @author weinpau
 */
public class ExchangeMutation implements Mutation {

    Random random = new Random();

    @Override
    public void mutate(Chromosome chromosome) {

        int iA = random.nextInt(chromosome.route.length);
        int iB = random.nextInt(chromosome.route.length);

        int a = chromosome.route[iA];
        chromosome.route[iA] = chromosome.route[iB];
        chromosome.route[iB] = a;
    }

}
