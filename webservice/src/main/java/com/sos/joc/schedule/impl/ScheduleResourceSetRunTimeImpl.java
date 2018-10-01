package com.sos.joc.schedule.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyScheduleAudit;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.ModifyRunTime;
import com.sos.joc.schedule.resource.IScheduleResourceSetRunTime;

@Path("schedule")
public class ScheduleResourceSetRunTimeImpl extends JOCResourceImpl implements IScheduleResourceSetRunTime {

	private static final String API_CALL = "./schedule/set_run_time";
	private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);

	@Override
	public JOCDefaultResponse postScheduleSetRuntime(String xAccessToken, String accessToken,
			ModifyRunTime modifyRuntime) throws Exception {
		return postScheduleSetRuntime(getAccessToken(xAccessToken, accessToken), modifyRuntime);
	}

	public JOCDefaultResponse postScheduleSetRuntime(String accessToken, ModifyRunTime modifyRuntime) throws Exception {
		SOSHibernateSession session = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, modifyRuntime, accessToken,
					modifyRuntime.getJobschedulerId(),
					getPermissonsJocCockpit(modifyRuntime.getJobschedulerId(), accessToken).getSchedule().getChange()
							.isEditContent());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			checkRequiredComment(modifyRuntime.getAuditLog());
			ModifyScheduleAudit scheduleAudit = new ModifyScheduleAudit(modifyRuntime);
			logAuditMessage(scheduleAudit);
			checkRequiredParameter("schedule", modifyRuntime.getSchedule());
			ValidateXML.validateScheduleAgainstJobSchedulerSchema(modifyRuntime.getRunTime());

			String schedulePath = normalizePath(modifyRuntime.getSchedule());
			XMLBuilder command = new XMLBuilder("modify_hot_folder");

			Element scheduleElement = XMLBuilder.parse(modifyRuntime.getRunTime());
			scheduleElement.addAttribute("name", Paths.get(schedulePath).getFileName().toString());
			command.addAttribute("folder", getParent(schedulePath)).add(scheduleElement);

			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
			String commandAsXml = command.asXML();
			jocXmlCommand.executePostWithThrowBadRequest(commandAsXml, getAccessToken());

			session = Globals.createSosHibernateStatelessConnection(API_CALL);
			CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(session,
					dbItemInventoryInstance.getSchedulerId(), CalendarObjectType.SCHEDULE, schedulePath,
					modifyRuntime.getRunTime(), modifyRuntime.getCalendars());
			calendarUsedByWriter.updateUsedBy();
			jocXmlCommand.executePostWithThrowBadRequest(calendarUsedByWriter.getEvent(), getAccessToken());

			storeAuditLogEntry(scheduleAudit);

			return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
		} catch (JocException e) {
			AUDIT_LOGGER.error(e.getMessage());
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			AUDIT_LOGGER.error(e.getMessage());
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(session);
		}
	}

}