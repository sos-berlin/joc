package com.sos.joc.schedule.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyScheduleAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.joe.common.XmlDeserializer;
import com.sos.joc.joe.common.XmlSerializer;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.joe.schedule.Schedule;
import com.sos.joc.model.schedule.ModifyRunTime;
import com.sos.joc.schedule.resource.IScheduleResourceSetRunTime;

@Path("schedule")
public class ScheduleResourceSetRunTimeImpl extends JOCResourceImpl implements IScheduleResourceSetRunTime {

	private static final String API_CALL = "./schedule/set_run_time";
	private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);

	@Override
	public JOCDefaultResponse postScheduleSetRuntime(String accessToken, byte[] modifyRuntimeBytes) {
		SOSHibernateSession session = null;
		try {
		    ModifyRunTime modifyRuntime = Globals.objectMapper.readValue(modifyRuntimeBytes, ModifyRunTime.class);
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
			if (modifyRuntime.getRunTime() == null) {
                throw new JocMissingRequiredParameterException("undefined 'runTime'");
            }
			
			Schedule runTime = XmlSerializer.serializeAbstractSchedule(modifyRuntime.getRunTime());
			modifyRuntime.setRunTimeXml(Globals.xmlMapper.writeValueAsString(runTime));
			ValidateXML.validateScheduleAgainstJobSchedulerSchema(modifyRuntime.getRunTimeXml());

			String schedulePath = normalizePath(modifyRuntime.getSchedule());
			
            if (versionIsOlderThan("1.13.1")) {

                XMLBuilder command = new XMLBuilder("modify_hot_folder");

                Element scheduleElement = XMLBuilder.parse(modifyRuntime.getRunTimeXml());
                scheduleElement.addAttribute("name", Paths.get(schedulePath).getFileName().toString());
                command.addAttribute("folder", getParent(schedulePath)).add(scheduleElement);

                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
                jocXmlCommand.executePostWithThrowBadRequest(command.asXML(), getAccessToken());

            } else {

                Schedule schedulePojo = XmlDeserializer.deserialize(modifyRuntime.getRunTimeXml(), Schedule.class);
                if (modifyRuntime.getCalendars() != null && !modifyRuntime.getCalendars().isEmpty()) {
                    Calendars calendars = new Calendars();
                    calendars.setCalendars(modifyRuntime.getCalendars());
                    schedulePojo.setCalendars(Globals.objectMapper.writeValueAsString(calendars));
                }
                schedulePojo = XmlSerializer.serializeAbstractSchedule(schedulePojo);
                JOCHotFolder jocHotFolder = new JOCHotFolder(this);
                jocHotFolder.putFile(schedulePath + ".schedule.xml", XmlSerializer.serializeToStringWithHeader(schedulePojo));
            }

			session = Globals.createSosHibernateStatelessConnection(API_CALL);
			CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(session,
					dbItemInventoryInstance.getSchedulerId(), CalendarObjectType.SCHEDULE, schedulePath,
					modifyRuntime.getRunTimeXml(), modifyRuntime.getCalendars());
			calendarUsedByWriter.updateUsedBy();
			
			CalendarEvent calEvt = calendarUsedByWriter.getCalendarEvent();
			List<DBItemInventoryInstance> clusterMembers = null;
            if (calEvt != null) {
                if ("active".equals(dbItemInventoryInstance.getClusterType())) {
                    InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(session);
                    clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(modifyRuntime.getJobschedulerId());
                }
                if (clusterMembers != null) {
                    SendCalendarEventsUtil.sendEvent(calEvt, clusterMembers, getAccessToken());
                } else {
                    SendCalendarEventsUtil.sendEvent(calEvt, dbItemInventoryInstance, getAccessToken());
                }
            }

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