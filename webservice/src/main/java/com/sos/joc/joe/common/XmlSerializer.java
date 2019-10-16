package com.sos.joc.joe.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.processclass.RemoteScheduler;
import com.sos.joc.model.joe.processclass.RemoteSchedulers;
import com.sos.joc.model.joe.schedule.RunTime;

public class XmlSerializer {

    private static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n\n";
    private static final List<String> objectsWithSpecialSerialization = Arrays.asList("JOB", "JOBCHAIN", "ORDER", "PROCESSCLASS");

    public static String serializeToStringWithHeader(String json, String objType) throws JsonParseException, JsonMappingException,
            JsonProcessingException, IOException, JobSchedulerBadRequestException {
        return xmlHeader + serializeToString(json, objType);
    }

    public static String serializeToString(String json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!Helper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsString(serialize(json, objType, Helper.CLASS_MAPPING.get(objType)));
    }

    public static byte[] serializeToBytes(String json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!Helper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsBytes(serialize(json, objType, Helper.CLASS_MAPPING.get(objType)));
    }

    public static byte[] serializeToBytes(byte[] json, String objType) throws JsonParseException, JsonMappingException, JsonProcessingException,
            IOException, JobSchedulerBadRequestException {
        if (!Helper.CLASS_MAPPING.containsKey(objType)) {
            throw new JobSchedulerBadRequestException("unsupported json object: " + objType);
        }
        return Globals.xmlMapper.writeValueAsBytes(serialize(json, objType, Helper.CLASS_MAPPING.get(objType)));
    }

    private static <T> T serialize(byte[] json, String objType, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException,
            JobSchedulerBadRequestException {
        if (objectsWithSpecialSerialization.contains(objType)) {
            return serialize(Globals.objectMapper.readValue(json, clazz), clazz);
        } else {
            return Globals.objectMapper.readValue(json, clazz);
        }
    }

    private static <T> T serialize(String json, String objType, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException,
            JobSchedulerBadRequestException {
        if (objectsWithSpecialSerialization.contains(objType)) {
            return serialize(Globals.objectMapper.readValue(json, clazz), clazz);
        } else {
            return Globals.objectMapper.readValue(json, clazz);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T serialize(T obj, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        switch (clazz.getSimpleName()) {
        case "Job":
            Job job = (Job) obj;
            if (job.getEnabled() != null && "true,1,yes".contains(job.getEnabled())) {
                job.setEnabled(null);
            }
            if (job.getIsOrderJob() != null && "false,0,no".contains(job.getIsOrderJob())) {
                job.setIsOrderJob(null);
            }
            if (job.getMaxTasks() != null && job.getMaxTasks() == 1) {
                job.setMaxTasks(null);
            }
            if (job.getMinTasks() != null && job.getMinTasks() == 0) {
                job.setMinTasks(null);
            }
            if (job.getForceIdleTimeout() != null && "false,0,no".contains(job.getForceIdleTimeout())) {
                job.setForceIdleTimeout(null);
            }
            if (job.getStopOnError() != null && "true,1,yes".contains(job.getStopOnError())) {
                job.setStopOnError(null);
            }
            if (job.getStderrLogLevel() != null && "info".equals(job.getStderrLogLevel())) {
                job.setStderrLogLevel(null);
            }
            if (job.getRunTime() == null) {
                job.setRunTime(new RunTime());
            }
            return (T) job;

        case "JobChain":
            JobChain jobChain = (JobChain) obj;
            if (jobChain.getOrdersRecoverable() != null && "true,1,yes".contains(jobChain.getOrdersRecoverable())) {
                jobChain.setOrdersRecoverable(null);
            }
            if (jobChain.getDistributed() != null && "false,0,no".contains(jobChain.getDistributed())) {
                jobChain.setDistributed(null);
            }
            return (T) jobChain;

        case "Order":
            Order order = (Order) obj;
            if (order.getRunTime() == null) {
                order.setRunTime(new RunTime());
            }
            return (T) order;

        case "ProcessClass":
            ProcessClass processClass = (ProcessClass) obj;
            RemoteSchedulers agents = processClass.getRemoteSchedulers();
            if (agents != null) {
                List<RemoteScheduler> agentList = agents.getRemoteSchedulerList();
                if (agentList == null || agentList.isEmpty()) {
                    processClass.setRemoteSchedulers(null);
                } else if (agents.getSelect() != null && "first".equals(agents.getSelect())) {
                    processClass.getRemoteSchedulers().setSelect(null);
                }
            }
            return (T) processClass;
        }
        return obj;
    }

}
