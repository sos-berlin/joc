package com.sos.joc.classes.jobscheduler;

import org.w3c.dom.Element;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.scheduler.model.commands.JSCmdShowState;


public class JobSchedulerVolatile extends JobSchedulerV {
    private DBItemInventoryInstance dbItemInventoryInstance;
    private String accessToken;

    public JobSchedulerVolatile(DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        this.dbItemInventoryInstance = dbItemInventoryInstance;
        this.accessToken = accessToken;
    }
    
    public JobSchedulerV getJobScheduler() throws Exception {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
        try {
            jocXmlCommand.executePost(getXMLCommand(), accessToken);
        } catch (JocException e) {
            return getUnreachableJobScheduler();
        }
        jocXmlCommand.throwJobSchedulerError();
        return getReachableJobScheduler(jocXmlCommand);
    }
    
    private String getXMLCommand() {
        JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
        jsCmdShowState.setWhat("folders no_subfolders");
        jsCmdShowState.setPath("/does/not/exist");
        jsCmdShowState.setSubsystems("folder");
        return jsCmdShowState.toXMLString();
    }
    
    private JobSchedulerV getUnreachableJobScheduler() {
        setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
        setHost(dbItemInventoryInstance.getHostname());
        setPort(dbItemInventoryInstance.getPort());
        JobSchedulerState jobSchedulerState = new JobSchedulerState();
        jobSchedulerState.set_text(JobSchedulerStateText.UNREACHABLE);
        jobSchedulerState.setSeverity(2);
        setState(jobSchedulerState);
        return this;
    }
    
    private JobSchedulerV getReachableJobScheduler(JOCXmlCommand jocXmlCommand) throws Exception {
        setSurveyDate(jocXmlCommand.getSurveyDate());
        Element stateElem = jocXmlCommand.executeXPath("/spooler/answer/state");
        setHost(stateElem.getAttribute("host"));
        setJobschedulerId(stateElem.getAttribute("id"));
        setPort(Integer.parseInt(jocXmlCommand.getAttributeValue(stateElem, "tcp_port", "0")));
        setStartedAt(JobSchedulerDate.getDateFromISO8601String(stateElem.getAttribute("running_since")));
        setState(getJobSchedulerState(stateElem));
        if (stateElem.hasAttribute("waiting_errno_text")) {
            Err err = new Err();
            err.setCode(stateElem.getAttribute("waiting_errno"));
            err.setMessage(stateElem.getAttribute("waiting_errno_text"));
            setError(err);
        }
        return this;
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
