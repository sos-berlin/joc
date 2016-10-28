package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobSchedulerCluster;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;
import com.sos.scheduler.model.commands.JSCmdTerminate;

@Path("jobscheduler")

public class JobSchedulerResourceModifyJobSchedulerClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobSchedulerCluster {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceModifyJobSchedulerClusterImpl.class);
    private HostPortTimeOutParameter jobSchedulerModifyJobSchedulerClusterBody;

    @Override
    public JOCDefaultResponse postJobschedulerTerminate(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        try {
            init(accessToken, urlTimeoutParamSchema);

            JOCDefaultResponse JOCDefaultResponse = check(accessToken, getPermissons(accessToken).getJobschedulerMasterCluster().isTerminate());

            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }

            return executeModifyJobSchedulerClusterCommand(WebserviceConstants.NO, urlTimeoutParamSchema);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {

        try {
            init(accessToken, urlTimeoutParamSchema);

            JOCDefaultResponse JOCDefaultResponse = check(accessToken, getPermissons(accessToken).getJobschedulerMasterCluster().isRestart());

            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }

            return executeModifyJobSchedulerClusterCommand(WebserviceConstants.YES, urlTimeoutParamSchema);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }

    @Override
    public JOCDefaultResponse postJobschedulerTerminateFailSafe(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        try {
            init(accessToken, urlTimeoutParamSchema);

            JOCDefaultResponse JOCDefaultResponse = check(accessToken, getPermissons(accessToken).getJobschedulerMasterCluster()
                    .isTerminateFailSafe());

            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }

            return executeModifyJobSchedulerClusterCommand(WebserviceConstants.NO, urlTimeoutParamSchema);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }
    
    private JOCDefaultResponse check(String accessToken, boolean right) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerModifyJobSchedulerClusterBody.getJobschedulerId(), right);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

        return null;

    }

    private JOCDefaultResponse executeModifyJobSchedulerClusterCommand(String restart, HostPortTimeOutParameter urlTimeoutParamSchema) {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdTerminate jsCmdTerminate = new JSCmdTerminate(Globals.schedulerObjectFactory);
            jsCmdTerminate.setAllSchedulersIfNotEmpty(WebserviceConstants.YES);
            jsCmdTerminate.setContinueExclusiveOperationIfNotEmpty(WebserviceConstants.NO);
            jsCmdTerminate.setRestartIfNotEmpty(restart);
            jsCmdTerminate.setTimeoutIfNotEmpty(urlTimeoutParamSchema.getTimeout());

            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdTerminate);
            LOGGER.debug(String.format("Executing command: %s", xml));
            jocXmlCommand.executePost(xml);
            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

    private void init(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) {
        this.jobSchedulerModifyJobSchedulerClusterBody = urlTimeoutParamSchema;
        this.jobschedulerUser = new JobSchedulerUser(accessToken);

        jobSchedulerIdentifier = new JobSchedulerIdentifier(urlTimeoutParamSchema.getJobschedulerId());
    }
}
