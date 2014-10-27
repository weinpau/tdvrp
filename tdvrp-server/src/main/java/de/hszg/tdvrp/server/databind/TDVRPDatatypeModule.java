package de.hszg.tdvrp.server.databind;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.hszg.tdvrp.core.model.Instance;
import de.hszg.tdvrp.core.scheduler.Schedule;
import de.hszg.tdvrp.server.databind.deser.SolveRequestDeserializer;
import de.hszg.tdvrp.server.databind.ser.InstanceSerializer;
import de.hszg.tdvrp.server.databind.ser.ScheduleSerializer;
import de.hszg.tdvrp.server.resources.SolveRequest;

/**
 *
 * @author weinpau
 */
public final class TDVRPDatatypeModule extends SimpleModule {

    public TDVRPDatatypeModule() {
        super("tdvrp-datatypes");

        addDeserializer(SolveRequest.class, new SolveRequestDeserializer());

        addSerializer(Instance.class, new InstanceSerializer());
        addSerializer(Schedule.class, new ScheduleSerializer());

    }

}
