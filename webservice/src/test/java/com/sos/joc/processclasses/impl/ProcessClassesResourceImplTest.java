package com.sos.joc.processclasses.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesV;
import com.sos.joc.processClasses.impl.ProcessClassesResourceImpl;

public class ProcessClassesResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postProcessClassesTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ProcessClassesFilter processesClassFilterSchema = new ProcessClassesFilter();
        processesClassFilterSchema.setJobschedulerId(SCHEDULER_ID);
        ProcessClassesResourceImpl processClassesResourceImpl = new ProcessClassesResourceImpl();
        JOCDefaultResponse jobsResponse = processClassesResourceImpl.postProcessClasses(sosShiroCurrentUserAnswer.getAccessToken(), processesClassFilterSchema);
        ProcessClassesV processClassesVSchema = (ProcessClassesV) jobsResponse.getEntity();
        assertEquals("postProcessClassesTest", "myName", processClassesVSchema.getProcessClasses().get(0).getName());
     }

}
