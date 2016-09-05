package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.model.job.JobFilterSchema;

public class JobRuntimeResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postJobRuntimeTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobFilterSchema jobFilterSchema = new JobFilterSchema();
        jobFilterSchema.setJob("myPath");
        jobFilterSchema.setJobschedulerId("scheduler_current");
        JobRunTimeResourceImpl jobRunTimeImpl = new JobRunTimeResourceImpl();
        JOCDefaultResponse jobResponse = jobRunTimeImpl.postJobRunTime(sosShiroCurrentUserAnswer.getAccessToken(), jobFilterSchema);
        Runtime200Schema jobRunTimeSchema = (Runtime200Schema) jobResponse.getEntity();
        assertEquals("postJobRuntimeTest","myRuntime", jobRunTimeSchema.getRunTime().getRunTime());
     }

}

