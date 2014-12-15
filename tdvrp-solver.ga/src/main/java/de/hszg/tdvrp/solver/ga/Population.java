package de.hszg.tdvrp.solver.ga;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author weinpau
 */
public final class Population {

    private final Set<Chromosome> chromosomes = new HashSet<>();

    public Population(Collection<Chromosome> chromosomes) {
        this.chromosomes.addAll(chromosomes);
    }

    public Set<Chromosome> getChromosomes() {
        return Collections.unmodifiableSet(chromosomes);
    }

    public void add(Chromosome chromosome) {
        chromosomes.add(chromosome);
    }

    public void addAll(Collection<Chromosome> chromosomes) {
        this.chromosomes.addAll(chromosomes);
    }

    public boolean remove(Chromosome chromosome) {
        return chromosomes.remove(chromosome);
    }

    public boolean removeAll() {
        chromosomes.clear();
        return true;
    }

    public Optional<Chromosome> getBestChromosome() {
        if (chromosomes.isEmpty()) {
            return Optional.empty();
        }
        Chromosome best = null;
        for (Chromosome c : chromosomes) {
            if (best == null || c.fitness() > best.fitness()) {
                best = c;
            }
        }
        return Optional.of(best);
    }

    public Optional<Chromosome> getWorstChromosome() {
        if (chromosomes.isEmpty()) {
            return Optional.empty();
        }
        Chromosome worst = null;
        for (Chromosome c : chromosomes) {
            if (worst == null || c.fitness() < worst.fitness()) {
                worst = c;
            }
        }
        return Optional.of(worst);
    }

}
