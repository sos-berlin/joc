package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobs.impl.JobsResourceImplTest;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.job.JobFilter;

public class JobRuntimeResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";
     
    @Test
    public void postJobRuntimeTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobFilter jobFilterSchema = new JobFilter();
        jobFilterSchema.setJobschedulerId(SCHEDULER_ID);
        jobFilterSchema.setJob("/sos/dailyschedule/CheckDaysSchedule");
        JobRunTimeResourceImpl jobRunTimeImpl = new JobRunTimeResourceImpl();
        JOCDefaultResponse jobResponse = jobRunTimeImpl.postJobRunTime(sosShiroCurrentUserAnswer.getAccessToken(), jobFilterSchema);
        RunTime200 jobRunTimeSchema = (RunTime200) jobResponse.getEntity();
        LOGGER.info(jobRunTimeSchema.getRunTime().getRunTime());
        assertTrue("", jobRunTimeSchema.getRunTime().getRunTime().startsWith("<run_time>"));
     }

}

