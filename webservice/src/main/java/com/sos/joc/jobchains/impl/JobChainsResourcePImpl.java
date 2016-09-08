package com.sos.joc.jobchains.impl;

import java.util.Date;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchain.JobChains;
import com.sos.joc.jobchains.resource.IJobChainsResourceP;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;
import com.sos.joc.model.jobChain.JobChainsPSchema;

@Path("job_chains")
public class JobChainsResourcePImpl extends JOCResourceImpl implements IJobChainsResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourcePImpl.class);  
    
  
     
    @Override
    public JOCDefaultResponse postJobChainsP(String accessToken, JobChainsFilterSchema  jobChainsFilterSchema) throws Exception {
        LOGGER.debug("init job_chains/p");
        JOCDefaultResponse jocDefaultResponse = init(jobChainsFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobChain().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
            // TODO JOC Cockpit Webservice
 
            JobChainsPSchema entity = new JobChainsPSchema();
            entity.setDeliveryDate(new Date());
            entity.setJobChains(JobChains.getPJobChains(true));

              
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }


}
