package com.sos.joc.tasks.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.job.ModifyTasks;
import com.sos.joc.model.job.TaskId;
import com.sos.joc.model.job.TasksFilter;

public class TasksResourceKillImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postTasksKillTest() throws Exception   {
        //Preparation: Start the jobs on JobScheduler assigned to scheduler_id and put the task id in the list.
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ModifyTasks modifyTasksSchema = new ModifyTasks();
        List<TasksFilter> listOfJobs = new ArrayList<TasksFilter>();
        TasksFilter jobKill1 = new TasksFilter();
        jobKill1.setComment("myComment1");
        jobKill1.setJob("/test/job2kill");
       
        List<TaskId> listOfTasks = new ArrayList<TaskId>();
        TaskId taskId1 = new TaskId();
        taskId1.setTaskId("18386843");
        listOfTasks.add(taskId1);
        
        TaskId taskId2 = new TaskId();
        taskId2.setTaskId("18386845");
        listOfTasks.add(taskId2);
        
        jobKill1.setTaskIds(listOfTasks);
        listOfJobs.add(jobKill1);

        TasksFilter jobKill2 = new TasksFilter();
        jobKill2.setComment("myComment2");
        jobKill2.setJob("/test/job3kill");

        List<TaskId> listOfTasks2 = new ArrayList<TaskId>();
        TaskId taskId3 = new TaskId();
        taskId3.setTaskId("18386846");
        listOfTasks2.add(taskId3);
      
        TaskId taskId4 = new TaskId();
        taskId4.setTaskId("18386847");
        listOfTasks2.add(taskId4);
        
        jobKill2.setTaskIds(listOfTasks2);
        listOfJobs.add(jobKill2);
        
        modifyTasksSchema.setJobs(listOfJobs);
        modifyTasksSchema.setJobschedulerId("scheduler_current");
        TasksResourceKillImpl tasksHistoryImpl = new TasksResourceKillImpl();
        JOCDefaultResponse taskKillResponse = tasksHistoryImpl.postTasksKill(sosShiroCurrentUserAnswer.getAccessToken(), modifyTasksSchema);
        Ok okSchema = (Ok) taskKillResponse.getEntity();
        assertEquals("postTasksHistoryTest",true, okSchema.getOk());
     }

}

