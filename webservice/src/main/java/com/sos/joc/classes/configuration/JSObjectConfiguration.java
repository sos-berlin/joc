package com.sos.joc.classes.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration200;

public class JSObjectConfiguration {
    private String accessToken;
    private String configuration;

    public JSObjectConfiguration(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    public Configuration200 getOrderConfiguration(JOCResourceImpl jocResourceImpl, String jobChain, String orderId, boolean responseInHtml) throws JocException {
        Configuration200 entity = new Configuration200();
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
        String orderCommand = jocXmlCommand.getShowOrderCommand(jocResourceImpl.normalizePath(jobChain), orderId, "source");
        entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, orderCommand, "/spooler/answer/order", "order", responseInHtml, accessToken);
        configuration = entity.getConfiguration().getContent().getXml();
        return entity;
    }

    public Configuration200 getJobConfiguration(JOCResourceImpl jocResourceImpl, String job, boolean responseInHtml) throws JocException {
        Configuration200 entity = new Configuration200();
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jocResourceImpl);
        String jobCommand = jocXmlCommand.getShowJobCommand(jocResourceImpl.normalizePath(job), "source", 0, 0);
        entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, jobCommand, "/spooler/answer/job", "job", responseInHtml, accessToken);
        return entity;
    }

    public String changeRuntimeElement(String newRunTime) {
        Matcher m = Pattern.compile("<run_time.*>.*<\\/run_time>",Pattern.DOTALL).matcher("");
        String newConfiguration = m.reset(configuration).replaceAll(newRunTime);
         
        if (newConfiguration.equals(configuration)) {
            newConfiguration = newConfiguration.replaceAll("<run_time.*\\/>", newRunTime);
        }
        configuration = newConfiguration;
        return newConfiguration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

}
