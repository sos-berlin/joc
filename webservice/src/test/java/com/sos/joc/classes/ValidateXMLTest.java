package com.sos.joc.classes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.model.job.JobsV;
import com.sos.joc.model.processClass.Configuration;
import com.sos.joc.model.processClass.ModifyProcessClass;

public class ValidateXMLTest {
    
    
    @Test
    public void runTimeTest() throws Exception{
        StringBuffer s = new StringBuffer();
        s.append("<run_time begin=\"00:00\" time_zone=\"Europe/Berlin\"/>");
        boolean check = false;
        try {
            check = ValidateXML.validateRunTimeAgainstJobSchedulerSchema(s.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(check);
    }
    
    @Test
    public void xml2jsonTestWithArraysOfObjects() throws JsonProcessingException, IOException {
        //String xml = "<JobsV><deliveryDate>2018-01-30T11:31:01.755Z</deliveryDate><jobs><jobs><surveyDate>2018-01-30T11:31:01.737Z</surveyDate><path>/test/echo2</path><name>echo2</name><allSteps>0</allSteps><state><severity>1</severity><_text>PENDING</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><runTimeIsTemporary>false</runTimeIsTemporary></jobs><jobs><surveyDate>2018-01-30T11:31:01.737Z</surveyDate><path>/test/echo1</path><name>echo1</name><allSteps>0</allSteps><state><severity>1</severity><_text>PENDING</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><runTimeIsTemporary>false</runTimeIsTemporary></jobs><jobs><surveyDate>2018-01-30T11:31:01.737Z</surveyDate><path>/test/joc93</path><name>joc93</name><allSteps>0</allSteps><state><severity>2</severity><_text>STOPPED</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><ordersSummary><pending>0</pending><running>0</running><suspended>0</suspended><setback>0</setback><waitingForResource>0</waitingForResource></ordersSummary><runTimeIsTemporary>false</runTimeIsTemporary></jobs><jobs><surveyDate>2018-01-30T11:31:01.737Z</surveyDate><path>/test/yade</path><name>yade</name><allSteps>0</allSteps><state><severity>1</severity><_text>PENDING</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><params><params><name>profile</name><value>testSFTP</value></params><params><name>settings</name><value>C:\\Users\\oh\\Documents\\XMLEditor.3.0.8\\xmls\\https.xml</value></params></params><runTimeIsTemporary>false</runTimeIsTemporary></jobs><jobs><surveyDate>2018-01-30T11:31:01.737Z</surveyDate><path>/test/echo</path><name>echo</name><allSteps>0</allSteps><state><severity>1</severity><_text>PENDING</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><runTimeIsTemporary>false</runTimeIsTemporary></jobs></jobs></JobsV>";
        String xml = "<JobsV><deliveryDate>2018-01-30T12:31:01.755Z</deliveryDate><jobs><job><surveyDate>2018-01-30T12:31:01.737Z</surveyDate><path>/test/echo2</path><name>echo2</name><allSteps>0</allSteps><state><severity>1</severity><_text>PENDING</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><runTimeIsTemporary>false</runTimeIsTemporary></job><job><surveyDate>2018-01-30T12:31:01.737Z</surveyDate><path>/test/echo1</path><name>echo1</name><allSteps>0</allSteps><state><severity>1</severity><_text>PENDING</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><runTimeIsTemporary>false</runTimeIsTemporary></job><job><surveyDate>2018-01-30T12:31:01.737Z</surveyDate><path>/test/joc93</path><name>joc93</name><allSteps>0</allSteps><state><severity>2</severity><_text>STOPPED</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><ordersSummary><pending>0</pending><running>0</running><suspended>0</suspended><setback>0</setback><waitingForResource>0</waitingForResource></ordersSummary><runTimeIsTemporary>false</runTimeIsTemporary></job><job><surveyDate>2018-01-30T12:31:01.737Z</surveyDate><path>/test/yade</path><name>yade</name><allSteps>0</allSteps><state><severity>1</severity><_text>PENDING</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><params><param><name>profile</name><value>testSFTP</value></param><param><name>settings</name><value>C:\\Users\\oh\\Documents\\XMLEditor.3.0.8\\xmls\\https.xml</value></param></params><runTimeIsTemporary>false</runTimeIsTemporary></job><job><surveyDate>2018-01-30T12:31:01.737Z</surveyDate><path>/test/echo</path><name>echo</name><allSteps>0</allSteps><state><severity>1</severity><_text>PENDING</_text></state><stateText></stateText><numOfRunningTasks>0</numOfRunningTasks><numOfQueuedTasks>0</numOfQueuedTasks><runTimeIsTemporary>false</runTimeIsTemporary></job></jobs></JobsV>";
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        xmlMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        xmlMapper.disable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        xmlMapper.disable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
        //xmlMapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
        xmlMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'"));
        //JsonNode node = xmlMapper.readTree(xml.getBytes()); //doesn't work with array of objects
        JobsV jobs = xmlMapper.readValue(xml.getBytes(), JobsV.class);
        
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        jsonMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
        jsonMapper.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
        jsonMapper.disable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
//        jsonMapper.enable(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS);
        jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'"));
        
//        JobsV jobs = jsonMapper.convertValue(node, JobsV.class);
//        jobs.setDeliveryDate(null);
        String json = jsonMapper.writeValueAsString(jobs);
        System.out.println(json);
    }
    
    @Test
    public void json2xmlTestWithArraysOfObjects() throws JsonParseException, JsonMappingException, IOException {
        String json = "{\"deliveryDate\":\"2018-01-30T11:31:01.755Z\",\"jobs\":[{\"allSteps\":0,\"name\":\"echo2\",\"numOfQueuedTasks\":0,\"numOfRunningTasks\":0,\"path\":\"/test/echo2\",\"runTimeIsTemporary\":false,\"state\":{\"_text\":\"PENDING\",\"severity\":1},\"stateText\":\"\",\"surveyDate\":\"2018-01-30T11:31:01.737Z\"},{\"allSteps\":0,\"name\":\"echo1\",\"numOfQueuedTasks\":0,\"numOfRunningTasks\":0,\"path\":\"/test/echo1\",\"runTimeIsTemporary\":false,\"state\":{\"_text\":\"PENDING\",\"severity\":1},\"stateText\":\"\",\"surveyDate\":\"2018-01-30T11:31:01.737Z\"},{\"allSteps\":0,\"name\":\"joc93\",\"numOfQueuedTasks\":0,\"numOfRunningTasks\":0,\"ordersSummary\":{\"pending\":0,\"running\":0,\"setback\":0,\"suspended\":0,\"waitingForResource\":0},\"path\":\"/test/joc93\",\"runTimeIsTemporary\":false,\"state\":{\"_text\":\"STOPPED\",\"severity\":2},\"stateText\":\"\",\"surveyDate\":\"2018-01-30T11:31:01.737Z\"},{\"allSteps\":0,\"name\":\"yade\",\"numOfQueuedTasks\":0,\"numOfRunningTasks\":0,\"params\":[{\"name\":\"profile\",\"value\":\"testSFTP\"},{\"name\":\"settings\",\"value\":\"C:\\\\Users\\\\oh\\\\Documents\\\\XMLEditor.3.0.8\\\\xmls\\\\https.xml\"}],\"path\":\"/test/yade\",\"runTimeIsTemporary\":false,\"state\":{\"_text\":\"PENDING\",\"severity\":1},\"stateText\":\"\",\"surveyDate\":\"2018-01-30T11:31:01.737Z\"},{\"allSteps\":0,\"name\":\"echo\",\"numOfQueuedTasks\":0,\"numOfRunningTasks\":0,\"path\":\"/test/echo\",\"runTimeIsTemporary\":false,\"state\":{\"_text\":\"PENDING\",\"severity\":1},\"stateText\":\"\",\"surveyDate\":\"2018-01-30T11:31:01.737Z\"}]}";
        
        ObjectMapper jsonMapper = new ObjectMapper();
//        jsonMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, Boolean.TRUE);
//        jsonMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, Boolean.TRUE);
//        jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'"));
        JobsV jobs = jsonMapper.readValue(json, JobsV.class);
        
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, Boolean.FALSE);
//        xmlMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, Boolean.FALSE);
//        xmlMapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, Boolean.TRUE);
        xmlMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, Boolean.FALSE);
//        xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, Boolean.FALSE);
//        xmlMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, Boolean.TRUE);
        xmlMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'"));
        System.out.println(xmlMapper.writeValueAsString(jobs));
        // curious result for array of objects: <jobs><jobs>...</jobs><jobs>...</jobs></jobs>
    }
    
    
    @Test
    public void xml2jsonTestWithAgentObject() throws JsonProcessingException, IOException {
        String xml = "<process_class  max_processes=\"10\" timeout=\"30\"><remote_schedulers select=\"first\"><remote_scheduler remote_scheduler=\"http://127.0.0.2:5000\" http_heartbeat_period=\"10\" http_heartbeat_timeout=\"15\"/><remote_scheduler remote_scheduler=\"http://127.0.0.2:5001\"/></remote_schedulers></process_class>";
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Configuration agent = xmlMapper.readValue(xml.getBytes(), Configuration.class);
        ObjectMapper jsonMapper = new ObjectMapper();
        String json = jsonMapper.writeValueAsString(agent);
        System.out.println(json);
        String expected = "{\"maxProcesses\":10,\"timeout\":30,\"remoteSchedulers\":{\"select\":\"first\",\"list\":[{\"remoteScheduler\":\"http://127.0.0.2:5000\",\"httpHeartbeatTimeout\":15,\"httpHeartbeatPeriod\":10},{\"remoteScheduler\":\"http://127.0.0.2:5001\"}]}}";
        Assert.assertEquals(expected, json);
    }
    
