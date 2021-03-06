package com.sos.joc.processclasses.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesV;
import com.sos.joc.processClasses.impl.ProcessClassesResourceImpl;

public class ProcessClassesResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postProcessClassesTest() throws Exception {
        ProcessClassesFilter processesClassFilterSchema = new ProcessClassesFilter();
        processesClassFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        ProcessClassesResourceImpl processClassesResourceImpl = new ProcessClassesResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(processesClassFilterSchema);
        JOCDefaultResponse jobsResponse = processClassesResourceImpl.postProcessClasses(accessToken, b);
        ProcessClassesV processClassesVSchema = (ProcessClassesV) jobsResponse.getEntity();
        assertEquals("postProcessClassesTest", "win7", processClassesVSchema.getProcessClasses().get(0).getName());
    }

}
