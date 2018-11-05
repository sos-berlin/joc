package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryFilter;

public class JobResourceHistoryImplTest {
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }


    @Test
    public void postJobHistoryTest() throws Exception {

        TaskHistoryFilter jobHistoryFilterSchema = new TaskHistoryFilter();
        jobHistoryFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        jobHistoryFilterSchema.setJob(GlobalsTest.JOB);
        JobResourceHistoryImpl jobHistoryImpl = new JobResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobHistoryImpl.postJobHistory(accessToken, jobHistoryFilterSchema);
        TaskHistory history = (TaskHistory) jobsResponse.getEntity();
        assertEquals("postJobHistoryTest", GlobalsTest.JOB, Globals.normalizePath(history.getHistory().get(0).getJob()));
    }

}