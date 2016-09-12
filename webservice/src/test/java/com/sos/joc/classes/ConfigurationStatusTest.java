package com.sos.joc.classes;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;

import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.common.ConfigurationStatusSchema;

import sos.xml.SOSXMLXPath;

public class ConfigurationStatusTest {
    
//    private Function<JsonValue, String[]> path = json -> {
//        String[] s = new String[2];
//        s[0] = ((JsonObject) json).getString("path");
//        s[1] = "test";
//        return s;
//    };
//    
//    private Function<JsonValue, OrderFilterSchema> requestOrder = json -> {
//        OrderFilterSchema o = new OrderFilterSchema();
//        String path = ((JsonObject) json).getString("path");
//        String[] pathParts = path.split(",", 2);
//        o.setJobChain(pathParts[0]);
//        o.setOrderId(pathParts[1]);
//        return o;
//    };
//    
//    private static Consumer<Map.Entry<String[],OrderFilterSchema>> printit = order -> {
//        System.out.println(order.getValue());
//    };
    
    @Test
    public void getConfigurationStatusFromOrderTest() throws Exception{
        StringBuffer s = new StringBuffer();
        s.append("<order>");
        s.append("<file_based state=\"not_initialized\">");
        s.append("<requisites/>");
        s.append("</file_based>");
        s.append("</order>");
        Element element = new SOSXMLXPath(s).getRoot();
        ConfigurationStatusSchema confStatus = ConfigurationStatus.getConfigurationStatus(element);
        Assert.assertEquals(new ConfigurationStatusSchema(), confStatus);
    }
    
//    @Test
//    public void getTest() {
//        
//        String s = "[{\"sourceType\":\"Permanent\",\"path\":\"/webservice/setback,1\",\"processingState\":{\"at\":\"2016-09-04T08:00:00Z\",\"TYPE\":\"Planned\"},\"nextStepAt\":\"2016-09-04T08:00:00Z\",\"fileBasedState\":\"active\",\"obstacles\":[],\"nodeId\":\"start\"},{\"sourceType\":\"Permanent\",\"path\":\"/webservice/setback,2\",\"processingState\":{\"TYPE\":\"NotPlanned\"},\"fileBasedState\":\"active\",\"obstacles\":[],\"nodeId\":\"start\"}]";
//        JsonReader rdr = Json.createReader(new StringReader(s));
//        JsonArray a = rdr.readArray();
//        Map<String[], OrderFilterSchema> orders = (Map<String[], OrderFilterSchema>) a.stream().collect( Collectors.toMap(path, requestOrder));
//        orders.entrySet().stream().forEach(printit);
//         
//    }
//    
//    @Test
//    public void usedNodesTest() {
//        
//        String s = "[{\"errorNodeId\":\"Error\",\"nextNodeId\":\"Job04\",\"nodeKey\":{\"jobChainPath\":\"/examples/16_SplitAndSync/01_SpiltAndSync\",\"nodeId\":\"Sync\"},\"orderCount\":3,\"jobPath\":\"/examples/16_SplitAndSync/01_SyncJobExecution\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"err\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/logging/test\",\"nodeId\":\"100\"},\"orderCount\":1,\"jobPath\":\"/logging/copyFile\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"\",\"nextNodeId\":\"fourth\",\"nodeKey\":{\"jobChainPath\":\"/rotring/BMSOS\",\"nodeId\":\"third\"},\"orderCount\":1,\"jobPath\":\"/rotring/log_hostname\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"end\",\"nodeKey\":{\"jobChainPath\":\"/sos/events/scheduler_event_service\",\"nodeId\":\"start\"},\"orderCount\":1,\"jobPath\":\"/sos/events/scheduler_event_service\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/sos/notification/CheckHistory\",\"nodeId\":\"check\"},\"orderCount\":1,\"jobPath\":\"/sos/notification/CheckHistory\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/sos/notification/CleanupNotifications\",\"nodeId\":\"cleanup\"},\"orderCount\":1,\"jobPath\":\"/sos/notification/CleanupNotifications\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/sos/notification/ResetNotifications\",\"nodeId\":\"reset\"},\"orderCount\":1,\"jobPath\":\"/sos/notification/ResetNotifications\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/sos/notification/SystemNotifier\",\"nodeId\":\"notifier\"},\"orderCount\":1,\"jobPath\":\"/sos/notification/SystemNotifier\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/sos/operations/criticalpath/UncriticalJobNodes\",\"nodeId\":\"start\"},\"orderCount\":2,\"jobPath\":\"/sos/operations/criticalpath/UncriticalJobNodes\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/sos/reporting/Inventory\",\"nodeId\":\"inventory\"},\"orderCount\":1,\"jobPath\":\"/sos/reporting/Inventory\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"aggregation\",\"nodeKey\":{\"jobChainPath\":\"/sos/reporting/Reporting\",\"nodeId\":\"facts\"},\"orderCount\":1,\"jobPath\":\"/sos/reporting/ReportingFacts\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/test/checkEvents\",\"nodeId\":\"check\"},\"orderCount\":1,\"jobPath\":\"/test/checkEvents2\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"\",\"nextNodeId\":\"\",\"nodeKey\":{\"jobChainPath\":\"/test/checkRun\",\"nodeId\":\"error\"},\"orderCount\":2,\"jobPath\":\"/scheduler_file_order_sink\",\"action\":\"stop\",\"TYPE\":\"Sink\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/test/job_chain1\",\"nodeId\":\"start\"},\"orderCount\":1,\"jobPath\":\"/test/job1\",\"action\":\"stop\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"200\",\"nodeKey\":{\"jobChainPath\":\"/test/job_chain3\",\"nodeId\":\"100\"},\"orderCount\":1,\"jobPath\":\"/test/changeExitCodeInMonitor\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"read\",\"nodeKey\":{\"jobChainPath\":\"/test/readwriteparams\",\"nodeId\":\"set\"},\"orderCount\":2,\"jobPath\":\"/test/setParam\",\"action\":\"stop\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"200\",\"nodeKey\":{\"jobChainPath\":\"/tests/functions/functions\",\"nodeId\":\"100\"},\"orderCount\":1,\"jobPath\":\"/tests/functions/spooler_init\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"sync_tests\",\"nodeKey\":{\"jobChainPath\":\"/tests/globals/run_test_suite\",\"nodeId\":\"run_test_suite\"},\"orderCount\":1,\"jobPath\":\"/tests/globals/run_test_suite\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/webservice/setback\",\"nodeId\":\"start\"},\"orderCount\":2,\"jobPath\":\"/webservice/sleep\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/webservice/suspend\",\"nodeId\":\"start\"},\"orderCount\":2,\"jobPath\":\"/webservice/sleep\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"}],\"usedJobs\":[{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/examples/16_SplitAndSync/01_SyncJobExecution\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/logging/copyFile\",\"state\":\"pending\",\"defaultProcessClassPath\":\"/logging/agent4447-oh\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/rotring/log_hostname\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/scheduler_file_order_sink\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/sos/events/scheduler_event_service\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/sos/notification/CheckHistory\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/sos/notification/CleanupNotifications\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/sos/notification/ResetNotifications\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/sos/notification/SystemNotifier\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/sos/operations/criticalpath/UncriticalJobNodes\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/sos/reporting/Inventory\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/sos/reporting/ReportingFacts\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/test/changeExitCodeInMonitor\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/test/checkEvents2\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":false,\"usedTaskCount\":0,\"path\":\"/test/job1\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[{\"plannedAt\":\"2016-09-07T11:23:00Z\",\"TYPE\":\"NoRuntime\"}],\"taskLimit\":6},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/test/setParam\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/tests/functions/spooler_init\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/tests/globals/run_test_suite\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":1},{\"isInPeriod\":true,\"usedTaskCount\":0,\"path\":\"/webservice/sleep\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[],\"taskLimit\":2}]";
//        JsonReader rdr = Json.createReader(new StringReader(s));
//        JsonArray a = rdr.readArray();
//        long start = Instant.now().toEpochMilli();
//        for (int i=0; i<1000000; i++) {
//        UsedNodes usedNodes = new UsedNodes();
//        usedNodes.addEntries(a);
//        }
//        long end = Instant.now().toEpochMilli();
//        long duration = end - start;
//        System.out.println(duration+"");      
//        
//    }
    
}
