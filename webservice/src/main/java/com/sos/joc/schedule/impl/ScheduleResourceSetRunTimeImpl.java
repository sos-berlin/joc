package com.sos.joc.schedule.impl;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Path;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joe.common.XmlDeserializer;
import com.sos.joe.common.XmlSerializer;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.audit.ModifyScheduleAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.schedule.Schedule;
import com.sos.joc.model.schedule.ModifyRunTime;
import com.sos.joc.schedule.resource.IScheduleResourceSetRunTime;
import com.sos.schema.JsonValidator;
import com.sos.xml.XMLBuilder;

 

@Path("schedule")
public class ScheduleResourceSetRunTimeImpl extends JOCResourceImpl implements IScheduleResourceSetRunTime {

	private static final String API_CALL = "./schedule/set_run_time";
	private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);

	@Override
	public JOCDefaultResponse postScheduleSetRuntime(String accessToken, byte[] modifyRuntimeBytes) {
		SOSHibernateSession session = null;
		try {
		    JsonValidator.validateFailFast(modifyRuntimeBytes, ModifyRunTime.class);
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
			
			String schedulePath = normalizePath(modifyRuntime.getSchedule());
			String substitutePath = null;
			boolean addSubstitute = false;
			Schedule runTime = modifyRuntime.getRunTime();
			
			//JOC-901 "substitute" is missing in request but "path" is filled.
            if (runTime.getValidFrom() != null && !runTime.getValidFrom().isEmpty()) {
                if (runTime.getSubstitute() == null || runTime.getSubstitute().isEmpty()) {
                    JsonReader rdr = Json.createReader(new ByteArrayInputStream(modifyRuntimeBytes));
                    JsonObject jsonObj = rdr.readObject();
                    substitutePath = jsonObj.getJsonObject("runTime").getString("path", null);
                    rdr.close();
                } else {
                    substitutePath = runTime.getSubstitute();
                }
                if (substitutePath != null) {
                    substitutePath = normalizePath(substitutePath);
                    if (!schedulePath.equalsIgnoreCase(substitutePath)) {
                        addSubstitute = true;
                        runTime.setSubstitute(substitutePath);
                    }
                }
            }
			
			if (addSubstitute) {
			    Configuration conf = new Configuration();
	            if (versionIsOlderThan("1.13.1")) {
	                conf = getConfiguration(schedulePath);
	            } else {
	                try {
	                    conf = ConfigurationUtils.getConfigurationSchema(new JOCHotFolder(this), schedulePath, JobSchedulerObjectType.SCHEDULE, false);
	                } catch (JobSchedulerObjectNotExistException e) {
	                    conf = getConfiguration(schedulePath);
	                }
	            }
	            Schedule s = XmlDeserializer.deserialize(conf.getContent().getXml(), Schedule.class);
                s.setSubstitute(substitutePath);
                s.setValidFrom(runTime.getValidFrom());
                s.setValidTo(runTime.getValidTo());
                runTime = XmlSerializer.serializeAbstractSchedule(s);
			} else {
			    runTime = XmlSerializer.serializeAbstractSchedule(runTime);
			}
			
			modifyRuntime.setRunTimeXml(Globals.xmlMapper.writeValueAsString(runTime));
			Document doc = ValidateXML.validateScheduleAgainstJobSchedulerSchema(modifyRuntime.getRunTimeXml());

			
            if (versionIsOlderThan("1.13.1")) {
                
                XMLBuilder command = new XMLBuilder("modify_hot_folder");

                if (doc != null) {
                    Element scheduleElement = doc.getRootElement();
                    scheduleElement.addAttribute("name", Paths.get(schedulePath).getFileName().toString());
                    command.addAttribute("folder", getParent(schedulePath)).add(scheduleElement);
                }

                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
                jocXmlCommand.executePostWithThrowBadRequest(command.asXML(), getAccessToken());

            } else {

                JOCHotFolder jocHotFolder = new JOCHotFolder(this);

                if (modifyRuntime.getCalendars() != null && !modifyRuntime.getCalendars().isEmpty()) {
                    Calendars calendars = new Calendars();
                    calendars.setCalendars(modifyRuntime.getCalendars());
                    runTime.setCalendars(Globals.objectMapper.writeValueAsString(calendars));
                }
                jocHotFolder.putFile(schedulePath + JOEHelper.getFileExtension(JobSchedulerObjectType.SCHEDULE), XmlSerializer
                        .serializeToStringWithHeader(runTime));
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
	
	private Configuration getConfiguration(String path) throws JocException {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
        String xPath = String.format("/spooler/answer//schedules/schedule[@path='%s']", path);
        String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders source", getParent(path));
        return ConfigurationUtils.getConfigurationSchema(jocXmlCommand, command, xPath, "schedule", false, getAccessToken());
    }

}