package com.sos.joc.calendars.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsImportResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.classes.calendar.JobSchedulerCalendarCallable;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsagesAndInstance;
import com.sos.joc.db.calendars.CalendarsDBLayer;
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

@Path("calendars")
public class CalendarsImportResourceImpl extends JOCResourceImpl implements ICalendarsImportResource {

    private static final String API_CALL = "./calendars/import";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private List<String> eventCommands = new ArrayList<String>();
    private Map<String, Boolean> updateJobOrderScheduleIsNecessary = new HashMap<String, Boolean>();
    private Map<String, List<String>> newDatesMap = new HashMap<String, List<String>>();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public JOCDefaultResponse importCalendars(String accessToken, CalendarImportFilter calendarImportFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, calendarImportFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getCalendar().getEdit().isCreate());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            List<Calendar> calendars = calendarImportFilter.getCalendars();

            if (calendars != null && !calendars.isEmpty()) {

                checkRequiredComment(calendarImportFilter.getAuditLog());

                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);
                CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);
                InventoryOrdersDBLayer ordersDBLayer = new InventoryOrdersDBLayer(connection);
                InventoryJobsDBLayer jobsDBLayer = new InventoryJobsDBLayer(connection);
                InventorySchedulesDBLayer schedulesDBLayer = new InventorySchedulesDBLayer(connection);
                Map<String, DBItemCalendar> calendarsMap = new HashMap<String, DBItemCalendar>();

                for (Calendar calendar : calendars) {
                    checkRequirements(calendar);
                    if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) {
                        DBItemCalendar dbItemCalendar = calendarDbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath());

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
                    if (calendar.getBasedOn() != null && !calendar.getBasedOn().isEmpty() && calendarsMap.containsKey(calendar.getBasedOn())) {
                        // check if corresponding order, job or schedule exists
                        boolean objectExists = false;
                        switch (calendar.getType()) {
                        case ORDER:
                            objectExists = (ordersDBLayer.getInventoryOrderByName(dbItemInventoryInstance.getId(), calendar.getPath()) != null);
                            break;
                        case JOB:
                            objectExists = (jobsDBLayer.getInventoryJobByName(calendar.getPath(), dbItemInventoryInstance.getId()) != null);
                            break;
                        case SCHEDULE:
                            objectExists = (schedulesDBLayer.getSchedule(calendar.getPath(), dbItemInventoryInstance.getId()) != null);
                            break;
                        default:
                            break;
                        }
                        if (!objectExists) {
                            continue;
                        }
                        // check if usage already exists
                        DBItemInventoryCalendarUsage dbItemInventoryCalendarUsage = calendarUsageDbLayer.getCalendarUsageOfAnObject(
                                dbItemInventoryInstance.getId(), calendar.getBasedOn(), calendar.getType().name(), calendar.getPath());
                        if (dbItemInventoryCalendarUsage != null && dbItemInventoryCalendarUsage.getConfiguration() != null) {
                            continue;
                        } else {
                            importCalendarUsage(dbItemInventoryCalendarUsage, calendar, calendarUsageDbLayer, calendarsMap.get(calendar
                                    .getBasedOn()));
                        }
                    }
                }

                for (Calendar calendar : calendars) {
                    if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) {
                        if (updateJobOrderScheduleIsNecessary.get(calendar.getPath()) && calendarsMap.containsKey(calendar.getPath())) {
                            CalendarUsagesAndInstance calendarUsageInstance = new CalendarUsagesAndInstance(dbItemInventoryInstance, false);
                            List<DBItemInventoryCalendarUsage> dbCalendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarsMap.get(calendar
                                    .getPath()).getId());
                            if (!dbCalendarUsages.isEmpty()) {
                                calendarUsageInstance.setCalendarUsages(dbCalendarUsages);
                                calendarUsageInstance.setCalendarPath(calendar.getPath());
                                calendarUsageInstance.setOldCalendarPath(calendar.getPath());
                                calendarUsageInstance.setBaseCalendar(calendar);
                                calendarUsageInstance.setDates(newDatesMap.get(calendar.getPath()));
                                JobSchedulerCalendarCallable callable = new JobSchedulerCalendarCallable(calendarUsageInstance, getAccessToken());
                                CalendarUsagesAndInstance c = callable.call();
                                calendarUsageDbLayer.updateEditFlag(c.getCalendarUsages(), true);

                                for (Entry<String, Exception> exception : c.getExceptions().entrySet()) {
                                    if (exception.getValue() instanceof JocException) {
                                        listOfErrors.add(new BulkError().get((JocException) exception.getValue(), getJocError(), exception.getKey()));
                                    } else {
                                        listOfErrors.add(new BulkError().get(exception.getValue(), getJocError(), exception.getKey()));
                                    }
                                }
                            }
                        }
                    }
                }

                SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
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
                // throw new JobSchedulerInvalidResponseDataException("calendar field 'from' is undefined");
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

    private DBItemInventoryCalendarUsage importCalendarUsage(DBItemInventoryCalendarUsage dbItemInventoryCalendarUsage, Calendar calendar,
            CalendarUsageDBLayer calendarUsageDbLayer, DBItemCalendar dbItemCalendar) throws JsonProcessingException, JocException {
        if (dbItemInventoryCalendarUsage == null) {
            dbItemInventoryCalendarUsage = new DBItemInventoryCalendarUsage();
        }
        dbItemInventoryCalendarUsage.setInstanceId(dbItemInventoryInstance.getId());
        dbItemInventoryCalendarUsage.setEdited(false);
        dbItemInventoryCalendarUsage.setObjectType(calendar.getType().name());
        dbItemInventoryCalendarUsage.setPath(calendar.getPath());
        dbItemInventoryCalendarUsage.setCalendarId(dbItemCalendar.getId());
        if (CalendarType.NON_WORKING_DAYS.name().equals(dbItemCalendar.getType())) {
            calendar.setType(CalendarType.NON_WORKING_DAYS);
        } else {
            calendar.setType(CalendarType.WORKING_DAYS);
        }
        calendar.setPath(null);
        calendar.setName(null);
        calendar.setUsedBy(null);
        dbItemInventoryCalendarUsage.setConfiguration(mapper.writeValueAsString(calendar));
        if (dbItemInventoryCalendarUsage.getId() == null) {
            calendarUsageDbLayer.saveCalendarUsage(dbItemInventoryCalendarUsage);
        } else {
            calendarUsageDbLayer.updateCalendarUsage(dbItemInventoryCalendarUsage);
        }
        eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(dbItemInventoryCalendarUsage.getPath(), dbItemInventoryCalendarUsage
                .getObjectType(), "CalendarUsageUpdated"));
        return dbItemInventoryCalendarUsage;
    }

    private DBItemCalendar importNewCalendar(Calendar calendar, CalendarsDBLayer calendarDbLayer) {
        DBItemCalendar calendarDbItem = null;
        try {
            CalendarEvent calEvt = new CalendarEvent();
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendar.getPath());
            calEvt.setKey("CalendarCreated");

            calendarDbItem = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), calendarDbItem, calendar);
            if (CalendarObjectType.NONWORKINGDAYSCALENDAR.name().equals(calendarDbItem.getType())) {
                calEvtVars.setObjectType(CalendarObjectType.NONWORKINGDAYSCALENDAR);
            } else {
                calEvtVars.setObjectType(CalendarObjectType.WORKINGDAYSCALENDAR);
            }
            calEvt.setVariables(calEvtVars);
            eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));

        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        }
        return calendarDbItem;
    }

    private DBItemCalendar importCalendar(Calendar calendar, DBItemCalendar calendarDbItem, CalendarsDBLayer calendarDbLayer,
            CalendarUsageDBLayer calendarUsageDbLayer) {
        try {
            CalendarEvent calEvt = new CalendarEvent();
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendar.getPath());
            calEvt.setKey("CalendarUpdated");

            Dates newDates = null;
            Dates oldDates = null;
            Calendar oldCalendar = null;

            try {
                oldCalendar = mapper.readValue(calendarDbItem.getConfiguration(), Calendar.class);
                newDates = new FrequencyResolver().resolveFromToday(calendar);
                oldDates = new FrequencyResolver().resolveFromToday(oldCalendar);
                boolean updateIsNecessary = (!newDates.getDates().equals(oldDates.getDates()));
                if (updateIsNecessary) {
                    newDatesMap.put(calendar.getPath(), newDates.getDates());
                }
                updateJobOrderScheduleIsNecessary.put(calendar.getPath(), updateIsNecessary);
                if ((calendar.getType() != null && !calendar.getType().name().equals(calendarDbItem.getType()))) {
                    List<DBItemInventoryCalendarUsage> dbCalendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
                    if (dbCalendarUsages != null && !dbCalendarUsages.isEmpty()) {
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

            calendarDbItem = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), calendarDbItem, calendar);
            if (CalendarObjectType.NONWORKINGDAYSCALENDAR.name().equals(calendarDbItem.getType())) {
                calEvtVars.setObjectType(CalendarObjectType.NONWORKINGDAYSCALENDAR);
            } else {
                calEvtVars.setObjectType(CalendarObjectType.WORKINGDAYSCALENDAR);
            }
            calEvt.setVariables(calEvtVars);
            Calendar curCalendar = mapper.readValue(calendarDbItem.getConfiguration(), Calendar.class);
            if (!curCalendar.equals(oldCalendar)) {
                eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));
            }

        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        }
        return calendarDbItem;
    }

}