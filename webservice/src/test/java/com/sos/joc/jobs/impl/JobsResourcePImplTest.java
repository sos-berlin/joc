package com.sos.joc.jobs.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsP;

public class JobsResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobsPTest() throws Exception {

        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = new Folder();
        folder.setFolder(TestEnvWebserviceTest.JOB_CHAIN_FOLDER);
        folders.add(folder);
        jobsFilterSchema.setFolders(folders);
        JobsResourcePImpl jobsPImpl = new JobsResourcePImpl();
        JOCDefaultResponse jobsPResponse = jobsPImpl.postJobsP(accessToken, jobsFilterSchema);
        JobsP jobsPSchema = (JobsP) jobsPResponse.getEntity();
        assertEquals("postJobsPTest", TestEnvWebserviceTest.JOB, jobsPSchema.getJobs().get(0).getPath());
    }

}
