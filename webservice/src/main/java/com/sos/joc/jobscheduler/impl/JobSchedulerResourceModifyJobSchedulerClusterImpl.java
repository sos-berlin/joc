package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyJobSchedulerClusterAudit;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobSchedulerCluster;
import com.sos.joc.model.jobscheduler.TimeoutParameter;
import com.sos.xml.XMLBuilder;

@Path("jobscheduler")
public class JobSchedulerResourceModifyJobSchedulerClusterImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobSchedulerCluster {

    private static final String TERMINATE = "terminate";
    private static final String RESTART = "restart";
    private static final String TERMINATE_FAILSAFE = "terminate_failsafe";
    private static final String REACTIVATE = "reactivate";
    private static String API_CALL = "./jobscheduler/cluster/";

    @Override
    public JOCDefaultResponse postJobschedulerTerminate(String xAccessToken, String accessToken, TimeoutParameter timeoutParameter) throws Exception {
        return postJobschedulerTerminate(getAccessToken(xAccessToken, accessToken), timeoutParameter);
    }

    public JOCDefaultResponse postJobschedulerTerminate(String accessToken, TimeoutParameter timeoutParameter) throws Exception {
        try {
            boolean permission = getPermissonsJocCockpit(timeoutParameter.getJobschedulerId(), accessToken).getJobschedulerMasterCluster()
                    .getExecute().isTerminate();
            return executeModifyJobSchedulerClusterCommand(TERMINATE, timeoutParameter, accessToken, permission);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartTerminate(String xAccessToken, String accessToken, TimeoutParameter timeoutParameter)
            throws Exception {
        return postJobschedulerRestartTerminate(getAccessToken(xAccessToken, accessToken), timeoutParameter);
    }

    public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, TimeoutParameter timeoutParameter) throws Exception {
        try {
            boolean permission = getPermissonsJocCockpit(timeoutParameter.getJobschedulerId(), accessToken).getJobschedulerMasterCluster()
                    .getExecute().isRestart();
            return executeModifyJobSchedulerClusterCommand(RESTART, timeoutParameter, accessToken, permission);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerReactivate(String xAccessToken, String accessToken, TimeoutParameter timeoutParameter)
            throws Exception {
        return postJobschedulerReactivate(getAccessToken(xAccessToken, accessToken), timeoutParameter);
    }

    public JOCDefaultResponse postJobschedulerReactivate(String accessToken, TimeoutParameter timeoutParameter) throws Exception {
        try {
            boolean permission = getPermissonsJocCockpit(timeoutParameter.getJobschedulerId(), accessToken).getJobschedulerMasterCluster()
                    .getExecute().isRestart();
            return executeModifyJobSchedulerClusterCommand(REACTIVATE, timeoutParameter, accessToken, permission);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerTerminateFailSafe(String xAccessToken, String accessToken, TimeoutParameter timeoutParameter)
            throws Exception {
        return postJobschedulerTerminateFailSafe(getAccessToken(xAccessToken, accessToken), timeoutParameter);
    }

    public JOCDefaultResponse postJobschedulerTerminateFailSafe(String accessToken, TimeoutParameter timeoutParameter) throws Exception {
        try {
            boolean permission = getPermissonsJocCockpit(timeoutParameter.getJobschedulerId(), accessToken).getJobschedulerMasterCluster()
                    .getExecute().isTerminateFailSafe();
            return executeModifyJobSchedulerClusterCommand(TERMINATE_FAILSAFE, timeoutParameter, accessToken, permission);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private JOCDefaultResponse executeModifyJobSchedulerClusterCommand(String command, TimeoutParameter timeoutParameter, String accessToken,
            boolean permission) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(API_CALL + command, timeoutParameter, accessToken, timeoutParameter.getJobschedulerId(),
                permission);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        checkRequiredComment(timeoutParameter.getAuditLog());
        ModifyJobSchedulerClusterAudit clusterAudit = new ModifyJobSchedulerClusterAudit(timeoutParameter);
        logAuditMessage(clusterAudit);

        XMLBuilder xml = null;
        if (command.equals(REACTIVATE)) {
            if (!"passive".equals(dbItemInventoryInstance.getClusterType())) {
                throw new JobSchedulerBadRequestException("Reactivation makes only sense for a passive cluster");
            }
            if (dbItemInventoryInstance.getPrecedence() == null || dbItemInventoryInstance.getPrecedence() == 0) {
                throw new JobSchedulerBadRequestException("Reactivation makes only sense if a backup JobScheduler is currently active");
            }
            xml = new XMLBuilder("modify_spooler");
        } else {
            xml = new XMLBuilder("terminate");
        }

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
        case REACTIVATE:
            xml.addAttribute("cmd", "terminate_and_restart");
            break;
        }
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(xml.asXML(), getAccessToken());
        storeAuditLogEntry(clusterAudit);

        return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
    }
}
