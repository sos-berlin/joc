package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsImportResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarImportFilter;

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
//            if (uploadedInputStream == null || fileDetail == null) {
//                throw new JocMissingRequiredParameterException("calendar import file is required");
//            }
//            LOGGER.info(fileDetail.getName());
//            LOGGER.info(fileDetail.getType());
//            LOGGER.info(fileDetail.getSize() + "");

//            Calendars calendars = null;
//            try {
//                ObjectMapper objMapper = new ObjectMapper();
//                calendars = objMapper.readValue(uploadedInputStream, Calendars.class);
//                LOGGER.info(objMapper.writeValueAsString(calendars));
//            } catch (Exception e) {
//                throw new JobSchedulerInvalidResponseDataException("calendar import file is invalid", e);
//            }
            List<String> eventCommands = new ArrayList<String>();
            List<Calendar> calendars = calendarImportFilter.getCalendars();
//            if (calendars != null && calendars.getCalendars() != null && !calendars.getCalendars().isEmpty()) {
            if (calendars != null && !calendars.isEmpty()) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
                CalendarUsageDBLayer dbUsageLayer = new CalendarUsageDBLayer(connection);
                // first save or update all calendars
                Map<String, Long> calendarPaths = new HashMap<String, Long>();
                for (Calendar calendar : calendars) {
                    if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) { // Calendar
                        DBItemCalendar dbItemCalendar = dbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath());
                        boolean update = false;
                        if (dbItemCalendar != null) {
                            update = true;
                        }
                        dbItemCalendar = dbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), dbItemCalendar, calendar);
                        ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(null, dbItemCalendar.getName(), 
                                calendarImportFilter.getAuditLog());
                        logAuditMessage(calendarAudit);
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
                        // save or update calendar usage (restrition) only if corresponding order, job or schedule exists
                        if (dbItem != null) {
                            DBItemInventoryCalendarUsage dbUsage = dbUsageLayer.getCalendarUsageByConstraint(
                                    dbItemInventoryInstance.getId(), calendarPaths.get(calendarUsage.getBasedOn()),
                                    calendarUsage.getType().toString(), calendarUsage.getPath());
                            if (dbUsage != null) {
                                // update
                                dbUsage.setObjectType(calendarUsage.getType().toString());
                                calendarUsage.setType(null);
                                dbUsage.setPath(calendarUsage.getPath());
                                calendarUsage.setPath(null);
                                dbUsage.setEdited(false);
                                ObjectMapper mapper = new ObjectMapper();
                                dbUsage.setConfiguration(mapper.writeValueAsString(calendarUsage));
                                dbUsage.setModified(Date.from(Instant.now()));
                                dbUsageLayer.updateCalendarUsage(dbUsage);
                                ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(null, dbUsage.getPath(), 
                                        calendarImportFilter.getAuditLog());
                                logAuditMessage(calendarAudit);
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
                                ObjectMapper mapper = new ObjectMapper();
                                dbUsage.setConfiguration(mapper.writeValueAsString(calendarUsage));
                                dbUsageLayer.saveCalendarUsage(dbUsage);
                                ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(null, dbUsage.getPath(), 
                                        calendarImportFilter.getAuditLog());
                                logAuditMessage(calendarAudit);
                                eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(dbUsage.getPath(), dbUsage.getObjectType(),
                                        "CalendarUsageCreated"));
                            }
                        }
                    }
                }
                SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, getAccessToken());
                // TODO call ModifyHotFolder for the corresponding orders, jobs and schedules of the calendar usage (restriction)
                for(DbItem item : inventoryDBItemsToUpdate) {
                    if (item instanceof DBItemInventoryOrder) {
                        // TODO implementation for ModifyHotFolder for orders
                    } else if (item instanceof DBItemInventoryJob) {
                        // TODO implementation for ModifyHotFolder for jobs
                    } else if (item instanceof DBItemInventorySchedule) {
                        // TODO implementation for ModifyHotFolder for schedules
                    }
                }
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