package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.jobscheduler.model.event.CalendarEvent;
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
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarImportFilter;
import com.sos.joc.model.calendar.Dates;
import com.sos.joc.model.common.Err419;

@Path("calendars")
public class CalendarsImportResourceImpl extends JOCResourceImpl implements ICalendarsImportResource {

    private static final String API_CALL = "./calendars/import";
//    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarsImportResourceImpl.class);

    @Override
    // public JOCDefaultResponse importCalendars(String accessToken, InputStream uploadedInputStream, FormDataContentDisposition fileDetail,
    // String jobschedulerId) throws Exception {
    public JOCDefaultResponse importCalendars(String accessToken, CalendarImportFilter calendarImportFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, calendarImportFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken)
                    .getCalendar().getEdit().isCreate());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            List<String> eventCommands = new ArrayList<String>();
            List<Calendar> calendars = calendarImportFilter.getCalendars();
            List<DBItemInventoryCalendarUsage> calendarUsages = null;
            List<Err419> listOfErrors = new ArrayList<Err419>();
            if (calendars != null && !calendars.isEmpty()) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
                CalendarUsageDBLayer dbUsageLayer = new CalendarUsageDBLayer(connection);
                // first save or update all calendars
                Map<String, Long> calendarPaths = new HashMap<String, Long>();
                for (Calendar calendar : calendars) {
                    if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) { // Calendar
                        DBItemCalendar dbItemCalendar = dbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath());
                        boolean update = (dbItemCalendar != null);
                        if (update) {
                            if ((calendar.getType() != null && !calendar.getType().name().equals(dbItemCalendar.getType()))) {
                                calendarUsages = dbUsageLayer.getCalendarUsages(dbItemCalendar.getId());
                                if (!calendarUsages.isEmpty()) {
                                    throw new JobSchedulerBadRequestException(String.format(
                                            "It is not allowed to change the calendar type (%1$s -> %2$s) when it is already assigned "
                                            + "to a job, order or schedule.", dbItemCalendar.getType(), calendar.getType().name()));
                                } else {
                                    dbItemCalendar = 
                                            dbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), dbItemCalendar, calendar);
                                }
                            }
                        } else {
                            dbItemCalendar = dbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), dbItemCalendar, calendar);
                        }
                        ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(null, dbItemCalendar.getName(), 
                                calendarImportFilter.getAuditLog(), calendarImportFilter.getJobschedulerId());
                        logAuditMessage(calendarAudit);
                        storeAuditLogEntry(calendarAudit);
                        CalendarEvent calEvt = new CalendarEvent();
                        if (update) {
                            calEvt.setKey("CalendarUpdated");
                        } else {
                            calEvt.setKey("CalendarCreated");
                        }
                        CalendarVariables calEvtVars = new CalendarVariables();
                        calEvtVars.setPath(dbItemCalendar.getName());
                        calEvt.setVariables(calEvtVars);
                        eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));
                        calendarPaths.put(dbItemCalendar.getName(), dbItemCalendar.getId());
                    }
                }
                // then save or update calendar usages (restrictions)
                InventoryOrdersDBLayer ordersDBLayer = new InventoryOrdersDBLayer(connection);
                InventoryJobsDBLayer jobsDBLayer = new InventoryJobsDBLayer(connection);
                InventorySchedulesDBLayer schedulesDBLayer = new InventorySchedulesDBLayer(connection);
                Set<DbItem> inventoryDBItemsToUpdate = new HashSet<DbItem>();
                ObjectMapper mapper = new ObjectMapper();
                for (Calendar calendarUsage : calendars) {
                    if (calendarUsage.getBasedOn() != null && !calendarUsage.getBasedOn().isEmpty()
                            && calendarPaths.containsKey(calendarUsage.getBasedOn())) { // CalendarUsage
                        DbItem dbItem = null;
                        // check first if corresponding order, job or schedule exists
                        switch(calendarUsage.getType()) {
                            case ORDER:
                                dbItem = ordersDBLayer.getInventoryOrderByName(dbItemInventoryInstance.getId(), calendarUsage.getPath());
                                inventoryDBItemsToUpdate.add(dbItem);
                                break;
                            case JOB:
                                dbItem = jobsDBLayer.getInventoryJobByName(calendarUsage.getPath(), dbItemInventoryInstance.getId());
                                inventoryDBItemsToUpdate.add(dbItem);
                                break;
                            case SCHEDULE:
                                dbItem = schedulesDBLayer.getSchedule(calendarUsage.getPath(), dbItemInventoryInstance.getId());
                                inventoryDBItemsToUpdate.add(dbItem);
                                break;
                            default:
                                break;
                        }
                        // save or update calendar usage (restriction) only if corresponding order, job or schedule exists
                        if (dbItem != null) {
                            DBItemInventoryCalendarUsage dbUsage = dbUsageLayer.getCalendarUsageByConstraint(
                                    dbItemInventoryInstance.getId(), calendarPaths.get(calendarUsage.getBasedOn()),
                                    calendarUsage.getType().toString(), calendarUsage.getPath());
                            if (dbUsage != null) {
                                // update
                                calendarUsage.setType(null);
                                calendarUsage.setPath(null);
                                dbUsage.setEdited(false);
                                dbUsage.setConfiguration(mapper.writeValueAsString(calendarUsage));
                                dbUsage.setModified(Date.from(Instant.now()));
                                dbUsageLayer.updateCalendarUsage(dbUsage);
                                eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(dbUsage.getPath(), dbUsage.getObjectType(),
                                        "CalendarUsageUpdated"));
                            } else {
                                // save
                                dbUsage = new DBItemInventoryCalendarUsage();
                                dbUsage.setInstanceId(dbItemInventoryInstance.getId());
                                dbUsage.setCalendarId(calendarPaths.get(calendarUsage.getBasedOn()));
                                dbUsage.setObjectType(calendarUsage.getType().toString());
                                calendarUsage.setType(null);
                                dbUsage.setPath(calendarUsage.getPath());
                                calendarUsage.setPath(null);
                                dbUsage.setEdited(false);
                                dbUsage.setModified(Date.from(Instant.now()));
                                dbUsage.setCreated(Date.from(Instant.now()));
                                dbUsage.setConfiguration(mapper.writeValueAsString(calendarUsage));
                                dbUsageLayer.saveCalendarUsage(dbUsage);
                                eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(dbUsage.getPath(), dbUsage.getObjectType(),
                                        "CalendarUsageCreated"));
                            }
                        }
                    }
                }
                SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, getAccessToken());
                for (String calendarPath : calendarPaths.keySet()) {
                    calendarUsages = dbUsageLayer.getCalendarUsages(calendarPaths.get(calendarPath));
                    if (!calendarUsages.isEmpty()) {
                        for (Calendar calendar : calendars) {
                            if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) {
                                Dates newDates = new FrequencyResolver().resolveFromToday(calendar);
                                CalendarUsagesAndInstance calendarUsageInstance = 
                                        new CalendarUsagesAndInstance(dbItemInventoryInstance, false);
                                calendarUsageInstance.setCalendarUsages(calendarUsages);
                                calendarUsageInstance.setCalendarPath(calendar.getPath());
                                calendarUsageInstance.setOldCalendarPath(calendar.getPath());
                                calendarUsageInstance.setBaseCalendar(calendar);
                                calendarUsageInstance.setDates(newDates.getDates());
                                JobSchedulerCalendarCallable callable = 
                                        new JobSchedulerCalendarCallable(calendarUsageInstance, accessToken);
                                CalendarUsagesAndInstance c = callable.call();
                                dbUsageLayer.updateEditFlag(c.getCalendarUsages(), true);
                                
                                for (Entry<String, Exception> exception : c.getExceptions().entrySet()) {
                                    if (exception.getValue() instanceof JocException) {
                                        listOfErrors.add(new BulkError().get((JocException) exception.getValue(), 
                                                getJocError(), exception.getKey()));
                                    } else {
                                        listOfErrors.add(new BulkError().get(exception.getValue(), getJocError(), exception.getKey()));
                                    }
                                }
                            }
                        }
                    }
                }
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

}