package com.sos.joc.joe.common.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.dom4j.io.SAXReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.joe.common.resource.IMapConfigurationResource;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.order.Order;

@Path("joe/common")
public class MapConfigurationResource extends JOCResourceImpl implements IMapConfigurationResource {
    
    @Override
    public JOCDefaultResponse toJson(final String accessToken, final byte[] requestBody) {
        try {
            ValidateXML.validateAgainstJobSchedulerSchema(new ByteArrayInputStream(requestBody));
            
            SAXReader reader = new SAXReader();
            reader.setValidation(false);
            final String rootElementName = reader.read(new ByteArrayInputStream(requestBody)).getRootElement().getName();
            
            StreamingOutput streamOut = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    try {
                        switch (rootElementName.toLowerCase()) {
                        case "job":
                            output.write(writeValue(Job.class));
                            break;
                        case "order":
                            output.write(writeValue(Order.class));
                            break;
                        }
                        output.flush();
                    } finally {
                        try {
                            output.close();
                        } catch (Exception e) {
                        }
                    }
                }
                
                private byte[] writeValue(Class<?> clazz) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
                    return new ObjectMapper().writeValueAsBytes(Globals.xmlMapper.readValue(requestBody, clazz));
                }
            };
            return JOCDefaultResponse.responseStatus200(streamOut, MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            JocError err = new JocError();
            err.setMessage("KACKE");
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

}
