package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceDbImpl;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.DbSchema;

public class JobSchedulerResourceDbImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerDbTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerFilterSchema jobSchedulerFilterSchema = new JobSchedulerFilterSchema();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceDbImpl jobschedulerResourceDbImpl = new JobSchedulerResourceDbImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceDbImpl.postJobschedulerDb(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        DbSchema dbSchema = (DbSchema) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerDbTest", "myDbms", dbSchema.getDatabase().getDbms());
     }

}

