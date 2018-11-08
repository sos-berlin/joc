package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainHistoryFilter;
import com.sos.joc.model.order.OrderHistory;

public class JobChainResourceHistoryImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobChainHistoryTest() throws Exception {

        JobChainHistoryFilter jobChainHistoryFilterSchema = new JobChainHistoryFilter();
        jobChainHistoryFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobChainHistoryFilterSchema.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        JobChainResourceHistoryImpl jobChainHistoryImpl = new JobChainResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobChainHistoryImpl.postJobChainHistory(accessToken, jobChainHistoryFilterSchema);
        OrderHistory historySchema = (OrderHistory) jobsResponse.getEntity();
        assertEquals("postJobChainHistoryTest", TestEnvWebserviceTest.JOB_CHAIN, historySchema.getHistory().get(0).getJobChain());
    }

}