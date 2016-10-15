package com.sos.joc.jobchains.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchain.JOCXmlJobChainCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchains.resource.IJobChainsResource;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsV;

@Path("job_chains")
public class JobChainsResourceImpl extends JOCResourceImpl implements IJobChainsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceImpl.class);  
    
  
     
    @Override
    public JOCDefaultResponse postJobChains(String accessToken, JobChainsFilter  jobChainsFilter) throws Exception {
        LOGGER.debug("init job_chains");
        
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobChainsFilter.getJobschedulerId(), getPermissons(accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobChainsV entity = new JobChainsV();
            JOCXmlJobChainCommand jocXmlCommand = new JOCXmlJobChainCommand(dbItemInventoryInstance.getCommandUrl(), dbItemInventoryInstance.getUrl());
            List<JobChainPath> jobChains = jobChainsFilter.getJobChains();
            List<Folder> folders = jobChainsFilter.getFolders();
            if (jobChains != null && !jobChains.isEmpty()) {
                entity.setJobChains(jocXmlCommand.getJobChainsFromShowJobChain(jobChains, jobChainsFilter));
            } else if (folders != null && !folders.isEmpty()) {
                entity.setJobChains(jocXmlCommand.getJobChainsFromShowState(folders, jobChainsFilter));
            } else {
                entity.setJobChains(jocXmlCommand.getJobChainsFromShowState(jobChainsFilter));
            }
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }


}
