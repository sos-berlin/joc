package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.calendar.CalendarsFilter;

@Path("calendars")
public class CalendarsResourceImpl extends JOCResourceImpl implements ICalendarsResource {

    private static final String API_CALL = "./calendars";

    @Override
    public JOCDefaultResponse postCalendars(String accessToken, CalendarsFilter calendarsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarsFilter, accessToken, "", getPermissonsJocCockpit(accessToken)
                    .getCalendar().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            List<DBItemCalendar> dbCalendars = null;
            if (!calendarsFilter.getCalendars().isEmpty()) {
                dbCalendars = dbLayer.getCalendars(new HashSet<String>(calendarsFilter.getCalendars()));
            } else {
                // TODO folder
                dbCalendars = dbLayer.getCalendars(calendarsFilter.getType(), new HashSet<String>(calendarsFilter
                        .getCategories()), new HashSet<String>());
            }
            List<Calendar> calendarList = new ArrayList<Calendar>();
            if (dbCalendars != null) {
               for (DBItemCalendar dbCalendar : dbCalendars) {
                   //
               }
            }
            Calendars entity = new Calendars();
            entity.setCalendars(calendarList);
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