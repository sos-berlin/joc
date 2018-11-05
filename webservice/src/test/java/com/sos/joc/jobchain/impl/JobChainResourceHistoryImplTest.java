package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainHistoryFilter;
import com.sos.joc.model.order.OrderHistory;

public class JobChainResourceHistoryImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainHistoryTest() throws Exception {

        JobChainHistoryFilter jobChainHistoryFilterSchema = new JobChainHistoryFilter();
        jobChainHistoryFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        jobChainHistoryFilterSchema.setJobChain(GlobalsTest.JOB_CHAIN);
        JobChainResourceHistoryImpl jobChainHistoryImpl = new JobChainResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobChainHistoryImpl.postJobChainHistory(accessToken, jobChainHistoryFilterSchema);
        OrderHistory historySchema = (OrderHistory) jobsResponse.getEntity();
        assertEquals("postJobChainHistoryTest", GlobalsTest.JOB_CHAIN, historySchema.getHistory().get(0).getJobChain());
    }

}