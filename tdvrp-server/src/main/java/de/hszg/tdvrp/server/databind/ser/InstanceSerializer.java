package de.hszg.tdvrp.server.databind.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hszg.tdvrp.core.model.Customer;
import de.hszg.tdvrp.core.model.Instance;
import java.io.IOException;

/**
 *
 * @author weinpau
 */
public class InstanceSerializer extends StdSerializer<Instance> {

    public InstanceSerializer() {
        super(Instance.class);
    }

    @Override
    public void serialize(Instance instance, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("name", instance.getName());
        jgen.writeNumberField("availableVehicles", instance.getAvailableVehicles());
        jgen.writeNumberField("vehicleCapacity", instance.getVehicleCapacity());
        jgen.writeObjectField("depot", instance.getDepot());

        jgen.writeArrayFieldStart("customers");
        for (Customer customer : instance.getCustomers()) {
            jgen.writeObject(customer);
        }
        jgen.writeEndArray();
        jgen.writeEndObject();

    }

}
