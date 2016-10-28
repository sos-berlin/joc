package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainHistoryFilter;
import com.sos.joc.model.order.OrderHistory;

public class JobChainResourceHistoryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainHistoryTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainHistoryFilter jobChainHistoryFilterSchema = new JobChainHistoryFilter();
        jobChainHistoryFilterSchema.setJobschedulerId("scheduler_current");
        jobChainHistoryFilterSchema.setJobChain("batch_install_universal_agent/PerformInstall");
        JobChainResourceHistoryImpl jobChainHistoryImpl = new JobChainResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobChainHistoryImpl.postJobChainHistory(sosShiroCurrentUserAnswer.getAccessToken(), jobChainHistoryFilterSchema);
        OrderHistory historySchema = (OrderHistory) jobsResponse.getEntity();
        assertEquals("postJobChainHistoryTest", "myJobChain", historySchema.getHistory().get(0).getJobChain());
    }

}