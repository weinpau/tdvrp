package de.hszg.tdvrp.instances;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.instances.solomon.SolomonReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
public final class Instances {

    private static final List<Instance> instances = new ArrayList<>();

    private Instances() {
    }

    static {
        readInstances(new SolomonReader());
    }

    private static void readInstances(InstanceReader... readers) {
        for (InstanceReader reader : readers) {
            instances.addAll(reader.readInstances());
        }
    }

    public static List<Instance> getInstances() {
        return Collections.unmodifiableList(instances);
    }

    public static Optional<Instance> getInstanceByName(String instanceName) {
        if (instanceName == null) {
            return Optional.empty();
        } else {
            return getInstances().stream().
                    filter(f -> instanceName.equals(f.getName())).
                    findAny();
        }
    }

}
