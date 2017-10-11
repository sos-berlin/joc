package com.sos.joc.classes.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSObjectConfigurationTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testChangeRuntimeElement() {
        JSObjectConfiguration jsObjectConfiguration = new JSObjectConfiguration("");
        jsObjectConfiguration.setConfiguration(
                "<order  title=\"test\" job_chain=\"job_chain2\" id=\"2\">\n\r<params />\n\r<run_time  test>\n\r<weekdays >\n\r<day  day=\"1 2 3 4 5 6 7\">\n\r<period  single_start=\"13:00:00\"/>\n\r </day>\n\r</weekdays>\n\r    </run_time>\n\r</order>");

        String s = jsObjectConfiguration.changeRuntimeElement("<run_time>xxx</run_time>");
        assertEquals("testChangeRuntimeElement", "<order  title=\"test\" job_chain=\"job_chain2\" id=\"2\">\n\r<params />\n\r<run_time>xxx</run_time>\n\r</order>", s);

        jsObjectConfiguration.setConfiguration(
                "<order  title=\"test\" job_chain=\"job_chain2\" id=\"2\">\n\r<params />\n\r<run_time/>\n\r</order>");

        s = jsObjectConfiguration.changeRuntimeElement("<run_time>xxx</run_time>");
        assertEquals("testChangeRuntimeElement", "<order  title=\"test\" job_chain=\"job_chain2\" id=\"2\">\n\r<params />\n\r<run_time>xxx</run_time>\n\r</order>", s);

    }

}
