package com.sos.joc.job.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.job.JobFilter;

public class JobResourceOrderQueueImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceOrderQueueImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postJobOrderQueueTest() throws Exception {

        LOGGER.info("postJobOrderQueueTest start");
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobFilter jobFilterSchema = new JobFilter();
        jobFilterSchema.setJobschedulerId(SCHEDULER_ID);
        jobFilterSchema.setJob("check_history/check");
        JobResourceOrderQueueImpl jobOrderQueueImpl = new JobResourceOrderQueueImpl();
        jobOrderQueueImpl.postJobOrderQueue(sosShiroCurrentUserAnswer.getAccessToken(), jobFilterSchema);
        assertEquals("postJobOrderQueueTest", "check", jobFilterSchema.getJob());
    }

}