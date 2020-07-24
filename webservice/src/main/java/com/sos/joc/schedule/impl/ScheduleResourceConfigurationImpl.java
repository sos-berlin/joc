package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.sos.joe.common.XmlDeserializer;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.configuration.CalendarUsageConfiguration;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.JobSchedulerObjectType;
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

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_C, scheduleBody, accessToken, scheduleBody.getJobschedulerId(),
                    getPermissonsJocCockpit(scheduleBody.getJobschedulerId(), accessToken).getSchedule().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("schedule", scheduleBody.getSchedule());
            String schedulePath = normalizePath(scheduleBody.getSchedule());
            checkFolderPermissions(schedulePath);

            Configuration200 entity = new Configuration200();
            boolean responseInHtml = scheduleBody.getMime() == ConfigurationMime.HTML;
            if (versionIsOlderThan("1.13.1")) {
                entity.setConfiguration(getConfiguration(schedulePath, responseInHtml));
            } else {
                try {
                    entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(new JOCHotFolder(this), schedulePath,
                            JobSchedulerObjectType.SCHEDULE, responseInHtml));
                } catch (JobSchedulerObjectNotExistException e) {
                    entity.setConfiguration(getConfiguration(schedulePath, responseInHtml));
                }
            }
            entity.setDeliveryDate(Date.from(Instant.now()));

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
    public JOCDefaultResponse postScheduleRunTime(String accessToken, byte[] scheduleBodyBytes) {
        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(scheduleBodyBytes, ScheduleConfigurationFilter.class);
            ScheduleConfigurationFilter scheduleBody = Globals.objectMapper.readValue(scheduleBodyBytes, ScheduleConfigurationFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_R, scheduleBody, accessToken, scheduleBody.getJobschedulerId(),
                    getPermissonsJocCockpit(scheduleBody.getJobschedulerId(), accessToken).getSchedule().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("schedule", scheduleBody.getSchedule());
            String schedulePath = normalizePath(scheduleBody.getSchedule());
            checkFolderPermissions(schedulePath);

            RunTime200 runTimeAnswer = new RunTime200();

            Configuration conf = new Configuration();
            if (versionIsOlderThan("1.13.1")) {
                conf = getConfiguration(schedulePath, false);
            } else {
                try {
                    conf = ConfigurationUtils.getConfigurationSchema(new JOCHotFolder(this), schedulePath, JobSchedulerObjectType.SCHEDULE, false);
                } catch (JobSchedulerObjectNotExistException e) {
                    conf = getConfiguration(schedulePath, false);
                }
            }

            RunTime runTime = new RunTime();
            runTime.setRunTimeIsTemporary(false);
            runTime.setSurveyDate(conf.getSurveyDate());

            runTime.setRunTimeXml(conf.getContent().getXml());
            runTime.setRunTime(XmlDeserializer.deserialize(conf.getContent().getXml(), com.sos.joc.model.joe.schedule.Schedule.class));
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

    private Configuration getConfiguration(String path, boolean responseInHtml) throws JocException {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
        String xPath = String.format("/spooler/answer//schedules/schedule[@path='%s']", path);
        String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders source", getParent(path));
        return ConfigurationUtils.getConfigurationSchema(jocXmlCommand, command, xPath, "schedule", responseInHtml, getAccessToken());
    }
}