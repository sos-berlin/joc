package com.sos.joc.classes.configuration;

import java.io.InputStream;
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

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.common.Content;
import com.sos.joc.model.order.OrderConfigurationFilterSchema;

public class ConfigurationUtils {

    public static ConfigurationSchema getEntity(){
        //only for dummy values
        ConfigurationSchema entity = new ConfigurationSchema();

        entity.setDeliveryDate(new Date());
        Configuration configuration = new Configuration();
        configuration.setConfigurationDate(new Date());
        Content content = new Content();
        //content.setHtml("<html></html>");
        content.setXml("<order><myXml/></order>");
        configuration.setContent(content);
        configuration.setPath("myPath");
        configuration.setSurveyDate(new Date());
        configuration.setType(Configuration.Type.ORDER);
        entity.setConfiguration(configuration);
        return entity;
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
        return writer.toString();
    }
    
    public static Content getContent(boolean responseInHtml, String configurationXml) throws Exception {
        Content content = new Content();
        if(responseInHtml) {
            InputStream inputStream = JOCResourceImpl.class.getResourceAsStream("/show_configuration.xsl");
            content.setHtml(transformXmlToHtml("<source>" + configurationXml + "</source>", inputStream));
        } else {
            content.setXml(configurationXml);
        }
        return content;
    }
    
    public static ConfigurationSchema getConfigurationSchema(JOCXmlCommand jocXmlCommand, String postCommand, String xPathObjElement, String objName,
            int mime) throws Exception {
        jocXmlCommand.excutePost(postCommand);
        Configuration configuration = new Configuration();
        configuration.setSurveyDate(jocXmlCommand.getSurveyDate());
        Element objElem = (Element) jocXmlCommand.getSosxml().selectSingleNode(xPathObjElement);
        Element fileBased = (Element) jocXmlCommand.getSosxml().selectSingleNode(objElem, "file_based");
        if (fileBased != null) {
            configuration.setConfigurationDate(JobSchedulerDate.getDate(fileBased.getAttribute("last_write_time")));
        }
        configuration.setPath(objElem.getAttribute("path"));
        configuration.setType(Configuration.Type.fromValue(objName.replaceAll("_", "").toUpperCase()));
        boolean responseInHtml = (mime == OrderConfigurationFilterSchema.Mime.HTML.ordinal());
        Content content = getContent(responseInHtml, getSourceXmlString(jocXmlCommand.getSosxml().selectSingleNode(objElem, "source/"+objName)));
        configuration.setContent(content);
        ConfigurationSchema entity = new ConfigurationSchema();
        entity.setConfiguration(configuration);
        entity.setDeliveryDate(new Date());
        return entity;
    }
}