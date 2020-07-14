package com.sos.joc.jobstreams.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobstreams.resource.IStartConditionResolverResource;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class StartConditionResolverImpl extends JOCResourceImpl implements IStartConditionResolverResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartConditionResolverImpl.class);
    private static final String API_CALL = "./conditions/startConditionResolver";

    @Override
    public JOCDefaultResponse startConditionResolver(String accessToken, byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, JobSchedulerId.class);
            JobSchedulerId jobschedulerId = Globals.objectMapper.readValue(filterBytes, JobSchedulerId.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobschedulerId, accessToken, jobschedulerId.getJobschedulerId(),
                    getPermissonsJocCockpit(null, accessToken).getJobStream().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            try {
                notifyEventHandler(accessToken);
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Start Condition Resolver: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Jobstream not started");
                JOCDefaultResponse.responseStatusJSError(e, getJocError());
            }

            return JOCDefaultResponse.responseStatusJSOk(null);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

        }
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(StartConditionResolverImpl.class.getName());
        customEventsUtil.addEvent("StartConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }

}