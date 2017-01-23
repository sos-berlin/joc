package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobSchedulerCluster;
import com.sos.joc.model.jobscheduler.TimeoutParameter;

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
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, timeoutParameter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
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
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, timeoutParameter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
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
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, timeoutParameter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
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
        logAuditMessage(timeoutParameter);
        XMLBuilder xml = new XMLBuilder("terminate");
        if (timeoutParameter.getTimeout() != null) {
            xml.addAttribute("timeout", timeoutParameter.getTimeout().toString());
        }
        switch (command) {
        case TERMINATE:
            xml.addAttribute("all_schedulers", "yes");
            break;
        case RESTART:
            xml.addAttribute("all_schedulers", "yes").addAttribute("restart", "yes");
            break;
        case TERMINATE_FAILSAFE:
            xml.addAttribute("continue_exclusive_operation", "yes");
            break;
        }
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(xml.asXML(), getAccessToken());
        return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
    }
}
