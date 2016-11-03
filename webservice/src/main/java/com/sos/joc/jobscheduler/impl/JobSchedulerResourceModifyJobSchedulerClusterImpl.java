package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobSchedulerCluster;
import com.sos.joc.model.jobscheduler.TimeoutParameter;
import com.sos.scheduler.model.commands.JSCmdTerminate;

@Path("jobscheduler")
public class JobSchedulerResourceModifyJobSchedulerClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobSchedulerCluster {

    private static final String TERMINATE = "terminate";
    private static final String RESTART = "restart";
    private static final String TERMINATE_FAILSAFE = "terminate_failsafe";
    private static String API_CALL = "./jobscheduler/cluster/";

    @Override
    public JOCDefaultResponse postJobschedulerTerminate(String accessToken, TimeoutParameter timeoutParameter) throws Exception {
        try {
            initLogging(API_CALL + TERMINATE, timeoutParameter);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, timeoutParameter.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMasterCluster().isTerminate());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerClusterCommand(TERMINATE, timeoutParameter);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, TimeoutParameter timeoutParameter) throws Exception {
        try {
            initLogging(API_CALL + RESTART, timeoutParameter);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, timeoutParameter.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMasterCluster().isRestart());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerClusterCommand(RESTART, timeoutParameter);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerTerminateFailSafe(String accessToken, TimeoutParameter timeoutParameter) throws Exception {
        try {
            initLogging(API_CALL + TERMINATE_FAILSAFE, timeoutParameter);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, timeoutParameter.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMasterCluster().isTerminateFailSafe());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerClusterCommand(TERMINATE_FAILSAFE, timeoutParameter);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private JOCDefaultResponse executeModifyJobSchedulerClusterCommand(String command, TimeoutParameter timeoutParameter) throws Exception {
        logAuditMessage();
        JSCmdTerminate jsCmdTerminate = new JSCmdTerminate(Globals.schedulerObjectFactory);
        jsCmdTerminate.setTimeoutIfNotEmpty(timeoutParameter.getTimeout());
        switch (command) {
        case TERMINATE:
            jsCmdTerminate.setAllSchedulers(WebserviceConstants.YES);
            break;
        case RESTART:
            jsCmdTerminate.setAllSchedulers(WebserviceConstants.YES);
            jsCmdTerminate.setRestart(WebserviceConstants.YES);
            break;
        case TERMINATE_FAILSAFE:
            jsCmdTerminate.setContinueExclusiveOperation(WebserviceConstants.YES);
            break;
        }
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
        jocXmlCommand.executePostWithThrowBadRequest(jsCmdTerminate.toXMLString(), getAccessToken());
        return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
    }
}
