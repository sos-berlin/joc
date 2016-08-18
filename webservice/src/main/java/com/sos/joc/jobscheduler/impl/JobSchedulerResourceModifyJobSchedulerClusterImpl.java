package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerClusterBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobSchedulerCluster;
import com.sos.joc.response.JOCCockpitResponse;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdTerminate;
import com.sos.scheduler.model.objects.Spooler;

@Path("jobscheduler")

public class JobSchedulerResourceModifyJobSchedulerClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobSchedulerCluster {

    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);
    private JobSchedulerIdentifier jobSchedulerIdentifier;
    private JobSchedulerModifyJobSchedulerClusterBody jobSchedulerModifyJobSchedulerClusterBody;

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


        if (jobSchedulerModifyJobSchedulerClusterBody.getJobschedulerId() == null) {
            return JOCCockpitResponse.responseStatus420("schedulerId is null");
        }

        if (!right) {
            return JOCCockpitResponse.responseStatus403(jobschedulerUser);
        }

        if (!

        getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()) {
            return JOCCockpitResponse.responseStatus403(jobschedulerUser);
        }

        return null;

    }

    private JOCCockpitResponse executeModifyJobSchedulerClusterCommand(String restart, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) {
        try {
            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(jobSchedulerIdentifier);

            if (dbItemInventoryInstance == null) {
                return JOCCockpitResponse.responseStatus420(String.format("schedulerId %s not found in table %s", jobSchedulerIdentifier.getSchedulerId(),
                        DBLayer.TABLE_INVENTORY_INSTANCES));
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdTerminate jsCmdTerminate = new JSCmdTerminate(schedulerObjectFactory);
            jsCmdTerminate.setAllSchedulersIfNotEmpty(YES);
            jsCmdTerminate.setContinueExclusiveOperationIfNotEmpty(NO);
            jsCmdTerminate.setRestartIfNotEmpty(restart);
            jsCmdTerminate.setTimeoutIfNotEmpty(jobSchedulerClusterTerminateBody.getTimeoutAsString());

            jocXmlCommand.excutePost(schedulerObjectFactory.toXMLString(jsCmdTerminate));
            return JOCCockpitResponse.responseStatus200(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCCockpitResponse.responseStatus420(e.getMessage());
        }
    }

    private void init(String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerModifyJobSchedulerClusterBody) {
        this.jobSchedulerModifyJobSchedulerClusterBody = jobSchedulerModifyJobSchedulerClusterBody;
        this.jobschedulerUser = new JobSchedulerUser(accessToken);

        jobSchedulerIdentifier = new JobSchedulerIdentifier(jobSchedulerModifyJobSchedulerClusterBody.getJobschedulerId());
    }

    @Override
    public JOCCockpitResponse postJobschedulerTerminate(String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception {
        init(accessToken, jobSchedulerClusterTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMasterCluster().isTerminate());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerClusterCommand(NO, jobSchedulerClusterTerminateBody);
    }

    @Override
    public JOCCockpitResponse postJobschedulerRestartTerminate(String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception {
        init(accessToken, jobSchedulerClusterTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMasterCluster().isRestart());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerClusterCommand(YES, jobSchedulerClusterTerminateBody);
    }

    @Override
    public JOCCockpitResponse postJobschedulerTerminateFailSafe(String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception {
        init(accessToken, jobSchedulerClusterTerminateBody);
        JOCCockpitResponse jocCockpitResponse = check(getPermissons().getJobschedulerMasterCluster().isTerminateFailSafe());

        if (jocCockpitResponse != null) {
            return jocCockpitResponse;
        }

        return executeModifyJobSchedulerClusterCommand(NO, jobSchedulerClusterTerminateBody);
    }

}
