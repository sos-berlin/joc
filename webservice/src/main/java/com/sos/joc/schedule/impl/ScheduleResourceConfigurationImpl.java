package com.sos.joc.schedule.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.schedule.ScheduleConfigurationFilterSchema;
import com.sos.joc.schedule.resource.IScheduleResourceConfiguration;

@Path("schedule")
public class ScheduleResourceConfigurationImpl extends JOCResourceImpl implements IScheduleResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postScheduleConfiguration(String accessToken, ScheduleConfigurationFilterSchema scheduleConfigurationFilterSchema) throws Exception {

        LOGGER.debug("init schedule/configuration");
        JOCDefaultResponse jocDefaultResponse = init(scheduleConfigurationFilterSchema.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isConfiguration());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            ConfigurationSchema entity = ConfigurationUtils.getEntity();
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}