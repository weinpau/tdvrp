package de.hszg.tdvrp.solver.ga;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 *
 * @author weinpau
 */
public class RouletteSelection implements Selection {

    Random random = new Random();

    @Override
    public Collection<Chromosome> select(Population population, int selectionSize) {

        List<Chromosome> result = new ArrayList<>();

        List<Chromosome> candidates = new ArrayList<>(population.getChromosomes());

        while (result.size() < selectionSize) {

            double totalFitness = candidates.stream().mapToDouble(c -> c.fitness()).sum();
            double ball = random.nextDouble() * totalFitness;
            double pos = 0;
            Chromosome winner = null;

            for (Chromosome c : candidates) {
                pos += c.fitness();
                if (ball < pos) {
                    winner = c;
                    break;
                }
            }

            if (winner != null) {
                result.add(winner);
                candidates.remove(winner);
            }
        }
        return result;
    }

}
