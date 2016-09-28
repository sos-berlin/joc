package com.sos.joc.schedule.impl;

import java.util.Date;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.schedule.Schedule;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.Schedule200VSchema;
import com.sos.joc.model.schedule.ScheduleFilterSchema;
import com.sos.joc.schedule.resource.IScheduleResource;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("schedule")
public class ScheduleResourceImpl extends JOCResourceImpl implements IScheduleResource {
    private static final String WHAT = "folders";
    private static final String SUBSYSTEMS = "folder schedule";
    private static final String XPATH_SCHEDULES = "//schedule[@path='%s']";

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceImpl.class);

    @Override
    public JOCDefaultResponse postSchedule(String accessToken, ScheduleFilterSchema scheduleFilterSchema) throws Exception {
        LOGGER.debug("init schedule");
        try {
            JOCDefaultResponse jocDefaultResponse = init(scheduleFilterSchema.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            String scheduleIn = scheduleFilterSchema.getSchedule();
            this.checkRequiredParameter("schedule", scheduleIn);

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());

            JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
            jsCmdShowState.setSubsystems(SUBSYSTEMS);
            jsCmdShowState.setWhat(WHAT);
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowState);
            jocXmlCommand.excutePost(xml);

            Element scheduleElement  = jocXmlCommand.executeXPath(String.format(XPATH_SCHEDULES,scheduleIn));

            Schedule200VSchema entity = new Schedule200VSchema();
            entity.setDeliveryDate(new Date());

            Schedule schedule = new Schedule(jocXmlCommand, scheduleElement);
            entity.setSchedule(schedule.getSchedule());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}