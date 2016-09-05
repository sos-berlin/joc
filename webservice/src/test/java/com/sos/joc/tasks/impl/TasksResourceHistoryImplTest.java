package com.sos.joc.tasks.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.HistorySchema;
import com.sos.joc.model.job.JobsFilterSchema;

public class TasksResourceHistoryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postTasksHistoryTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsFilterSchema jobsFilterSchema = new JobsFilterSchema();
        jobsFilterSchema.setJobschedulerId("scheduler_current");
        TasksResourceHistoryImpl tasksHistoryImpl = new TasksResourceHistoryImpl();
        JOCDefaultResponse taskHistoryResponse = tasksHistoryImpl.postTasksHistory(sosShiroCurrentUserAnswer.getAccessToken(), jobsFilterSchema);
        HistorySchema historySchema = (HistorySchema) taskHistoryResponse.getEntity();
        assertEquals("postTasksHistoryTest","myJob", historySchema.getHistory().get(0).getJob());
     }

}

