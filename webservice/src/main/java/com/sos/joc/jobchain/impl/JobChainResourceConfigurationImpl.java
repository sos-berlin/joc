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
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.jobChain.JobChainConfigurationFilter;
import com.sos.scheduler.model.commands.JSCmdShowJobChain;

@Path("job_chain")
public class JobChainResourceConfigurationImpl extends JOCResourceImpl implements IJobChainResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postJobChainConfiguration(String accessToken, JobChainConfigurationFilter jobChainBody) throws Exception {

        LOGGER.debug("init job_chain/configuration");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobChainBody.getJobschedulerId(), getPermissons(accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());
            if (checkRequiredParameter("jobChain", jobChainBody.getJobChain())) {
                boolean responseInHtml = jobChainBody.getMime() == ConfigurationMime.HTML;
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createOrderConfigurationPostCommand(jobChainBody), "/spooler/answer/job_chain", "job_chain",
                        responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private String createOrderConfigurationPostCommand(JobChainConfigurationFilter body) {
        JSCmdShowJobChain showJobChain = new JSCmdShowJobChain(Globals.schedulerObjectFactory);
        showJobChain.setJobChain(normalizePath(body.getJobChain()));
        showJobChain.setWhat("source");
        showJobChain.setMaxOrderHistory(BigInteger.valueOf(0));
        showJobChain.setMaxOrders(BigInteger.valueOf(0));
        return Globals.schedulerObjectFactory.toXMLString(showJobChain);
    }

}