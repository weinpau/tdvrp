package de.hszg.tdvrp.server;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.hszg.tdvrp.core.scheduler.Scheduler;
import de.hszg.tdvrp.core.solver.Solver;
import de.hszg.tdvrp.scheduler.straight.StraightScheduler;
import de.hszg.tdvrp.solver.ga.GASolver;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author weinpau
 */
public class App {

    public static final int HTTP_PORT = 8080;

    private HttpServer server;

    public void start() {
        try {
            URI baseUri = UriBuilder.fromUri("http://localhost/api").port(HTTP_PORT).build();
            ResourceConfig config = createResourceConfig();
            server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
            Thread.currentThread().join();
        } catch (IllegalArgumentException | InterruptedException | UriBuilderException e) {
        }

    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private ResourceConfig createResourceConfig() {
        return new ResourceConfig().
                packages(true, "de.hszg.tdvrp.server").
                register(JacksonJsonProvider.class).
                register(new AbstractBinder() {

                    @Override
                    protected void configure() {
                        bind(new GASolver()).to(Solver.class);
                        bind(new StraightScheduler()).to(Scheduler.class);
                    }
                });

    }

    public static void main(String[] args) {
        new App().start();
    }
}
