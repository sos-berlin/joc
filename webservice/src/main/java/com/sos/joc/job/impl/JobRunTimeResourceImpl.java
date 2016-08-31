package com.sos.joc.job.impl;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.runtime.RunTimeEntity;
import com.sos.joc.job.post.JobRunTimeBody;
import com.sos.joc.job.resource.IJobRunTimeResource;
 
@Path("job")
public class JobRunTimeResourceImpl extends JOCResourceImpl implements IJobRunTimeResource {
    private static final Logger LOGGER = Logger.getLogger(JobRunTimeResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobRunTime(String accessToken, JobRunTimeBody jobRunTimeBody) throws Exception {
        LOGGER.debug("init OrderRunTime");
        JOCDefaultResponse jocDefaultResponse = init(jobRunTimeBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            // TODO JOC Cockpit Webservice
            RunTimeEntity runTimeEntity = new RunTimeEntity();
            return JOCDefaultResponse.responseStatus200(runTimeEntity.getEntity());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }
  
}
