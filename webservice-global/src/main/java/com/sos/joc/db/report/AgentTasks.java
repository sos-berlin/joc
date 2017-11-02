package com.sos.joc.db.report;

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
}
