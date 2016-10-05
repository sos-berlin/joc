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
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.jobChain.JobChain____;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;
import com.sos.joc.model.jobChain.JobChainsVSchema;

@Path("job_chains")
public class JobChainsResourceImpl extends JOCResourceImpl implements IJobChainsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceImpl.class);  
    
  
     
    @Override
    public JOCDefaultResponse postJobChains(String accessToken, JobChainsFilterSchema  jobChainsFilterSchema) throws Exception {
        LOGGER.debug("init job_chains");
        
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobChainsFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobChainsVSchema entity = new JobChainsVSchema();
            JOCXmlJobChainCommand jocXmlCommand = new JOCXmlJobChainCommand(dbItemInventoryInstance.getCommandUrl(), dbItemInventoryInstance.getUrl());
            List<JobChain____> jobChains = jobChainsFilterSchema.getJobChains();
            List<FoldersSchema> folders = jobChainsFilterSchema.getFolders();
            if (jobChains != null && !jobChains.isEmpty()) {
                entity.setJobChains(jocXmlCommand.getJobChainsFromShowJobChain(jobChains, jobChainsFilterSchema));
            } else if (folders != null && !folders.isEmpty()) {
                entity.setJobChains(jocXmlCommand.getJobChainsFromShowState(folders, jobChainsFilterSchema));
            } else {
                entity.setJobChains(jocXmlCommand.getJobChainsFromShowState(jobChainsFilterSchema));
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
