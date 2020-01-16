package com.sos.joc.classes.jobscheduler;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dom4j.Document;
import org.dom4j.io.DocumentSource;

import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;

public class ValidateXML {

    public static boolean validateAgainstJobSchedulerSchema(Source src) throws JobSchedulerBadRequestException {

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream inputStream = null;
            try {
                inputStream = ValidateXML.class.getResourceAsStream("/scheduler.xsd");
            } catch (Exception e) {
            }
            if (inputStream != null) {
                Schema schema = factory.newSchema(new StreamSource(inputStream));
                Validator validator = schema.newValidator();
                validator.validate(src);
            }
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
        return true;
    }

    public static Document validateAgainstJobSchedulerSchema(String xml) throws JobSchedulerBadRequestException {
        
        try {
            Document doc = XMLBuilder.parse(xml);
            if (validateAgainstJobSchedulerSchema(new DocumentSource(doc))) {
                return doc;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public static Document validateAgainstJobSchedulerSchema(String xml, String expectedRootElement) throws JobSchedulerBadRequestException {

        try {
            Document doc = XMLBuilder.parse(xml);
            String rootElement = doc.getRootElement().getName();
            if (!expectedRootElement.equals(rootElement)) {
                throw new JobSchedulerBadRequestException("Expected root element is '" + expectedRootElement + "' instead of " + rootElement);
            }
            if (validateAgainstJobSchedulerSchema(new DocumentSource(doc))) {
                return doc;
            } else {
                return null;
            }
        } catch (JobSchedulerBadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }

    public static Document validateRunTimeAgainstJobSchedulerSchema(String xml) throws JobSchedulerBadRequestException {
        return validateAgainstJobSchedulerSchema(xml, "run_time");
    }

    public static Document validateScheduleAgainstJobSchedulerSchema(String xml) throws JobSchedulerBadRequestException {
        return validateAgainstJobSchedulerSchema(xml, "schedule");
    }
}
