package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobScheduler;
import com.sos.joc.model.jobscheduler.UrlTimeoutParamSchema;
import com.sos.scheduler.model.commands.JSCmdModifySpooler;

@Path("jobscheduler")

public class JobSchedulerResourceModifyJobSchedulerImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    protected UrlTimeoutParamSchema urlTimeoutParamSchema;

    private JOCDefaultResponse check(boolean right) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(urlTimeoutParamSchema.getJobschedulerId(),right);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    
        return null;

    }

    private JOCDefaultResponse executeModifyJobSchedulerCommand(String cmd) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdModifySpooler jsCmdModifySpooler = new JSCmdModifySpooler(Globals.schedulerObjectFactory);
            jsCmdModifySpooler.setCmd(cmd);
            jsCmdModifySpooler.setTimeoutIfNotEmpty(urlTimeoutParamSchema.getTimeout());

            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdModifySpooler);
            LOGGER.debug(String.format("Executing command: %s",xml));
            jocXmlCommand.excutePost(xml);
         
            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

    private void init(String accessToken, UrlTimeoutParamSchema jobSchedulerTerminateBody) {
        this.urlTimeoutParamSchema = jobSchedulerTerminateBody;
        this.jobschedulerUser = new JobSchedulerUser(accessToken);

        jobSchedulerIdentifier = new JobSchedulerIdentifier(jobSchedulerTerminateBody.getJobschedulerId());
        jobSchedulerIdentifier.setHost(jobSchedulerTerminateBody.getHost());
        jobSchedulerIdentifier.setPort(jobSchedulerTerminateBody.getPort());

    }

    @Override
    public JOCDefaultResponse postJobschedulerTerminate(String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception {

        init(accessToken, urlTimeoutParamSchema);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().isTerminate());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("terminate");
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception {

        init(accessToken, urlTimeoutParamSchema);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().getRestart().isTerminate());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("terminate_and_restart");
    }

    @Override
    public JOCDefaultResponse postJobschedulerAbort(String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception {

        init(accessToken, urlTimeoutParamSchema);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().isAbort());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("abort_immediately");
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartAbort(String accessToken, UrlTimeoutParamSchema jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().getRestart().isAbort());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("abort_immediately_and_restart");
    }

    @Override
    public JOCDefaultResponse postJobschedulerPause(String accessToken, UrlTimeoutParamSchema jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().isPause());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("pause");
    }

    @Override
    public JOCDefaultResponse postJobschedulerContinue(String accessToken, UrlTimeoutParamSchema jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().isContinue());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("continue");
    }
}
