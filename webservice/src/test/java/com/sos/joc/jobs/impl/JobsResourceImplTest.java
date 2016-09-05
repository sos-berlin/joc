package com.sos.joc.jobs.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.JobsVSchema;

public class JobsResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postJobsTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsFilterSchema jobsFilterSchema = new JobsFilterSchema();
        jobsFilterSchema.setJobschedulerId("scheduler_4444");
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsFilterSchema);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
     }

}

