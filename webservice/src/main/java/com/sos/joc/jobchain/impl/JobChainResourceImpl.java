package com.sos.joc.jobchain.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JOCXmlJobChainCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResource;
import com.sos.joc.model.jobChain.JobChainV200;
import com.sos.joc.model.jobChain.JobChainFilter;

@Path("job_chain")
public class JobChainResourceImpl extends JOCResourceImpl implements IJobChainResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobChain(String accessToken, JobChainFilter jobChainFilter) throws Exception {
        LOGGER.debug("init job_chain");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobChainFilter.getJobschedulerId(), getPermissons(accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JobChainV200 entity = new JobChainV200();
            JOCXmlJobChainCommand jocXmlCommand = new JOCXmlJobChainCommand(dbItemInventoryInstance.getCommandUrl(), dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("jobChain", jobChainFilter.getJobChain())) {
                entity.setDeliveryDate(new Date());
                entity.setJobChain(jocXmlCommand.getJobChain(jobChainFilter.getJobChain(), jobChainFilter.getCompact()));
                entity.setNestedJobChains(jocXmlCommand.getNestedJobChains());
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}