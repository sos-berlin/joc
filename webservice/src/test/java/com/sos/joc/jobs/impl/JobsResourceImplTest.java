package com.sos.joc.jobs.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobs.post.Folder;
import com.sos.joc.jobs.post.JobsBody;
import com.sos.joc.model.job.JobsVSchema;

public class JobsResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = Logger.getLogger(JobsResourceImplTest.class);
     
    @Test
    public void postMinConfJobsTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsBody jobsBody = new JobsBody();
        jobsBody.setJobschedulerId("scheduler_4444");
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsBody);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
     }

    @Test
    public void postCompactJobsTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsBody jobsBody = new JobsBody();
        jobsBody.setJobschedulerId("scheduler_4444");
        jobsBody.setCompact(true);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsBody);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
//        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
     }

    @Test
    public void postJobsWithFoldersRecursiveTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsBody jobsBody = new JobsBody();
        jobsBody.setJobschedulerId("scheduler_4444");
        jobsBody.setCompact(true);
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = new Folder();
        folder.setFolder("/sos/housekeeping/scheduler_cleanup_history");
        folder.setRecursive(true);
        folders.add(folder);
        jobsBody.setFolders(folders);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsBody);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
//        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
     }

    @Test
    public void postJobsWithFoldersNotRecursiveTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobsBody jobsBody = new JobsBody();
        jobsBody.setJobschedulerId("scheduler_4444");
        jobsBody.setCompact(true);
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = new Folder();
        folder.setFolder("/sos/housekeeping/scheduler_cleanup_history");
        folder.setRecursive(false);
        folders.add(folder);
        jobsBody.setFolders(folders);
        JobsResourceImpl jobsImpl = new JobsResourceImpl();
        JOCDefaultResponse jobsResponse = jobsImpl.postJobs(sosShiroCurrentUserAnswer.getAccessToken(), jobsBody);
        JobsVSchema jobsVSchema = (JobsVSchema) jobsResponse.getEntity();
//        assertEquals("postJobsTest","scheduler_file_order_sink", jobsVSchema.getJobs().get(0).getName());
        LOGGER.info(jobsResponse.toString());
     }

}

