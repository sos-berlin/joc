package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryFilter;

public class JobResourceHistoryImplTest {
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }


    @Test
    public void postJobHistoryTest() throws Exception {

        TaskHistoryFilter jobHistoryFilterSchema = new TaskHistoryFilter();
        jobHistoryFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobHistoryFilterSchema.setJob(TestEnvWebserviceTest.JOB);
        JobResourceHistoryImpl jobHistoryImpl = new JobResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobHistoryImpl.postJobHistory(accessToken, jobHistoryFilterSchema);
        TaskHistory history = (TaskHistory) jobsResponse.getEntity();
        assertEquals("postJobHistoryTest", TestEnvWebserviceTest.JOB, Globals.normalizePath(history.getHistory().get(0).getJob()));
    }

}