package de.hszg.tdvrp.instances.solomon;

import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Depot;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.model.Position;
import de.hszg.tdvrp.instances.InstanceReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author weinpau
 */
public class SolomonReader implements InstanceReader {

    private final String[] categories = {"025", "050", "100"};

    @Override
    public List<Instance> readInstances() {
        List<Instance> result = new ArrayList<>();
        try {

            for (String category : categories) {
                for (String resource : getResources(category + "/")) {

                    String instanceName = category + "_" + resource.substring(0, resource.indexOf("."));
                    InputStream in = getClass().getResource(category + "/" + resource).openStream();
                    result.add(readInstance(instanceName, in));

                }
            }
        } catch (IOException ex) {
        }
        return result;
    }

    private Instance readInstance(String name, InputStream rawContent) throws IOException {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(rawContent))) {

            skipLines(in, 4);

            StringTokenizer tokenizer = new StringTokenizer(in.readLine());
            int availableVehicles = Integer.parseInt(tokenizer.nextToken());
            int vehicleCapacity = Integer.parseInt(tokenizer.nextToken());

            skipLines(in, 4);

            Depot depot = readDepot(in);
            List<Customer> customers = readCustomers(in);

            return createInstance(name, depot, customers, availableVehicles, vehicleCapacity);
        }

    }

    private List<Customer> readCustomers(BufferedReader in) throws NumberFormatException, IOException {
        List<Customer> customers = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {

            StringTokenizer tokenizer = new StringTokenizer(line);
            if (tokenizer.countTokens() != 7) {
                break;
            }

            int number = Integer.parseInt(tokenizer.nextToken());
            double x = Double.parseDouble(tokenizer.nextToken());
            double y = Double.parseDouble(tokenizer.nextToken());
            double demand = Double.parseDouble(tokenizer.nextToken());
            double readTime = Double.parseDouble(tokenizer.nextToken());
            double dueTime = Double.parseDouble(tokenizer.nextToken());
            double serviceTime = Double.parseDouble(tokenizer.nextToken());

            customers.add(new Customer(number, new Position(x, y), demand, readTime, dueTime, serviceTime));
        }
        return customers;
    }

    private Depot readDepot(BufferedReader in) throws NumberFormatException, IOException {
        StringTokenizer tokenizer = new StringTokenizer(in.readLine());
        tokenizer.nextToken();
        double x = Double.parseDouble(tokenizer.nextToken());
        double y = Double.parseDouble(tokenizer.nextToken());
        tokenizer.nextToken();
        tokenizer.nextToken();
        double closingTime = Double.parseDouble(tokenizer.nextToken());
        Depot depot = new Depot(new Position(x, y), closingTime);
        return depot;
    }

    private void skipLines(BufferedReader in, int lines) throws IOException {
        for (int i = 0; i < lines; i++) {
            in.readLine();
        }
    }

    private Instance createInstance(String name, Depot depot, List<Customer> customers, int availableVehicles, int vehicleCapacity) {
        return new Instance() {

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Depot getDepot() {
                return depot;
            }

            @Override
            public List<Customer> getCustomers() {
                return customers;
            }

            @Override
            public int getAvailableVehicles() {
                return availableVehicles;
            }

            @Override
            public int getVehicleCapacity() {
                return vehicleCapacity;
            }
        };
    }

    private String[] getResources(String path) throws IOException {

        URL dirURL = getClass().getResource(path);

        if (dirURL.getProtocol().equals("jar")) {

            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
            String innerPath = dirURL.getPath().substring(dirURL.getPath().indexOf("!") + 2);
           
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries();
            Set<String> result = new HashSet<>();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(innerPath)) {
                    String entry = name.substring(innerPath.length());
                    int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                        entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        String fileName = URLDecoder.decode(dirURL.getFile(), "UTF-8");
        File cwd = new File(fileName);
        return cwd.list();

    }

}
