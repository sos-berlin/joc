package com.sos.joc.classes;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriBuilder;

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
    
    @Test
    public void testUriBuilder() throws URISyntaxException {
//        StringBuilder s = new StringBuilder();
//        s.append("http://oh:40411").append("/jobscheduler/master/api/order");
        //UriBuilder uriBuilder = UriBuilder.fromUri(new URI("http://oh:40411"));//fromPath(s.toString());
        UriBuilder uriBuilder = UriBuilder.fromPath("http://oh:40411");//fromPath(s.toString());
        uriBuilder.path("/jobscheduler/master/api/order");
        uriBuilder.queryParam("return", "hallo");
        uriBuilder.queryParam("olli", "welt");
        URI uri = uriBuilder.build();
        UriBuilder uriBuilder2 = UriBuilder.fromPath("http://sp:40412/a/");
        uriBuilder2.path(uri.getPath());
        uriBuilder2.replaceQuery(uri.getQuery());
        System.out.println(uri.getAuthority());
        System.out.println(uri.getScheme());
        System.out.println(uri.getHost());
        System.out.println(uri.getPort());
        System.out.println(uri.getPath());
        System.out.println(uri.getQuery());
        System.out.println(uri.getRawQuery());
        System.out.println(uriBuilder.build().toString());
        System.out.println(uriBuilder2.build().toString());
    }
}
