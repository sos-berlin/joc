package com.sos.joc.jobchain.impl;

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
import com.sos.joc.jobchain.resource.IJobChainResourceConfiguration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.jobChain.JobChainConfigurationFilterSchema;
import com.sos.scheduler.model.commands.JSCmdShowJobChain;

@Path("job_chain")
public class JobChainResourceConfigurationImpl extends JOCResourceImpl implements IJobChainResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postJobChainConfiguration(String accessToken, JobChainConfigurationFilterSchema jobChainBody) throws Exception {

        LOGGER.debug("init job_chain/configuration");
        JOCDefaultResponse jocDefaultResponse =
                init(jobChainBody.getJobschedulerId(), getPermissons(accessToken).getJobChain().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            ConfigurationSchema entity = new ConfigurationSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (jocXmlCommand.checkRequiredParameter("jobChain", jobChainBody.getJobChain())) {
                boolean responseInHtml = jobChainBody.getMime() == JobChainConfigurationFilterSchema.Mime.HTML;
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createOrderConfigurationPostCommand(jobChainBody), "/spooler/answer/job_chain", "job_chain", responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private String createOrderConfigurationPostCommand(JobChainConfigurationFilterSchema body) {
        JSCmdShowJobChain showJobChain = new JSCmdShowJobChain(Globals.schedulerObjectFactory);
        showJobChain.setJobChain(("/"+body.getJobChain()).replaceAll("//+", "/"));
        showJobChain.setWhat("source");
        showJobChain.setMaxOrderHistory(BigInteger.valueOf(0));
        showJobChain.setMaxOrders(BigInteger.valueOf(0));
        return Globals.schedulerObjectFactory.toXMLString(showJobChain);
    }

}