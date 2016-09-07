package com.sos.joc.job.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.job.resource.IJobResourceConfiguration;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.common.Content;
import com.sos.joc.model.common.FilterByMimeTypeSchema.Mime;
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
            Configuration configuration = new Configuration();
            configuration.setPath(jobConfigurationFilterSchema.getJob());
            configuration.setSurveyDate(ConfigurationUtils.getSurveyDate(jocXmlCommand));
            configuration.setType(Configuration.Type.JOB);
            configuration.setConfigurationDate(ConfigurationUtils.getConfigurationDate(jocXmlCommand));
            Content content = new Content();
            if(jobConfigurationFilterSchema.getMime().equals(JobConfigurationFilterSchema.Mime.HTML)) {
                InputStream inputStream = getClass().getResourceAsStream("/show_configuration.xsl");
                content.setHtml(ConfigurationUtils.transformXmlToHtml("<source>" + configurationXml + "</source>", inputStream));
            } else {
                content.setXml(configurationXml);
            }
            configuration.setContent(content);
            entity.setConfiguration(configuration);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}