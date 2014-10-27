package de.hszg.tdvrp.server.filters;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;



/**
 *
 * @author weinpau
 */
@Provider
public class CACHEFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Pragma", "no-cache");
        responseContext.getHeaders().add(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate"); 
        responseContext.getHeaders().add(HttpHeaders.EXPIRES, "Thu, 01 Jan 1970 00:00:00");
    }

}
