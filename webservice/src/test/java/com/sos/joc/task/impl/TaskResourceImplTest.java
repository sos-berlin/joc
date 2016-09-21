package com.sos.joc.task.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.job.Task200Schema;
import com.sos.joc.model.job.TaskFilterSchema;

public class TaskResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        TaskFilterSchema taskFilterSchema = new TaskFilterSchema();
        taskFilterSchema.setJobschedulerId("scheduler_current");
        TaskResourceImpl taskImpl = new TaskResourceImpl();
        JOCDefaultResponse ordersResponse = taskImpl.postTask(sosShiroCurrentUserAnswer.getAccessToken(), taskFilterSchema);
        Task200Schema task200Schema = (Task200Schema) ordersResponse.getEntity();
        assertEquals("postOrderTest","myPath", task200Schema.getTask().getOrder().getPath());
     }

}

