package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.scheduler.model.commands.JSCmdShowState;

public class JobSchedulerResource extends JOCResourceImpl {
    private static final String XPATH_SPOOLER_STATE = "/spooler/answer/state";
    private static final String SPOOLER_RUNNING_SINCE = "spooler_running_since";
    private static final String TCP_PORT = "tcp_port";
    private static final String ID = "id";
    private static final String HOST = "host";
    private static final String FOLDER = "folder";
    private static final String DOES_NOT_EXIST = "/does/not/exist";
    private static final String FOLDERS_NO_SUBFOLDERS = "folders no_subfolders";
    private JobSchedulerId jobSchedulerId;

    public void setJobSchedulerFilterSchema(JobSchedulerId jobSchedulerId) {
        this.jobSchedulerId = jobSchedulerId;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);
    private String accessToken;

    public JobSchedulerResource(String accessToken, JobSchedulerId jobSchedulerId) {
        super();
        this.jobSchedulerId = jobSchedulerId;
        jobschedulerUser = new JobSchedulerUser(accessToken);
        this.accessToken = accessToken;
    }

    
    public JOCDefaultResponse postJobscheduler() {
        LOGGER.debug("init jobscheduler");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerId.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobSchedulerV200 entity = new JobSchedulerV200();
            entity.setDeliveryDate(new Date());
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());
            try {
                jocXmlCommand.excutePost(getXMLCommand());
            } catch (Exception e) {
                entity.setJobscheduler(getUnreachableJobScheduler());
                return JOCDefaultResponse.responseStatus200(entity);
            }
            jocXmlCommand.throwJobSchedulerError();
            entity.setJobscheduler(getJobScheduler(jocXmlCommand));
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private String getXMLCommand() {
        JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
        jsCmdShowState.setWhat(FOLDERS_NO_SUBFOLDERS);;
        jsCmdShowState.setPath(DOES_NOT_EXIST);
        jsCmdShowState.setSubsystems(FOLDER);
        return jsCmdShowState.toXMLString();
    }
    
    private JobSchedulerV getUnreachableJobScheduler() {
        JobSchedulerV jobscheduler = new JobSchedulerV();
        jobscheduler.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
        jobscheduler.setHost(dbItemInventoryInstance.getHostname());
        jobscheduler.setPort(dbItemInventoryInstance.getPort());
        JobSchedulerState jobSchedulerState = new JobSchedulerState();
        jobSchedulerState.set_text(JobSchedulerStateText.UNREACHABLE);
        jobSchedulerState.setSeverity(2);
        jobscheduler.setState(jobSchedulerState);
        return jobscheduler;
    }
    
    private JobSchedulerV getJobScheduler(JOCXmlCommand jocXmlCommand) throws Exception {
        JobSchedulerV jobscheduler = new JobSchedulerV();
        jobscheduler.setSurveyDate(jocXmlCommand.getSurveyDate());
        Element stateElem = jocXmlCommand.executeXPath(XPATH_SPOOLER_STATE);
        jobscheduler.setHost(jocXmlCommand.getAttribute(HOST));
        jobscheduler.setJobschedulerId(jocXmlCommand.getAttribute(ID));
        jobscheduler.setPort(jocXmlCommand.getAttributeAsInteger(TCP_PORT));
        jobscheduler.setStartedAt(jocXmlCommand.getAttributeAsDate(SPOOLER_RUNNING_SINCE));
        jobscheduler.setState(getJobSchedulerState(stateElem));
        if (stateElem.hasAttribute("waiting_errno_text")) {
            com.sos.joc.model.common.Error err = new com.sos.joc.model.common.Error();
            err.setCode(stateElem.getAttribute("waiting_errno"));
            err.setMessage(stateElem.getAttribute("waiting_errno_text"));
            jobscheduler.setError(err);
        }
        return jobscheduler;
    }
    
    private JobSchedulerState getJobSchedulerState(Element stateElem) {
        JobSchedulerState  jobSchedulerState = new JobSchedulerState();
        if ("yes".equals(stateElem.getAttribute("db_waiting"))) {
            jobSchedulerState.set_text(JobSchedulerStateText.WAITING_FOR_DATABASE);
            jobSchedulerState.setSeverity(2);
        } else {
            switch(stateElem.getAttribute("state")) {
            case "starting":
                jobSchedulerState.set_text(JobSchedulerStateText.STARTING);
                jobSchedulerState.setSeverity(0);
                break;
            case "running":
                jobSchedulerState.set_text(JobSchedulerStateText.RUNNING);
                jobSchedulerState.setSeverity(0);
                break;
            case "paused":
                jobSchedulerState.set_text(JobSchedulerStateText.PAUSED);
                jobSchedulerState.setSeverity(1);
                break;
            case "stopping":
            case "stopping_let_run":
            case "stopped":
                jobSchedulerState.set_text(JobSchedulerStateText.TERMINATING);
                jobSchedulerState.setSeverity(3);
                break;
            case "waiting_for_activation":
                jobSchedulerState.set_text(JobSchedulerStateText.WAITING_FOR_ACTIVATION);
                jobSchedulerState.setSeverity(3);
                break;
            }
        }
        return jobSchedulerState;
    }
}
