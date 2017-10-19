package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsDeleteResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.CalendarInRuntimes;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.BulkError;
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
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarsFilter, accessToken, "", getPermissonsJocCockpit(accessToken)
                    .getCalendar().getEdit().isDelete());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(calendarsFilter.getAuditLog());
            if (calendarsFilter.getCalendarIds() == null || calendarsFilter.getCalendarIds().size() == 0) {
                if (calendarsFilter.getCalendars() == null || calendarsFilter.getCalendars().size() == 0) {
                    throw new JocMissingRequiredParameterException("undefined 'calendarIds' parameter");
                }
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);
            AuditParams auditParams = calendarsFilter.getAuditLog();

            if (calendarsFilter.getCalendarIds() == null || calendarsFilter.getCalendarIds().size() == 0) {
                for (String calendarPath : new HashSet<String>(calendarsFilter.getCalendars())) {
                    executeDeleteCalendar(calendarDbLayer.getCalendar(calendarPath), auditParams, calendarDbLayer, accessToken);
                }
            } else {
                for (Long calendarId : new HashSet<Long>(calendarsFilter.getCalendarIds())) {
                    executeDeleteCalendar(calendarDbLayer.getCalendar(calendarId), auditParams, calendarDbLayer, accessToken);
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

    private void executeDeleteCalendar(DBItemCalendar calendarDbItem, AuditParams auditParams, CalendarsDBLayer calendarDbLayer, String accessToken) {
        if (calendarDbItem != null) {
            String calendarPath = calendarDbItem.getName();
            Long calendarId = calendarDbItem.getId();
            try {
                ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendarId, calendarPath, auditParams);
                logAuditMessage(calendarAudit);
                calendarDbLayer.deleteCalendar(calendarDbItem);
                Map<String, Exception> exceptions = CalendarInRuntimes.delete(calendarId, calendarDbLayer.getSession(), accessToken);
                
                for (Entry<String, Exception> exception : exceptions.entrySet()) {
                    if (exception.getValue() instanceof JocException) {
                        listOfErrors.add(new BulkError().get((JocException) exception.getValue(), getJocError(), exception.getKey())); 
                    } else {
                        listOfErrors.add(new BulkError().get(exception.getValue(), getJocError(), exception.getKey()));
                    }
                }
                
                storeAuditLogEntry(calendarAudit);
            } catch (JocException e) {
                listOfErrors.add(new BulkError().get(e, getJocError(), calendarPath));
            } catch (Exception e) {
                listOfErrors.add(new BulkError().get(e, getJocError(), calendarPath));
            }
        }
    }

}