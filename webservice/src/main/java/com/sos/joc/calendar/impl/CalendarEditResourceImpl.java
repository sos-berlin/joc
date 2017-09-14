package com.sos.joc.calendar.impl;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarEditResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarMoveFilter;

@Path("calendar")
public class CalendarEditResourceImpl extends JOCResourceImpl implements ICalendarEditResource {

    private static final String API_CALL_STORE = "./calendar/store";
    private static final String API_CALL_MOVE = "./calendar/move";

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
            checkLazyRequiredParameter("calendar category", calendar.getCategory());

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_STORE);
            String calendarPath = normalizePath(calendar.getPath());
            calendar.setName(null);
            return JOCDefaultResponse.responseStatusJSOk(new CalendarsDBLayer(connection).saveOrUpdateCalendar(calendarPath, calendar.getCategory(),
                    new ObjectMapper().writeValueAsString(calendar)));
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
    public JOCDefaultResponse postMoveCalendar(String accessToken, CalendarMoveFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_MOVE, calendarFilter, accessToken, "", getPermissonsJocCockpit(accessToken)
                    .getCalendar().getEdit().isChange());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("calendar path", calendarFilter.getCalendar());
            checkLazyRequiredParameter("calendar category", calendarFilter.getCategory());
            checkRequiredParameter("calendar new path", calendarFilter.getNewCalendar());
            checkLazyRequiredParameter("calendar new category", calendarFilter.getNewCategory());

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_MOVE);
            String calendarPath = normalizePath(calendarFilter.getCalendar());
            String calendarNewPath = normalizePath(calendarFilter.getNewCalendar());
            return JOCDefaultResponse.responseStatusJSOk(new CalendarsDBLayer(connection).moveCalendar(calendarPath, calendarFilter.getCategory(),
                    calendarNewPath, calendarFilter.getNewCategory()));
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