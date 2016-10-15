package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChains;

public class JobChainsResourceModifyJobChainImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainsStopTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ModifyJobChains modifySchema = new ModifyJobChains();
        modifySchema.setJobschedulerId("scheduler_current");
        
        List<ModifyJobChain> listOfJobChains = new ArrayList<ModifyJobChain>();
        ModifyJobChain jobChain1 = new ModifyJobChain();
        jobChain1.setJobChain("test/job_chain1");
        listOfJobChains.add(jobChain1);
        ModifyJobChain jobChain2 = new ModifyJobChain();
        jobChain2.setJobChain("test/job_chain2");
        listOfJobChains.add(jobChain2);
        
        modifySchema.setJobChains(listOfJobChains);
        
        JobChainsResourceModifyJobChainsImpl jobChainsResourceCommandModifyJobChainsImpl = new JobChainsResourceModifyJobChainsImpl();
        JOCDefaultResponse jobsResponse = jobChainsResourceCommandModifyJobChainsImpl.postJobChainsStop(sosShiroCurrentUserAnswer.getAccessToken(), modifySchema);
        Ok okSchema = (Ok) jobsResponse.getEntity();
        assertEquals("postJobChainsStopTest", true, okSchema.getOk());
    }


    @Test
    public void postJobChainsUnStopTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ModifyJobChains modifySchema = new ModifyJobChains();
        modifySchema.setJobschedulerId("scheduler_current");
        
        List<ModifyJobChain> listOfJobChains = new ArrayList<ModifyJobChain>();
        ModifyJobChain jobChain1 = new ModifyJobChain();
        jobChain1.setJobChain("test/job_chain1");
        listOfJobChains.add(jobChain1);
        ModifyJobChain jobChain2 = new ModifyJobChain();
        jobChain2.setJobChain("test/job_chain2");
        listOfJobChains.add(jobChain2);
        
        modifySchema.setJobChains(listOfJobChains);
        
        JobChainsResourceModifyJobChainsImpl jobChainsResourceCommandModifyJobChainsImpl = new JobChainsResourceModifyJobChainsImpl();
        JOCDefaultResponse jobsResponse = jobChainsResourceCommandModifyJobChainsImpl.postJobChainsUnStop(sosShiroCurrentUserAnswer.getAccessToken(), modifySchema);
        Ok okSchema = (Ok) jobsResponse.getEntity();
        assertEquals("postJobChainsUnStopTest", true, okSchema.getOk());
    }

    
}