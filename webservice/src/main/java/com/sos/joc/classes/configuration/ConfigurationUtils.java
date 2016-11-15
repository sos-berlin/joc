package com.sos.joc.classes.configuration;

import java.io.InputStream;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationContent;
import com.sos.joc.model.common.JobSchedulerObjectType;

import sos.xml.SOSXMLTransformer;

public class ConfigurationUtils {

    public static Configuration200 getConfigurationSchema(JOCXmlCommand jocXmlCommand, String postCommand, String xPathObjElement, String objName,
            boolean responseInHtml, String accessToken) throws JocException {
        jocXmlCommand.executePostWithThrowBadRequest(postCommand, accessToken);
        try {
            Configuration configuration = new Configuration();
            configuration.setSurveyDate(jocXmlCommand.getSurveyDate());
            Element objElem = (Element) jocXmlCommand.getSosxml().selectSingleNode(xPathObjElement);
            Element fileBased = (Element) jocXmlCommand.getSosxml().selectSingleNode(objElem, "file_based");
            if (fileBased != null) {
                configuration.setConfigurationDate(JobSchedulerDate.getDateFromISO8601String(fileBased.getAttribute("last_write_time")));
            }
            configuration.setPath(objElem.getAttribute("path"));
            configuration.setType(JobSchedulerObjectType.fromValue(objName.replaceAll("_", "").toUpperCase()));
            ConfigurationContent content = getContent(responseInHtml, getSourceXmlString(jocXmlCommand.getSosxml().selectSingleNode(objElem, "source/"
                    + objName)));
            configuration.setContent(content);
            Configuration200 entity = new Configuration200();
            entity.setConfiguration(configuration);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return entity;
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public static Configuration200 getConfigurationSchemaOfDefaultProcessClass(JOCXmlCommand jocXmlCommand, String postCommand,
            String xPathObjElement, boolean responseInHtml, String accessToken) throws JobSchedulerBadRequestException, JobSchedulerNoResponseException, JobSchedulerConnectionRefusedException {
        jocXmlCommand.executePostWithThrowBadRequest(postCommand, accessToken);
        try {
            Configuration configuration = new Configuration();
            configuration.setSurveyDate(jocXmlCommand.getSurveyDate());
            Element objElem = (Element) jocXmlCommand.getSosxml().selectSingleNode(xPathObjElement);
            configuration.setConfigurationDate(null);
            configuration.setPath("/(default)");
            configuration.setType(JobSchedulerObjectType.PROCESSCLASS);
            ConfigurationContent content = getContent(responseInHtml, "<process_class max_processes=\"" + objElem.getAttribute("max_processes") + "\"/>");
            configuration.setContent(content);
            Configuration200 entity = new Configuration200();
            entity.setConfiguration(configuration);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return entity;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public static String transformXmlToHtml(String xml, InputStream inputStream) throws JobSchedulerBadRequestException {
        StringWriter writer = new StringWriter();
        try {
            SOSXMLTransformer.transform(xml, new StreamSource(inputStream), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    private static String getSourceXmlString(Node sourceNode) throws JobSchedulerBadRequestException {
        if (sourceNode == null) {
            throw new JobSchedulerBadRequestException("No configuration found.");
        }
        StringWriter writer = new StringWriter();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            transformer.transform(new DOMSource(sourceNode), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    private static ConfigurationContent getContent(boolean responseInHtml, String configurationXml) throws JobSchedulerBadRequestException {
        ConfigurationContent content = new ConfigurationContent();
        if (responseInHtml) {
            InputStream inputStream = ConfigurationUtils.class.getResourceAsStream("/show_configuration.xsl");
            content.setHtml(transformXmlToHtml("<source>" + configurationXml + "</source>", inputStream));
        } else {
            content.setXml(configurationXml.trim());
        }
        return content;
    }
}