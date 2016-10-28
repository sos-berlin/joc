package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP200;

public class JobResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobPTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobFilter jobFilterSchema = new JobFilter();
        jobFilterSchema.setJobschedulerId("scheduler_current");
        jobFilterSchema.setJob("batch_install_universal_agent/PerformInstall");
        JobResourcePImpl jobPImpl = new JobResourcePImpl();
        JOCDefaultResponse jobsResponse = jobPImpl.postJobP(sosShiroCurrentUserAnswer.getAccessToken(), jobFilterSchema);
        JobP200 jobP200Schema = (JobP200) jobsResponse.getEntity();
        assertEquals("postJobPTest", "batch_install_universal_agent/PerformInstall", jobP200Schema.getJob().getPath());
    }

}