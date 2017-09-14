package com.sos.joc.calendar.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendar200;
import com.sos.joc.model.calendar.CalendarFilter;

@Path("calendar")
public class CalendarResourceImpl extends JOCResourceImpl implements ICalendarResource {

    private static final String API_CALL = "./calendar";

    @Override
    public JOCDefaultResponse postCalendar(String accessToken, CalendarFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, "", getPermissonsJocCockpit(
                    accessToken).getCalendar().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("calendar", calendarFilter.getCalendar());
            if (calendarFilter.getCategory() == null) {
                throw new JocMissingRequiredParameterException("undefined 'category'");
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            String calendarPath = normalizePath(calendarFilter.getCalendar());
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            DBItemCalendar calendarItem = dbLayer.getCalendar(calendarPath, calendarFilter.getCategory());
            Calendar calendar = new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class);
            Calendar200 entity = new Calendar200();
            calendar.setPath(calendarPath);
            calendar.setCategory(calendarFilter.getCategory());
            entity.setCalendar(calendar);
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