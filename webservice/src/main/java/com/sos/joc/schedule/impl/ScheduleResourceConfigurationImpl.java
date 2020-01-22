package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.configuration.CalendarUsageConfiguration;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.XmlDeserializer;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.schedule.Configuration200;
import com.sos.joc.model.schedule.RunTime;
import com.sos.joc.model.schedule.RunTime200;
import com.sos.joc.model.schedule.ScheduleConfigurationFilter;
import com.sos.joc.schedule.resource.IScheduleResourceConfiguration;
import com.sos.schema.JsonValidator;

@Path("schedule")
public class ScheduleResourceConfigurationImpl extends JOCResourceImpl implements IScheduleResourceConfiguration {

    private static final String API_CALL_C = "./schedule/configuration";
    private static final String API_CALL_R = "./schedule/run_time";

    @Override
	public JOCDefaultResponse postScheduleConfiguration(String accessToken, byte[] scheduleBodyBytes) {
        SOSHibernateSession connection = null;
        try {
		    JsonValidator.validateFailFast(scheduleBodyBytes, ScheduleConfigurationFilter.class);
		    ScheduleConfigurationFilter scheduleBody = Globals.objectMapper.readValue(scheduleBodyBytes, ScheduleConfigurationFilter.class);
            
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, scheduleBody, accessToken,
					scheduleBody.getJobschedulerId(),
					getPermissonsJocCockpit(scheduleBody.getJobschedulerId(), accessToken).getSchedule().getView()
							.isConfiguration());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

            checkRequiredParameter("schedule", scheduleBody.getSchedule());

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            String schedulePath = normalizePath(scheduleBody.getSchedule());
            String scheduleParent = getParent(schedulePath);
            boolean responseInHtml = scheduleBody.getMime() == ConfigurationMime.HTML;
            String xPath = String.format("/spooler/answer//schedules/schedule[@path='%s']", schedulePath);
            String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders source", scheduleParent);
            entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(jocXmlCommand, command, xPath, "schedule", responseInHtml,
                    accessToken));
            entity.setDeliveryDate(Date.from(Instant.now()));

            if (!responseInHtml) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL_C);
                CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(connection);
                List<CalendarUsageConfiguration> dbCalendars = calendarUsageDBLayer.getConfigurationsOfAnObject(dbItemInventoryInstance
                        .getSchedulerId(), "SCHEDULE", schedulePath);
                if (dbCalendars != null && !dbCalendars.isEmpty()) {
                    List<Calendar> calendars = new ArrayList<Calendar>();
                    for (CalendarUsageConfiguration dbCalendar : dbCalendars) {
                        if (dbCalendar.getCalendar() != null) {
                            calendars.add(dbCalendar.getCalendar());
                        }
                    }
                    entity.setCalendars(calendars);
                }
            }

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    @Override
    public JOCDefaultResponse postScheduleRunTime(String accessToken, ScheduleConfigurationFilter scheduleBody) {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_R, scheduleBody, accessToken, scheduleBody.getJobschedulerId(),
                    getPermissonsJocCockpit(scheduleBody.getJobschedulerId(), accessToken).getSchedule().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("schedule", scheduleBody.getSchedule());

            RunTime200 runTimeAnswer = new RunTime200();

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            String schedulePath = normalizePath(scheduleBody.getSchedule());
            String scheduleParent = getParent(schedulePath);
            String xPath = String.format("/spooler/answer//schedules/schedule[@path='%s']", schedulePath);
            String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders source", scheduleParent);
            entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(jocXmlCommand, command, xPath, "schedule", false, accessToken));
            // entity.setDeliveryDate(Date.from(Instant.now()));

            RunTime runTime = new RunTime();
            runTime.setRunTimeIsTemporary(false);
            runTime.setSurveyDate(entity.getConfiguration().getSurveyDate());

            runTime.setRunTimeXml(entity.getConfiguration().getContent().getXml());
            runTime.setRunTime(XmlDeserializer.deserialize(entity.getConfiguration().getContent().getXml(),
                    com.sos.joc.model.joe.schedule.Schedule.class));
            runTimeAnswer.setRunTime(runTime);
            runTimeAnswer.setDeliveryDate(Date.from(Instant.now()));

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_R);
            CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(connection);
            List<CalendarUsageConfiguration> dbCalendars = calendarUsageDBLayer.getConfigurationsOfAnObject(dbItemInventoryInstance.getSchedulerId(),
                    "SCHEDULE", schedulePath);
            if (dbCalendars != null && !dbCalendars.isEmpty()) {
                List<Calendar> calendars = new ArrayList<Calendar>();
                for (CalendarUsageConfiguration dbCalendar : dbCalendars) {
                    if (dbCalendar.getCalendar() != null) {
                        calendars.add(dbCalendar.getCalendar());
                    }
                }
                runTimeAnswer.getRunTime().setCalendars(calendars);
            }

            final byte[] bytes = Globals.objectMapper.writeValueAsBytes(runTimeAnswer);
            
            return JOCDefaultResponse.responseStatus200(bytes, MediaType.APPLICATION_JSON);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }
}