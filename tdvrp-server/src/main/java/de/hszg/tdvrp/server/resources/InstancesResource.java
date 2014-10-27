package de.hszg.tdvrp.server.resources;

import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.instances.Instances;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * This resource represents all available problem instances.
 *
 * @author weinpau
 */
@Path("instances")
@Singleton
public class InstancesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getInstanceNames() {
        return Instances.getInstances().stream().
                map(i -> i.getName()).
                collect(Collectors.toList());
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInstance(@PathParam("name") String name) {
        return Instances.getInstanceByName(name).
                map(i -> Response.ok(i).build()).
                orElse(Response.status(Status.NOT_FOUND).build());
    }

}
