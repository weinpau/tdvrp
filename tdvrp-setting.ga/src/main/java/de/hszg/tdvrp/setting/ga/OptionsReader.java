package de.hszg.tdvrp.setting.ga;

import de.hszg.tdvrp.solver.ga.GAOptions;
import de.hszg.tdvrp.solver.ga.crossover.Crossover;
import de.hszg.tdvrp.solver.ga.mutation.Mutation;
import de.hszg.tdvrp.solver.ga.replacement.Replacement;
import de.hszg.tdvrp.solver.ga.selection.Selection;
import de.hszg.tdvrp.solver.ga.splitter.Splitter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author weinpau
 */
public final class OptionsReader {

    public static GAOptions readOptions(String path) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        String source = new String(Files.readAllBytes(Paths.get(path)));

        JSONObject json = new JSONObject(source);

        GAOptions options = new GAOptions();

        options.populationSize(json.optInt("populationSize", options.populationSize()));
        options.maxRounds(json.optInt("maxRounds", options.maxRounds()));
        options.maxRoundsWithoutImproving(json.optInt("maxRoundsWithoutImproving", options.maxRoundsWithoutImproving()));
        options.initPopulationVariance(json.optDouble("initPopulationVariance", options.initPopulationVariance()));
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
