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
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.audit.StartJobAudit;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.jobstreams.resource.IStartJobInInInstanceResource;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;
import com.sos.joc.model.jobstreams.JobStreamStartJob;
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
            JobStreamStartJob jobStreamStartJob = Globals.objectMapper.readValue(filterBytes, JobStreamStartJob.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobStreamStartJob, accessToken, jobStreamStartJob.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStreamStartJob.getJobschedulerId(), accessToken).getJobStream().getChange().isConditions());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("session", jobStreamStartJob.getSession());
            checkRequiredParameter("job", jobStreamStartJob.getJob());

            try {
                checkFolderPermissions(jobStreamStartJob.getJob());

                StartJob startJob = new StartJob();

                startJob.setAt(jobStreamStartJob.getAt());
                startJob.setJob(jobStreamStartJob.getJob());

                StartJobs startJobs = new StartJobs();
                startJobs.setAuditLog(jobStreamStartJob.getAuditLog());
                startJobs.setJobschedulerId(jobStreamStartJob.getJobschedulerId());

                StartJobAudit jobAudit = new StartJobAudit(startJob, startJobs);
                logAuditMessage(jobAudit);
                storeAuditLogEntry(jobAudit);
                
                

                notifyEventHandler(accessToken, jobStreamStartJob);
            } catch (JocFolderPermissionsException e) {
                LOGGER.debug("Folder permission for " + jobStreamStartJob.getJob() + " is missing. Job start ignored.");

            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Start Job: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Job not started");
                return JOCDefaultResponse.responseStatusJSError(e, getJocError());

            }

            return JOCDefaultResponse.responseStatus200(jobStreamStartJob);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

        }
    }

    private void notifyEventHandler(String accessToken, JobStreamStartJob startJob) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(ResetJobStreamImpl.class.getName());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("job", startJob.getJob());
        parameters.put("session", startJob.getSession());
        String at;

        if (startJob.getAt() == null || startJob.getAt().isEmpty()) {
            at = "now";
        } else {
            if (startJob.getAt().contains("now")) {
                at = startJob.getAt();
            } else {
                at = JobSchedulerDate.getAtInJobSchedulerTimezone(startJob.getAt(), startJob.getTimeZone(), dbItemInventoryInstance.getTimeZone());
            }
        }

        parameters.put("at", at);
        
        for (NameValuePair param: startJob.getParams()) {
            parameters.put("#" + param.getName(), param.getValue());
        }

        customEventsUtil.addEvent("StartJob", parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();

    }
    
 

}