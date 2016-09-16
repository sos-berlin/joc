package com.sos.joc.schedule.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.schedule.ScheduleConfigurationFilterSchema;
import com.sos.joc.schedule.resource.IScheduleResourceConfiguration;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("schedule")
public class ScheduleResourceConfigurationImpl extends JOCResourceImpl implements IScheduleResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postScheduleConfiguration(String accessToken, ScheduleConfigurationFilterSchema scheduleBody) throws Exception {

        LOGGER.debug("init schedule/configuration");
        JOCDefaultResponse jocDefaultResponse = init(scheduleBody.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isConfiguration());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            ConfigurationSchema entity = new ConfigurationSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (jocXmlCommand.checkRequiredParameter("schedule", scheduleBody.getSchedule())) {
                boolean responseInHtml = scheduleBody.getMime() == ScheduleConfigurationFilterSchema.Mime.HTML;
                String xPath = "/spooler/answer//schedules/schedule[@path='"+("/"+scheduleBody.getSchedule()).replaceAll("//+", "/")+"']"; 
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createScheduleConfigurationPostCommand(scheduleBody), xPath, "schedule", responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private String createScheduleConfigurationPostCommand(ScheduleConfigurationFilterSchema scheduleBody) {
        JSCmdShowState showSchedules = new JSCmdShowState(Globals.schedulerObjectFactory);
        showSchedules.setSubsystems("folder schedule");
        showSchedules.setWhat("folders source");
        return Globals.schedulerObjectFactory.toXMLString(showSchedules);
    }

}