package com.sos.joc.calendar.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarDatesResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendars.CalendarResolver;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.CalendarDatesFilter;
import com.sos.joc.model.calendar.Dates;
import com.sos.schema.JsonValidator;

@Path("calendar")
public class CalendarDatesResourceImpl extends JOCResourceImpl implements ICalendarDatesResource {

    private static final String API_CALL = "./calendar/dates";

    @Override
    public JOCDefaultResponse postCalendarDates(String accessToken, byte[] calendarFilterBytes)  {
        SOSHibernateSession sosHibernateSession = null;
        Dates dates = null;
        try {
            JsonValidator.validateFailFast(calendarFilterBytes, CalendarDatesFilter.class);
            CalendarDatesFilter calendarFilter = Globals.objectMapper.readValue(calendarFilterBytes, CalendarDatesFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, calendarFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(calendarFilter.getJobschedulerId(), accessToken).getCalendar().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            calendarFilter = CalendarResolver.getCalendarDatesFilter(sosHibernateSession, calendarFilter);
            if (calendarFilter.getCalendar() == null) {
                throw new JocMissingRequiredParameterException("undefined 'calendar'");
            }
            if (calendarFilter.getPath() != null) {
                checkCalendarFolderPermissions(calendarFilter.getPath());
            } else if(calendarFilter.getCalendar().getBasedOn() != null && !calendarFilter.getCalendar().getBasedOn().isEmpty()) {
                checkCalendarFolderPermissions(calendarFilter.getCalendar().getBasedOn());
            }
            dates = CalendarResolver.getCalendarDates(sosHibernateSession, calendarFilter);

            return JOCDefaultResponse.responseStatus200(dates);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

}