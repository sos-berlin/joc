package com.sos.joc.job.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceConfiguration;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.job.JobConfigurationFilter;

@Path("job")
public class JobResourceConfigurationImpl extends JOCResourceImpl implements IJobResourceConfiguration {

    private static final String API_CALL = "./job/configuration";

    @Override
    public JOCDefaultResponse postJobConfiguration(String accessToken, JobConfigurationFilter jobBody) throws Exception {

        try {
            initLogging(API_CALL, jobBody);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("job", jobBody.getJob())) {
                boolean responseInHtml = jobBody.getMime() == ConfigurationMime.HTML;
                String jobCommand = jocXmlCommand.getShowJobCommand(normalizePath(jobBody.getJob()), "source", 0, 0);
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, jobCommand, "/spooler/answer/job",
                        "job", responseInHtml, accessToken);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}