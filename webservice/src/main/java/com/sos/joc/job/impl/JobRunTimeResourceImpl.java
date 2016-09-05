package com.sos.joc.job.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.runtime.RunTimeEntity;
import com.sos.joc.job.resource.IJobRunTimeResource;
import com.sos.joc.model.job.JobFilterSchema;
 
@Path("job")
public class JobRunTimeResourceImpl extends JOCResourceImpl implements IJobRunTimeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunTimeResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobRunTime(String accessToken, JobFilterSchema jobFilterSchema) throws Exception {
        LOGGER.debug("init OrderRunTime");
        JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
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
