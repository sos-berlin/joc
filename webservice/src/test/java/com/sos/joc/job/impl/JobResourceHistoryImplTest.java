package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryFilter;

public class JobResourceHistoryImplTest {
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }


    @Test
    public void postJobHistoryTest() throws Exception {

        TaskHistoryFilter jobHistoryFilterSchema = new TaskHistoryFilter();
        jobHistoryFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        jobHistoryFilterSchema.setJob(TestEnvWebserviceGlobalsTest.JOB);
        JobResourceHistoryImpl jobHistoryImpl = new JobResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobHistoryImpl.postJobHistory(accessToken, jobHistoryFilterSchema);
        TaskHistory history = (TaskHistory) jobsResponse.getEntity();
        assertEquals("postJobHistoryTest", TestEnvWebserviceGlobalsTest.JOB, Globals.normalizePath(history.getHistory().get(0).getJob()));
    }

}