package com.sos.joc.classes;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;

import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.common.ConfigurationStatusSchema;

import sos.xml.SOSXMLXPath;

public class ConfigurationStatusTest {
    
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
