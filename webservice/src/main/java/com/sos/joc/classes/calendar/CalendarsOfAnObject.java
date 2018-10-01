package com.sos.joc.classes.calendar;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Calendars;

public class CalendarsOfAnObject {

    public static Calendars get(SOSHibernateSession connection, String schedulerId, CalendarType type, String path, Boolean compact)
            throws JsonParseException, JsonMappingException, IOException, DBConnectionRefusedException, DBInvalidDataException {
        CalendarsDBLayer dbCalendarLayer = new CalendarsDBLayer(connection);

        List<DBItemInventoryClusterCalendar> dbCalendars = dbCalendarLayer.getCalendarsOfAnObject(schedulerId, type.name(), path);

        List<Calendar> calendarList = new ArrayList<Calendar>();
        if (dbCalendars != null) {
            ObjectMapper om = new ObjectMapper();
            for (DBItemInventoryClusterCalendar dbCalendar : dbCalendars) {
                Calendar calendar = om.readValue(dbCalendar.getConfiguration(), Calendar.class);
                calendar.setId(dbCalendar.getId());
                calendar.setPath(dbCalendar.getName());
                calendar.setName(dbCalendar.getBaseName());
                if (compact != null && compact) {
                    calendar.setIncludes(null);
                    calendar.setExcludes(null);
                }
                calendarList.add(calendar);
            }
        }
        Calendars entity = new Calendars();
        entity.setCalendars(calendarList);
        entity.setDeliveryDate(Date.from(Instant.now()));

        return entity;
    }

}
