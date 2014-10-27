package de.hszg.tdvrp.server.resources;

import de.hszg.tdvrp.tdfactories.TDFunctionFactories;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This resource represents all available time-dependent functions.
 *
 * @author weinpau
 */
@Path("td-functions")
@Singleton
public class TDFunctionsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getTDFunctionNames() {
        return TDFunctionFactories.getFactories().stream().
                map(f -> f.getName()).
                collect(Collectors.toList());
    }
    
    
    

}
