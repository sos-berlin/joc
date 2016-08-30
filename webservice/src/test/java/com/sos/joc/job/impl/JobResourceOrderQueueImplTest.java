package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.job.post.JobOrderQueueBody;
import com.sos.joc.model.job.Job200VSchema;

public class JobResourceOrderQueueImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobOrderQueueTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobOrderQueueBody jobOrderQueueBody = new JobOrderQueueBody();
        jobOrderQueueBody.setJobschedulerId("scheduler_current");
        JobResourceOrderQueueImpl jobOrderQueueImpl = new JobResourceOrderQueueImpl();
        JOCDefaultResponse jobsResponse = jobOrderQueueImpl.postJobOrderQueue(sosShiroCurrentUserAnswer.getAccessToken(), jobOrderQueueBody);
        Job200VSchema jobV200Schema = (Job200VSchema) jobsResponse.getEntity();
        assertEquals("postJobOrderQueueTest", "myName", jobV200Schema.getJob().getName());
    }

}