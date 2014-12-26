package de.hszg.tdvrp.benchmark.ga;

import de.hszg.tdvrp.solver.ga.GAOptions;
import de.hszg.tdvrp.solver.ga.crossover.Crossover;
import de.hszg.tdvrp.solver.ga.mutation.Mutation;
import de.hszg.tdvrp.solver.ga.replacement.Replacement;
import de.hszg.tdvrp.solver.ga.selection.Selection;
import de.hszg.tdvrp.solver.ga.splitter.Splitter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author weinpau
 */
public final class ConfigReader {

    public static Config readConfig(String path) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        String source = new String(Files.readAllBytes(Paths.get(path)));

        JSONObject json = new JSONObject(source);
        int attempts = json.optInt("attempts", 3);
        String[] problemClasses = readProblemClasses(json);
        GAOptions options = readGAOptions(json.getJSONObject("options"));
        String tdFunction = json.optString("tdFunction", "DEFAULT");
        return new Config(problemClasses, attempts, tdFunction, options);
    }

    private static String[] readProblemClasses(JSONObject json) throws JSONException {
        JSONArray jsonArray = json.getJSONArray("problemClasses");
        String[] problemClasses = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            problemClasses[i] = jsonArray.getString(i);
        }
        return problemClasses;
    }

    private static GAOptions readGAOptions(JSONObject json) throws IllegalAccessException, ClassNotFoundException, InstantiationException, JSONException {

        GAOptions options = new GAOptions();
        options.populationSize(json.optInt("populationSize", options.populationSize()));
        options.maxRounds(json.optInt("maxRounds", options.maxRounds()));
        options.maxRoundsWithoutImproving(json.optInt("maxRoundsWithoutImproving", options.maxRoundsWithoutImproving()));
        options.mutationProbability(json.optDouble("mutationProbability", options.mutationProbability()));
        options.selectionRate(json.optDouble("selectionRate", options.selectionRate()));
        options.selection((Selection) Class.forName(json.optString("selection",
                options.selection().getClass().getName())).newInstance());
        options.replacement((Replacement) Class.forName(json.optString("replacement",
                options.replacement().getClass().getName())).newInstance());
        options.splitter((Splitter) Class.forName(json.optString("splitter",
                options.splitter().getClass().getName())).newInstance());
        options.crossovers(readCrossovers(json));
        options.mutations(readMutations(json));
        return options;
    }

    private static Crossover[] readCrossovers(JSONObject json) throws IllegalAccessException, JSONException, ClassNotFoundException, InstantiationException {
        JSONArray array = json.getJSONArray("crossovers");
        Crossover[] result = new Crossover[array.length()];
        for (int i = 0; i < array.length(); i++) {
            Class clazz = Class.forName(array.getString(i));
            result[i] = (Crossover) clazz.newInstance();

        }
        return result;
    }

    private static Mutation[] readMutations(JSONObject json) throws IllegalAccessException, JSONException, ClassNotFoundException, InstantiationException {
        JSONArray array = json.getJSONArray("mutations");
        Mutation[] result = new Mutation[array.length()];
        for (int i = 0; i < array.length(); i++) {
            Class clazz = Class.forName(array.getString(i));
            result[i] = (Mutation) clazz.newInstance();

        }
        return result;
    }

}
