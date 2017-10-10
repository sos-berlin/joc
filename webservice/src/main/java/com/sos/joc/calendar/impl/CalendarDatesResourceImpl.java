package com.sos.joc.calendar.impl;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarDatesResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarDatesFilter;

@Path("calendar")
public class CalendarDatesResourceImpl extends JOCResourceImpl implements ICalendarDatesResource {

    private static final String API_CALL = "./calendar/dates";

    @Override
    public JOCDefaultResponse postCalendarDates(String accessToken, CalendarDatesFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            // TODO permissions for runtime and calendar
            // getPermissonsJocCockpit(accessToken).getCalendar().isView()
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            boolean calendarIdIsDefined = calendarFilter.getId() != null;
            boolean calendarPathIsDefined = calendarFilter.getPath() != null && !calendarFilter.getPath().isEmpty();
            if (!calendarIdIsDefined && !calendarPathIsDefined && calendarFilter.getCalendar() == null) {
                throw new JocMissingRequiredParameterException("'calendar' or 'id' parameter is required");
            }
            
            if (calendarIdIsDefined) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
                DBItemCalendar calendarItem = dbLayer.getCalendar(calendarFilter.getId());
                if (calendarItem == null) {
                    throw new DBMissingDataException(String.format("calendar with id '%1$d' not found", calendarFilter.getId()));
                }
                calendarFilter.setCalendar(new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class));
            } else if (calendarPathIsDefined) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
                String calendarPath = normalizePath(calendarFilter.getPath());
                DBItemCalendar calendarItem = dbLayer.getCalendar(calendarPath);
                if (calendarItem == null) {
                    throw new DBMissingDataException(String.format("calendar '%1$s' not found", calendarPath));
                }
                calendarFilter.setCalendar(new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class));
            }

            return JOCDefaultResponse.responseStatus200(new FrequencyResolver().resolve(calendarFilter));
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