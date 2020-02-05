package com.sos.joc.jobchain.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResourceConfiguration;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.jobChain.JobChainConfigurationFilter;
import com.sos.schema.JsonValidator;

@Path("job_chain")
public class JobChainResourceConfigurationImpl extends JOCResourceImpl implements IJobChainResourceConfiguration {

    private static final String API_CALL = "./job_chain/configuration";

    @Override
    public JOCDefaultResponse postJobChainConfiguration(String accessToken, byte[] jobChainBodyBytes) {
        try {
            JsonValidator.validateFailFast(jobChainBodyBytes, JobChainConfigurationFilter.class);
            JobChainConfigurationFilter jobChainBody = Globals.objectMapper.readValue(jobChainBodyBytes, JobChainConfigurationFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainBody, accessToken, jobChainBody.getJobschedulerId(),
                    getPermissonsJocCockpit(jobChainBody.getJobschedulerId(), accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobChain", jobChainBody.getJobChain());
            Configuration200 entity = new Configuration200();
            boolean responseInHtml = jobChainBody.getMime() == ConfigurationMime.HTML;
            if (versionIsOlderThan("1.13.1")) {
                entity.setConfiguration(getConfiguration(jobChainBody.getJobChain(), responseInHtml));
            } else {
                try {
                    entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(new JOCHotFolder(this), jobChainBody.getJobChain(),
                            JobSchedulerObjectType.JOBCHAIN, responseInHtml));
                } catch (JobSchedulerObjectNotExistException e) {
                    entity.setConfiguration(getConfiguration(jobChainBody.getJobChain(), responseInHtml));
                }
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
    
    private Configuration getConfiguration(String path, boolean responseInHtml) throws JocException {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
        String jobChainCommand = jocXmlCommand.getShowJobChainCommand(normalizePath(path), "source", 0, 0);
        return ConfigurationUtils.getConfigurationSchema(jocXmlCommand, jobChainCommand, "/spooler/answer/job_chain", "job_chain", responseInHtml,
                getAccessToken());
    }
}