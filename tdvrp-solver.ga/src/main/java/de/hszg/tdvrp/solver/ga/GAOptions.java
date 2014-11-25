package de.hszg.tdvrp.solver.ga;

/**
 *
 * @author weinpau
 */
public final class GAOptions {

    private int populationSize = 50;

    private int maxRounds = 1000;
    private double selectionRate = .3;
    private double mutationProbability = 0.1;
    private double crossoverProbability = 0.20;

    private Selection selection = new ElitistSelection();
    private Replacement replacement = new ElitistReplacement();
    private Splitter splitter = new StraightSplitter();

    private Mutation mutation = new ExchangeMutation();
    private Crossover crossover = new OX();

    public int populationSize() {
        return populationSize;
    }

    public int maxRounds() {
        return maxRounds;
    }

    public double mutationProbability() {
        return mutationProbability;
    }

    public double crossoverProbability() {
        return crossoverProbability;
    }

    public Selection selection() {
        return selection;
    }

    public double selectionRate() {
        return selectionRate;
    }

    public Replacement replacement() {
        return replacement;
    }

    public Splitter splitter() {
        return splitter;
    }

    public Mutation mutation() {
        return mutation;
    }

    public Crossover crossover() {
        return crossover;
    }

    public GAOptions populationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public GAOptions maxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
        return this;
    }

    public GAOptions mutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
        return this;
    }

    public GAOptions crossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
        return this;
    }

    public GAOptions selection(Selection selection) {
        this.selection = selection;
        return this;
    }

    public GAOptions selectionRate(double selectionRate) {
        this.selectionRate = selectionRate;
        return this;
    }

    public GAOptions replacement(Replacement replacement) {
        this.replacement = replacement;
        return this;
    }

    public GAOptions splitter(Splitter splitter) {
        this.splitter = splitter;
        return this;
    }

    public GAOptions mutation(Mutation mutation) {
        this.mutation = mutation;
        return this;
    }

    public GAOptions crossover(Crossover crossover) {
        this.crossover = crossover;
        return this;
    }

}