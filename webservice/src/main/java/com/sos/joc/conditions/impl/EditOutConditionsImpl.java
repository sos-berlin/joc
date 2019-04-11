package com.sos.joc.conditions.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.eventhandlerservice.db.DBLayerOutConditions;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.conditions.resource.IEditOutConditionsResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.OutConditions;

@Path("conditions/edit")
public class EditOutConditionsImpl extends JOCResourceImpl implements IEditOutConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditOutConditionsImpl.class);
    private static final String API_CALL = "./conditions/edit/out_condition";

    @Override
    public JOCDefaultResponse editJobOutConditions(String accessToken, OutConditions outConditions) throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, outConditions, accessToken, outConditions.getMasterId(), getPermissonsJocCockpit(
                    outConditions.getMasterId(), accessToken).getEvent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            sosHibernateSession.setAutoCommit(false);
            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerOutConditions.deleteInsert(outConditions);
            sosHibernateSession.commit();
            // Hier nun Ereignis erzeugen, damit die geänderten Daten im Service neu eingelesen werden.

            return JOCDefaultResponse.responseStatus200(outConditions);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

            Globals.disconnect(sosHibernateSession);
        }
    }

}