package com.sos.joc.classes.calendars;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.joc.Globals;
import com.sos.joc.classes.calendar.FrequencyResolver;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarDatesFilter;
import com.sos.joc.model.calendar.Dates;

public class CalendarResolver {

    public static CalendarDatesFilter getCalendarDatesFilter(SOSHibernateSession sosHibernateSession, CalendarDatesFilter calendarFilter) throws DBMissingDataException, JocMissingRequiredParameterException, DBConnectionRefusedException, DBInvalidDataException, JsonParseException, JsonMappingException, IOException, JobSchedulerInvalidResponseDataException  {
        boolean calendarIdIsDefined = calendarFilter.getId() != null;
        boolean calendarPathIsDefined = calendarFilter.getPath() != null && !calendarFilter.getPath().isEmpty();
        if (!calendarIdIsDefined && !calendarPathIsDefined) {
            throw new JocMissingRequiredParameterException("'calendar' or 'path' parameter is required");
        }

        if (calendarPathIsDefined || calendarIdIsDefined) {
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(sosHibernateSession);
            DBItemInventoryClusterCalendar calendarItem = null;
            if (calendarPathIsDefined) {
                String calendarPath = Globals.normalizePath(calendarFilter.getPath());
                calendarItem = dbLayer.getCalendar(calendarFilter.getJobschedulerId(), calendarPath);
                if (calendarItem == null) {
                    throw new DBMissingDataException(String.format("calendar '%1$s' not found", calendarPath));
                }
                calendarFilter.setId(calendarItem.getId());
            } else {
                calendarItem = dbLayer.getCalendar(calendarFilter.getId());
                if (calendarItem == null) {
                    throw new DBMissingDataException(String.format("calendar with id '%1$d' not found", calendarFilter.getId()));
                }
                calendarFilter.setPath(calendarItem.getName());
            }
            calendarFilter.setCalendar(new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class));
        }
        return calendarFilter;
    }
    
    
    public static Dates getCalendarDates(SOSHibernateSession sosHibernateSession, CalendarDatesFilter calendarFilter) throws DBMissingDataException, JocMissingRequiredParameterException, DBConnectionRefusedException, DBInvalidDataException, JsonParseException, JsonMappingException, IOException, JobSchedulerInvalidResponseDataException  {

        if (calendarFilter.getCalendar() == null) {
            throw new JocMissingRequiredParameterException("undefined 'calendar'");
        }
        
        Dates dates = null;
        FrequencyResolver fr = new FrequencyResolver();

        try {
            if (calendarFilter.getCalendar().getBasedOn() != null && !calendarFilter.getCalendar().getBasedOn().isEmpty()) {
                CalendarsDBLayer dbLayer = new CalendarsDBLayer(sosHibernateSession);
                String calendarPath = Globals.normalizePath(calendarFilter.getCalendar().getBasedOn());
                DBItemInventoryClusterCalendar calendarItem = dbLayer.getCalendar(calendarFilter.getJobschedulerId(), calendarPath);
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
        return dates;
    }
}
