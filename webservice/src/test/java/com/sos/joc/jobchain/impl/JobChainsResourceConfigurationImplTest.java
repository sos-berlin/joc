package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobchain.impl.JobChainResourceConfigurationImpl;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.jobChain.JobChainConfigurationFilter;

public class JobChainsResourceConfigurationImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainConfigurationTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainConfigurationFilter jobChainFilterSchema = new JobChainConfigurationFilter();
        jobChainFilterSchema.setJobschedulerId("scheduler.1.10");
        jobChainFilterSchema.setJobChain("/webservice/setback");
        JobChainResourceConfigurationImpl jobChainsImpl = new JobChainResourceConfigurationImpl();
        JOCDefaultResponse response = jobChainsImpl.postJobChainConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), jobChainFilterSchema);
        Configuration200 configurationSchema = (Configuration200) response.getEntity();
        assertEquals("postJobChainConfigurationTest", 116, configurationSchema.getConfiguration().getConfigurationDate().getYear());
    }

}