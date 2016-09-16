package com.sos.joc.classes.jobs;


import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.job.JobFilterSchema;

public class JobsUtilTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
   

    @Test
     public void createJobPostCommandTest() {
        
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();

        JobFilterSchema jobFilterSchema = new JobFilterSchema();
        jobFilterSchema.setCompact(false);
        jobFilterSchema.setJob("myJob");
        jobFilterSchema.setJobschedulerId("mySchedulerId");
        JobsUtils.createJobPostCommand(jobFilterSchema.getJob(), jobFilterSchema.getCompact());

    }

}
