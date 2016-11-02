package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceModifyJobScheduler;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;
import com.sos.scheduler.model.commands.JSCmdModifySpooler;

@Path("jobscheduler")
public class JobSchedulerResourceModifyJobSchedulerImpl extends JOCResourceImpl implements IJobSchedulerResourceModifyJobScheduler {

    private static final String TERMINATE = "terminate";
    private static final String RESTART = "restart";
    private static final String ABORT = "abort";
    private static final String ABORT_AND_RESTART = "abort_and_restart";
    private static final String PAUSE = "pause";
    private static final String CONTINUE = "continue";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceModifyJobSchedulerImpl.class);
    private static String API_CALL = "./jobscheduler/";

    @Override
    public JOCDefaultResponse postJobschedulerTerminate(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        API_CALL += TERMINATE;
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().isTerminate());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand(TERMINATE, urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartTerminate(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        API_CALL += RESTART;
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().getRestart()
                    .isTerminate());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand("terminate_and_restart", urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerAbort(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        API_CALL += ABORT;
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().isAbort());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand("abort_immediately", urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerRestartAbort(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        API_CALL += ABORT_AND_RESTART;
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().getRestart()
                    .isAbort());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }

            return executeModifyJobSchedulerCommand("abort_immediately_and_restart", urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerPause(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        API_CALL += PAUSE;
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().isPause());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand(PAUSE, urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobschedulerContinue(String accessToken, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        API_CALL += CONTINUE;
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse JOCDefaultResponse = init(accessToken, urlTimeoutParamSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().isContinue());
            if (JOCDefaultResponse != null) {
                return JOCDefaultResponse;
            }
            return executeModifyJobSchedulerCommand(CONTINUE, urlTimeoutParamSchema);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, urlTimeoutParamSchema));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private JOCDefaultResponse executeModifyJobSchedulerCommand(String cmd, HostPortTimeOutParameter urlTimeoutParamSchema) throws Exception {
        getJobSchedulerInstanceByHostPort(urlTimeoutParamSchema);
        JSCmdModifySpooler jsCmdModifySpooler = new JSCmdModifySpooler(Globals.schedulerObjectFactory);
        jsCmdModifySpooler.setCmd(cmd);
        jsCmdModifySpooler.setTimeoutIfNotEmpty(urlTimeoutParamSchema.getTimeout());

        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
        jocXmlCommand.executePostWithThrowBadRequest(jsCmdModifySpooler.toXMLString(), getAccessToken());
        return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
    }

    private void getJobSchedulerInstanceByHostPort(HostPortTimeOutParameter jobSchedulerTerminateBody) throws Exception {
        JobSchedulerIdentifier jobSchedulerIdentifier = new JobSchedulerIdentifier(jobSchedulerTerminateBody.getJobschedulerId());
        jobSchedulerIdentifier.setHost(jobSchedulerTerminateBody.getHost());
        jobSchedulerIdentifier.setPort(jobSchedulerTerminateBody.getPort());

        InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        dbItemInventoryInstance = dbLayer.getInventoryInstanceByHostPort(jobSchedulerIdentifier);

        if (dbItemInventoryInstance == null) {
            String errMessage = String.format("jobschedulerId for host:%s and port:%s not found in table %s", jobSchedulerIdentifier.getHost(), jobSchedulerIdentifier.getPort(),
                    DBLayer.TABLE_INVENTORY_INSTANCES);
            throw new DBInvalidDataException(errMessage);
        }
    }
}
