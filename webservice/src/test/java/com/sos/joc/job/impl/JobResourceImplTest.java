package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.job.post.JobBody;
import com.sos.joc.model.job.Job200VSchema;

public class JobResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobBody jobBody = new JobBody();
        jobBody.setJobschedulerId("scheduler_current");
        JobResourceImpl jobImpl = new JobResourceImpl();
        JOCDefaultResponse jobsResponse = jobImpl.postJob(sosShiroCurrentUserAnswer.getAccessToken(), jobBody);
        Job200VSchema jobV200Schema = (Job200VSchema) jobsResponse.getEntity();
        assertEquals("postJobTest", "myName", jobV200Schema.getJob().getName());
    }

}