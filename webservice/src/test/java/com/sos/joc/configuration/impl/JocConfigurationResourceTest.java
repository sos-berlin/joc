package com.sos.joc.configuration.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.configuration.Configuration;
import com.sos.joc.model.configuration.Configuration200;
import com.sos.joc.model.configuration.ConfigurationObjectType;
import com.sos.joc.model.configuration.ConfigurationOk;
import com.sos.joc.model.configuration.ConfigurationType;

public class JocConfigurationResourceTest {
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

     
    @Test
    public void saveJocConfigurationsTest() throws Exception   {
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
        byte[] b = Globals.objectMapper.writeValueAsBytes(configuration);
        JOCDefaultResponse jocResponse = jocConfigurationResourceImpl.postSaveConfiguration(accessToken, b);
        ConfigurationOk configurationSchema = (ConfigurationOk) jocResponse.getEntity();
        assertEquals("saveJocConfigurationsTest",String.valueOf(2L), String.valueOf(configurationSchema.getId()));
    }

    @Test
    public void readJocConfigurationsTest() throws Exception   {
        Configuration configuration = new Configuration();
        configuration.setId(2L);
        configuration.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JocConfigurationResourceImpl jocConfigurationResourceImpl = new JocConfigurationResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(configuration);
        JOCDefaultResponse jocResponse = jocConfigurationResourceImpl.postReadConfiguration(accessToken, b);
        Configuration200 configurationSchema = (Configuration200) jocResponse.getEntity();
        assertEquals("postJobConfigurationTest","root", configurationSchema.getConfiguration().getAccount());
    }

    
    
   
    @Test
    public void makePrivateJocConfigurationsTest() throws Exception   {
        Configuration configuration = new Configuration();
        configuration.setId(2L);
        configuration.setJobschedulerId("scheduler.1.12");

        JocConfigurationResourceImpl jocConfigurationResourceImpl = new JocConfigurationResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(configuration);
        JOCDefaultResponse jocResponse = jocConfigurationResourceImpl.postMakePrivate(accessToken, b);
        ConfigurationOk configurationSchema = (ConfigurationOk) jocResponse.getEntity();
        assertEquals("makePrivateJocConfigurationsTest",String.valueOf(2L), String.valueOf(configurationSchema.getId()));
     }
    
    @Test
    public void sharePrivateJocConfigurationsTest() throws Exception   {
        Configuration configuration = new Configuration();
        configuration.setId(2L);
        configuration.setJobschedulerId("scheduler.1.12");

        JocConfigurationResourceImpl jocConfigurationResourceImpl = new JocConfigurationResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(configuration);
        JOCDefaultResponse jocResponse = jocConfigurationResourceImpl.postShareConfiguration(accessToken, b);
        ConfigurationOk configurationSchema = (ConfigurationOk) jocResponse.getEntity();
        assertEquals("postJobConfigurationTest",String.valueOf(2L), String.valueOf(configurationSchema.getId()));
     }
}
