package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsDeleteResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.JobSchedulerCalendarCallable;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsagesAndInstance;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.calendar.CalendarsFilter;
import com.sos.joc.model.common.Err419;

@Path("calendars")
public class CalendarsDeleteResourceImpl extends JOCResourceImpl implements ICalendarsDeleteResource {

    private static final String API_CALL = "./calendars/delete";
    private List<Err419> listOfErrors = new ArrayList<Err419>();

    @Override
    public JOCDefaultResponse postDeleteCalendars(String accessToken, CalendarsFilter calendarsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarsFilter, accessToken, calendarsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getCalendar().getEdit().isDelete());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(calendarsFilter.getAuditLog());
            if (calendarsFilter.getCalendarIds() == null || calendarsFilter.getCalendarIds().size() == 0) {
                if (calendarsFilter.getCalendars() == null || calendarsFilter.getCalendars().size() == 0) {
                    throw new JocMissingRequiredParameterException("undefined 'calendars' parameter");
                }
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);

            if (calendarsFilter.getCalendars() == null || calendarsFilter.getCalendars().size() == 0) {
                for (Long calendarId : new HashSet<Long>(calendarsFilter.getCalendarIds())) {
                    executeDeleteCalendar(calendarDbLayer.getCalendar(calendarId), calendarsFilter, calendarDbLayer, accessToken);
                }
            } else {
                for (String calendarPath : new HashSet<String>(calendarsFilter.getCalendars())) {
                    executeDeleteCalendar(calendarDbLayer.getCalendar(dbItemInventoryInstance.getId(), calendarPath), calendarsFilter,
                            calendarDbLayer, accessToken);
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

    private void executeDeleteCalendar(DBItemCalendar calendarDbItem, CalendarsFilter calendarsFilter, CalendarsDBLayer calendarDbLayer,
            String accessToken) {
        if (calendarDbItem != null) {
            try {
                ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendarDbItem.getId(), calendarDbItem.getName(), calendarsFilter
                        .getAuditLog(), calendarsFilter.getJobschedulerId());
                logAuditMessage(calendarAudit);
                calendarDbLayer.deleteCalendar(calendarDbItem);

                CalendarEvent calEvt = new CalendarEvent();
                calEvt.setKey("CalendarDeleted");
                CalendarVariables calEvtVars = new CalendarVariables();
                calEvtVars.setPath(calendarDbItem.getName());
                calEvt.setVariables(calEvtVars);
                sendEvent(calEvt, accessToken);

                // delete calendar in jobs, orders and schedules
                CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(calendarDbLayer.getSession());
                CalendarUsagesAndInstance calendarUsageInstance = new CalendarUsagesAndInstance(dbItemInventoryInstance, false);
                calendarUsageInstance.setCalendarUsages(calendarUsageDbLayer.getCalendarUsages(calendarDbItem.getId()));
                calendarUsageInstance.setCalendarPath(calendarDbItem.getName());
                calendarUsageInstance.setOldCalendarPath(calendarDbItem.getName());

                JobSchedulerCalendarCallable callable = new JobSchedulerCalendarCallable(calendarUsageInstance, accessToken);
                CalendarUsagesAndInstance c = callable.call();
                calendarUsageDbLayer.updateEditFlag(c.getCalendarUsages(), false);

                for (Entry<String, Exception> exception : c.getExceptions().entrySet()) {
                    if (exception.getValue() instanceof JocException) {
                        listOfErrors.add(new BulkError().get((JocException) exception.getValue(), getJocError(), exception.getKey()));
                    } else {
                        listOfErrors.add(new BulkError().get(exception.getValue(), getJocError(), exception.getKey()));
                    }
                }

                storeAuditLogEntry(calendarAudit);
            } catch (JocException e) {
                listOfErrors.add(new BulkError().get(e, getJocError(), calendarDbItem.getName()));
            } catch (Exception e) {
                listOfErrors.add(new BulkError().get(e, getJocError(), calendarDbItem.getName()));
            }
        }
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