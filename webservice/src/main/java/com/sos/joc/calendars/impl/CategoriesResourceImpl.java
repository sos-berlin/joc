package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICategoriesResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Categories;

@Path("calendars")
public class CategoriesResourceImpl extends JOCResourceImpl implements ICategoriesResource {

    private static final String API_CALL = "./calendars/categories";

    @Override
    public JOCDefaultResponse postCategories(String accessToken) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, "", getPermissonsJocCockpit(
                    accessToken).getCalendar().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Categories entity = new Categories();
            entity.setCategories(new CalendarsDBLayer(connection).getCategories());
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