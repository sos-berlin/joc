package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobchain.impl.JobChainResourceConfigurationImpl;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.jobChain.JobChainConfigurationFilterSchema;

public class JobChainsResourceConfigurationImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainCOnfigurationTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainConfigurationFilterSchema jobChainFilterSchema = new JobChainConfigurationFilterSchema();
        jobChainFilterSchema.setJobschedulerId("scheduler.1.10");
        jobChainFilterSchema.setJobChain("/webservice/setback");
        JobChainResourceConfigurationImpl jobChainsImpl = new JobChainResourceConfigurationImpl();
        JOCDefaultResponse response = jobChainsImpl.postJobChainConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), jobChainFilterSchema);
        ConfigurationSchema configurationSchema = (ConfigurationSchema) response.getEntity();
        assertEquals("postJobChainsTest", 116, configurationSchema.getConfiguration().getConfigurationDate().getYear());
    }

}