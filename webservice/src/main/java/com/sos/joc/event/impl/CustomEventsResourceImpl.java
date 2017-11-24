package com.sos.joc.event.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.event.resource.ICustomEventsResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.event.custom.Event;
import com.sos.joc.model.event.custom.Events;
import com.sos.joc.model.event.custom.EventsFilter;

@Path("events")
public class CustomEventsResourceImpl extends JOCResourceImpl implements ICustomEventsResource {

    private static final String API_CALL = "./events/custom/";
    
    @Override
    public JOCDefaultResponse postCustomEvents(String accessToken, EventsFilter eventFilter) {
        
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, eventFilter, accessToken, eventFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            
            //TODO fill list of events
            List<Event> events = new ArrayList<Event>();
            
            Events entity = new Events();
            entity.setEvents(events);
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
