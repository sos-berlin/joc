package com.sos.joc.tasks.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.JobsFilter;

public class TasksResourceHistoryImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postTasksHistoryTest() throws Exception {

        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        TasksResourceHistoryImpl tasksHistoryImpl = new TasksResourceHistoryImpl();
        JOCDefaultResponse taskHistoryResponse = tasksHistoryImpl.postTasksHistory(accessToken, jobsFilterSchema);
        TaskHistory historySchema = (TaskHistory) taskHistoryResponse.getEntity();
        assertEquals("postTasksHistoryTest", TestEnvWebserviceGlobalsTest.JOB, historySchema.getHistory().get(0).getJob());
    }

}
