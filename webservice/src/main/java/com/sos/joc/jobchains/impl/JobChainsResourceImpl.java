package com.sos.joc.jobchains.impl;

import java.util.Date;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchain.JobChains;
import com.sos.joc.jobchains.resource.IJobChainsResource;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;
import com.sos.joc.model.jobChain.JobChainsVSchema;

@Path("job_chains")
public class JobChainsResourceImpl extends JOCResourceImpl implements IJobChainsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceImpl.class);  
    
  
     
    @Override
    public JOCDefaultResponse postJobChains(String accessToken, JobChainsFilterSchema  jobChainsFilterSchema) throws Exception {
        LOGGER.debug("init job_chains");
        JOCDefaultResponse jocDefaultResponse = init(jobChainsFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobChain().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
            // TODO JOC Cockpit Webservice
 
            JobChainsVSchema entity = new JobChainsVSchema();
            entity.setDeliveryDate(new Date());
            entity.setJobChains(JobChains.getJobChains(jobChainsFilterSchema.getCompact())); 
              
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }


}
