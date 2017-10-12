package com.sos.joc.classes.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Node;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;

import sos.xml.SOSXMLXPath;

public class JSObjectConfigurationTest {
    
    private JOCXmlCommand jocXmlCommand = new JOCXmlCommand("");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    
    
    @Test
    public void testChangeRuntimeElement() throws JocException, Exception {
        JSObjectConfiguration jsObjectConfiguration = new JSObjectConfiguration("");
        SOSXMLXPath newRuntime = new SOSXMLXPath(new StringBuffer("<run_time>xxx</run_time>"));
        
        String jobSchedulerAnswer = "<order  title=\"test\" job_chain=\"job_chain2\" id=\"2\">\r\n  <params />\r\n  <run_time>\r\n    <weekdays >\r\n      <day  day=\"1 2 3 4 5 6 7\">\r\n        <period  single_start=\"13:00:00\"/>\r\n      </day>\r\n    </weekdays>\r\n  </run_time>\r\n</order>";
        Node node = jsObjectConfiguration.modifyOrderRuntimeNode(getSosXmlXPath(jobSchedulerAnswer), newRuntime);
        String s = jocXmlCommand.getXmlString(node).replaceAll("(\\r\\n|\\n)+", "\n");
        assertEquals("testChangeRuntimeElement", "<order id=\"2\" job_chain=\"job_chain2\" title=\"test\">\n  <params/>\n  <run_time>xxx</run_time>\n</order>", s);

        jobSchedulerAnswer = "<order  title=\"test\" job_chain=\"job_chain2\" id=\"2\">\r\n  <params />\r\n  <run_time/>\r\n</order>";
        node = jsObjectConfiguration.modifyOrderRuntimeNode(getSosXmlXPath(jobSchedulerAnswer), newRuntime);
        s = jocXmlCommand.getXmlString(node).replaceAll("(\\r\\n|\\n)+", "\n");;
        assertEquals("testChangeRuntimeElement", "<order id=\"2\" job_chain=\"job_chain2\" title=\"test\">\n  <params/>\n  <run_time>xxx</run_time>\n</order>", s);
        
        jobSchedulerAnswer = "<order  title=\"test\" job_chain=\"job_chain2\" id=\"2\">\r\n  <params />\r\n  <payload/>\r\n</order>";
        node = jsObjectConfiguration.modifyOrderRuntimeNode(getSosXmlXPath(jobSchedulerAnswer), newRuntime);
        s = jocXmlCommand.getXmlString(node).replaceAll("(\\r\\n|\\n)+", "\n");;
        assertEquals("testChangeRuntimeElement", "<order id=\"2\" job_chain=\"job_chain2\" title=\"test\">\n  <params/>\n  <run_time>xxx</run_time>\n  <payload/>\n</order>", s);
        
        jobSchedulerAnswer = "<order  title=\"test\" job_chain=\"job_chain2\" id=\"2\">\r\n  <params />\r\n</order>";
        node = jsObjectConfiguration.modifyOrderRuntimeNode(getSosXmlXPath(jobSchedulerAnswer), newRuntime);
        s = jocXmlCommand.getXmlString(node).replaceAll("(\\r\\n|\\n)+", "\n");;
        assertEquals("testChangeRuntimeElement", "<order id=\"2\" job_chain=\"job_chain2\" title=\"test\">\n  <params/>\n  <run_time>xxx</run_time>\n</order>", s);

    }
    
    private SOSXMLXPath getSosXmlXPath(String xml) throws Exception {
        return new SOSXMLXPath(new StringBuffer("<source>"+xml+"</source>"));
    }

}
