package com.sos.joc.calendar.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarUsedByResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.CalendarFilter;
import com.sos.joc.model.calendar.UsedBy200;

@Path("calendar")
public class CalendarUsedByResourceImpl extends JOCResourceImpl implements ICalendarUsedByResource {

    private static final String API_CALL = "./calendar/used";

    @Override
    public JOCDefaultResponse postUsedBy(String accessToken, CalendarFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, "", getPermissonsJocCockpit(accessToken).getCalendar()
                    .isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("calendar path", calendarFilter.getPath());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            UsedBy200 entity = new UsedBy200();
            // TODO entity.setUsedBy(new CalendarsDBLayer(connection).getUsedBy(normalizePath(calendarFilter.getPath())));
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