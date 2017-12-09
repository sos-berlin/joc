package com.sos.joc.calendars.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

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
            checkRequiredComment(calendarImportFilter.getAuditLog());
            
            List<String> eventCommands = new ArrayList<String>();
            List<Calendar> calendars = calendarImportFilter.getCalendars();
            List<Err419> listOfErrors = new ArrayList<Err419>();
            ObjectMapper mapper = new ObjectMapper();
            
            if (calendars != null && !calendars.isEmpty()) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);
                CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);
                
                for (Calendar calendar : calendars) {
                    checkRequirements(calendar);
                    DBItemCalendar calendarDbItem = calendarDbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath());
                    
                    ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar.getId(), calendar.getPath(), calendarImportFilter.getAuditLog(), calendarImportFilter.getJobschedulerId());
                    logAuditMessage(calendarAudit);
                    importCalendar(calendar, calendarDbItem, mapper, calendarDbLayer, calendarUsageDbLayer, eventCommands, listOfErrors);
                    storeAuditLogEntry(calendarAudit);
                }
            }
            
            SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
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
    
    
    private void checkRequirements(Calendar calendar) throws JocMissingCommentException, JocMissingRequiredParameterException, JobSchedulerInvalidResponseDataException {
        if (calendar == null) {
            throw new JocMissingRequiredParameterException("undefined 'calendar'");
        }
        
        checkRequiredParameter("calendar path", calendar.getPath());
        
        if (calendar.getTo() == null || calendar.getTo().isEmpty()) {
            throw new JobSchedulerInvalidResponseDataException("calendar field 'to' is undefined");
        } else if (!calendar.getTo().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new JobSchedulerInvalidResponseDataException("calendar field 'to' must have the format YYYY-MM-DD.");
        }
        
        if (calendar.getFrom() == null || calendar.getFrom().isEmpty()) {
            //throw new JobSchedulerInvalidResponseDataException("calendar field 'from' is undefined");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setFrom(df.format(Date.from(Instant.now())));
        } else if (!calendar.getFrom().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new JobSchedulerInvalidResponseDataException("calendar field 'from' must have the format YYYY-MM-DD.");
        }
        
        if (calendar.getType() == null || calendar.getType().name().isEmpty()) {
            calendar.setType(CalendarType.WORKING_DAYS);
        }
        calendar.setPath(normalizePath(calendar.getPath()));
    }
    
    private void importCalendar(Calendar calendar, DBItemCalendar calendarDbItem, ObjectMapper mapper, CalendarsDBLayer calendarDbLayer,
            CalendarUsageDBLayer calendarUsageDbLayer, List<String> eventCommands, List<Err419> listOfErrors) {
        try {
            CalendarEvent calEvt = new CalendarEvent();
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendar.getPath());

            boolean updateJobOrderScheduleIsNecessary = false;
            boolean updateCalendarUsageIsNecessary = false;
            boolean newCalendar = (calendarDbItem == null);
            Dates newDates = null;
            Dates oldDates = null;
            String oldCalendarPath = calendar.getPath();
            Calendar oldCalendar = null;

            CalendarUsagesAndInstance calendarUsageInstance = new CalendarUsagesAndInstance(dbItemInventoryInstance, false);
            List<DBItemInventoryCalendarUsage> calendarUsages = null;

            if (!newCalendar) {
                calEvt.setKey("CalendarUpdated");
                try {
                    oldCalendar = mapper.readValue(calendarDbItem.getConfiguration(), Calendar.class);
                    oldCalendarPath = calendarDbItem.getName();
                    newDates = new FrequencyResolver().resolveFromToday(calendar);
                    if (!calendar.getPath().equals(oldCalendarPath)) {
                        calEvtVars.setOldPath(calendarDbItem.getName());
                        updateJobOrderScheduleIsNecessary = true;
                        updateCalendarUsageIsNecessary = true; // calendar.getType() == CalendarType.WORKING_DAYS;
                    } else {
                        oldDates = new FrequencyResolver().resolveFromToday(oldCalendar);
                        updateJobOrderScheduleIsNecessary = (!newDates.getDates().equals(oldDates.getDates()));
                    }
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

            if (updateCalendarUsageIsNecessary) {
                if (calendarUsages == null) {
                    calendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
                }
                if (!calendarUsages.isEmpty()) {
                    try {
                        calendarDbLayer.getSession().beginTransaction();
                        for (DBItemInventoryCalendarUsage item : calendarUsages) {
                            Calendar c = mapper.readValue(item.getConfiguration(), Calendar.class);
                            c.setBasedOn(calendar.getPath());
                            item.setConfiguration(mapper.writeValueAsString(c));
                            calendarUsageDbLayer.updateCalendarUsage(item);
                            eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(item.getPath(), item.getObjectType(), "CalendarUsageUpdated"));
                        }
                        calendarDbLayer.getSession().commit();
                    } catch (JocException e) {
                        calendarDbLayer.getSession().rollback();
                        throw e;
                    } catch (Exception e) {
                        calendarDbLayer.getSession().rollback();
                        throw e;
                    }
                }
            }

            if (updateJobOrderScheduleIsNecessary) {
                // update calendar in jobs, orders and schedules
                if (calendarUsages == null) {
                    calendarUsages = calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId());
                }
                if (!calendarUsages.isEmpty()) {
                    calendarUsageInstance.setCalendarUsages(calendarUsages);
                    calendarUsageInstance.setCalendarPath(calendar.getPath());
                    calendarUsageInstance.setOldCalendarPath(oldCalendarPath);
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