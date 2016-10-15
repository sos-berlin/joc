package com.sos.joc.tasks.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.JobsFilter;

public class TasksResourceHistoryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postTasksHistoryTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId("scheduler_current");
        TasksResourceHistoryImpl tasksHistoryImpl = new TasksResourceHistoryImpl();
        JOCDefaultResponse taskHistoryResponse = tasksHistoryImpl.postTasksHistory(sosShiroCurrentUserAnswer.getAccessToken(), jobsFilterSchema);
        TaskHistory historySchema = (TaskHistory) taskHistoryResponse.getEntity();
        assertEquals("postTasksHistoryTest","myJob", historySchema.getHistory().get(0).getJob());
     }

}

