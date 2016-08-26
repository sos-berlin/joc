package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerClusterBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobSchedulerCluster;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdTerminate;
import com.sos.scheduler.model.objects.Spooler;

@Path("jobscheduler")

public class JobSchedulerResourceModifyJobSchedulerClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobSchedulerCluster {

    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);
    private JobSchedulerModifyJobSchedulerClusterBody jobSchedulerModifyJobSchedulerClusterBody;

    private JOCDefaultResponse check(boolean right) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerModifyJobSchedulerClusterBody.getJobschedulerId(),right);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

        return null;

    }

    private JOCDefaultResponse executeModifyJobSchedulerClusterCommand(String restart, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdTerminate jsCmdTerminate = new JSCmdTerminate(schedulerObjectFactory);
            jsCmdTerminate.setAllSchedulersIfNotEmpty(YES);
            jsCmdTerminate.setContinueExclusiveOperationIfNotEmpty(NO);
            jsCmdTerminate.setRestartIfNotEmpty(restart);
            jsCmdTerminate.setTimeoutIfNotEmpty(jobSchedulerClusterTerminateBody.getTimeoutAsString());

            jocXmlCommand.excutePost(schedulerObjectFactory.toXMLString(jsCmdTerminate));
            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

    private void init(String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerModifyJobSchedulerClusterBody) {
        this.jobSchedulerModifyJobSchedulerClusterBody = jobSchedulerModifyJobSchedulerClusterBody;
        this.jobschedulerUser = new JobSchedulerUser(accessToken);

        jobSchedulerIdentifier = new JobSchedulerIdentifier(jobSchedulerModifyJobSchedulerClusterBody.getJobschedulerId());
    }

    @Override
    public JOCDefaultResponse postJobschedulerTerminate(String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception {
        init(accessToken, jobSchedulerClusterTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMasterCluster().isTerminate());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerClusterCommand(NO, jobSchedulerClusterTerminateBody);
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception {
        init(accessToken, jobSchedulerClusterTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMasterCluster().isRestart());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerClusterCommand(YES, jobSchedulerClusterTerminateBody);
    }

    @Override
    public JOCDefaultResponse postJobschedulerTerminateFailSafe(String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception {
        init(accessToken, jobSchedulerClusterTerminateBody);
        JOCDefaultResponse JOCDefaultResponse = check(getPermissons(accessToken).getJobschedulerMasterCluster().isTerminateFailSafe());

        if (JOCDefaultResponse != null) {
            return JOCDefaultResponse;
        }

        return executeModifyJobSchedulerClusterCommand(NO, jobSchedulerClusterTerminateBody);
    }

}
