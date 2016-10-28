package com.sos.joc.task.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.job.Task200;
import com.sos.joc.model.job.TaskFilter;

public class TaskResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        TaskFilter taskFilterSchema = new TaskFilter();
        taskFilterSchema.setJobschedulerId("scheduler_current");
        TaskResourceImpl taskImpl = new TaskResourceImpl();
        JOCDefaultResponse ordersResponse = taskImpl.postTask(sosShiroCurrentUserAnswer.getAccessToken(), taskFilterSchema);
        Task200 task200Schema = (Task200) ordersResponse.getEntity();
        assertEquals("postOrderTest","myPath", task200Schema.getTask().getOrder().getPath());
     }

}

