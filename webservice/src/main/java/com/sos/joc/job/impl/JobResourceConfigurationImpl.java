package com.sos.joc.job.impl;

import java.math.BigInteger;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceConfiguration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.job.JobConfigurationFilterSchema;
import com.sos.scheduler.model.commands.JSCmdShowJob;

@Path("job")
public class JobResourceConfigurationImpl extends JOCResourceImpl implements IJobResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postJobConfiguration(String accessToken, JobConfigurationFilterSchema jobBody) throws Exception {

        LOGGER.debug("init job/configuration");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            ConfigurationSchema entity = new ConfigurationSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());
            if (checkRequiredParameter("job", jobBody.getJob())) {
                boolean responseInHtml = jobBody.getMime() == JobConfigurationFilterSchema.Mime.HTML;
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createJobConfigurationPostCommand(jobBody), "/spooler/answer/job", "job", responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private String createJobConfigurationPostCommand(final JobConfigurationFilterSchema body) {
        JSCmdShowJob showJob = new JSCmdShowJob(Globals.schedulerObjectFactory);
        showJob.setWhat("source");
        showJob.setJob(normalizePath(body.getJob()));
        showJob.setMaxOrders(BigInteger.valueOf(0));
        showJob.setMaxTaskHistory(BigInteger.valueOf(0));
        return Globals.schedulerObjectFactory.toXMLString(showJob);
    }
}