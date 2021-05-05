package com.sos.joc.joe.impl;

import java.io.ByteArrayInputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.dom4j.Document;
import org.dom4j.io.DocumentSource;

import com.sos.joe.common.XmlDeserializer;
import com.sos.joe.common.XmlSerializer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.resource.IMapConfigurationResource;
import com.sos.xml.XMLBuilder;

 

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
            
            Document doc = XMLBuilder.parse(new ByteArrayInputStream(requestBody));

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
