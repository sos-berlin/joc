package com.sos.joc.jobchain.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchain.JOCXmlJobChainCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResource;
import com.sos.joc.model.jobChain.JobChain200VSchema;
import com.sos.joc.model.jobChain.JobChainFilterSchema;

@Path("job_chain")
public class JobChainResourceImpl extends JOCResourceImpl implements IJobChainResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourceImpl.class);

    public JOCDefaultResponse postJobChain(String accessToken, JobChainFilterSchema jobChainFilterSchema) throws Exception {
        LOGGER.debug("init job_chain");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobChainFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JobChain200VSchema entity = new JobChain200VSchema();
            JOCXmlJobChainCommand jocXmlCommand = new JOCXmlJobChainCommand(dbItemInventoryInstance.getCommandUrl(), dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("jobChain", jobChainFilterSchema.getJobChain())) {
                entity.setDeliveryDate(new Date());
                entity.setJobChain(jocXmlCommand.getJobChain(jobChainFilterSchema.getJobChain(), jobChainFilterSchema.getCompact()));
            }

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}