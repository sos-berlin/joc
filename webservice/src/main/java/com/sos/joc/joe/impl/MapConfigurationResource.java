package com.sos.joc.joe.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import com.sos.joc.joe.resource.IMapConfigurationResource;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.Monitor;
import com.sos.joc.model.joe.jobChain.JobChain;
import com.sos.joc.model.joe.lock.Lock;
import com.sos.joc.model.joe.nodeParams.Config;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.processClass.ProcessClass;
import com.sos.joc.model.joe.schedule.HolidaysFile;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joc.model.joe.schedule.Schedule;

@Path("joe")
public class MapConfigurationResource extends JOCResourceImpl implements IMapConfigurationResource {

    private static final String API_CALL_JSON = "./joe/tojson";
    private static final String API_CALL_XML = "./joe/toxml";
    private static final Map<String, Class<?>> CLASS_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<?>>() {

        private static final long serialVersionUID = 1L;

        {
            put("job", Job.class);
            put("order", Order.class);
            put("job_chain", JobChain.class);
            put("process_class", ProcessClass.class);
            put("lock", Lock.class);
            put("schedule", Schedule.class);
            put("monitor", Monitor.class);
            put("settings", Config.class);
            put("holidays", HolidaysFile.class);
            put("run_time", RunTime.class);
        }
    });

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

            if (!CLASS_MAPPING.containsKey(rootElementName)) {
                throw new JobSchedulerBadRequestException("unsupported xml");
            }
            if (!"settings".equals(rootElementName)) {
                ValidateXML.validateAgainstJobSchedulerSchema(new ByteArrayInputStream(requestBody));
            }

            // final byte[] bytes = Globals.objectMapper.writeValueAsBytes(Globals.xmlMapper.readValue(requestBody, CLASS_MAPPING.get(rootElementName)));
            // set runtime as xml string for job and order

            IJSObject obj = null;
            if ("job".equals(rootElementName)) {
                Job job = Globals.xmlMapper.readValue(requestBody, Job.class);
                if (job.getRunTimeJson() != null) {
                    job.setRunTime(Globals.objectMapper.writeValueAsString(job.getRunTimeJson()));
                    job.setRunTimeJson(null);
                }
                obj = job;
            } else if ("order".equals(rootElementName)) {
                Order order = Globals.xmlMapper.readValue(requestBody, Order.class);
                if (order.getRunTimeJson() != null) {
                    order.setRunTime(Globals.objectMapper.writeValueAsString(order.getRunTimeJson()));
                    order.setRunTimeJson(null);
                }
                obj = order;
            } else {
                obj = (IJSObject) Globals.xmlMapper.readValue(requestBody, CLASS_MAPPING.get(rootElementName));
            }
            final byte[] bytes = Globals.objectMapper.writeValueAsBytes(obj);

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
    public JOCDefaultResponse toXML(final String accessToken, final byte[] requestBody) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_XML, null, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            // final byte[] bytes = Globals.xmlMapper.writeValueAsBytes(Globals.objectMapper.readValue(requestBody, IJSObject.class));
            // read runtime as xml string for job and order
            IJSObject obj = Globals.objectMapper.readValue(requestBody, IJSObject.class);
            if (obj instanceof Job) {
                Job job = (Job) obj;
                if (job.getRunTime() != null && !job.getRunTime().isEmpty()) {
                    job.setRunTimeJson(Globals.objectMapper.readValue(job.getRunTime(), RunTime.class));
                    job.setRunTime(null);
                } else {
                    job.setRunTimeJson(new RunTime());
                }
                obj = job;
            } else if (obj instanceof Order) {
                Order order = (Order) obj;
                if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
                    order.setRunTimeJson(Globals.objectMapper.readValue(order.getRunTime(), RunTime.class));
                    order.setRunTime(null);
                } else {
                    order.setRunTimeJson(new RunTime());
                }
                obj = order;
            }

            final byte[] bytes = Globals.xmlMapper.writeValueAsBytes(obj);

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
