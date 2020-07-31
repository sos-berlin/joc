package com.sos.joc.jobstreams.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.StartJobAudit;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.jobstreams.resource.IStartJobInInInstanceResource;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;
import com.sos.joc.model.jobstreams.JobStreamsFilter;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class StartJobInInstanceImpl extends JOCResourceImpl implements IStartJobInInInstanceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetJobStreamImpl.class);
    private static final String API_CALL = "./jobstreams/startjob";

    @Override
    public JOCDefaultResponse startJob(String accessToken, byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, JobStreamsFilter.class);
            JobStreamsFilter jobStreamsFilter = Globals.objectMapper.readValue(filterBytes, JobStreamsFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobStreamsFilter, accessToken, jobStreamsFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    jobStreamsFilter.getJobschedulerId(), accessToken).getJobStream().getChange().isConditions());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("session", jobStreamsFilter.getSession());
            checkRequiredParameter("job", jobStreamsFilter.getJob());

            try {
                checkFolderPermissions(jobStreamsFilter.getJob());
                
                StartJob startJob = new StartJob();
                startJob.setAt("now");
                startJob .setJob(jobStreamsFilter.getJob());
                
                StartJobs startJobs = new StartJobs();
                startJobs.setAuditLog(jobStreamsFilter.getAuditLog());
                startJobs.setJobschedulerId(jobStreamsFilter.getJobschedulerId());

                StartJobAudit jobAudit = new StartJobAudit(startJob, startJobs);
                logAuditMessage(jobAudit);

                notifyEventHandler(accessToken, jobStreamsFilter);
            } catch (JocFolderPermissionsException e) {
                LOGGER.debug("Folder permission for " + jobStreamsFilter.getJob() + " is missing. Job start ignored.");

            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Start Job: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Job not started");
                return JOCDefaultResponse.responseStatusJSError(e, getJocError());

            }

            return JOCDefaultResponse.responseStatus200(jobStreamsFilter);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

        }
    }

    private void notifyEventHandler(String accessToken, JobStreamsFilter startJob) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(ResetJobStreamImpl.class.getName());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("job", startJob.getJob());
        parameters.put("session", startJob.getSession());

        customEventsUtil.addEvent("StartJob", parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();

    }

}