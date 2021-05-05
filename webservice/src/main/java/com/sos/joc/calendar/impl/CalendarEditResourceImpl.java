package com.sos.joc.calendar.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendarUsage;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarEditResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingCommentException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.JocObjectAlreadyExistException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarObjectFilter;
import com.sos.joc.model.calendar.CalendarRenameFilter;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Dates;
import com.sos.schema.JsonValidator;

@Path("calendar")
public class CalendarEditResourceImpl extends JOCResourceImpl implements ICalendarEditResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarEditResourceImpl.class);
    private static final String API_CALL_STORE = "./calendar/store";
    private static final String API_CALL_SAVE_AS = "./calendar/save_as";
    private static final String API_CALL_MOVE = "./calendar/rename";

    @Override
    public JOCDefaultResponse postStoreCalendar(String accessToken, byte[] calendarFilterBytes) {
        SOSHibernateSession connection = null;
        Set<String> eventCommands = new HashSet<String>();
        List<DBItemInventoryInstance> clusterMembers = null;
        try {
            JsonValidator.validateFailFast(calendarFilterBytes, CalendarObjectFilter.class);
            CalendarObjectFilter calendarFilter = Globals.objectMapper.readValue(calendarFilterBytes, CalendarObjectFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_STORE, calendarFilter, accessToken, calendarFilter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredComment(calendarFilter.getAuditLog());
            Calendar calendar = checkRequirements(calendarFilter.getCalendar());

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_STORE);
            CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);

            DBItemInventoryClusterCalendar calendarDbItem = null;
            if (calendar.getId() != null) {
                calendarDbItem = calendarDbLayer.getCalendar(calendar.getId());
            }

            if ((calendarDbItem == null && !getPermissonsJocCockpit(calendarFilter.getJobschedulerId(), accessToken).getCalendar().getEdit()
                    .isCreate()) || (calendarDbItem != null && !getPermissonsJocCockpit(calendarFilter.getJobschedulerId(), accessToken).getCalendar()
                            .getEdit().isChange())) {
                return accessDeniedResponse();
            }
            
            if (calendar.getPath() != null && !calendar.getPath().isEmpty()) {
                checkCalendarFolderPermissions(calendar.getPath());
            } else if (calendarDbItem != null && calendarDbItem.getName() != null && !calendarDbItem.getName().isEmpty()) {
                checkCalendarFolderPermissions(calendarDbItem.getName());
            }
            
            ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar.getId(), calendar.getPath(), calendarFilter.getAuditLog(),
                    calendarFilter.getJobschedulerId());
            logAuditMessage(calendarAudit);

            CalendarEvent calEvt = new CalendarEvent();
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendar.getPath());
            if (CalendarType.NON_WORKING_DAYS == calendar.getType()) {
                calEvtVars.setObjectType(CalendarObjectType.NONWORKINGDAYSCALENDAR);
            } else {
                calEvtVars.setObjectType(CalendarObjectType.WORKINGDAYSCALENDAR);
            }
            calEvt.setVariables(calEvtVars);

            boolean updateJobOrderScheduleIsNecessary = false;
            boolean newCalendar = (calendarDbItem == null);
            Dates newDates = null;
            Dates oldDates = null;
            Calendar oldCalendar = null;

            CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);
            List<DBItemInventoryClusterCalendarUsage> calendarUsages = null;

            if (!newCalendar) {
                calEvt.setKey("CalendarUpdated");
                try {
                    oldCalendar = new ObjectMapper().readValue(calendarDbItem.getConfiguration(), Calendar.class);
                    newDates = new FrequencyResolver().resolveFromUTCYesterday(calendar);
                    oldDates = new FrequencyResolver().resolveFromUTCYesterday(oldCalendar);
                    updateJobOrderScheduleIsNecessary = (!newDates.getDates().equals(oldDates.getDates()));
                    if ((calendar.getType() != null && !calendar.getType().name().equals(calendarDbItem.getType()))) {
                        calendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
                        if (!calendarUsages.isEmpty()) {
                            throw new JobSchedulerBadRequestException(String.format(
                                    "It is not allowed to change the calendar type (%1$s -> %2$s) when it is already assigned to a job, order or schedule.",
                                    calendarDbItem.getType(), calendar.getType().name()));
                        }
                    }
                } catch (SOSMissingDataException e) {
                    throw new JocMissingRequiredParameterException(e);
                } catch (SOSInvalidDataException e) {
                    throw new JobSchedulerInvalidResponseDataException(e);
                }
            } else {
                calEvt.setKey("CalendarCreated");
                DBItemInventoryClusterCalendar oldCalendarDbItem = calendarDbLayer.getCalendar(dbItemInventoryInstance.getSchedulerId(), calendar
                        .getPath());
                if (oldCalendarDbItem != null) {
                    throw new JocObjectAlreadyExistException(calendar.getPath());
                }
            }

            calendarDbItem = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getSchedulerId(), calendarDbItem, calendar);

            Date surveyDate = calendarDbItem.getModified();

            if (newCalendar) {
                eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));
            } else {
                // Calendar curCalendar = objMapper.readValue(calendarDbItem.getConfiguration(), Calendar.class);
                // if (!curCalendar.equals(oldCalendar)) {
                if (updateJobOrderScheduleIsNecessary) {
                    eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));
                    if (calendarUsages == null) {
                        calendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
                    }
                    if (calendarUsages != null) {
                        for (DBItemInventoryClusterCalendarUsage usage : calendarUsages) {
                            eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(usage.getPath(), usage.getObjectType(),
                                    "CalendarUsageUpdated"));
                        }
                    }
                }
            }

            storeAuditLogEntry(calendarAudit);
            if (eventCommands != null && !eventCommands.isEmpty() && "active".equals(dbItemInventoryInstance.getClusterType())) {
                InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
                clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(calendarFilter.getJobschedulerId());
            }

            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
            try {
                if (eventCommands != null) {
                    if (clusterMembers != null) {
                        SendCalendarEventsUtil.sendEvent(eventCommands, clusterMembers, accessToken);
                    } else if (dbItemInventoryInstance != null) {
                        SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Couldn't send calendar events", e);
            }
            eventCommands = null;
        }
    }

    @Override
    public JOCDefaultResponse postSaveAsCalendar(String accessToken, byte[] calendarFilterBytes) {
        SOSHibernateSession connection = null;
        List<DBItemInventoryInstance> clusterMembers = null;
        try {
            JsonValidator.validateFailFast(calendarFilterBytes, CalendarObjectFilter.class);
            CalendarObjectFilter calendarFilter = Globals.objectMapper.readValue(calendarFilterBytes, CalendarObjectFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_SAVE_AS, calendarFilter, accessToken, calendarFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(calendarFilter.getJobschedulerId(), accessToken).getCalendar().getEdit().isCreate());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredComment(calendarFilter.getAuditLog());
            Calendar calendar = checkRequirements(calendarFilter.getCalendar());
            checkCalendarFolderPermissions(calendar.getPath());
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL_SAVE_AS);
            CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);

            ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar.getId(), calendar.getPath(), calendarFilter.getAuditLog(),
                    calendarFilter.getJobschedulerId());
            logAuditMessage(calendarAudit);

            DBItemInventoryClusterCalendar oldCalendarDbItem = calendarDbLayer.getCalendar(dbItemInventoryInstance.getSchedulerId(), calendar
                    .getPath());
            if (oldCalendarDbItem != null) {
                throw new JocObjectAlreadyExistException(calendar.getPath());
            }
            DBItemInventoryClusterCalendar dbItemCalendar = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getSchedulerId(), null,
                    calendar);
            Date surveyDate = dbItemCalendar.getModified();
            storeAuditLogEntry(calendarAudit);

            CalendarEvent calEvt = new CalendarEvent();
            calEvt.setKey("CalendarCreated");
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendar.getPath());
            if (CalendarType.NON_WORKING_DAYS.name().equals(dbItemCalendar.getType())) {
                calEvtVars.setObjectType(CalendarObjectType.NONWORKINGDAYSCALENDAR);
            } else {
                calEvtVars.setObjectType(CalendarObjectType.WORKINGDAYSCALENDAR);
            }
            calEvt.setVariables(calEvtVars);
            SendCalendarEventsUtil.sendEvent(calEvt, dbItemInventoryInstance, getAccessToken());
            if ("active".equals(dbItemInventoryInstance.getClusterType())) {
                InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
                clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(calendarFilter.getJobschedulerId());
            }
            if (clusterMembers != null) {
                SendCalendarEventsUtil.sendEvent(calEvt, clusterMembers, accessToken);
            } else {
                SendCalendarEventsUtil.sendEvent(calEvt, dbItemInventoryInstance, accessToken);
            }

            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
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
    public JOCDefaultResponse postRenameCalendar(String accessToken, byte[] calendarFilterBytes) {
        SOSHibernateSession connection = null;
        Set<String> eventCommands = new HashSet<String>();
        List<DBItemInventoryInstance> clusterMembers = null;
        try {
            JsonValidator.validateFailFast(calendarFilterBytes, CalendarRenameFilter.class);
            CalendarRenameFilter calendarFilter = Globals.objectMapper.readValue(calendarFilterBytes, CalendarRenameFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_MOVE, calendarFilter, accessToken, calendarFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(calendarFilter.getJobschedulerId(), accessToken).getCalendar().getEdit().isChange());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(calendarFilter.getAuditLog());
            checkRequiredParameter("calendar path", calendarFilter.getPath());
            checkRequiredParameter("calendar new path", calendarFilter.getNewPath());

            String calendarPath = normalizePath(calendarFilter.getPath());
            String calendarNewPath = normalizePath(calendarFilter.getNewPath());
            
            checkCalendarFolderPermissions(calendarNewPath);
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL_MOVE);

            ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(null, calendarPath, calendarFilter.getAuditLog(), calendarFilter
                    .getJobschedulerId());
            logAuditMessage(calendarAudit);

            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);

            DBItemInventoryClusterCalendar calendarNewDbItem = dbLayer.getCalendar(dbItemInventoryInstance.getSchedulerId(), calendarNewPath);
            if (calendarNewDbItem != null) {
                throw new JocObjectAlreadyExistException(calendarNewPath);
            }
            DBItemInventoryClusterCalendar calendarDbItem = new CalendarsDBLayer(connection).renameCalendar(dbItemInventoryInstance.getSchedulerId(),
                    calendarPath, calendarNewPath);
            storeAuditLogEntry(calendarAudit);

            CalendarEvent calEvt = new CalendarEvent();
            calEvt.setKey("CalendarUpdated");
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendarNewPath);
            calEvtVars.setOldPath(calendarPath);
            if (CalendarType.NON_WORKING_DAYS.name().equals(calendarDbItem.getType())) {
                calEvtVars.setObjectType(CalendarObjectType.NONWORKINGDAYSCALENDAR);
            } else {
                calEvtVars.setObjectType(CalendarObjectType.WORKINGDAYSCALENDAR);
            }
            calEvt.setVariables(calEvtVars);
            eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));

            CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);
            List<DBItemInventoryClusterCalendarUsage> usages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
            if (usages != null) {
                for (DBItemInventoryClusterCalendarUsage usage : usages) {
                    eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(usage.getPath(), usage.getObjectType(), "CalendarUsageUpdated"));
                }
            }

            if (eventCommands != null && !eventCommands.isEmpty() && "active".equals(dbItemInventoryInstance.getClusterType())) {
                InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
                clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(calendarFilter.getJobschedulerId());
            }
            if (clusterMembers != null) {
                SendCalendarEventsUtil.sendEvent(eventCommands, clusterMembers, accessToken);
            } else {
                SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
            }
            eventCommands = null;

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
            try {
                if (clusterMembers != null) {
                    SendCalendarEventsUtil.sendEvent(eventCommands, clusterMembers, accessToken);
                } else {
                    SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
                }
            } catch (Exception e) {
                LOGGER.error("Couldn't send calendar events", e);
            }
        }
    }

    private Calendar checkRequirements(Calendar calendar) throws JocMissingCommentException, JocMissingRequiredParameterException,
            JobSchedulerInvalidResponseDataException {
        if (calendar == null) {
            throw new JocMissingRequiredParameterException("undefined 'calendar'");
        }

        checkRequiredParameter("calendar path", calendar.getPath());
        if (calendar.getIncludes() == null && calendar.getExcludes() == null) {
            throw new JocMissingRequiredParameterException("undefined frequencies");
        }

        if (calendar.getTo() == null || calendar.getTo().isEmpty()) {
            throw new JobSchedulerInvalidResponseDataException("calendar field 'to' is undefined");
        } else if (!calendar.getTo().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new JobSchedulerInvalidResponseDataException("calendar field 'to' must have the format YYYY-MM-DD.");
        }

        if (calendar.getFrom() == null || calendar.getFrom().isEmpty()) {
            // throw new JobSchedulerInvalidResponseDataException("calendar field 'from' is
            // undefined");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setFrom(df.format(Date.from(Instant.now())));
        } else if (!calendar.getFrom().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new JobSchedulerInvalidResponseDataException("calendar field 'from' must have the format YYYY-MM-DD.");
        }

        if (calendar.getType() == null || calendar.getType().name().isEmpty()) {
            calendar.setType(CalendarType.WORKING_DAYS);
        }
        calendar.setPath(normalizePath(calendar.getPath()));
        return calendar;
    }

}