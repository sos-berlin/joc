package com.sos.joc.jobchains.impl;

import java.util.Date;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JobChains;
import com.sos.joc.jobchains.resource.IJobChainsResourceP;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsP;

@Path("job_chains")
public class JobChainsResourcePImpl extends JOCResourceImpl implements IJobChainsResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourcePImpl.class);  
    
  
     
    @Override
    public JOCDefaultResponse postJobChainsP(String accessToken, JobChainsFilter  jobChainsFilter) throws Exception {
        LOGGER.debug("init job_chains/p");
        JOCDefaultResponse jocDefaultResponse = init(jobChainsFilter.getJobschedulerId(),getPermissons(accessToken).getJobChain().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
            // TODO JOC Cockpit Webservice
 
            JobChainsP entity = new JobChainsP();
            entity.setDeliveryDate(new Date());
            entity.setJobChains(JobChains.getPJobChains(true));

              
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }


}
