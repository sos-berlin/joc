package com.sos.joc.classes.jobscheduler;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.sos.joc.Globals;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;

public class ValidateXML {

    public static boolean validateAgainstJobSchedulerSchema(String xml) throws JobSchedulerBadRequestException {

        if (Globals.jobSchedulerSchema != null) {
            try {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(Globals.jobSchedulerSchema);
                Validator schemaValidator = schema.newValidator();
                schemaValidator.validate(new StreamSource(new StringReader(xml)));
            } catch (Exception e) {
                throw new JobSchedulerBadRequestException(e);
            }
        }
        return true;
    }
    
    public static boolean validateAgainstJobSchedulerSchema(InputStream xml) throws JobSchedulerBadRequestException {

        if (Globals.jobSchedulerSchema != null) {
            try {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(Globals.jobSchedulerSchema);
                Validator schemaValidator = schema.newValidator();
                schemaValidator.validate(new StreamSource(xml));
            } catch (Exception e) {
                throw new JobSchedulerBadRequestException(e);
            }
        }
        return true;
    }
    
    public static boolean validateAgainstJobSchedulerSchema(Reader xml) throws JobSchedulerBadRequestException {

        if (Globals.jobSchedulerSchema != null) {
            try {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(Globals.jobSchedulerSchema);
                Validator schemaValidator = schema.newValidator();
                schemaValidator.validate(new StreamSource(xml));
            } catch (Exception e) {
                throw new JobSchedulerBadRequestException(e);
            }
        }
        return true;
    }

    public static boolean validateAgainstJobSchedulerSchema(String xml, String expectedRootElement) throws JobSchedulerBadRequestException {
        String rootElement = "";
        try {
            rootElement = XMLBuilder.getRootElementName(xml);
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
        if (!expectedRootElement.equals(rootElement)) {
            throw new JobSchedulerBadRequestException("Expected root element is '" + expectedRootElement + "' instead of " + rootElement);
        }

        return validateAgainstJobSchedulerSchema(xml);
    }

    public static boolean validateRunTimeAgainstJobSchedulerSchema(String xml) throws JobSchedulerBadRequestException {
        return validateAgainstJobSchedulerSchema(xml, "run_time");
    }

    public static boolean validateScheduleAgainstJobSchedulerSchema(String xml) throws JobSchedulerBadRequestException {
        return validateAgainstJobSchedulerSchema(xml, "schedule");
    }
}
