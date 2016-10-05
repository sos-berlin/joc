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
    
}
