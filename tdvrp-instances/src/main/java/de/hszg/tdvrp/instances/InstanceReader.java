package de.hszg.tdvrp.instances;

import de.hszg.tdvrp.core.model.Instance;
import java.util.List;

/**
 *
 * @author weinpau
 */
public interface InstanceReader {
    
       List<Instance> readInstances();
}
