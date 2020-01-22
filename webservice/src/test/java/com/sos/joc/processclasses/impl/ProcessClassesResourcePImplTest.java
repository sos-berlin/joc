package com.sos.joc.processclasses.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesP;
import com.sos.joc.processClasses.impl.ProcessClassesResourcePImpl;

public class ProcessClassesResourcePImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassesResourcePImplTest.class);
    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postProcessClassesTest() throws Exception {
        ProcessClassesFilter processClassesFilterSchema = new ProcessClassesFilter();
        processClassesFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        ProcessClassesResourcePImpl processClassesResourcePImpl = new ProcessClassesResourcePImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(processClassesResourcePImpl);
        JOCDefaultResponse jobsResponse = processClassesResourcePImpl.postProcessClassesP(accessToken, b);
        ProcessClassesP processClassesPSchema = (ProcessClassesP) jobsResponse.getEntity();
        assertEquals("postProcessClassesTest", "win7", processClassesPSchema.getProcessClasses().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

}
