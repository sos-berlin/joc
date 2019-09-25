package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobs.impl.JobsResourceImplTest;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.job.JobFilter;

public class JobRuntimeResourceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImplTest.class);
    
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

     
    @Test
    public void postJobRuntimeTest() throws Exception   {
         
        JobFilter jobFilterSchema = new JobFilter();
        jobFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobFilterSchema.setJob(TestEnvWebserviceTest.JOB);
        JobRunTimeResourceImpl jobRunTimeImpl = new JobRunTimeResourceImpl();
        JOCDefaultResponse jobResponse = jobRunTimeImpl.postJobRunTime(accessToken, jobFilterSchema);
        RunTime200 jobRunTimeSchema = (RunTime200) jobResponse.getEntity();
        LOGGER.info(jobRunTimeSchema.getRunTime().getRunTimeXML());
        assertTrue("", jobRunTimeSchema.getRunTime().getRunTimeXML().startsWith("<run_time"));
     }

}

