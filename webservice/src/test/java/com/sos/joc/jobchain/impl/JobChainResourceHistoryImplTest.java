package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainHistoryFilter;
import com.sos.joc.model.order.OrderHistory;

public class JobChainResourceHistoryImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainHistoryTest() throws Exception {

        JobChainHistoryFilter jobChainHistoryFilterSchema = new JobChainHistoryFilter();
        jobChainHistoryFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        jobChainHistoryFilterSchema.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        JobChainResourceHistoryImpl jobChainHistoryImpl = new JobChainResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobChainHistoryImpl.postJobChainHistory(accessToken, jobChainHistoryFilterSchema);
        OrderHistory historySchema = (OrderHistory) jobsResponse.getEntity();
        assertEquals("postJobChainHistoryTest", TestEnvWebserviceGlobalsTest.JOB_CHAIN, historySchema.getHistory().get(0).getJobChain());
    }

}