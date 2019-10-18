package com.sos.joc.xmleditor.common;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class JocXmlEditorTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getResourceImplPathTest() throws Exception {
        assertEquals("getResourceImplPathTest", "./xmleditor/test", JocXmlEditor.getResourceImplPath("test"));
    }

}
