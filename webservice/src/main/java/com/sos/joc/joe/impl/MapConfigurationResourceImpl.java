package com.sos.joc.joe.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.dom4j.io.SAXReader;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
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
            reader.setValidation(false);
            final String rootElementName = reader.read(new ByteArrayInputStream(requestBody)).getRootElement().getName().toLowerCase();
            String objType = rootElementName.replaceAll("_", "").toUpperCase();
            if ("SETTINGS".equals(objType)) {
                objType = "NODEPARAMS";
            }

            if (!Helper.CLASS_MAPPING.containsKey(objType)) {
                throw new JobSchedulerBadRequestException("unsupported xml: " + rootElementName);
            }
            if (!"settings".equals(objType)) {
                ValidateXML.validateAgainstJobSchedulerSchema(new ByteArrayInputStream(requestBody));
            }

            final byte[] bytes = Globals.objectMapper.writeValueAsBytes(Globals.xmlMapper.readValue(requestBody, Helper.CLASS_MAPPING.get(objType)));
            // set runtime as xml string for job and order

//            IJSObject obj = null;
//            if ("job".equals(objType)) {
//                Job job = Globals.xmlMapper.readValue(requestBody, Job.class);
//                if (job.getRunTimeJson() != null) {
//                    job.setRunTime(Globals.objectMapper.writeValueAsString(job.getRunTimeJson()));
//                    job.setRunTimeJson(null);
//                }
//                obj = job;
//            } else if ("order".equals(objType)) {
//                Order order = Globals.xmlMapper.readValue(requestBody, Order.class);
//                if (order.getRunTimeJson() != null) {
//                    order.setRunTime(Globals.objectMapper.writeValueAsString(order.getRunTimeJson()));
//                    order.setRunTimeJson(null);
//                }
//                obj = order;
//            } else {
//                obj = (IJSObject) Globals.xmlMapper.readValue(requestBody, CLASS_MAPPING.get(objType));
//            }
//            final byte[] bytes = Globals.objectMapper.writeValueAsBytes(obj);

            StreamingOutput streamOut = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    try {
                        output.write(bytes);
                        output.flush();
                    } finally {
                        try {
                            output.close();
                        } catch (Exception e) {
                        }
                    }
                }
            };
            return JOCDefaultResponse.responseStatus200(streamOut, MediaType.APPLICATION_JSON);
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

            String objType = objectType.replaceAll("_", "").toUpperCase();
            
            if (!Helper.CLASS_MAPPING.containsKey(objType)) {
                throw new JobSchedulerBadRequestException("unsupported json object: " + objectType);
            }
            
            final byte[] bytes = Globals.xmlMapper.writeValueAsBytes(Globals.objectMapper.readValue(requestBody, Helper.CLASS_MAPPING.get(objType)));
            // read runtime as xml string for job and order
            
//            IJSObject obj = null;
//            if ("job".equals(objType)) {
//                Job job = Globals.objectMapper.readValue(requestBody, Job.class);
//                if (job.getRunTime() != null && !job.getRunTime().isEmpty()) {
//                    job.setRunTimeJson(Globals.objectMapper.readValue(job.getRunTime(), RunTime.class));
//                    job.setRunTime(null);
//                } else if (job.getRunTimeJson() == null) {
//                    job.setRunTimeJson(new RunTime());
//                }
//                obj = job;
//            } else if ("order".equals(objType)) {
//                Order order = Globals.objectMapper.readValue(requestBody, Order.class);
//                if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
//                    order.setRunTimeJson(Globals.objectMapper.readValue(order.getRunTime(), RunTime.class));
//                    order.setRunTime(null);
//                } else if (order.getRunTimeJson() == null) {
//                    order.setRunTimeJson(new RunTime());
//                }
//                obj = order;
//            } else {
//                obj = (IJSObject) Globals.objectMapper.readValue(requestBody, CLASS_MAPPING.get(objType));
//            }
//
//            final byte[] bytes = Globals.xmlMapper.writeValueAsBytes(obj);

            StreamingOutput streamOut = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    try {
                        output.write(bytes);
                        output.flush();
                    } finally {
                        try {
                            output.close();
                        } catch (Exception e) {
                        }
                    }
                }
            };
            return JOCDefaultResponse.responseStatus200(streamOut, MediaType.APPLICATION_XML);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
