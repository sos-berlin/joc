package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobScheduler;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;

@Path("jobscheduler")
public class JobSchedulerResourceModifyJobSchedulerImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobScheduler {

    private static final String TERMINATE = "terminate";
    private static final String[] RESTART = { "restart", "terminate_and_restart" };
    private static final String[] ABORT = { "abort", "abort_immediately" };
    private static final String[] ABORT_AND_RESTART = { "abort_and_restart", "abort_immediately_and_restart" };
    private static final String PAUSE = "pause";
    private static final String CONTINUE = "continue";
    private static String API_CALL = "./jobscheduler/";

    @Override
    public JOCDefaultResponse postJobschedulerTerminate(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        try {
            initLogging(API_CALL + TERMINATE, urlTimeoutParamSchema);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMaster().isTerminate());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand(TERMINATE, urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        try {
            initLogging(API_CALL + RESTART[0], urlTimeoutParamSchema);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMaster().getRestart().isTerminate());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand(RESTART[1], urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerAbort(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        try {
            initLogging(API_CALL + ABORT[0], urlTimeoutParamSchema);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMaster().isAbort());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand(ABORT[1], urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartAbort(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        try {
            initLogging(API_CALL + ABORT_AND_RESTART[0], urlTimeoutParamSchema);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMaster().getRestart().isAbort());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }

            return executeModifyJobSchedulerCommand(ABORT_AND_RESTART[1], urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerPause(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        try {
            initLogging(API_CALL + PAUSE, urlTimeoutParamSchema);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMaster().isPause());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand(PAUSE, urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerContinue(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        try {
            initLogging(API_CALL + CONTINUE, urlTimeoutParamSchema);
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMaster().isContinue());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand(CONTINUE, urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private JOCDefaultResponse executeModifyJobSchedulerCommand(String cmd, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        getJobSchedulerInstanceByHostPort(urlTimeoutParamSchema);
        XMLBuilder xml = new XMLBuilder("modify_spooler");
        xml.addAttribute("cmd", cmd);
        if (urlTimeoutParamSchema.getTimeout() != null) {
            xml.addAttribute("timeout", urlTimeoutParamSchema.getTimeout().toString());
        }
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
        if (cmd.contains(ABORT[0])) {
            try {
                jocXmlCommand.executePost(xml.asXML(), getAccessToken());
            } catch (JobSchedulerNoResponseException e) {
                // JobScheduler sends always no response if "abort" is called
            } catch (JobSchedulerConnectionRefusedException e) {
                throw e;
            }
        } else {
            jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
        }
        return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
    }

    private void getJobSchedulerInstanceByHostPort(HostPortTimeOutParameter jobSchedulerTerminateBody) throws DBInvalidDataException,
            DBMissingDataException {
        JobSchedulerIdentifier jobSchedulerIdentifier = new JobSchedulerIdentifier(jobSchedulerTerminateBody.getJobschedulerId());
        jobSchedulerIdentifier.setHost(jobSchedulerTerminateBody.getHost());
        jobSchedulerIdentifier.setPort(jobSchedulerTerminateBody.getPort());

        InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        dbItemInventoryInstance = dbLayer.getInventoryInstanceByHostPort(jobSchedulerIdentifier);

        if (dbItemInventoryInstance == null) {
            String errMessage = String.format("jobscheduler with id:%1$s, host:%2$s and port:%3$s couldn't be found in table %4$s",
                    jobSchedulerIdentifier.getId(), jobSchedulerIdentifier.getHost(), jobSchedulerIdentifier.getPort(),
                    DBLayer.TABLE_INVENTORY_INSTANCES);
            throw new DBInvalidDataException(errMessage);
        }
    }
}
