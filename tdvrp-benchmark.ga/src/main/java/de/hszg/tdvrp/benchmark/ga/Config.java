package de.hszg.tdvrp.benchmark.ga;

import de.hszg.tdvrp.solver.ga.GAOptions;

/**
 *
 * @author weinpau
 */
public class Config {

    private final String[] problemClasses;
    private final int attempts;
    private final GAOptions gaOptions;
    private final String tdFunction;

    public Config(String[] problemClasses, int attempts, String tdFunction, GAOptions gaOptions) {
        this.problemClasses = problemClasses;
        this.attempts = attempts;
        this.gaOptions = gaOptions;
        this.tdFunction = tdFunction;
    }

    public int getAttempts() {
        return attempts;
    }

    public GAOptions getGAOptions() {
        return gaOptions;
    }

    public String[] getProblemClasses() {
        return problemClasses;
    }

    public String getTDFunction() {
        return tdFunction;
    }
    
    

}
