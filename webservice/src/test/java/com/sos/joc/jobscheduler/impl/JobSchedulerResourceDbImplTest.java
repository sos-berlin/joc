package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceDbImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceDb.JobschedulerDbResponse;
import com.sos.joc.model.jobscheduler.DbSchema;
import com.sos.joc.model.jobscheduler.Database.Dbms;

public class JobSchedulerResourceDbImplTest {
    private static final String LDAP_PASSWORD = "root";
    private static final String LDAP_USER = "secret";
     
    @Test
    public void postjobschedulerDbTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerDefaultBody = new JobSchedulerDefaultBody();
        jobSchedulerDefaultBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceDbImpl jobschedulerResourceDbImpl = new JobSchedulerResourceDbImpl();
        JobschedulerDbResponse jobschedulerClusterResponse = jobschedulerResourceDbImpl.postJobschedulerDb(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerDefaultBody);
        DbSchema dbSchema = (DbSchema) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerClusterTest", Dbms.DB_2, dbSchema.getDatabase().getDbms());
     }

}

