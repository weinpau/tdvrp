package de.hszg.tdvrp.solver.ga;

/**
 *
 * @author weinpau
 */
public final class GAOptions {

    private int populationSize = 30;

    private int maxRounds = 2000;
    private double selectionRate = .5;
    private double mutationProbability = 0.3;

    private Selection selection = new RouletteSelection();
    private Replacement replacement = new ElitistReplacement();
    private Splitter splitter = new TravelTimeMinimizingSplitter();

    private Mutation[] mutations = new Mutation[]{new LocalSearchMutation(), new ExchangeMutation()};
    private Crossover[] crossovers = new Crossover[]{new OX()};

    public int populationSize() {
        return populationSize;
    }

    public int maxRounds() {
        return maxRounds;
    }

    public double mutationProbability() {
        return mutationProbability;
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

    public Mutation[] mutations() {
        return mutations;
    }

    public Crossover[] crossovers() {
        return crossovers;
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

    public GAOptions mutations(Mutation... mutations) {
        this.mutations = mutations;
        return this;
    }

    public GAOptions crossovers(Crossover... crossovers) {
        this.crossovers = crossovers;
        return this;
    }

}
