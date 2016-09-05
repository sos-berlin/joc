package com.sos.joc.jobs.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.JobsVSchema;

public class JobsResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = Logger.getLogger(JobsResourceImplTest.class);
     
    @Test
    public void postMinConfJobsTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsFilterSchema jobsFilterSchema = new JobsFilterSchema();
        jobsFilterSchema.setJobschedulerId("scheduler_4444");
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsFilterSchema);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
     }

    @Test
    public void postCompactJobsTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsFilterSchema jobsFilterSchema = new JobsFilterSchema();
        jobsFilterSchema.setJobschedulerId("scheduler_4444");
        jobsFilterSchema.setCompact(true);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsFilterSchema);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
//        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
     }

    @Test
    public void postJobsWithFoldersRecursiveTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsFilterSchema jobsFilterSchema = new JobsFilterSchema();
        jobsFilterSchema.setJobschedulerId("scheduler_4444");
        jobsFilterSchema.setCompact(true);
        List<FoldersSchema> folders = new ArrayList<FoldersSchema>();
        FoldersSchema folder = new FoldersSchema();
        folder.setFolder("/sos/housekeeping/scheduler_cleanup_history");
        folder.setRecursive(true);
        folders.add(folder);
        jobsFilterSchema.setFolders(folders);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsFilterSchema);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
//        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
     }

    @Test
    public void postJobsWithFoldersNotRecursiveTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsFilterSchema jobsFilterSchema = new JobsFilterSchema();
        jobsFilterSchema.setJobschedulerId("scheduler_4444");
        jobsFilterSchema.setCompact(true);
        List<FoldersSchema> folders = new ArrayList<FoldersSchema>();
        FoldersSchema folder = new FoldersSchema();
        folder.setFolder("/sos/housekeeping/scheduler_cleanup_history");
        folder.setRecursive(false);
        folders.add(folder);
        jobsFilterSchema.setFolders(folders);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsFilterSchema);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
//        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
     }

}

