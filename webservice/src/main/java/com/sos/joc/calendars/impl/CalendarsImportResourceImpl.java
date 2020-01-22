package com.sos.joc.calendars.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.sos.joc.calendars.resource.ICalendarsImportResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingCommentException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarImportFilter;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Dates;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.Folder;
import com.sos.schema.JsonValidator;

@Path("calendars")
public class CalendarsImportResourceImpl extends JOCResourceImpl implements ICalendarsImportResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarsImportResourceImpl.class);
    private static final String API_CALL = "./calendars/import";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private Set<String> eventCommands = new HashSet<String>();
    private ObjectMapper mapper = new ObjectMapper();
    private boolean updateUsageIsNecessary = false;

    @Override
    public JOCDefaultResponse importCalendars(String accessToken, byte[] filterBytes) {
        SOSHibernateSession connection = null;
        List<DBItemInventoryInstance> clusterMembers = null;
        try {
            JsonValidator.validateFailFast(filterBytes, CalendarImportFilter.class);
            CalendarImportFilter calendarImportFilter = Globals.objectMapper.readValue(filterBytes, CalendarImportFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, calendarImportFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(calendarImportFilter.getJobschedulerId(), accessToken).getCalendar().getEdit().isCreate());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            List<Calendar> calendars = calendarImportFilter.getCalendars();
            Set<Folder> folders = folderPermissions.getListOfFolders();

            if (calendars != null && !calendars.isEmpty()) {

                checkRequiredComment(calendarImportFilter.getAuditLog());

                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);
                CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);
                InventoryOrdersDBLayer ordersDBLayer = new InventoryOrdersDBLayer(connection);
                InventoryJobsDBLayer jobsDBLayer = new InventoryJobsDBLayer(connection);
                InventorySchedulesDBLayer schedulesDBLayer = new InventorySchedulesDBLayer(connection);
                Map<String, DBItemInventoryClusterCalendar> calendarsMap = new HashMap<String, DBItemInventoryClusterCalendar>();

                for (Calendar calendar : calendars) {
                    checkRequirements(calendar);
                    if (!canAdd(calendar.getPath(), folders)) {
                        continue;
                    }
                    if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) {
                        DBItemInventoryClusterCalendar dbItemCalendar = calendarDbLayer.getCalendar(dbItemInventoryInstance.getSchedulerId(), calendar.getPath());

                        ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar.getId(), calendar.getPath(), calendarImportFilter
                                .getAuditLog(), calendarImportFilter.getJobschedulerId());
                        logAuditMessage(calendarAudit);

                        if (dbItemCalendar == null) {
                            dbItemCalendar = importNewCalendar(calendar, calendarDbLayer);
                        } else {
                            dbItemCalendar = importCalendar(calendar, dbItemCalendar, calendarDbLayer, calendarUsageDbLayer);
                        }
                        if (dbItemCalendar != null) {
                            calendarsMap.put(calendar.getPath(), dbItemCalendar);
                        }
                        storeAuditLogEntry(calendarAudit);
                    }
                }

                for (Calendar calendar : calendars) {
                    if (!canAdd(calendar.getPath(), folders)) {
                        continue;
                    }
                    if (calendar.getBasedOn() != null && !calendar.getBasedOn().isEmpty() && calendarsMap.containsKey(calendar.getBasedOn())) {
                        // check if corresponding order, job or schedule exists
                        boolean objectMissing = false;
                        switch (calendar.getType()) {
                        case ORDER:
                            objectMissing = (ordersDBLayer.getInventoryOrderByName(dbItemInventoryInstance.getId(), calendar.getPath()) == null);
                            break;
                        case JOB:
                            objectMissing = (jobsDBLayer.getInventoryJobByName(calendar.getPath(), dbItemInventoryInstance.getId()) == null);
                            break;
                        case SCHEDULE:
                            objectMissing = (schedulesDBLayer.getSchedule(calendar.getPath(), dbItemInventoryInstance.getId()) == null);
                            break;
                        default:
                            break;
                        }
                        // check if usage already exists
                        DBItemInventoryClusterCalendarUsage dbItemInventoryCalendarUsage = calendarUsageDbLayer.getCalendarUsageOfAnObject(
                                dbItemInventoryInstance.getSchedulerId(), calendar.getBasedOn(), calendar.getType().name(), calendar.getPath());
                        if (dbItemInventoryCalendarUsage != null && dbItemInventoryCalendarUsage.getConfiguration() != null) {
                            continue;
                        } else {
                            importCalendarUsage(dbItemInventoryCalendarUsage, calendar, calendarUsageDbLayer, calendarsMap.get(calendar
                                    .getBasedOn()), objectMissing);
                        }
                    }
                }

                if(eventCommands != null && !eventCommands.isEmpty() && "active".equals(dbItemInventoryInstance.getClusterType())) {
                    InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
                    clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(calendarImportFilter.getJobschedulerId());
                }
                if(clusterMembers != null) {
                    SendCalendarEventsUtil.sendEvent(eventCommands, clusterMembers, accessToken);
                } else {
                    SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
                }
                eventCommands = null;
            }

            if (listOfErrors.size() > 0) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
            }
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            try {
                if(clusterMembers != null) {
                    SendCalendarEventsUtil.sendEvent(eventCommands, clusterMembers, accessToken);
                } else {
                    SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
                }
            } catch (Exception e) {
                LOGGER.error("Couldn't send calendar events", e);
            }
            Globals.disconnect(connection);
        }
    }

    private void checkRequirements(Calendar calendar) throws JocMissingCommentException, JocMissingRequiredParameterException,
            JobSchedulerInvalidResponseDataException {
        if (calendar == null) {
            throw new JocMissingRequiredParameterException("undefined 'calendar'");
        }

        checkRequiredParameter("calendar path", calendar.getPath());
        calendar.setPath(normalizePath(calendar.getPath()));

        if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) {
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
        } else {
            calendar.setBasedOn(normalizePath(calendar.getBasedOn()));
        }
    }

    private DBItemInventoryClusterCalendarUsage importCalendarUsage(DBItemInventoryClusterCalendarUsage dbItemInventoryCalendarUsage, Calendar calendar,
            CalendarUsageDBLayer calendarUsageDbLayer, DBItemInventoryClusterCalendar dbItemCalendar, boolean objectMissing) throws JocException, IOException {
        Calendar oldConfiguration = null;
        Boolean oldEdited = null;
        if (dbItemInventoryCalendarUsage == null) {
            dbItemInventoryCalendarUsage = new DBItemInventoryClusterCalendarUsage();
            dbItemInventoryCalendarUsage.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
            dbItemInventoryCalendarUsage.setObjectType(calendar.getType().name());
            dbItemInventoryCalendarUsage.setPath(calendar.getPath());
            dbItemInventoryCalendarUsage.setCalendarId(dbItemCalendar.getId());
        } else {
            oldConfiguration = mapper.readValue(dbItemInventoryCalendarUsage.getConfiguration(), Calendar.class);
            oldConfiguration.setBasedOn(null);
            oldConfiguration.setType(null);
            oldEdited = dbItemInventoryCalendarUsage.getEdited();
        }
        dbItemInventoryCalendarUsage.setEdited(objectMissing);
        
//        if (CalendarType.NON_WORKING_DAYS.name().equals(dbItemCalendar.getType())) {
//            calendar.setType(CalendarType.NON_WORKING_DAYS);
//        } else {
//            calendar.setType(CalendarType.WORKING_DAYS);
//        }
        calendar.setType(null);
        calendar.setBasedOn(null);
        calendar.setPath(null);
        calendar.setName(null);
        calendar.setUsedBy(null);
        dbItemInventoryCalendarUsage.setConfiguration(mapper.writeValueAsString(calendar));
        if (dbItemInventoryCalendarUsage.getId() == null) {
            calendarUsageDbLayer.saveCalendarUsage(dbItemInventoryCalendarUsage);
        } else {
            if (oldConfiguration.equals(calendar)) {
                if (dbItemInventoryCalendarUsage.getEdited() != oldEdited) {
                    calendarUsageDbLayer.updateEditFlag(dbItemInventoryCalendarUsage);
                }
            } else {
                calendarUsageDbLayer.updateCalendarUsage(dbItemInventoryCalendarUsage);
            }
        }
        eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(dbItemInventoryCalendarUsage.getPath(), dbItemInventoryCalendarUsage
                .getObjectType(), "CalendarUsageUpdated"));
        return dbItemInventoryCalendarUsage;
    }

    private DBItemInventoryClusterCalendar importNewCalendar(Calendar calendar, CalendarsDBLayer calendarDbLayer) {
        DBItemInventoryClusterCalendar calendarDbItem = null;
        try {
            calendarDbItem = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getSchedulerId(), calendarDbItem, calendar);
            eventCommands.add(SendCalendarEventsUtil.addEvent(getCalendarEvent(calendar, "CalendarCreated")));
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        }
        return calendarDbItem;
    }

    private DBItemInventoryClusterCalendar importCalendar(Calendar calendar, DBItemInventoryClusterCalendar calendarDbItem, CalendarsDBLayer calendarDbLayer,
            CalendarUsageDBLayer calendarUsageDbLayer) {
        try {
            CalendarEvent calEvt = getCalendarEvent(calendar, "CalendarUpdated");

            Dates newDates = null;
            Dates oldDates = null;
            Calendar oldCalendar = null;
            List<DBItemInventoryClusterCalendarUsage> dbCalendarUsages = null;

            try {
                if ((calendar.getType() != null && !calendar.getType().name().equals(calendarDbItem.getType()))) {
                    dbCalendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
                    if (dbCalendarUsages != null && !dbCalendarUsages.isEmpty()) {
                        throw new JobSchedulerBadRequestException(String.format(
                                "It is not allowed to change the calendar type (%1$s -> %2$s) when it is already assigned to a job, order or schedule.",
                                calendarDbItem.getType(), calendar.getType().name()));
                    }
                }
                //check if update necessary
                oldCalendar = mapper.readValue(calendarDbItem.getConfiguration(), Calendar.class);
                newDates = new FrequencyResolver().resolveFromUTCYesterday(calendar);
                oldDates = new FrequencyResolver().resolveFromUTCYesterday(oldCalendar);
                updateUsageIsNecessary = (!newDates.getDates().equals(oldDates.getDates()));
                //TODO check if update necessary
                calendarDbItem = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getSchedulerId(), calendarDbItem, calendar);
                eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));
                if (updateUsageIsNecessary) {
                    if (dbCalendarUsages == null) {
                        dbCalendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
                    }
                    if (dbCalendarUsages != null) {
                        for (DBItemInventoryClusterCalendarUsage dbCalendarUsage : dbCalendarUsages) {
                            eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(dbCalendarUsage.getPath(), dbCalendarUsage.getObjectType(),
                                    "CalendarUsageUpdated"));
                        }

                    }
                }
            } catch (SOSMissingDataException e) {
                throw new JocMissingRequiredParameterException(e);
            } catch (SOSInvalidDataException e) {
                throw new JobSchedulerInvalidResponseDataException(e);
            }
            
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        }
        return calendarDbItem;
    }
    
    private CalendarEvent getCalendarEvent(Calendar calendar, String key) {
        CalendarEvent calEvt = new CalendarEvent();
        CalendarVariables calEvtVars = new CalendarVariables();
        calEvtVars.setPath(calendar.getPath());
        calEvt.setKey(key);
        if (CalendarType.NON_WORKING_DAYS == calendar.getType()) {
            calEvtVars.setObjectType(CalendarObjectType.NONWORKINGDAYSCALENDAR);
        } else {
            calEvtVars.setObjectType(CalendarObjectType.WORKINGDAYSCALENDAR);
        }
        calEvt.setVariables(calEvtVars);
        return calEvt;
    }

}