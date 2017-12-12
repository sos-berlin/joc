package com.sos.joc.calendars.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.Path;

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
import com.sos.joc.exceptions.JocObjectAlreadyExistException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarImportFilter;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Dates;
import com.sos.joc.model.common.Err419;

@Path("calendars")
public class CalendarsImportResourceImpl extends JOCResourceImpl implements ICalendarsImportResource {

    private static final String API_CALL = "./calendars/import";

    @Override
    public JOCDefaultResponse importCalendars(String accessToken, CalendarImportFilter calendarImportFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, calendarImportFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getCalendar().getEdit().isCreate());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            List<String> eventCommands = new ArrayList<String>();
            List<Calendar> calendars = calendarImportFilter.getCalendars();
            List<Err419> listOfErrors = new ArrayList<Err419>();
            ObjectMapper mapper = new ObjectMapper();

            if (calendars != null && !calendars.isEmpty()) {

                checkRequiredComment(calendarImportFilter.getAuditLog());

                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);
                CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);
                InventoryOrdersDBLayer ordersDBLayer = new InventoryOrdersDBLayer(connection);
                InventoryJobsDBLayer jobsDBLayer = new InventoryJobsDBLayer(connection);
                InventorySchedulesDBLayer schedulesDBLayer = new InventorySchedulesDBLayer(connection);
                Map<String, Set<DBItemInventoryCalendarUsage>> calendarUsagesMap = new HashMap<String, Set<DBItemInventoryCalendarUsage>>();
                Map<String, DBItemCalendar> calendarsMap = new HashMap<String, DBItemCalendar>();

                for (Calendar calendar : calendars) {
                    checkRequirements(calendar);
                    if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) {
                        calendarsMap.put(calendar.getPath(), calendarDbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath()));
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
                            if (dbItemInventoryCalendarUsage == null) {
                                dbItemInventoryCalendarUsage = new DBItemInventoryCalendarUsage();
                            }
                            dbItemInventoryCalendarUsage.setInstanceId(dbItemInventoryInstance.getId());
                            dbItemInventoryCalendarUsage.setEdited(false);
                            dbItemInventoryCalendarUsage.setObjectType(calendar.getType().name());
                            dbItemInventoryCalendarUsage.setPath(calendar.getPath());
                            dbItemInventoryCalendarUsage.setCalendarId(calendarsMap.get(calendar.getBasedOn()).getId());
                            if (CalendarType.NON_WORKING_DAYS.name().equals(calendarsMap.get(calendar.getBasedOn()).getType())) {
                                calendar.setType(CalendarType.NON_WORKING_DAYS);
                            } else {
                                calendar.setType(CalendarType.WORKING_DAYS);
                            }
                            calendar.setPath(null);
                            calendar.setName(null);
                            calendar.setUsedBy(null);
                            dbItemInventoryCalendarUsage.setConfiguration(mapper.writeValueAsString(calendar));

                            calendarUsagesMap.putIfAbsent(calendar.getBasedOn(), new HashSet<DBItemInventoryCalendarUsage>());
                            calendarUsagesMap.get(calendar.getBasedOn()).add(dbItemInventoryCalendarUsage);
                        }
                    }

                }

                for (Calendar calendar : calendars) {
                    if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) {
                        ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar.getId(), calendar.getPath(), calendarImportFilter
                                .getAuditLog(), calendarImportFilter.getJobschedulerId());
                        logAuditMessage(calendarAudit);
                        importCalendar(calendar, calendarUsagesMap.get(calendar.getPath()), calendarsMap.get(calendar.getPath()), mapper,
                                calendarDbLayer, calendarUsageDbLayer, eventCommands, listOfErrors);
                        storeAuditLogEntry(calendarAudit);
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

    private void importCalendar(Calendar calendar, Set<DBItemInventoryCalendarUsage> calendarUsagesFromImportFile, DBItemCalendar calendarDbItem,
            ObjectMapper mapper, CalendarsDBLayer calendarDbLayer, CalendarUsageDBLayer calendarUsageDbLayer, List<String> eventCommands,
            List<Err419> listOfErrors) {
        try {
            CalendarEvent calEvt = new CalendarEvent();
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendar.getPath());

            boolean newCalendar = (calendarDbItem == null);
            Dates newDates = null;
            Dates oldDates = null;
            Calendar oldCalendar = null;

            CalendarUsagesAndInstance calendarUsageInstance = new CalendarUsagesAndInstance(dbItemInventoryInstance, false);
            List<DBItemInventoryCalendarUsage> dbCalendarUsages = null;
//            List<DBItemInventoryCalendarUsage> dbCalendarUsagesFromFile = new ArrayList<DBItemInventoryCalendarUsage>();

            if (!newCalendar) {
                calEvt.setKey("CalendarUpdated");
                try {
                    oldCalendar = mapper.readValue(calendarDbItem.getConfiguration(), Calendar.class);
                    newDates = new FrequencyResolver().resolveFromToday(calendar);
                    oldDates = new FrequencyResolver().resolveFromToday(oldCalendar);
                    if (!newDates.getDates().equals(oldDates.getDates()) && dbCalendarUsages == null) {
                        dbCalendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
                    }
                    if ((calendar.getType() != null && !calendar.getType().name().equals(calendarDbItem.getType()))) {
                        dbCalendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
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
            } else {
                calEvt.setKey("CalendarCreated");
                DBItemCalendar oldCalendarDbItem = calendarDbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath());
                if (oldCalendarDbItem != null) {
                    throw new JocObjectAlreadyExistException(calendar.getPath());
                }
            }

            calendarDbItem = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), calendarDbItem, calendar);
            if (CalendarObjectType.NONWORKINGDAYSCALENDAR.name().equals(calendarDbItem.getType())) {
                calEvtVars.setObjectType(CalendarObjectType.NONWORKINGDAYSCALENDAR);
            } else {
                calEvtVars.setObjectType(CalendarObjectType.WORKINGDAYSCALENDAR);
            }
            calEvt.setVariables(calEvtVars);

            if (newCalendar) {
                eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));
            } else {
                Calendar curCalendar = mapper.readValue(calendarDbItem.getConfiguration(), Calendar.class);
                if (!curCalendar.equals(oldCalendar)) {
                    eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));
                }
            }

            // insert new CalendarUsages from import file
            if (calendarUsagesFromImportFile != null) {
                try {
                    calendarUsageDbLayer.getSession().beginTransaction();
                    for (DBItemInventoryCalendarUsage dbItemCalendarUsage : calendarUsagesFromImportFile) {
                        if (dbItemCalendarUsage.getId() == null) {
                            calendarUsageDbLayer.saveCalendarUsage(dbItemCalendarUsage);
                        } else {
                            calendarUsageDbLayer.updateCalendarUsage(dbItemCalendarUsage);
                        }
//                        dbCalendarUsagesFromFile.add(dbItemCalendarUsage);
                        eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(dbItemCalendarUsage.getPath(), dbItemCalendarUsage.getObjectType(),
                                "CalendarUsageUpdated"));
                    }
                } catch (JocException e) {
                    calendarUsageDbLayer.getSession().rollback();
//                    dbCalendarUsagesFromFile = null;
                    throw e;
                } catch (Exception e) {
                    calendarUsageDbLayer.getSession().rollback();
//                    dbCalendarUsagesFromFile = null;
                    throw e;
                }
            }

            // add new CalendarUsages to list of objects where recalculation is necessary
//            if (dbCalendarUsagesFromFile != null && !dbCalendarUsagesFromFile.isEmpty()) {
//                if (dbCalendarUsages == null) {
//                    dbCalendarUsages = new ArrayList<DBItemInventoryCalendarUsage>();
//                }
//                dbCalendarUsages.addAll(dbCalendarUsagesFromFile);
//            }

            if (dbCalendarUsages != null && !dbCalendarUsages.isEmpty()) {
                // update calendar in jobs, orders and schedules
                // TODO improve performance -> update object for each Calendar in one <modify_hot_folder/>
                if (!dbCalendarUsages.isEmpty()) {
                    calendarUsageInstance.setCalendarUsages(dbCalendarUsages);
                    calendarUsageInstance.setCalendarPath(calendar.getPath());
                    calendarUsageInstance.setOldCalendarPath(calendar.getPath());
                    calendarUsageInstance.setBaseCalendar(calendar);
                    calendarUsageInstance.setDates(newDates.getDates());
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
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar.getPath()));
        }
    }

}