package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsExportResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.calendar.CalendarsFilter;

@Path("calendars")
public class CalendarsExportResourceImpl extends JOCResourceImpl implements ICalendarsExportResource {

    private static final String API_CALL = "./calendars/export";

    @Override
    public JOCDefaultResponse exportCalendars(String accessToken, CalendarsFilter calendarsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarsFilter, accessToken, calendarsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getCalendar().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            //CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            ObjectMapper objMapper = new ObjectMapper();
            
            List<Calendar> calendarList = new ArrayList<Calendar>();
            Calendars entity = new Calendars();
            entity.setCalendars(calendarList);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(objMapper.writeValueAsString(entity), "calendars.json");
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