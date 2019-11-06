package com.sos.joc.calendar.impl;

import javax.ws.rs.Path;

import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarDatesResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendars.CalendarResolver;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.CalendarDatesFilter;
import com.sos.joc.model.calendar.Dates;

@Path("calendar")
public class CalendarDatesResourceImpl extends JOCResourceImpl implements ICalendarDatesResource {

    private static final String API_CALL = "./calendar/dates";

    @Override
    public JOCDefaultResponse postCalendarDates(String accessToken, CalendarDatesFilter calendarFilter)  {
        SOSHibernateSession sosHibernateSession = null;
        Dates dates = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, calendarFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(calendarFilter.getJobschedulerId(), accessToken).getCalendar().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            CalendarResolver calendarResolver = new CalendarResolver();
            dates = calendarResolver.getCalendarDates(sosHibernateSession, calendarFilter);

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