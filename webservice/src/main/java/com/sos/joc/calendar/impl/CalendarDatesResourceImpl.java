package com.sos.joc.calendar.impl;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarDatesResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarDatesFilter;
import com.sos.joc.model.calendar.Dates;

@Path("calendar")
public class CalendarDatesResourceImpl extends JOCResourceImpl implements ICalendarDatesResource {

    private static final String API_CALL = "./calendar/dates";

    @Override
    public JOCDefaultResponse postCalendarDates(String accessToken, CalendarDatesFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, calendarFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getCalendar().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            boolean calendarIdIsDefined = calendarFilter.getId() != null;
            boolean calendarPathIsDefined = calendarFilter.getPath() != null && !calendarFilter.getPath().isEmpty();
            if (!calendarIdIsDefined && !calendarPathIsDefined && calendarFilter.getCalendar() == null) {
                throw new JocMissingRequiredParameterException("'calendar' or 'path' parameter is required");
            }
            Dates dates = null;
            FrequencyResolver fr = new FrequencyResolver();

            if (calendarPathIsDefined || calendarIdIsDefined) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
                DBItemCalendar calendarItem = null;
                if (calendarPathIsDefined) {
                    String calendarPath = normalizePath(calendarFilter.getPath());
                    calendarItem = dbLayer.getCalendar(dbItemInventoryInstance.getId(), calendarPath);
                    if (calendarItem == null) {
                        throw new DBMissingDataException(String.format("calendar '%1$s' not found", calendarPath));
                    }
                } else {
                    calendarItem = dbLayer.getCalendar(calendarFilter.getId());
                    if (calendarItem == null) {
                        throw new DBMissingDataException(String.format("calendar with id '%1$d' not found", calendarFilter.getId()));
                    }
                }
                calendarFilter.setCalendar(new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class));

            }

            try {
                if (calendarFilter.getCalendar().getBasedOn() != null && !calendarFilter.getCalendar().getBasedOn().isEmpty()) {
                    connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                    CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
                    String calendarPath = normalizePath(calendarFilter.getCalendar().getBasedOn());
                    DBItemCalendar calendarItem = dbLayer.getCalendar(dbItemInventoryInstance.getId(), calendarPath);
                    if (calendarItem == null) {
                        throw new DBMissingDataException(String.format("calendar '%1$s' not found", calendarPath));
                    }
                    Calendar basedCalendar = new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class);
                    if (calendarFilter.getDateFrom() == null || calendarFilter.getDateFrom().isEmpty()) {
                        calendarFilter.setDateFrom(fr.getToday());
                    }
                    dates = fr.resolveRestrictions(basedCalendar, calendarFilter.getCalendar(), calendarFilter.getDateFrom(), calendarFilter.getDateTo());
                } else {
                    dates = fr.resolve(calendarFilter);
                }
            } catch (SOSInvalidDataException e) {
                throw new JobSchedulerInvalidResponseDataException(e);
            } catch (SOSMissingDataException e) {
                throw new JocMissingRequiredParameterException(e);
            }
            return JOCDefaultResponse.responseStatus200(dates);
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