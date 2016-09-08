package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.job.resource.IJobResourceConfiguration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.common.Content;
import com.sos.joc.model.job.JobConfigurationFilterSchema;

@Path("job")
public class JobResourceConfigurationImpl extends JOCResourceImpl implements IJobResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postJobConfiguration(String accessToken, JobConfigurationFilterSchema jobConfigurationFilterSchema) throws Exception {

        LOGGER.debug("init Jobs Configuration");
        JOCDefaultResponse jocDefaultResponse =
                init(jobConfigurationFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            ConfigurationSchema entity = new ConfigurationSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            entity.setDeliveryDate(new Date());
            String postCommand = ConfigurationUtils.createJobConfigurationPostCommand(jobConfigurationFilterSchema);
            jocXmlCommand.excutePost(postCommand);
            Node jobNode = jocXmlCommand.getSosxml().selectSingleNode("//job");
            Node sourceNode = jocXmlCommand.getSosxml().selectSingleNode("//job/source/job");
            String configurationXml = ConfigurationUtils.getSourceXmlString(sourceNode);
            LOGGER.debug(configurationXml);
            Content content = ConfigurationUtils.getContent(jobConfigurationFilterSchema, getClass(), configurationXml);
            entity.setConfiguration(ConfigurationUtils.getConfiguration(jobConfigurationFilterSchema, jocXmlCommand, content));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}