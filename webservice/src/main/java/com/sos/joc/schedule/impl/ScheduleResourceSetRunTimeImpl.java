package com.sos.joc.schedule.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.ModifyRunTime;
import com.sos.joc.schedule.resource.IScheduleResourceSetRunTime;
import com.sos.scheduler.model.commands.JSCmdModifyHotFolder;
import com.sos.scheduler.model.objects.JSObjRunTime;

@Path("schedule")
public class ScheduleResourceSetRunTimeImpl extends JOCResourceImpl implements IScheduleResourceSetRunTime {

    private static final String API_CALL = "./schedule/set_run_time";
    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);

    @Override
    public JOCDefaultResponse postScheduleSetRuntime(String accessToken, ModifyRunTime modifyRuntime) throws Exception {
        try {
            initLogging(API_CALL, modifyRuntime);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, modifyRuntime.getJobschedulerId(), getPermissons(accessToken).getSchedule()
                    .isEdit());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            logAuditMessage(modifyRuntime);
            checkRequiredParameter("schedule", modifyRuntime.getSchedule());
            ValidateXML.validateScheduleAgainstJobSchedulerSchema(modifyRuntime.getRunTime());
            
            String schedule = normalizePath(modifyRuntime.getSchedule());
            JSObjRunTime objRuntime = new JSObjRunTime(Globals.schedulerObjectFactory, modifyRuntime.getRunTime());
            objRuntime.setName(Paths.get(schedule).getFileName().toString());
            JSCmdModifyHotFolder jsCmdModifySchedule = Globals.schedulerObjectFactory.createModifyHotFolder();
            jsCmdModifySchedule.setFolder(getParent(schedule));
            jsCmdModifySchedule.setSchedule(objRuntime);
            String xml = jsCmdModifySchedule.toXMLString();

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            AUDIT_LOGGER.error(e.getMessage());
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            AUDIT_LOGGER.error(e.getMessage());
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}