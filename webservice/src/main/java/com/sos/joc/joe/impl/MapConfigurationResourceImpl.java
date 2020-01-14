package com.sos.joc.joe.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.XmlDeserializer;
import com.sos.joc.joe.common.XmlSerializer;
import com.sos.joc.joe.resource.IMapConfigurationResource;

@Path("joe")
public class MapConfigurationResourceImpl extends JOCResourceImpl implements IMapConfigurationResource {

    private static final String API_CALL_JSON = "./joe/tojson";
    private static final String API_CALL_XML = "./joe/toxml";

    @Override
    public JOCDefaultResponse toJson(final String accessToken, final byte[] requestBody) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_JSON, null, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            SAXReader reader = new SAXReader();
            reader.setIncludeExternalDTDDeclarations(false);
            reader.setIncludeInternalDTDDeclarations(false);
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            reader.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            reader.setValidation(false);
            reader.setStripWhitespaceText(true);
            reader.setIgnoreComments(true);
            
            Document doc;
            try {
                doc = reader.read(new ByteArrayInputStream(requestBody));
            } catch (DocumentException e) {
                Throwable nested = e.getNestedException();
                if (nested != null && SAXParseException.class.isInstance(nested)) {
                    // On Apache, this should be thrown when disallowing DOCTYPE
                    throw new SAXException("A DOCTYPE was passed into the XML document", e);
                } else if (nested != null && IOException.class.isInstance(nested)) {
                    // XXE that points to a file that doesn't exist
                    throw new IOException("IOException occurred, XXE may still possible: " + e.getMessage(), e);
                } else {
                    throw e;
                }
            }

            final String rootElementName = doc.getRootElement().getName().toLowerCase();
            String objType = rootElementName.replaceAll("_", "").toUpperCase();
            if ("SETTINGS".equals(objType)) {
                objType = "NODEPARAMS";
            }

            if (!JOEHelper.CLASS_MAPPING.containsKey(objType)) {
                throw new JobSchedulerBadRequestException("unsupported xml: " + rootElementName);
            }
            if (!"NODEPARAMS".equals(objType)) {
                ValidateXML.validateAgainstJobSchedulerSchema(new DocumentSource(doc));
            }
            byte[] bytes = Globals.objectMapper.writeValueAsBytes(XmlDeserializer.deserialize(doc, JOEHelper.CLASS_MAPPING.get(objType)));
            return JOCDefaultResponse.responseStatus200(bytes, MediaType.APPLICATION_JSON);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse toXML(final String accessToken, final String objectType, final byte[] requestBody) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_XML, null, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            final byte[] bytes = XmlSerializer.serializeToBytes(requestBody, objectType.replaceAll("_", "").toUpperCase());
            return JOCDefaultResponse.responseStatus200(bytes, MediaType.APPLICATION_XML);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
