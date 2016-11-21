package com.sos.joc.schedule.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.schedule.ScheduleConfigurationFilter;
import com.sos.joc.schedule.resource.IScheduleResourceConfiguration;

@Path("schedule")
public class ScheduleResourceConfigurationImpl extends JOCResourceImpl implements IScheduleResourceConfiguration {

    private static final String API_CALL = "./schedule/configuration";

    @Override
    public JOCDefaultResponse postScheduleConfiguration(String accessToken, ScheduleConfigurationFilter scheduleBody) throws Exception {

        try {
            initLogging(API_CALL, scheduleBody);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, scheduleBody.getJobschedulerId(), getPermissons(accessToken).getSchedule()
                    .getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("schedule", scheduleBody.getSchedule())) {
                String schedulePath = normalizePath(scheduleBody.getSchedule());
                String scheduleParent = getParent(schedulePath);
                boolean responseInHtml = scheduleBody.getMime() == ConfigurationMime.HTML;
                String xPath = String.format("/spooler/answer//schedules/schedule[@path='%s']", schedulePath);
                String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders source", scheduleParent);
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, command, xPath, "schedule", responseInHtml, accessToken);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}