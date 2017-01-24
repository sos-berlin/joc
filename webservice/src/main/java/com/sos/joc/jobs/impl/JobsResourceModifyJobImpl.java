package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyJobAudit;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobs.resource.IJobsResourceModifyJob;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.job.ModifyJob;
import com.sos.joc.model.job.ModifyJobs;

@Path("jobs")
public class JobsResourceModifyJobImpl extends JOCResourceImpl implements IJobsResourceModifyJob {

    private static final String CONTINUE = "continue";
    private static final String END = "end";
    private static final String SUSPEND = "suspend";
    private static final String STOP = "stop";
    private static final String SET_RUN_TIME = "set_run_time";
    private static final String UNSTOP = "unstop";
    private static String API_CALL = "./jobs/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    
    @Override
    public JOCDefaultResponse postJobsStop(String accessToken, ModifyJobs modifyJobs) {
        initLogging(API_CALL + STOP, modifyJobs);
        try {
            return postJobsCommand(accessToken, STOP, getPermissonsJocCockpit(accessToken).getJob().isStop(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsUnstop(String accessToken, ModifyJobs modifyJobs) {
        initLogging(API_CALL + UNSTOP, modifyJobs);
        try {
            return postJobsCommand(accessToken, UNSTOP, getPermissonsJocCockpit(accessToken).getJob().isUnstop(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsSetRunTime(String accessToken, ModifyJobs modifyJobs) {
        initLogging(API_CALL + SET_RUN_TIME, modifyJobs);
        try {
            return postJobsCommand(accessToken, SET_RUN_TIME, getPermissonsJocCockpit(accessToken).getJob().isSetRunTime(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsEndAllTasks(String accessToken, ModifyJobs modifyJobs) {
        initLogging(API_CALL + END, modifyJobs);
        try {
            return postJobsCommand(accessToken, END, getPermissonsJocCockpit(accessToken).getJob().isEndAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsSuspendAllTasks(String accessToken, ModifyJobs modifyJobs) {
        initLogging(API_CALL + SUSPEND, modifyJobs);
        try {
            return postJobsCommand(accessToken, SUSPEND, getPermissonsJocCockpit(accessToken).getJob().isSuspendAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobsContinueAllTasks(String accessToken, ModifyJobs modifyJobs) {
        initLogging(API_CALL + CONTINUE, modifyJobs);
        try {
            return postJobsCommand(accessToken, CONTINUE, getPermissonsJocCockpit(accessToken).getJob().isContinueAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private Date executeModifyJobCommand(ModifyJob modifyJob, String jobschedulerId, String command) {

        try {
            ModifyJobAudit jobAudit = new ModifyJobAudit(modifyJob, jobschedulerId);
            logAuditMessage(jobAudit);

            checkRequiredParameter("job", modifyJob.getJob());
            if (SET_RUN_TIME.equals(command)) {
                checkRequiredParameter("runTime", modifyJob.getRunTime()); 
            }
            
            XMLBuilder xml = new XMLBuilder("modify_job");
            xml.addAttribute("job", normalizePath(modifyJob.getJob())).addAttribute("cmd", command);
            if (SET_RUN_TIME.equals(command)) {
                try {
                    ValidateXML.validateRunTimeAgainstJobSchedulerSchema(modifyJob.getRunTime());
                    xml.add(XMLBuilder.parse(modifyJob.getRunTime()));
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(modifyJob.getRunTime());
                }
            } 
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
            storeAuditLogEntry(jobAudit);

            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), modifyJob));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), modifyJob));
        }
        return null;
    }

    private JOCDefaultResponse postJobsCommand(String accessToken, String command, boolean permission, ModifyJobs modifyJobs) throws Exception {
        String jobschedulerId = modifyJobs.getJobschedulerId();
        JOCDefaultResponse jocDefaultResponse = init(accessToken, jobschedulerId, permission);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        if (modifyJobs.getJobs().size() == 0) {
            throw new JocMissingRequiredParameterException("undefined 'jobs'");
        }
        Date surveyDate = new Date();
        for (ModifyJob job : modifyJobs.getJobs()) {
            surveyDate = executeModifyJobCommand(job, jobschedulerId, command);
        }
        if (listOfErrors.size() > 0) {
            return JOCDefaultResponse.responseStatus419(listOfErrors);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
    }
}
