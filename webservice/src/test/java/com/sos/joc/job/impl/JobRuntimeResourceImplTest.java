package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobs.impl.JobsResourceImplTest;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.model.job.JobFilterSchema;

public class JobRuntimeResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";
     
    @Test
    public void postJobRuntimeTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobFilterSchema jobFilterSchema = new JobFilterSchema();
        jobFilterSchema.setJobschedulerId(SCHEDULER_ID);
        jobFilterSchema.setJob("/sos/dailyschedule/CheckDaysSchedule");
        JobRunTimeResourceImpl jobRunTimeImpl = new JobRunTimeResourceImpl();
        JOCDefaultResponse jobResponse = jobRunTimeImpl.postJobRunTime(sosShiroCurrentUserAnswer.getAccessToken(), jobFilterSchema);
        Runtime200Schema jobRunTimeSchema = (Runtime200Schema) jobResponse.getEntity();
        assertEquals("postJobRuntimeTest","<run_time>", jobRunTimeSchema.getRunTime().getRunTime());
     }

}

