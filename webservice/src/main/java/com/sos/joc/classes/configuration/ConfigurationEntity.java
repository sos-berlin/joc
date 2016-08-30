package com.sos.joc.classes.configuration;

import java.util.Date;

import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.common.Content;

public class ConfigurationEntity {

    public ConfigurationSchema getEntity(){

        ConfigurationSchema entity = new ConfigurationSchema();

        entity.setDeliveryDate(new Date());
        Configuration configuration = new Configuration();
        configuration.setConfigurationDate(new Date());
        Content content = new Content();
        content.setHtml("<html></html>");
        content.setXml("myXml");
        configuration.setContent(content);
        configuration.setPath("myPath");
        configuration.setSurveyDate(new Date());
        configuration.setType(Configuration.Type.order);
        entity.setConfiguration(configuration);
        return entity;
    }

}
