package com.sos.joc.jobstreams.impl;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.jobstreams.db.DBLayerInConditions;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobstreams.resource.IEditInConditionsResource;
import com.sos.joc.model.jobstreams.InConditions;
import com.sos.joc.exceptions.JocException;
 
@Path("jobstreams/edit")
public class EditInConditionsImpl extends JOCResourceImpl implements IEditInConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditInConditionsImpl.class);
    private static final String API_CALL = "./conditions/edit/in_condition";

    @Override
    public JOCDefaultResponse editJobInConditions(String accessToken, InConditions inConditions) throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, inConditions, accessToken, inConditions.getJobschedulerId(), getPermissonsJocCockpit(
                    inConditions.getJobschedulerId(), accessToken).getJobStream().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            sosHibernateSession.setAutoCommit(false);
            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerInConditions.deleteInsert(inConditions);
            sosHibernateSession.commit();
            notifyEventHandler(accessToken);

            return JOCDefaultResponse.responseStatus200(inConditions);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            sosHibernateSession.rollback();
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(EditInConditionsImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
    }
}