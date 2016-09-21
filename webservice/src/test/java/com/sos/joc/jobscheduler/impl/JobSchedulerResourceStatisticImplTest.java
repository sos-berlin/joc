package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceStatisticsImpl;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.StatisticsSchema;

public class JobSchedulerResourceStatisticImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerStatisticTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerFilterSchema jobSchedulerFilterSchema = new JobSchedulerFilterSchema();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceStatisticsImpl jobschedulerResourceStatisticImpl = new JobSchedulerResourceStatisticsImpl();
        JOCDefaultResponse jobschedulerStatisticsResponse = jobschedulerResourceStatisticImpl.postJobschedulerStatistics(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        StatisticsSchema statisticsSchema = (StatisticsSchema) jobschedulerStatisticsResponse.getEntity();
        assertEquals("postjobschedulerStatisticTest", -1, statisticsSchema.getOrders().getSuspended().intValue());

    }

}
