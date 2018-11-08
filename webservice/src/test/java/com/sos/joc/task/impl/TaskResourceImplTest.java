package com.sos.joc.task.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.Task200;
import com.sos.joc.model.job.TaskFilter;

public class TaskResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    @Ignore
    public void postOrderTest() throws Exception {

        TaskFilter taskFilterSchema = new TaskFilter();
        taskFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        TaskResourceImpl taskImpl = new TaskResourceImpl();
        taskFilterSchema.setTaskId("1");
        JOCDefaultResponse ordersResponse = taskImpl.postTask(accessToken, taskFilterSchema);
        Task200 task200Schema = (Task200) ordersResponse.getEntity();
        assertEquals("postOrderTest", "myPath", task200Schema.getTask().getOrder().getPath());
    }

}
