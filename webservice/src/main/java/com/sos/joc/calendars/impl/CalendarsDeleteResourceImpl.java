package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsDeleteResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
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
            if (calendarsFilter.getCalendars() == null || calendarsFilter.getCalendars().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'calendars' parameter");
            }
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            AuditParams auditParams = calendarsFilter.getAuditLog();
            Date surveyDate = Date.from(Instant.now());
            for (String calendar : new HashSet<String>(calendarsFilter.getCalendars())) {
                surveyDate = executeDeleteCalendar(calendar, auditParams, dbLayer);
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
    
    private Date executeDeleteCalendar(String calendar, AuditParams auditParams, CalendarsDBLayer dbLayer) {
        try {
            ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendar, auditParams);
            logAuditMessage(calendarAudit);
            dbLayer.deleteCalendar(calendar);
            storeAuditLogEntry(calendarAudit);
            return Date.from(Instant.now());
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), calendar));
        }
        return null;
    }

}