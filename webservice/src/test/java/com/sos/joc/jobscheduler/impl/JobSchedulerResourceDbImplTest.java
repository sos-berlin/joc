package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceDbImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.jobscheduler.DbSchema;
import com.sos.joc.model.jobscheduler.Database.Dbms;

public class JobSchedulerResourceDbImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerDbTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerDefaultBody = new JobSchedulerDefaultBody();
        jobSchedulerDefaultBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceDbImpl jobschedulerResourceDbImpl = new JobSchedulerResourceDbImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceDbImpl.postJobschedulerDb(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerDefaultBody);
        DbSchema dbSchema = (DbSchema) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerDbTest", Dbms.DB2, dbSchema.getDatabase().getDbms());
     }

}

