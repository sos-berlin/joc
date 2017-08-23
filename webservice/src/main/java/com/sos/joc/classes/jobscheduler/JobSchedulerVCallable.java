package com.sos.joc.classes.jobscheduler;

import java.util.concurrent.Callable;

import org.w3c.dom.Element;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.jobscheduler.ClusterMemberType;
import com.sos.joc.model.jobscheduler.ClusterType;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.JobSchedulerV;

public class JobSchedulerVCallable implements Callable<JobSchedulerV> {
    private final DBItemInventoryInstance dbItemInventoryInstance;
    private final String accessToken;
    
    public JobSchedulerVCallable(DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        this.dbItemInventoryInstance = dbItemInventoryInstance;
        this.accessToken = accessToken;
    }
    
    @Override
    public JobSchedulerV call() throws Exception {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
        try {
            String xmlCommand = jocXmlCommand.getShowStateCommand("folder", "folders no_subfolders", "/does/not/exist");
            jocXmlCommand.executePost(xmlCommand, accessToken);
        } catch (JocException e) {
            return getUnreachableJobScheduler();
        }
        jocXmlCommand.throwJobSchedulerError();
        return getReachableJobScheduler(jocXmlCommand);
    }
    
    private JobSchedulerV getUnreachableJobScheduler() {
        JobSchedulerV js = new JobSchedulerV();
        js.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
        js.setHost(dbItemInventoryInstance.getHostname());
        js.setPort(dbItemInventoryInstance.getPort());
        ClusterMemberType clusterMemberTypeSchema = new ClusterMemberType();
        clusterMemberTypeSchema.setPrecedence(dbItemInventoryInstance.getPrecedence());
        clusterMemberTypeSchema.set_type(ClusterType.fromValue(dbItemInventoryInstance.getClusterType()));
        js.setClusterType(clusterMemberTypeSchema);
        js.setUrl(dbItemInventoryInstance.getUrl());
        JobSchedulerState jobSchedulerState = new JobSchedulerState();
        jobSchedulerState.set_text(JobSchedulerStateText.UNREACHABLE);
        jobSchedulerState.setSeverity(2);
        js.setState(jobSchedulerState);
        return js;
    }
    
    private JobSchedulerV getReachableJobScheduler(JOCXmlCommand jocXmlCommand) throws Exception {
        JobSchedulerV js = new JobSchedulerV();
        js.setSurveyDate(jocXmlCommand.getSurveyDate());
        Element stateElem = jocXmlCommand.executeXPath("/spooler/answer/state");
        js.setHost(dbItemInventoryInstance.getHostname());
        js.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
        js.setPort(dbItemInventoryInstance.getPort());
        ClusterMemberType clusterMemberTypeSchema = new ClusterMemberType();
        clusterMemberTypeSchema.setPrecedence(dbItemInventoryInstance.getPrecedence());
        clusterMemberTypeSchema.set_type(ClusterType.fromValue(dbItemInventoryInstance.getClusterType()));
        js.setClusterType(clusterMemberTypeSchema);
        js.setUrl(dbItemInventoryInstance.getUrl());
        js.setStartedAt(JobSchedulerDate.getDateFromISO8601String(stateElem.getAttribute("spooler_running_since")));
        js.setState(getJobSchedulerState(stateElem));
        if (stateElem.hasAttribute("waiting_errno_text")) {
            Err err = new Err();
            err.setCode(stateElem.getAttribute("waiting_errno"));
            err.setMessage(stateElem.getAttribute("waiting_errno_text"));
            js.setError(err);
        }
        return js;
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
