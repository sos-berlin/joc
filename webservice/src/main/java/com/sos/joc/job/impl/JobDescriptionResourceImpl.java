package com.sos.joc.job.impl;


import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobDescriptionResource;
import com.sos.joc.model.job.JobFilter;

@Path("job")
public class JobDescriptionResourceImpl extends JOCResourceImpl implements IJobDescriptionResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobDescriptionResourceImpl.class);
    private static final String API_CALL = "./job/description";
    
    @Override
    public JOCDefaultResponse getJobDescription(String accessToken, String jobschedulerId, String job) throws Exception {
        LOGGER.debug(API_CALL);
        JobFilter jobFilter = new JobFilter();
        
        try {
            jobFilter.setJob(job);
            jobFilter.setJobschedulerId(jobschedulerId);
           
            checkRequiredParameter("jobschedulerId", jobFilter.getJobschedulerId());
            checkRequiredParameter("job", jobFilter.getJob());

            JOCDefaultResponse jocDefaultResponse = init(jobschedulerId, getPermissons(accessToken).getJob().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            //TODO
            JocError err = new JocError();
            err.setCode("JOC-404");
            err.setMessage("The response of '"+ API_CALL + "' is not yet implemented.");
            throw new JocException(err);
            //return JOCDefaultResponse.responseHtmlStatus200("TODO");
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobFilter));
            return JOCDefaultResponse.responseHTMLStatusJSError(e);

        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobFilter));
            return JOCDefaultResponse.responseHTMLStatusJSError(e, err);
        }
    }

}
