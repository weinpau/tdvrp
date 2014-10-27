package de.hszg.tdvrp.server.resources;

import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.solver.Solution;
import de.hszg.tdvrp.core.solver.Solver;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Represents the solver resource.
 *
 * @author weinpau
 */
@Path("solve")
@Singleton
public class SolveResource {

    @Inject
    Solver solver;

    @Inject
    Scheduler scheduler;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response solve(SolveRequest solveRequest) {
        if (solveRequest.getInstance() == null || solveRequest.getTDFunction() == null) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            System.out.printf("SOLVE %s%n",solveRequest.getInstance().getName());
            Solution solution = createSolution(solveRequest);
            if (solution == null) {
                return Response.status(Status.NO_CONTENT).build();
            }
            System.out.printf("CREATE SCHEDULE FOR %s%n", solveRequest.getInstance().getName());
            return scheduler.schedule(solution).
                    map(s -> Response.ok(s).build()).
                    orElse(Response.status(Status.NO_CONTENT).build());
        }
    }

    private Solution createSolution(SolveRequest solveRequest) {
        if (solveRequest.getExpectedNumberOfVehicles() == null) {
            return solver.solve(solveRequest.getInstance(), solveRequest.getTDFunction()).get();
        } else {
            return solver.solve(solveRequest.getInstance(), solveRequest.getTDFunction(), solveRequest.getExpectedNumberOfVehicles()).get();
        }
    }

}
