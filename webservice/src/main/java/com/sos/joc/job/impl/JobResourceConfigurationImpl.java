package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationEntity;
import com.sos.joc.job.resource.IJobResourceConfiguration;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.common.Content;
import com.sos.joc.model.job.JobConfigurationFilterSchema;

@Path("job")
public class JobResourceConfigurationImpl extends JOCResourceImpl implements IJobResourceConfiguration {
    private static final Logger LOGGER =  LoggerFactory.getLogger(JobResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postJobConfiguration(String accessToken, JobConfigurationFilterSchema jobConfigurationFilterSchema) throws Exception {

        LOGGER.debug("init Jobs Configuration");
        JOCDefaultResponse jocDefaultResponse = init(jobConfigurationFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            // TODO No Configuration200Schema available, which schema to use?
            ConfigurationSchema entity = new ConfigurationSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            entity.setDeliveryDate(new Date());
            Configuration configuration = new Configuration();
            
            configuration.setConfigurationDate(new Date());
            Content content = new Content();
            content.setHtml("<html></html>");
            content.setXml("myXml");
            configuration.setContent(content);
            configuration.setPath("myPath");
            configuration.setSurveyDate(new Date());
            configuration.setType(Configuration.Type.ORDER);

            ConfigurationEntity configurationEntity = new ConfigurationEntity();
            return JOCDefaultResponse.responseStatus200(configurationEntity.getEntity());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
