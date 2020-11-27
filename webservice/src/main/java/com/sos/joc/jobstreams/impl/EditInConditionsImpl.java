package com.sos.joc.jobstreams.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.jobstreams.resource.IEditInConditionsResource;
import com.sos.joc.model.jobstreams.InConditions;
import com.sos.joc.model.jobstreams.JobInCondition;
import com.sos.schema.JsonValidator;

@Path("jobstreams/edit")
public class EditInConditionsImpl extends JOCResourceImpl implements IEditInConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditInConditionsImpl.class);
    private static final String API_CALL = "./conditions/edit/in_condition";

    @Override
    public JOCDefaultResponse editJobInConditions(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, InConditions.class);
            InConditions inConditions = Globals.objectMapper.readValue(filterBytes, InConditions.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, inConditions, accessToken, inConditions.getJobschedulerId(),
                    getPermissonsJocCockpit(inConditions.getJobschedulerId(), accessToken).getJobStream().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            for (JobInCondition jobInCondition : inConditions.getJobsInconditions()) {
                 try {
                    checkFolderPermissions(jobInCondition.getJob());
                } catch (JocFolderPermissionsException e) {
                    jobInCondition.setJob("");
                    LOGGER.debug("Folder permission for " + jobInCondition.getJob() + " is missing. Edit Inconditon " + jobInCondition.getJob() + " ignored.");
                }
            }
                

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            sosHibernateSession.setAutoCommit(false);
            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerInConditions.deleteInsert(inConditions);
            sosHibernateSession.commit();
            try {
                notifyEventHandler(accessToken);
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Edit In Conditon: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Data are stored in Database");
                return JOCDefaultResponse.responseStatusJSError(e, getJocError());                
            }
            return JOCDefaultResponse.responseStatus200(inConditions);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            try {
                sosHibernateSession.rollback();
            } catch (SOSHibernateException e) {
                //
            }
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(EditInConditionsImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }
}