package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.Job200PSchema;
import com.sos.joc.model.job.JobFilterSchema;

public class JobResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobPTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobFilterSchema jobFilterSchema = new JobFilterSchema();
        jobFilterSchema.setJobschedulerId("scheduler_current");
        jobFilterSchema.setJob("batch_install_universal_agent/PerformInstall");
        JobResourcePImpl jobPImpl = new JobResourcePImpl();
        JOCDefaultResponse jobsResponse = jobPImpl.postJobP(sosShiroCurrentUserAnswer.getAccessToken(), jobFilterSchema);
        Job200PSchema jobP200Schema = (Job200PSchema) jobsResponse.getEntity();
        assertEquals("postJobPTest", "batch_install_universal_agent/PerformInstall", jobP200Schema.getJob().getPath());
    }

}