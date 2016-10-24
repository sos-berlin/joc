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
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceConfiguration;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.job.JobConfigurationFilter;
import com.sos.scheduler.model.commands.JSCmdShowJob;

@Path("job")
public class JobResourceConfigurationImpl extends JOCResourceImpl implements IJobResourceConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceConfigurationImpl.class);
    private static final String API_CALL = "./job/configuration";

    @Override
    public JOCDefaultResponse postJobConfiguration(String accessToken, JobConfigurationFilter jobBody) throws Exception {

        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("job", jobBody.getJob())) {
                boolean responseInHtml = jobBody.getMime() == ConfigurationMime.HTML;
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createJobConfigurationPostCommand(jobBody), "/spooler/answer/job", "job", responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobBody));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobBody));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

    private String createJobConfigurationPostCommand(final JobConfigurationFilter body) {
        JSCmdShowJob showJob = new JSCmdShowJob(Globals.schedulerObjectFactory);
        showJob.setWhat("source");
        showJob.setJob(normalizePath(body.getJob()));
        showJob.setMaxOrders(BigInteger.valueOf(0));
        showJob.setMaxTaskHistory(BigInteger.valueOf(0));
        return Globals.schedulerObjectFactory.toXMLString(showJob);
    }
}