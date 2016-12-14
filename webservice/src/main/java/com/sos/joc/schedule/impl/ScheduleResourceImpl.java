package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.w3c.dom.Element;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.schedule.ScheduleVolatile;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.ScheduleFilter;
import com.sos.joc.model.schedule.ScheduleV200;
import com.sos.joc.schedule.resource.IScheduleResource;

@Path("schedule")
public class ScheduleResourceImpl extends JOCResourceImpl implements IScheduleResource {

    private static final String API_CALL = "./schedule";

    @Override
    public JOCDefaultResponse postSchedule(String accessToken, ScheduleFilter scheduleFilter) throws Exception {
        try {
            initLogging(API_CALL, scheduleFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, scheduleFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getSchedule()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            this.checkRequiredParameter("schedule", scheduleFilter.getSchedule());

            String schedulePath = normalizePath(scheduleFilter.getSchedule());
            String scheduleParent = getParent(schedulePath);

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders", scheduleParent);
            jocXmlCommand.executePostWithThrowBadRequest(command, accessToken);

            String xPath = String.format("/spooler/answer//schedules/schedule[@path='%s']", schedulePath);
            Element scheduleElement = (Element) jocXmlCommand.getSosxml().selectSingleNode(xPath);

            ScheduleVolatile scheduleV = new ScheduleVolatile(jocXmlCommand, scheduleElement);
            scheduleV.setValues();

            ScheduleV200 entity = new ScheduleV200();
            entity.setSchedule(scheduleV);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}