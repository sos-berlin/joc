package com.sos.joc.calendar.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarDatesResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarFilter;
import com.sos.joc.model.calendar.Dates;
import com.sos.joc.model.calendar.Frequencies;

@Path("calendar")
public class CalendarDatesResourceImpl extends JOCResourceImpl implements ICalendarDatesResource {

    private static final String API_CALL = "./calendar/dates";

    @Override
    public JOCDefaultResponse postCalendarDates(String accessToken, CalendarFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, "", getPermissonsJocCockpit(
                    accessToken).getCalendar().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("calendar path", calendarFilter.getCalendar());
            checkLazyRequiredParameter("calendar category", calendarFilter.getCategory());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            String calendarPath = normalizePath(calendarFilter.getCalendar());
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            DBItemCalendar calendarItem = dbLayer.getCalendar(calendarPath, calendarFilter.getCategory());
            if (calendarItem == null) {
                throw new DBMissingDataException(String.format("calendar '%1$s'.'%2$s' not found", calendarPath, calendarFilter.getCategory()));
            }
            Calendar calendar = new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class);
            Set<String> dates = null;
            Frequencies includes = calendar.getIncludes();
            if (includes != null) {
                dates = new HashSet<String>();
                dates.addAll(includes.getDates());
                //TODO ...dates.add()
                Frequencies excludes = calendar.getExcludes();
                if (excludes != null) {
                    dates.removeAll(excludes.getDates());
                    //TODO ...dates.remove()
                }
            }
            
            Dates entity = new Dates();
            entity.setPath(calendarPath);
            entity.setName(calendarItem.getName());
            entity.setTitle(calendarItem.getTitle());
            entity.setCategory(calendarItem.getCategory());
            if (dates != null) {
                entity.setDates(new ArrayList<String>(dates)); 
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
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