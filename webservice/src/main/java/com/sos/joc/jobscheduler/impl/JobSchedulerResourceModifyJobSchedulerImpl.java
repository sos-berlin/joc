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
import com.sos.joc.response.JOCCockpitResponse;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifySpooler;
import com.sos.scheduler.model.objects.Spooler;

@Path("jobscheduler")

public class JobSchedulerResourceModifyJobSchedulerImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobScheduler {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    protected JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody;

    private JOCCockpitResponse check(boolean right) {

        try {
            if (!jobschedulerUser.isAuthenticated()) {
                return JOCCockpitResponse.responseStatus401(jobschedulerUser.getAccessToken());
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            return JOCCockpitResponse.responseStatus440(jobschedulerUser,e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return JOCCockpitResponse.responseStatus420(e.getMessage());
        }

        if (jobSchedulerTerminateBody.getJobschedulerId() == null) {
            return JOCCockpitResponse.responseStatus420("schedulerId is null");
        }

        if (!right) {
            return JOCCockpitResponse.responseStatus403(jobschedulerUser);
        }

        return null;

    }

    private JOCCockpitResponse executeModifyJobSchedulerCommand(String cmd) {
        try {
            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(jobSchedulerIdentifier);

            if (dbItemInventoryInstance == null) {
                return JOCCockpitResponse.responseStatus420(String.format("schedulerId %s not found in table %s", jobSchedulerIdentifier.getSchedulerId(),
                        DBLayer.TABLE_INVENTORY_INSTANCES));
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdModifySpooler jsCmdModifySpooler = new JSCmdModifySpooler(schedulerObjectFactory);
            jsCmdModifySpooler.setCmd(cmd);
            jsCmdModifySpooler.setTimeoutIfNotEmpty(jobSchedulerTerminateBody.getTimeoutAsString());

            String xml = schedulerObjectFactory.toXMLString(jsCmdModifySpooler);
            jocXmlCommand.excutePost(xml);
            return JOCCockpitResponse.responseStatus200(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCCockpitResponse.responseStatus420(e.getMessage());
        }
    }

    private void init(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) {
        this.jobSchedulerTerminateBody = jobSchedulerTerminateBody;
        this.jobschedulerUser = new JobSchedulerUser(accessToken);

        jobSchedulerIdentifier = new JobSchedulerIdentifier(jobSchedulerTerminateBody.getJobschedulerId());
        jobSchedulerIdentifier.setHost(jobSchedulerTerminateBody.getHost());
        jobSchedulerIdentifier.setPort(jobSchedulerTerminateBody.getPort());

    }

    @Override
    public JOCCockpitResponse postJobschedulerTerminate(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons(accessToken).getJobschedulerMaster().isTerminate());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("terminate");
    }

    @Override
    public JOCCockpitResponse postJobschedulerRestartTerminate(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons(accessToken).getJobschedulerMaster().getRestart().isTerminate());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("terminate_and_restart");
    }

    @Override
    public JOCCockpitResponse postJobschedulerAbort(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons(accessToken).getJobschedulerMaster().isAbort());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("abort_immediately");
    }

    @Override
    public JOCCockpitResponse postJobschedulerRestartAbort(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons(accessToken).getJobschedulerMaster().getRestart().isAbort());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("abort_immediately_and_restart");
    }

    @Override
    public JOCCockpitResponse postJobschedulerPause(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons(accessToken).getJobschedulerMaster().isPause());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("pause");
    }

    @Override
    public JOCCockpitResponse postJobschedulerContinue(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons(accessToken).getJobschedulerMaster().isContinue());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("continue");
    }
}
