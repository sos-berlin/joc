package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.TaskHistory;
import com.sos.joc.model.job.TaskHistoryFilter;

public class JobResourceHistoryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobHistoryTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        TaskHistoryFilter jobHistoryFilterSchema = new TaskHistoryFilter();
        jobHistoryFilterSchema.setJobschedulerId("scheduler_current");
        jobHistoryFilterSchema.setJob("job1");
        JobResourceHistoryImpl jobHistoryImpl = new JobResourceHistoryImpl();
        JOCDefaultResponse jobsResponse = jobHistoryImpl.postJobHistory(sosShiroCurrentUserAnswer.getAccessToken(), jobHistoryFilterSchema);
        TaskHistory history = (TaskHistory) jobsResponse.getEntity();
        assertEquals("postJobHistoryTest", "job1", history.getHistory().get(0).getJob());
    }

}