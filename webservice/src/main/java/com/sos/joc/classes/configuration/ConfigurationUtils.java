package com.sos.joc.classes.configuration;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sos.xml.SOSXMLTransformer;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.jobs.JobsUtils;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.common.Content;
import com.sos.joc.model.job.JobConfigurationFilterSchema;
import com.sos.scheduler.model.commands.JSCmdShowJob;

public class ConfigurationUtils {

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
        configuration.setType(Configuration.Type.ORDER);
        entity.setConfiguration(configuration);
        return entity;
    }

    public static String createJobConfigurationPostCommand(final JobConfigurationFilterSchema body) {
        if (!body.getJob().isEmpty()) {
            JSCmdShowJob showJob = Globals.schedulerObjectFactory.createShowJob();
            showJob.setWhat("source");
            showJob.setJob(body.getJob());
            return Globals.schedulerObjectFactory.toXMLString(showJob);
        }
        return null;
    }

    public static String getSourceXmlString(Node sourceNode) throws Exception {
        Source source = new DOMSource(sourceNode);
        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        transformer.transform(source, result);
        return writer.toString();
    }

    public static String transformXmlToHtml(String xml, InputStream inputStream) throws Exception {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        SOSXMLTransformer.transform(xml, new StreamSource(inputStream), result);
//        TransformerFactory tFactory = TransformerFactory.newInstance();
//        Transformer transformer = tFactory.newTransformer(new StreamSource(inputStream));
//        transformer.transform(new StreamSource(new StringReader(xml)), result);
        return writer.toString();
    }
    
    public static Date getConfigurationDate(JOCXmlCommand jocXmlCommand) throws Exception {
        Node node = jocXmlCommand.getSosxml().selectSingleNode("//job/file_based");
        String lastWriteTime = ((Element)node).getAttribute("last_write_time");
        return JobSchedulerDate.getDate(lastWriteTime);
    }

    public static Date getSurveyDate(JOCXmlCommand jocXmlCommand) throws Exception {
        Node node = jocXmlCommand.getSosxml().selectSingleNode("//answer");
        String time = ((Element)node).getAttribute("time");
        return JobSchedulerDate.getDate(time);
    }

}
