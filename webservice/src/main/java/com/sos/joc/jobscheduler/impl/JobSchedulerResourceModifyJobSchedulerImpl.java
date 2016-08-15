package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobScheduler;
import com.sos.joc.response.JocCockpitResponse;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifySpooler;
import com.sos.scheduler.model.objects.Spooler;

@Path("jobscheduler")

public class JobSchedulerResourceModifyJobSchedulerImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobScheduler {
 
    protected JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody;

    private JocCockpitResponse check(boolean right) {
        
        if (jobschedulerUser.isTimedOut()) {
            return JocCockpitResponse.responseStatus440(jobschedulerUser);
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JocCockpitResponse.responseStatus401(jobschedulerUser.getAccessToken());
        }

        if (jobSchedulerTerminateBody.getJobschedulerId() == null) {
            return JocCockpitResponse.responseStatus420("schedulerId is null");
        }

        if (!right) {
            return JocCockpitResponse.responseStatus403(jobschedulerUser);
        }

        
        return null;

    }

    private JocCockpitResponse executeModifyJobSchedulerCommand(String cmd) {
        try {
            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(jobSchedulerIdentifier);
            
            if (dbItemInventoryInstance == null) {
                return JocCockpitResponse.responseStatus420(String.format("schedulerId %s not found in table %s",jobSchedulerIdentifier.getSchedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES));
            }

            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdModifySpooler jsCmdModifySpooler = new JSCmdModifySpooler(schedulerObjectFactory);
            jsCmdModifySpooler.setCmd(cmd);
            jsCmdModifySpooler.setTimeoutIfNotEmpty(jobSchedulerTerminateBody.getTimeoutAsString());

            String xml = schedulerObjectFactory.toXMLString(jsCmdModifySpooler);
            jocXmlCommand.excutePost(xml);
            return JocCockpitResponse.responseStatus200(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JocCockpitResponse.responseStatus420(e.getMessage());
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
    public JocCockpitResponse postJobschedulerTerminate(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JocCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMaster().isTerminate());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("terminate");
    }

    @Override
    public JocCockpitResponse postJobschedulerRestartTerminate(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JocCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMaster().getRestart().isTerminate());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("terminate_and_restart");
    }

    @Override
    public JocCockpitResponse postJobschedulerAbort(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {

        init(accessToken, jobSchedulerTerminateBody);
        JocCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMaster().isAbort());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("abort_immediately");
    }

    @Override
    public JocCockpitResponse postJobschedulerRestartAbort(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JocCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMaster().getRestart().isAbort());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("abort_immediately_and_restart");
    }

    @Override
    public JocCockpitResponse postJobschedulerPause(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JocCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMaster().isPause());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("pause");
    }
    
    @Override
    public JocCockpitResponse postJobschedulerContinue(String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception {
        init(accessToken, jobSchedulerTerminateBody);
        JocCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMaster().isContinue());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerCommand("continue");
    }
}
