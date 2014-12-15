package de.hszg.tdvrp.setting.ga;

import de.hszg.tdvrp.benchmark.ProblemClassResults;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author weinpau
 */
public final class CSVOutput {

    public static void output(List<ProblemClassResults> results, String fileName) {

        try {
            FileWriter writer = new FileWriter(fileName);

            writer.append("Problem class");
            writer.append(";");
            writer.append("Avg number of vehicles");
            writer.append(";");
            writer.append("Avg distance");
            writer.append(";");
            writer.append("Avg travel time");
            writer.append('\n');

            for (ProblemClassResults res : results) {

                writer.append(res.getProblemClass());
                writer.append(";");
                writer.append(("" + res.getAvgNumberOfVehicles()).replaceAll("\\.", ","));
                writer.append(";");
                writer.append(("" + res.getAvgDistance()).replaceAll("\\.", ","));
                writer.append(";");
                writer.append(("" + res.getAvgTravelTime()).replaceAll("\\.", ","));
                writer.append('\n');

            }

            //generate whatever data you want
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

