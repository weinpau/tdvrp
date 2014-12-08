package de.hszg.tdvrp.solver.ga;

/**
 *
 * @author weinpau
 */
public final class GAOptions {

    private int populationSize = 30;

    private int maxRounds = 20000;
    private int maxRoundsWithoutImproving = 500;
    private double selectionRate = .5;
    private double mutationProbability = 0.3;
    private double initPopulationVariance = 1.5;

    private Selection selection = new RouletteSelection();
    private Replacement replacement = new RestrictedTournamentReplacement();
    private Splitter splitter = new VehicleMinimizingSplitter();

    private Mutation[] mutations = new Mutation[]{new LocalSearchMutation(), new ExchangeMutation()};
    private Crossover[] crossovers = new Crossover[]{new OX(), new CX()};

    public int populationSize() {
        return populationSize;
    }

    public int maxRounds() {
        return maxRounds;
    }

    public int maxRoundsWithoutImproving() {
        return maxRoundsWithoutImproving;
    }

    public double mutationProbability() {
        return mutationProbability;
    }

    public double initPopulationVariance() {
        return initPopulationVariance;
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

    public GAOptions maxRoundsWithoutImproving(int maxRoundsWithoutImproving) {
        this.maxRoundsWithoutImproving = maxRoundsWithoutImproving;
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

    public GAOptions initPopulationVariance(double initPopulationVariance) {
        this.initPopulationVariance = initPopulationVariance;
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
