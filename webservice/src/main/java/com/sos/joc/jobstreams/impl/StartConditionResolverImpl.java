package com.sos.joc.jobstreams.impl;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobstreams.resource.IStartConditionResolverResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.StartResolver;

@Path("conditions")
public class StartConditionResolverImpl extends JOCResourceImpl implements IStartConditionResolverResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartConditionResolverImpl.class);
    private static final String API_CALL = "./conditions/startConditionResolver";

    @Override
    public JOCDefaultResponse startConditionResolver(String accessToken, StartResolver startResolver) throws Exception {
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, startResolver, accessToken, startResolver.getJobschedulerId(), getPermissonsJocCockpit(
                    null, accessToken).getJobStreams().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            notifyEventHandler(accessToken);

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