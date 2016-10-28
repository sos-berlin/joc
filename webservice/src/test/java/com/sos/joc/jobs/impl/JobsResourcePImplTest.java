package com.sos.joc.jobs.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsP;

public class JobsResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postJobsPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId("scheduler_current");
        JobsResourcePImpl jobsPImpl = new JobsResourcePImpl();
        JOCDefaultResponse jobsPResponse = jobsPImpl.postJobsP(sosShiroCurrentUserAnswer.getAccessToken(), jobsFilterSchema);
        JobsP jobsPSchema = (JobsP) jobsPResponse.getEntity();
        assertEquals("postJobsPTest","Sync_ChainA_ChainB", jobsPSchema.getJobs().get(0).getName());
     }

}

