package com.sos.joc.schedules.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.schedules.resource.ISchedulesResource;
import com.sos.joc.model.schedule.Schedule_;
import com.sos.joc.model.schedule.SchedulesFilterSchema;
import com.sos.joc.model.schedule.SchedulesVSchema;
import com.sos.joc.model.schedule.State;

@Path("schedules")
public class ScheduleResourceImpl extends JOCResourceImpl implements ISchedulesResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postSchedules(String accessToken, SchedulesFilterSchema schedulesFilterSchema) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(schedulesFilterSchema.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            LOGGER.debug("init schedules");
 
            SchedulesVSchema entity = new SchedulesVSchema();
            entity.setDeliveryDate(new Date());
            List<Schedule_> listOfSchedules = new ArrayList<Schedule_>();
            Schedule_ schedule = new Schedule_();
            schedule.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
            schedule.setName("myName");
            schedule.setPath("myPath");
            State state = new State();
            state.setSeverity(-1);
            state.setText(State.Text.ACTIVE);
            schedule.setState(state);
            schedule.setSubstitutedBy("mySubstitutedBy");
            listOfSchedules.add(schedule);
            
            entity.setSchedules(listOfSchedules);
           
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}