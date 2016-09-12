package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.OkSchema;
import com.sos.joc.model.jobChain.JobChain_____;
import com.sos.joc.model.jobChain.JobChainsPSchema;
import com.sos.joc.model.jobChain.ModifySchema;

public class JobChainsResourceModifyJobChainImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainsStopTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ModifySchema modifySchema = new ModifySchema();
        modifySchema.setJobschedulerId("scheduler_current");
        
        List<JobChain_____> listOfJobChains = new ArrayList<JobChain_____>();
        JobChain_____ jobChain1 = new JobChain_____();
        jobChain1.setJobChain("test/job_chain1");
        listOfJobChains.add(jobChain1);
        JobChain_____ jobChain2 = new JobChain_____();
        jobChain2.setJobChain("test/job_chain2");
        listOfJobChains.add(jobChain2);
        
        modifySchema.setJobChains(listOfJobChains);
        
        JobChainsResourceModifyJobChainsImpl jobChainsResourceCommandModifyJobChainsImpl = new JobChainsResourceModifyJobChainsImpl();
        JOCDefaultResponse jobsResponse = jobChainsResourceCommandModifyJobChainsImpl.postJobChainsStop(sosShiroCurrentUserAnswer.getAccessToken(), modifySchema);
        OkSchema okSchema = (OkSchema) jobsResponse.getEntity();
        assertEquals("postJobChainsStopTest", true, okSchema.getOk());
    }


    @Test
    public void postJobChainsUnStopTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ModifySchema modifySchema = new ModifySchema();
        modifySchema.setJobschedulerId("scheduler_current");
        
        List<JobChain_____> listOfJobChains = new ArrayList<JobChain_____>();
        JobChain_____ jobChain1 = new JobChain_____();
        jobChain1.setJobChain("test/job_chain1");
        listOfJobChains.add(jobChain1);
        JobChain_____ jobChain2 = new JobChain_____();
        jobChain2.setJobChain("test/job_chain2");
        listOfJobChains.add(jobChain2);
        
        modifySchema.setJobChains(listOfJobChains);
        
        JobChainsResourceModifyJobChainsImpl jobChainsResourceCommandModifyJobChainsImpl = new JobChainsResourceModifyJobChainsImpl();
        JOCDefaultResponse jobsResponse = jobChainsResourceCommandModifyJobChainsImpl.postJobChainsUnStop(sosShiroCurrentUserAnswer.getAccessToken(), modifySchema);
        OkSchema okSchema = (OkSchema) jobsResponse.getEntity();
        assertEquals("postJobChainsUnStopTest", true, okSchema.getOk());
    }

    
}