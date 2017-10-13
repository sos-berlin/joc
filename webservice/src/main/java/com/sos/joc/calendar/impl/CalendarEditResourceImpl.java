package com.sos.joc.calendar.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarEditResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.CalendarInRuntimes;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarObjectFilter;
import com.sos.joc.model.calendar.CalendarRenameFilter;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Dates;

@Path("calendar")
public class CalendarEditResourceImpl extends JOCResourceImpl implements ICalendarEditResource {

    private static final String API_CALL_STORE = "./calendar/store";
    private static final String API_CALL_MOVE = "./calendar/rename";

    @Override
    public JOCDefaultResponse postStoreCalendar(String accessToken, CalendarObjectFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_STORE, calendarFilter, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
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
            if (calendar.getType() == null || calendar.getType().name().isEmpty()) {
                calendar.setType(CalendarType.WORKING_DAYS);
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_STORE);
            calendar.setPath(normalizePath(calendar.getPath()));
            CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);

            DBItemCalendar calendarDbItem = calendarDbLayer.getCalendar(calendar.getPath());
            if ((calendarDbItem == null && !getPermissonsJocCockpit(accessToken).getCalendar().getEdit().isCreate()) || (calendarDbItem != null
                    && !getPermissonsJocCockpit(accessToken).getCalendar().getEdit().isChange())) {
                return accessDeniedResponse();
            }
            
            ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar.getId(), calendar.getPath(), calendarFilter.getAuditLog());
            logAuditMessage(calendarAudit);
            
            boolean calendarHasChanged = true;
            //FrequencyResolver fr = new FrequencyResolver();
            Dates newDates = null;
            Dates oldDates = null;
            if (calendarDbItem != null) {
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

            Date surveyDate = calendarDbLayer.saveOrUpdateCalendar(calendarDbItem, calendar);
            storeAuditLogEntry(calendarAudit);
            
            if (calendarHasChanged) {
                CalendarInRuntimes.update(calendarDbItem, newDates, connection, accessToken);
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
    public JOCDefaultResponse postRenameCalendar(String accessToken, CalendarRenameFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_MOVE, calendarFilter, accessToken, "", getPermissonsJocCockpit(accessToken)
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
            Date surveyDate = new CalendarsDBLayer(connection).renameCalendar(calendarPath, calendarNewPath);
            storeAuditLogEntry(calendarAudit);

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

}