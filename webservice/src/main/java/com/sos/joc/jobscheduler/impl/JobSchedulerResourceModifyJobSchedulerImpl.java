package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobScheduler;
import com.sos.joc.response.JOCDefaultResponse;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifySpooler;
import com.sos.scheduler.model.objects.Spooler;

@Path("jobscheduler")

public class JobSchedulerResourceModifyJobSchedulerImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobScheduler {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    protected JobSchedulerModifyJobSchedulerBody jobSchedulerModifyJobSchedulerBody;

    private JOCDefaultResponse check(boolean right) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerModifyJobSchedulerBody.getJobschedulerId(),right);
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

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdModifySpooler jsCmdModifySpooler = new JSCmdModifySpooler(schedulerObjectFactory);
            jsCmdModifySpooler.setCmd(cmd);
            jsCmdModifySpooler.setTimeoutIfNotEmpty(jobSchedulerModifyJobSchedulerBody.getTimeoutAsString());

            String xml = schedulerObjectFactory.toXMLString(jsCmdModifySpooler);
            jocXmlCommand.excutePost(xml);
            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

    private void init(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) {
        this.jobSchedulerModifyJobSchedulerBody = jobSchedulerTerminateBody;
        this.jobschedulerUser = new JobSchedulerUser(accessToken);

        jobSchedulerIdentifier = new JobSchedulerIdentifier(jobSchedulerTerminateBody.getJobschedulerId());
        jobSchedulerIdentifier.setHost(jobSchedulerTerminateBody.getHost());
        jobSchedulerIdentifier.setPort(jobSchedulerTerminateBody.getPort());

    }

    @Override
    public JOCDefaultResponse postJobschedulerTerminate(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().isTerminate());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("terminate");
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().getRestart().isTerminate());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("terminate_and_restart");
    }

    @Override
    public JOCDefaultResponse postJobschedulerAbort(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().isAbort());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("abort_immediately");
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartAbort(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().getRestart().isAbort());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("abort_immediately_and_restart");
    }

    @Override
    public JOCDefaultResponse postJobschedulerPause(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().isPause());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("pause");
    }

    @Override
    public JOCDefaultResponse postJobschedulerContinue(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMaster().isContinue());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerCommand("continue");
    }
}
