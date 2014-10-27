package de.hszg.tdvrp.server.databind;


import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author weinpau
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public ObjectMapperProvider() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new TDVRPDatatypeModule());

    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;

    }

}
