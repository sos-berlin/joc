package com.sos.joc.conditions.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.conditions.resource.IResetWorkflowResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.ResetWorkflow;
import com.sos.xml.SOSXmlCommand.ResponseStream;

@Path("conditions")
public class ResetWorkflowImpl extends JOCResourceImpl implements IResetWorkflowResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetWorkflowImpl.class);
    private static final String API_CALL = "./conditions/edit/in_condition";

    @Override
    public JOCDefaultResponse resetWorkflow(String accessToken, ResetWorkflow resetWorkflow) throws Exception {
         try {
 
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, resetWorkflow, accessToken, resetWorkflow.getMasterId(), getPermissonsJocCockpit(
                    resetWorkflow.getMasterId(), accessToken).getCondition().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            notifyEventHandler(accessToken,resetWorkflow);
 
            return JOCDefaultResponse.responseStatus200(resetWorkflow);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
           
        }
    }

    private String notifyEventHandler(String accessToken, ResetWorkflow resetWorkflow) throws JsonProcessingException, JocException{
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(ResetWorkflowImpl.class.getName());
        CustomEvent customEvent = customEventsUtil.createEvent("ResetConditionResolver");
        customEvent.getVariables().setAdditionalProperty("workflow",resetWorkflow.getWorkflow());
        customEvent.getVariables().setAdditionalProperty("job",resetWorkflow.getJob());
        customEventsUtil.addEvent(customEvent);
        
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        return jocXmlCommand.executePost(notifyCommand, accessToken);
    }
 
    
}