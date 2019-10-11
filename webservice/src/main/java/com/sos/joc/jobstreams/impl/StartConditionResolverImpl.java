package com.sos.joc.jobstreams.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobstreams.resource.IStartConditionResolverResource;
import com.sos.joc.model.jobstreams.StartResolver;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;

@Path("jobstreams")
public class StartConditionResolverImpl extends JOCResourceImpl implements IStartConditionResolverResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartConditionResolverImpl.class);
    private static final String API_CALL = "./conditions/startConditionResolver";

    @Override
    public JOCDefaultResponse startConditionResolver(String accessToken, StartResolver startResolver) throws Exception {
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, startResolver, accessToken, startResolver.getJobschedulerId(),
                    getPermissonsJocCockpit(null, accessToken).getJobStream().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            try {
                notifyEventHandler(accessToken);
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Start Condition Resolver: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Conditions not resolved");
            }

            return JOCDefaultResponse.responseStatusJSOk(null);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

        }
    }

    private String notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(StartConditionResolverImpl.class.getName());
        customEventsUtil.addEvent("StartConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        return jocXmlCommand.executePost(notifyCommand, accessToken);
    }

}