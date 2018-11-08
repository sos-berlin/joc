package com.sos.joc.jobs.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobStateFilter;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsV;

public class JobsResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImplTest.class);

    @Test
    public void postMinConfJobsTest() throws Exception {
        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(accessToken, jobsFilterSchema);
        JobsV jobsVSchema = (JobsV) jobsResponse.getEntity();
        assertEquals("postJobsTest", "hookShell", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

    @Test
    public void postCompactJobsTest() throws Exception {
        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        jobsFilterSchema.setCompact(true);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(accessToken, jobsFilterSchema);
        // JobsV jobsVSchema = (JobsV) jobsResponse.getEntity();
        // assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

    @Test
    public void postJobsWithFoldersRecursiveTest() throws Exception {
        Date start = new Date();
        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        jobsFilterSchema.setCompact(true);
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = new Folder();
        folder.setFolder("/test_JS-1473");
        folder.setRecursive(true);
        folders.add(folder);
        Folder folder2 = new Folder();
        folder2.setFolder("/check_history");
        folder2.setRecursive(true);
        folders.add(folder2);
        Folder folder3 = new Folder();
        folder3.setFolder("/OrderJob");
        folder3.setRecursive(true);
        folders.add(folder3);
        jobsFilterSchema.setFolders(folders);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(accessToken, jobsFilterSchema);
        // JobsV jobsVSchema = (JobsV) jobsResponse.getEntity();
        // assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        Date end = new Date();
        long duration = end.getTime() - start.getTime();
        LOGGER.info("duration: " + duration + "ms");
        LOGGER.info(jobsResponse.toString());
    }

    @Test
    public void postJobsWithFoldersNotRecursiveTest() throws Exception {
        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        jobsFilterSchema.setCompact(true);
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = new Folder();
        folder.setFolder("/sos/housekeeping/scheduler_cleanup_history");
        folder.setRecursive(false);
        folders.add(folder);
        jobsFilterSchema.setFolders(folders);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(accessToken, jobsFilterSchema);
        // JobsV jobsVSchema = (JobsV) jobsResponse.getEntity();
        // assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

    @Test
    public void postJobsPendingTest() throws Exception {
        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        List<JobStateFilter> states = new ArrayList<JobStateFilter>();
        states.add(JobStateFilter.PENDING);
        jobsFilterSchema.setStates(states);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(accessToken, jobsFilterSchema);
        JobsV jobsVSchema = (JobsV) jobsResponse.getEntity();
        assertEquals("postJobsTest", "hookShell", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

    @Test
    public void postJobsFromToTest() throws Exception {
        JobsFilter jobsFilterSchema = new JobsFilter();
        jobsFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        jobsFilterSchema.setDateFrom("2016-09-06 00:00:00.000Z");
        jobsFilterSchema.setDateTo("2016-09-06 23:59:59.999Z");
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(accessToken, jobsFilterSchema);
        JobsV jobsVSchema = (JobsV) jobsResponse.getEntity();
        assertEquals("postJobsTest", "hookShell", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

}
