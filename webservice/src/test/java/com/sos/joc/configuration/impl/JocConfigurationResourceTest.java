package com.sos.joc.configuration.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.configuration.Configuration;
import com.sos.joc.model.configuration.Configuration200;
import com.sos.joc.model.configuration.ConfigurationObjectType;
import com.sos.joc.model.configuration.ConfigurationOk;
import com.sos.joc.model.configuration.ConfigurationType;

public class JocConfigurationResourceTest {
    private static final String PASSWORD = "root";
    private static final String USER = "root";
    
    @Test
    public void saveJocConfigurationsTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
        Configuration configuration = new Configuration();
        configuration.setId(2L);
        configuration.setAccount("root");
        configuration.setConfigurationItem("my configuration item");
        configuration.setConfigurationType(ConfigurationType.CUSTOMIZATION);
        configuration.setJobschedulerId("scheduler.1.12");
        configuration.setName("test");
        configuration.setObjectType(ConfigurationObjectType.JOBCHAIN);
        configuration.setShared(true);

        JocConfigurationResourceImpl jocConfigurationResourceImpl = new JocConfigurationResourceImpl();
        JOCDefaultResponse jocResponse = jocConfigurationResourceImpl.postSaveConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), configuration);
        ConfigurationOk configurationSchema = (ConfigurationOk) jocResponse.getEntity();
        assertEquals("saveJocConfigurationsTest",String.valueOf(2L), String.valueOf(configurationSchema.getId()));
    }

    @Test
    public void readJocConfigurationsTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
        Configuration configuration = new Configuration();
        configuration.setId(2L);
        configuration.setJobschedulerId("scheduler.1.12");

        JocConfigurationResourceImpl jocConfigurationResourceImpl = new JocConfigurationResourceImpl();
        JOCDefaultResponse jocResponse = jocConfigurationResourceImpl.postReadConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), configuration);
        Configuration200 configurationSchema = (Configuration200) jocResponse.getEntity();
        assertEquals("postJobConfigurationTest","root", configurationSchema.getConfiguration().getAccount());
    }

    
    
   
    @Test
    public void makePrivateJocConfigurationsTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
        Configuration configuration = new Configuration();
        configuration.setId(2L);
        configuration.setJobschedulerId("scheduler.1.12");

        JocConfigurationResourceImpl jocConfigurationResourceImpl = new JocConfigurationResourceImpl();
        JOCDefaultResponse jocResponse = jocConfigurationResourceImpl.postMakePrivate(sosShiroCurrentUserAnswer.getAccessToken(), configuration);
        ConfigurationOk configurationSchema = (ConfigurationOk) jocResponse.getEntity();
        assertEquals("makePrivateJocConfigurationsTest",String.valueOf(2L), String.valueOf(configurationSchema.getId()));
     }
    
    @Test
    public void sharePrivateJocConfigurationsTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
        Configuration configuration = new Configuration();
        configuration.setId(2L);
        configuration.setJobschedulerId("scheduler.1.12");

        JocConfigurationResourceImpl jocConfigurationResourceImpl = new JocConfigurationResourceImpl();
        JOCDefaultResponse jocResponse = jocConfigurationResourceImpl.postShareConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), configuration);
        ConfigurationOk configurationSchema = (ConfigurationOk) jocResponse.getEntity();
        assertEquals("postJobConfigurationTest",String.valueOf(2L), String.valueOf(configurationSchema.getId()));
     }
}
