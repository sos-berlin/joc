package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.HistorySchema;
import com.sos.joc.model.job.JobHistoryFilterSchema;

public class JobResourceHistoryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobPTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobHistoryFilterSchema jobHistoryFilterSchema = new JobHistoryFilterSchema();
        jobHistoryFilterSchema.setJobschedulerId("scheduler_current");
        jobHistoryFilterSchema.setJob("batch_install_universal_agent/PerformInstall");
        JobResourceHistoryImpl jobHistoryImpl = new JobResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobHistoryImpl.postJobHistory(sosShiroCurrentUserAnswer.getAccessToken(), jobHistoryFilterSchema);
        HistorySchema historySchema = (HistorySchema) jobsResponse.getEntity();
        assertEquals("postJobPTest", "myAgent", historySchema.getHistory().get(0).getAgent());
    }

}