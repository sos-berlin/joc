package com.sos.joc.calendar.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarEditResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarRenameFilter;

@Path("calendar")
public class CalendarEditResourceImpl extends JOCResourceImpl implements ICalendarEditResource {

    private static final String API_CALL_STORE = "./calendar/store";
    private static final String API_CALL_MOVE = "./calendar/rename";

    @Override
    public JOCDefaultResponse postStoreCalendar(String accessToken, Calendar calendar) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_STORE, calendar, accessToken, "", getPermissonsJocCockpit(accessToken).getCalendar()
                    .getEdit().isChange());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            if (calendar == null) {
                throw new JocMissingRequiredParameterException("undefined 'calendar'");
            }
            checkRequiredParameter("calendar path", calendar.getPath());
            if (calendar.getType() == null || calendar.getType().name().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined 'calendar type'");
            }
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL_STORE);
            calendar.setPath(normalizePath(calendar.getPath()));
            return JOCDefaultResponse.responseStatusJSOk(new CalendarsDBLayer(connection).saveOrUpdateCalendar(calendar));
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
            checkRequiredParameter("calendar path", calendarFilter.getPath());
            checkRequiredParameter("calendar new path", calendarFilter.getNewPath());

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_MOVE);
            String calendarPath = normalizePath(calendarFilter.getPath());
            String calendarNewPath = normalizePath(calendarFilter.getNewPath());
            return JOCDefaultResponse.responseStatusJSOk(new CalendarsDBLayer(connection).renameCalendar(calendarPath, calendarNewPath));
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