package de.hszg.tdvrp.solver.ga;

/**
 *
 * @author weinpau
 */
public final class ChromosomePair {

    private final Chromosome left, right;

    public ChromosomePair(Chromosome left, Chromosome right) {
        this.left = left;
        this.right = right;
    }

    public Chromosome left() {
        return left;
    }

    public Chromosome right() {
        return right;
    }
}
