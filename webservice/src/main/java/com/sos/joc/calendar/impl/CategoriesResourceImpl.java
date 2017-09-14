package com.sos.joc.calendar.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICategoriesResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.CalendarFilter;
import com.sos.joc.model.calendar.Categories;

@Path("calendar")
public class CategoriesResourceImpl extends JOCResourceImpl implements ICategoriesResource {

    private static final String API_CALL = "./calendar/categories";

    @Override
    public JOCDefaultResponse postCategories(String accessToken, CalendarFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, "", getPermissonsJocCockpit(
                    accessToken).getCalendar().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("calendar", calendarFilter.getCalendar());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            String calendarPath = normalizePath(calendarFilter.getCalendar());
            Categories entity = new Categories();
            entity.setPath(calendarPath);
            entity.setName(Paths.get(calendarPath).getFileName().toString());
            entity.setCategories(new CalendarsDBLayer(connection).getCategories(calendarPath));
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