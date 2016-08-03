package com.sos.jocjobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceStatisticsImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse;
import com.sos.joc.model.jobscheduler.StatisticsSchema;

public class JobSchedulerResourceStatisticImplTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";
     
    @Test
    public void postjobschedulerStatisticTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerStatisticsBody = new JobSchedulerDefaultBody();
        jobSchedulerStatisticsBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceStatisticsImpl jobschedulerResourceStatisticImpl = new JobSchedulerResourceStatisticsImpl();
        JobschedulerStatisticsResponse jobschedulerStatisticsResponse = jobschedulerResourceStatisticImpl.postJobschedulerStatistics(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerStatisticsBody);
        StatisticsSchema statisticsSchema = (StatisticsSchema) jobschedulerStatisticsResponse.getEntity();
        assertEquals("postjobschedulerStatisticTest", -1, statisticsSchema.getOrders().getSuspended().intValue());

    }

}
