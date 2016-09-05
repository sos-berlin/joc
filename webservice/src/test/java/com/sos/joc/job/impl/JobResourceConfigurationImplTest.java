package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.job.JobConfigurationFilterSchema;
 
public class JobResourceConfigurationImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postJobConfigurationTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobConfigurationFilterSchema jobConfigurationFilterSchema = new JobConfigurationFilterSchema();
        jobConfigurationFilterSchema.setJob("myPath");
        jobConfigurationFilterSchema.setJobschedulerId("scheduler_current");
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), jobConfigurationFilterSchema);
        ConfigurationSchema orderConfigurationSchema = (ConfigurationSchema) jobsResponse.getEntity();
        assertEquals("postJobConfigurationTest","myPath", orderConfigurationSchema.getConfiguration().getPath());
     }

}

