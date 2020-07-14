package com.sos.joc.jobstreams.impl;

import java.util.HashMap;
import java.util.Map;

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
import com.sos.joc.jobstreams.resource.IIsAliveResource;
import com.sos.joc.model.jobstreams.JobStreamsFilter;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class IsAliveImpl extends JOCResourceImpl implements IIsAliveResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetJobStreamImpl.class);
    private static final String API_CALL = "./jobstreams/isalive";

    @Override
    public JOCDefaultResponse isAlive(String accessToken, byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, JobStreamsFilter.class);
            JobStreamsFilter startJob = Globals.objectMapper.readValue(filterBytes, JobStreamsFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, startJob, accessToken, startJob.getJobschedulerId(),
                    getPermissonsJocCockpit(startJob.getJobschedulerId(), accessToken).getJobStream().getChange().isConditions());            
            
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            try {
                notifyEventHandler(accessToken, startJob);
                
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "IsAlive: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Job not started");
                return JOCDefaultResponse.responseStatusJSError(e, getJocError());

            }

            return JOCDefaultResponse.responseStatus200(startJob);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

        }
    }

    private void notifyEventHandler(String accessToken, JobStreamsFilter startJob) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(ResetJobStreamImpl.class.getName());

        Map<String, String> parameters = new HashMap<String, String>();

        customEventsUtil.addEvent("IsAlive", parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();

    }

 

}