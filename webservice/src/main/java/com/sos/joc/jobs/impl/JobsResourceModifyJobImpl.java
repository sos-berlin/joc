package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.jobscheduler.BulkError;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocError;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceModifyJobImpl.class);
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private static String API_CALL = "./jobs/";

    @Override
    public JOCDefaultResponse postJobsStop(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, STOP, getPermissons(accessToken).getJob().isStop(), modifyJobs);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobsUnstop(String accessToken, ModifyJobs modifyJobs)  {
        try {
            return postJobsCommand(accessToken, UNSTOP, getPermissons(accessToken).getJob().isUnstop(), modifyJobs);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobsSetRunTime(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, SET_RUN_TIME, getPermissons(accessToken).getJob().isSetRunTime(), modifyJobs);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobsEndAllTasks(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, END, getPermissons(accessToken).getJob().isEndAllTasks(), modifyJobs);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobsSuspendAllTasks(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, SUSPEND, getPermissons(accessToken).getJob().isSuspendAllTasks(), modifyJobs);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobsContinueAllTasks(String accessToken, ModifyJobs modifyJobs) {
        try {
            return postJobsCommand(accessToken, CONTINUE, getPermissons(accessToken).getJob().isContinueAllTasks(), modifyJobs);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private Date executeModifyJobCommand(ModifyJob modifyJob, String command) {

        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            
            JSCmdModifyJob jsCmdModifyJob = Globals.schedulerObjectFactory.createModifyJob();
            jsCmdModifyJob.setCmdIfNotEmpty(command);
            jsCmdModifyJob.setJobIfNotEmpty(modifyJob.getJob());
            if (SET_RUN_TIME.equals(command)) {
                try {
                    //TODO order.getRunTime() is checked against scheduler.xsd
                    JSObjRunTime objRuntime = new JSObjRunTime(Globals.schedulerObjectFactory, modifyJob.getRunTime());
                    jsCmdModifyJob.setRunTime(objRuntime);
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(modifyJob.getRunTime());
                }
            }
            String xml = jsCmdModifyJob.toXMLString();
            jocXmlCommand.executePostWithThrowBadRequest(xml);
            
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, modifyJob));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, modifyJob));
        }
        return null;
    }

    private JOCDefaultResponse postJobsCommand(String accessToken, String command, boolean permission, ModifyJobs modifyJobs) {
        API_CALL += command;
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(modifyJobs.getJobschedulerId(), permission);
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
                JocError err = new JocError();
                err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyJobs));
                return JOCDefaultResponse.responseStatus419(listOfErrors, err);
            }
            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyJobs));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyJobs));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
}
