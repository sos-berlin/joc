package com.sos.joc.jobstreams.impl;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.eventhandlerservice.db.DBLayerOutConditions;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobstreams.resource.IEditOutConditionsResource;
import com.sos.joc.model.jobstreams.OutConditions;
import com.sos.joc.exceptions.JocException;

@Path("conditions/edit")
public class EditOutConditionsImpl extends JOCResourceImpl implements IEditOutConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditOutConditionsImpl.class);
    private static final String API_CALL = "./conditions/edit/out_condition";

    @Override
    public JOCDefaultResponse editJobOutConditions(String accessToken, OutConditions outConditions) throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, outConditions, accessToken, outConditions.getJobschedulerId(), getPermissonsJocCockpit(
                    outConditions.getJobschedulerId(), accessToken).getJobStream().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            sosHibernateSession.setAutoCommit(false);
            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerOutConditions.deleteInsert(outConditions);
            sosHibernateSession.commit();
            notifyEventHandler(accessToken);
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

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException{
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(EditOutConditionsImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
    }
}