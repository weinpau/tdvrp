package de.hszg.tdvrp.server.databind.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hszg.tdvrp.core.scheduler.Schedule;
import de.hszg.tdvrp.core.scheduler.VehicleSchedule;
import java.io.IOException;
import java.math.BigDecimal;

/**
 *
 * @author weinpau
 */
public class ScheduleSerializer extends StdSerializer<Schedule> {

    public ScheduleSerializer() {
        super(Schedule.class);
    }

    @Override
    public void serialize(Schedule schedule, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeNumberField("travelingTime", schedule.getTravelingTime());
        jgen.writeNumberField("totalDistance", schedule.getTotalDistance());
        jgen.writeArrayFieldStart("routes");
        for (VehicleSchedule vSchedule : schedule.getVehicleSchedules()) {
            jgen.writeObject(vSchedule);
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

}
