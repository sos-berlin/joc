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
import com.sos.joc.jobstreams.resource.IResetJobStreamResource;
import com.sos.joc.model.jobstreams.JobStreamsFilter;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class ResetJobStreamImpl extends JOCResourceImpl implements IResetJobStreamResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetJobStreamImpl.class);
    private static final String API_CALL = "./conditions/resetjobstreams";

    @Override
    public JOCDefaultResponse resetJobStream(String accessToken, byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, JobStreamsFilter.class);
            JobStreamsFilter resetJobStream = Globals.objectMapper.readValue(filterBytes, JobStreamsFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, resetJobStream, accessToken, resetJobStream.getJobschedulerId(),
                    getPermissonsJocCockpit(resetJobStream.getJobschedulerId(), accessToken).getJobStream().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("session", resetJobStream.getSession());
            checkRequiredParameter("job", resetJobStream.getJob());

            try {
                notifyEventHandler(accessToken, resetJobStream);
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Reset Job Stream: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Job Stream not resetted");
                return JOCDefaultResponse.responseStatusJSError(e, getJocError());

            }

            return JOCDefaultResponse.responseStatus200(resetJobStream);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

        }
    }

    private String notifyEventHandler(String accessToken, JobStreamsFilter resetJobStream) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(ResetJobStreamImpl.class.getName());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("job", resetJobStream.getJob());
        parameters.put("session", resetJobStream.getSession());

        customEventsUtil.addEvent("ResetConditionResolver", parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        return jocXmlCommand.executePost(notifyCommand, accessToken);
    }

}