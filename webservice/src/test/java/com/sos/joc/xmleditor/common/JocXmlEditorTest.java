package com.sos.joc.xmleditor.common;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.model.xmleditor.common.ObjectType;

public class JocXmlEditorTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getResourceImplPathTest() throws Exception {
        assertEquals("getResourceImplPathTest", "./xmleditor/test", JocXmlEditor.getResourceImplPath("test"));
    }

    @Test
    public void getSchemaURITest() throws Exception {
        Globals.servletBaseUri = new URI("http://localhost:4446/joc/");
        assertEquals("getSchemaURITest", new URI("http://localhost:4446/joc/xsd/yade/YADE_configuration_v1.12.xsd"), JocXmlEditor.getSchemaURI(
                ObjectType.YADE, null));
    }
}
