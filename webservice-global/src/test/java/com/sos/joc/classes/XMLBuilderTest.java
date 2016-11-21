package com.sos.joc.classes;

import static org.junit.Assert.*;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.Test;

public class XMLBuilderTest {

    @Test
    public void testShowState() {
        XMLBuilder showState = new XMLBuilder("show_state");
        showState.addAttribute("subsystems", "folder").addAttribute("what", "folders").addAttribute("path", "/");
        assertEquals("<show_state subsystems=\"folder\" what=\"folders\" path=\"/\"/>", showState.asXML());
    }
    
    @Test
    public void testShowState2() {
        String xml = XMLBuilder.create("show_state").addAttribute("subsystems", "folder").addAttribute("what", "folders").addAttribute("path", "/").asXML();
        assertEquals("<show_state subsystems=\"folder\" what=\"folders\" path=\"/\"/>", xml);
    }
    
    @Test
    public void testAddOrderWithParams() {
        XMLBuilder addOrder = new XMLBuilder("add_order");
        addOrder.addAttribute("id", "hallo").addAttribute("job_chain", "/a/b/jobChain1").addElement("params").addElement("param").addAttribute("name", "hallo").addAttribute("value", "welt").addElement("param").addAttribute("name", "hello").addAttribute("value", "world");
        assertEquals("<add_order id=\"hallo\" job_chain=\"/a/b/jobChain1\"><params><param name=\"hallo\" value=\"welt\"><param name=\"hello\" value=\"world\"/></param></params></add_order>", addOrder.asXML());
    }
    
    @Test
    public void testAddOrderWithParams2() throws DocumentException {
        Element params = XMLBuilder.create("params");
        params.addElement("param").addAttribute("name", "hallo").addAttribute("value", "welt").addElement("param").addAttribute("name", "hello").addAttribute("value", "world");
        System.out.println(params.asXML());
        XMLBuilder addOrder = new XMLBuilder("add_order");
        addOrder.addAttribute("id", "hallo").addAttribute("job_chain", "/a/b/jobChain1").add(params);
        addOrder.add(XMLBuilder.parse("<run_time/>"));
        System.out.println(addOrder.asXML());
        assertEquals("<add_order id=\"hallo\" job_chain=\"/a/b/jobChain1\"><params><param name=\"hallo\" value=\"welt\"><param name=\"hello\" value=\"world\"/></param></params><run_time/></add_order>", addOrder.asXML());
    }
}
