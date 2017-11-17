package com.sos.joc.calendar.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarEditResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.classes.calendar.JobSchedulerCalendarCallable;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsagesAndInstance;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
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
import com.sos.joc.model.common.Err419;

@Path("calendar")
public class CalendarEditResourceImpl extends JOCResourceImpl implements ICalendarEditResource {

    private static final String API_CALL_STORE = "./calendar/store";
    private static final String API_CALL_SAVE_AS = "./calendar/save_as";
    private static final String API_CALL_MOVE = "./calendar/rename";

    @Override
    public JOCDefaultResponse postStoreCalendar(String accessToken, CalendarObjectFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_STORE, calendarFilter, accessToken, calendarFilter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Calendar calendar = checkRequirements(calendarFilter);

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_STORE);
            CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);
            
            DBItemCalendar calendarDbItem = null;
            if (calendar.getId() != null) {
                calendarDbItem = calendarDbLayer.getCalendar(calendar.getId());
            } 
            
            if ((calendarDbItem == null && !getPermissonsJocCockpit(accessToken).getCalendar().getEdit().isCreate()) || (calendarDbItem != null
                    && !getPermissonsJocCockpit(accessToken).getCalendar().getEdit().isChange())) {
                return accessDeniedResponse();
            }
            
            ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar.getId(), calendar.getPath(), calendarFilter.getAuditLog());
            logAuditMessage(calendarAudit);
            
            CalendarEvent calEvt = new CalendarEvent();
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendar.getPath());
            
            boolean calendarHasChanged = true;
            Dates newDates = null;
            Dates oldDates = null;
            if (calendarDbItem != null) {
                calEvt.setKey("CalendarUpdated");
                if (!calendar.getPath().equals(calendarDbItem.getName())) {
                    calEvtVars.setOldPath(calendarDbItem.getName());
                }
                try {
                    newDates = new FrequencyResolver().resolveFromToday(calendar);
                    oldDates = new FrequencyResolver().resolveFromToday(new ObjectMapper().readValue(calendarDbItem.getConfiguration(), Calendar.class));
                } catch (SOSMissingDataException e) {
                    throw new JocMissingRequiredParameterException(e);
                } catch (SOSInvalidDataException e) {
                    throw new JobSchedulerInvalidResponseDataException(e);
                }
                if (newDates.getDates().equals(oldDates.getDates())) {
                    calendarHasChanged = false;
                } 
            }
            
            if (calendarDbItem == null) {
                calEvt.setKey("CalendarCreated");
                DBItemCalendar oldCalendarDbItem = calendarDbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath());
                if (oldCalendarDbItem != null) {
                    throw new JocObjectAlreadyExistException(calendar.getPath());
                }
            }

            Date surveyDate = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), calendarDbItem, calendar);
            storeAuditLogEntry(calendarAudit);
            calEvt.setVariables(calEvtVars);
            
            List<Err419> listOfErrors = new ArrayList<Err419>();
            if (calendarHasChanged) {
                sendEvent(calEvt, accessToken);
                
                if (calendarDbItem != null) {
                    //update calendar in jobs, orders and schedules
                    CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);
                    CalendarUsagesAndInstance calendarUsageInstance = new CalendarUsagesAndInstance(dbItemInventoryInstance, false);
                    calendarUsageInstance.setCalendarUsages(calendarUsageDbLayer.getCalendarUsagesOfAnInstance(dbItemInventoryInstance.getId(), calendarDbItem.getId()));
                    calendarUsageInstance.setBaseCalendar(calendar);
                    calendarUsageInstance.setDates(newDates.getDates());
                    JobSchedulerCalendarCallable callable = new JobSchedulerCalendarCallable(calendarUsageInstance, accessToken);
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
                        
            if (listOfErrors.size() > 0) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
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
    public JOCDefaultResponse postSaveAsCalendar(String accessToken, CalendarObjectFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_SAVE_AS, calendarFilter, accessToken, calendarFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getCalendar().getEdit().isCreate());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Calendar calendar = checkRequirements(calendarFilter);

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_SAVE_AS);
            CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);

            ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar.getId(), calendar.getPath(), calendarFilter.getAuditLog());
            logAuditMessage(calendarAudit);

            DBItemCalendar oldCalendarDbItem = calendarDbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath());
            if (oldCalendarDbItem != null) {
                throw new JocObjectAlreadyExistException(calendar.getPath());
            }

            Date surveyDate = calendarDbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), null, calendar);
            storeAuditLogEntry(calendarAudit);
            
            CalendarEvent calEvt = new CalendarEvent();
            calEvt.setKey("CalendarCreated");
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendar.getPath());
            calEvt.setVariables(calEvtVars);
            sendEvent(calEvt, accessToken);

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
    public JOCDefaultResponse postRenameCalendar(String accessToken, CalendarRenameFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_MOVE, calendarFilter, accessToken, calendarFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getCalendar().getEdit().isChange());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(calendarFilter.getAuditLog());
            checkRequiredParameter("calendar path", calendarFilter.getPath());
            checkRequiredParameter("calendar new path", calendarFilter.getNewPath());

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_MOVE);
            String calendarPath = normalizePath(calendarFilter.getPath());
            String calendarNewPath = normalizePath(calendarFilter.getNewPath());

            ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(null, calendarPath, calendarFilter.getAuditLog());
            logAuditMessage(calendarAudit);
            
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            
            DBItemCalendar calendarNewDbItem = dbLayer.getCalendar(dbItemInventoryInstance.getId(), calendarNewPath);
            if (calendarNewDbItem != null) {
                throw new JocObjectAlreadyExistException(calendarNewPath);
            }
            Date surveyDate = new CalendarsDBLayer(connection).renameCalendar(dbItemInventoryInstance.getId(), calendarPath, calendarNewPath);
            storeAuditLogEntry(calendarAudit);
            
            CalendarEvent calEvt = new CalendarEvent();
            calEvt.setKey("CalendarUpdated");
            CalendarVariables calEvtVars = new CalendarVariables();
            calEvtVars.setPath(calendarNewPath);
            calEvtVars.setOldPath(calendarPath);
            calEvt.setVariables(calEvtVars);
            sendEvent(calEvt, accessToken);

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
    
    private Calendar checkRequirements(CalendarObjectFilter calendarFilter) throws JocMissingCommentException, JocMissingRequiredParameterException, JobSchedulerInvalidResponseDataException {
        checkRequiredComment(calendarFilter.getAuditLog());
        Calendar calendar = calendarFilter.getCalendar();
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
        return calendar;
    }
    
    private void sendEvent(CalendarEvent calEvt, String accessToken) throws JocException, JsonProcessingException {
        try {
            String xmlCommand = new ObjectMapper().writeValueAsString(calEvt);
            xmlCommand = "<publish_event>" + xmlCommand + "</publish_event>";
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            jocXmlCommand.executePostWithRetry(xmlCommand, accessToken);
        } catch (JobSchedulerConnectionRefusedException e) {
        } catch (JobSchedulerConnectionResetException e) {
        }
    }

}