package com.sos.joc.schedule.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.schedule.Schedule200VSchema;
import com.sos.joc.model.schedule.ScheduleFilterSchema;
import com.sos.joc.model.schedule.Schedule_;
import com.sos.joc.model.schedule.State;
import com.sos.joc.schedule.resource.IScheduleResource;

@Path("schedule")
public class ScheduleResourceImpl extends JOCResourceImpl implements IScheduleResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postSchedule(String accessToken, ScheduleFilterSchema scheduleFilterSchema) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(scheduleFilterSchema.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            LOGGER.debug("init schedule");
 
            Schedule200VSchema entity = new Schedule200VSchema();
            entity.setDeliveryDate(new Date());
            Schedule_ schedule = new Schedule_();
            schedule.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
            schedule.setName("myName");
            schedule.setPath("myPath");
            State state = new State();
            state.setSeverity(-1);
            state.setText(State.Text.ACTIVE);
            schedule.setState(state);
            schedule.setSubstitutedBy("mySubstitutedBy");
            entity.setSchedule(schedule);
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}