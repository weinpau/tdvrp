package de.hszg.tdvrp.solver.ga.crossover;

import de.hszg.tdvrp.solver.ga.Chromosome;
import de.hszg.tdvrp.solver.ga.ChromosomePair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author weinpau
 */
public class CX implements Crossover {

    Random random = new Random();

    @Override
    public ChromosomePair cross(Chromosome p1, Chromosome p2) {
        int length = p1.route().length;
        return cross(p1, p2, random.nextInt(length));
    }

    ChromosomePair cross(Chromosome p1, Chromosome p2, int idx) {

        int length = p1.route().length;

        int[] parent1Rep = p1.route();
        int[] parent2Rep = p2.route();
        int[] child1Rep = new int[length];
        int[] child2Rep = new int[length];
        System.arraycopy(parent1Rep, 0, child1Rep, 0, length);
        System.arraycopy(parent2Rep, 0, child2Rep, 0, length);

        int[] parent1Index = new int[length];
        for (int i = 0; i < length; i++) {
            parent1Index[parent1Rep[i] - 1] = i;
        }

        Set<Integer> visitedIndices = new HashSet<>(length);
        List<Integer> indices = new ArrayList<>(length);

        int cycle = 1;

        while (visitedIndices.size() < length) {
            indices.add(idx);

            int item = parent2Rep[idx];
            idx = parent1Index[item - 1];

            while (idx != indices.get(0)) {
                indices.add(idx);
                item = parent2Rep[idx];
                idx = parent1Index[item - 1];
            }

            if (cycle++ % 2 != 0) {
                for (int i : indices) {
                    int tmp = child1Rep[i];
                    child1Rep[i] = child2Rep[i];
                    child2Rep[i] = tmp;
                }
            }

            visitedIndices.addAll(indices);
            idx = (indices.get(0) + 1) % length;
            while (visitedIndices.contains(idx) && visitedIndices.size() < length) {
                idx++;
                if (idx >= length) {
                    idx = 0;
                }
            }
            indices.clear();
        }

        return new ChromosomePair(p1.copy(child1Rep), p2.copy(child2Rep));

    }

}
