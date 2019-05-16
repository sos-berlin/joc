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
import com.sos.joc.conditions.resource.ICheckConditionsResource;
import com.sos.joc.conditions.resource.IResetWorkflowResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.ConditionCheck;
import com.sos.joc.model.conditions.ResetWorkflow;
import com.sos.xml.SOSXmlCommand.ResponseStream;

@Path("conditions")
public class CheckConditionsImpl extends JOCResourceImpl implements ICheckConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckConditionsImpl.class);
    private static final String API_CALL = "./conditions/edit/in_condition";

    @Override
    public JOCDefaultResponse checkCondition(String accessToken, ConditionCheck conditionCheck) throws Exception {
         try {
 
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, conditionCheck, accessToken, conditionCheck.getMasterId(), getPermissonsJocCockpit(
                    conditionCheck.getMasterId(), accessToken).getCondition().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            notifyEventHandler(accessToken);
 
            return JOCDefaultResponse.responseStatus200(conditionCheck);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
           
        }
    }

    private String notifyEventHandler(String accessToken) throws JsonProcessingException, JocException{
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(CheckConditionsImpl.class.getName());
        CustomEvent customEvent = customEventsUtil.createEvent("CheckConditions");
        customEventsUtil.addEvent(customEvent);
        
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        return jocXmlCommand.executePost(notifyCommand, accessToken);
    }
 
    
}