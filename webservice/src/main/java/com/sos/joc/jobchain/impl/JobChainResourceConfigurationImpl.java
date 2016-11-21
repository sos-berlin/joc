package com.sos.joc.jobchain.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResourceConfiguration;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.jobChain.JobChainConfigurationFilter;

@Path("job_chain")
public class JobChainResourceConfigurationImpl extends JOCResourceImpl implements IJobChainResourceConfiguration {

    private static final String API_CALL = "./job_chain/configuration";

    @Override
    public JOCDefaultResponse postJobChainConfiguration(String accessToken, JobChainConfigurationFilter jobChainBody) throws Exception {
        try {
            initLogging(API_CALL, jobChainBody);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobChainBody.getJobschedulerId(), getPermissons(accessToken).getJobChain()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("jobChain", jobChainBody.getJobChain())) {
                boolean responseInHtml = jobChainBody.getMime() == ConfigurationMime.HTML;
                String jobChainCommand = jocXmlCommand.getShowJobChainCommand(normalizePath(jobChainBody.getJobChain()), "source", 0, 0);
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, jobChainCommand, "/spooler/answer/job_chain", "job_chain",
                        responseInHtml, accessToken);
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