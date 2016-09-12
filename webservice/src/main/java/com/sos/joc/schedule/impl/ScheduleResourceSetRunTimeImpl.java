package com.sos.joc.schedule.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.schedule.resource.IScheduleResourceSetRunTime;
import com.sos.joc.model.schedule.ModifyRuntimeSchema;

@Path("schedule")
public class ScheduleResourceSetRunTimeImpl extends JOCResourceImpl implements IScheduleResourceSetRunTime {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceSetRunTimeImpl.class);

    @Override
    public JOCDefaultResponse postScheduleSetRuntime(String accessToken, ModifyRuntimeSchema modifyRuntimeSchema) throws Exception {
        LOGGER.debug("init schedule/set_run_time");
        
            JOCDefaultResponse jocDefaultResponse = init(modifyRuntimeSchema.getSchedulerId(), getPermissons(accessToken).getSchedule().isEdit());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            try {

            return JOCDefaultResponse.responseStatusJSOk(new Date());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing schedule.set_run_time:  %s:%s", e.getCause(), e.getMessage()));
        }
    }

}