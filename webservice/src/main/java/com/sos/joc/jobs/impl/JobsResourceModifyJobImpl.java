package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobs.resource.IJobsResourceModifyJob;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.job.ModifyJob;
import com.sos.joc.model.job.ModifyJobs;
import com.sos.scheduler.model.commands.JSCmdModifyJob;
import com.sos.scheduler.model.objects.JSObjRunTime;

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
            return postJobsCommand(accessToken, STOP, getPermissons(accessToken).getJob().isStop(), modifyJobs);
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
            return postJobsCommand(accessToken, UNSTOP, getPermissons(accessToken).getJob().isUnstop(), modifyJobs);
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
            return postJobsCommand(accessToken, SET_RUN_TIME, getPermissons(accessToken).getJob().isSetRunTime(), modifyJobs);
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
            return postJobsCommand(accessToken, END, getPermissons(accessToken).getJob().isEndAllTasks(), modifyJobs);
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
            return postJobsCommand(accessToken, SUSPEND, getPermissons(accessToken).getJob().isSuspendAllTasks(), modifyJobs);
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
            return postJobsCommand(accessToken, CONTINUE, getPermissons(accessToken).getJob().isContinueAllTasks(), modifyJobs);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private Date executeModifyJobCommand(ModifyJob modifyJob, String command) {

        try {
            logAuditMessage(modifyJob);

            checkRequiredParameter("job", modifyJob.getJob());
            
            JSCmdModifyJob jsCmdModifyJob = Globals.schedulerObjectFactory.createModifyJob();
            jsCmdModifyJob.setCmd(command);
            jsCmdModifyJob.setJob(normalizePath(modifyJob.getJob()));
            if (SET_RUN_TIME.equals(command)) {
                try {
                    ValidateXML.validateRunTimeAgainstJobSchedulerSchema(modifyJob.getRunTime());
                    JSObjRunTime objRuntime = new JSObjRunTime(Globals.schedulerObjectFactory, modifyJob.getRunTime());
                    jsCmdModifyJob.setRunTime(objRuntime);
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(modifyJob.getRunTime());
                }
            }
            String xml = jsCmdModifyJob.toXMLString();
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());

            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), modifyJob));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), modifyJob));
        }
        return null;
    }

    private JOCDefaultResponse postJobsCommand(String accessToken, String command, boolean permission, ModifyJobs modifyJobs) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(accessToken, modifyJobs.getJobschedulerId(), permission);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        if (modifyJobs.getJobs().size() == 0) {
            throw new JocMissingRequiredParameterException("undefined 'jobs'");
        }
        Date surveyDate = new Date();
        for (ModifyJob job : modifyJobs.getJobs()) {
            surveyDate = executeModifyJobCommand(job, command);
        }
        if (listOfErrors.size() > 0) {
            return JOCDefaultResponse.responseStatus419(listOfErrors);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
    }
}
