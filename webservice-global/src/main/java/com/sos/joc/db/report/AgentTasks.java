package com.sos.joc.db.report;

import com.sos.jitl.reporting.db.DBItemReportTask;
import com.sos.joc.model.job.TaskCause;
import com.sos.joc.model.report.Agent;

public class AgentTasks extends Agent {

    public AgentTasks(Long numOfSuccessfulTasks, String jobschedulerId, String agent, String cause) {
        setNumOfSuccessfulTasks(numOfSuccessfulTasks);
        setJobschedulerId(jobschedulerId);
        setAgent(agent);
        TaskCause tc = TaskCause.NONE;
        try {
            if (cause != null) {
                tc = TaskCause.fromValue(cause.toUpperCase());
            }
        } catch (Exception e) {
        }
        setCause(tc);
    }
    
    public AgentTasks(DBItemReportTask itemReportTask) {
        setJobschedulerId(itemReportTask.getSchedulerId());
        setAgent(itemReportTask.getAgentUrl());
        TaskCause tc = TaskCause.NONE;
        try {
            if (itemReportTask.getCause() != null) {
                tc = TaskCause.fromValue(itemReportTask.getCause().toUpperCase());
            }
        } catch (Exception e) {
        }
        setCause(tc);
    }
}
