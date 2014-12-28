package de.hszg.tdvrp.server.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.tdfunction.TDFunction;
import de.hszg.tdvrp.core.tdfunction.TDFunctionFactory;
import de.hszg.tdvrp.instances.Instances;
import de.hszg.tdvrp.server.resources.SolveRequest;
import de.hszg.tdvrp.tdfactories.TDFunctionFactories;
import java.io.IOException;

/**
 *
 * @author weinpau
 */
public class SolveRequestDeserializer extends StdDeserializer<SolveRequest> {

    public SolveRequestDeserializer() {
        super(SolveRequest.class);
    }

    @Override
    public SolveRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IOException("invalid start marker");
        }
        String instanceName = null;
        String tdFunctionName = "DEFAULT";

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = jp.getCurrentName();
            jp.nextToken();
            if (jp.getCurrentToken() != JsonToken.VALUE_NULL) {
                switch (fieldname) {
                    case "tdFunction":
                        tdFunctionName = jp.getText();
                        break;
                    case "instance":
                        instanceName = jp.getText();                    
                }
            }
        }
        jp.close();

        return createSolveRequest(instanceName, tdFunctionName);

    }

    private SolveRequest createSolveRequest(String instanceName, String tdFunctionName) {
        TDFunction tdFunction = null;
        Instance instance = Instances.getInstanceByName(instanceName).orElse(null);
        if (instance != null) {
            TDFunctionFactory tdFunctionFactory = TDFunctionFactories.getFactoryByName(tdFunctionName).orElse(null);
            if (tdFunctionFactory != null) {
                tdFunction = tdFunctionFactory.createTDFunction(instance);
            }
        }
        return new SolveRequest(instance, tdFunction);
    }

}