    @Test
    public void json2xmlTestWithAgentObject() throws JsonProcessingException, IOException {
        String json = "{\"maxProcesses\":10,\"timeout\":\"30\",\"remoteSchedulers\":{\"select\": \"first\", \"list\":[{\"remoteScheduler\":\"http://127.0.0.2:5000\",\"httpHeartbeatTimeout\":15,\"httpHeartbeatPeriod\":10},{\"remoteScheduler\":\"http://127.0.0.2:5001\"}]}}";
        ObjectMapper jsonMapper = new ObjectMapper();
        Configuration agent = jsonMapper.readValue(json, Configuration.class);
        Path p = Paths.get("/agents/test");
        agent.setName(p.getFileName().toString());
        ModifyProcessClass modifyAgent = new ModifyProcessClass();
        modifyAgent.setFolder(p.getParent().toString().replace('\\', '/'));
        modifyAgent.setProcessClass(agent);
        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(modifyAgent);
        System.out.println(xml);
        String expected = "<modify_hot_folder folder=\"/agents\"><process_class name=\"test\" max_processes=\"10\" timeout=\"30\"><remote_schedulers select=\"first\"><remote_scheduler remote_scheduler=\"http://127.0.0.2:5000\" http_heartbeat_timeout=\"15\" http_heartbeat_period=\"10\"/><remote_scheduler remote_scheduler=\"http://127.0.0.2:5001\"/></remote_schedulers></process_class></modify_hot_folder>";
        Assert.assertEquals(expected, xml);
    }
    
}
